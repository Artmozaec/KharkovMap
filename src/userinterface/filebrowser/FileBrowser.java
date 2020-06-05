package filebrowser;

import java.io.*;

import java.util.*;

import javax.microedition.io.*;
import javax.microedition.io.file.*;
import javax.microedition.lcdui.*;
import javax.microedition.midlet.*;

public class FileBrowser extends List implements CommandListener{

private static final String UP_DIRECTORY = "..";
private static final String MEGA_ROOT = "/";
private static final String SEP_STR = "/";
private static final char SEP = '/';
//Префикс обращения к устройству файловой системы
private static final String FS_PREFIX = "file://";

private String currentPatch;
private Command view;

private Image dirIcon;



//Ссылка на обработчик событийк ласса
private FileBrowserListener listener;

//Массив имён директорий по наличию которых будет определятся является-ли выбираемая папка рабочим каталогом с картой
private String[] needFolders;



//inListener - интерфейс обработчика выбора
//inNeedFolders - список необходимых папок для определения папки содержащей карту
public FileBrowser(FileBrowserListener inListener, String[] inNeedFolders){
	//Создаём сам List
	super(MEGA_ROOT, List.IMPLICIT);
	
	//Команда выбора
	view = new Command("View", Command.ITEM, 1);
	this.setSelectCommand(view);
	this.setCommandListener(this);
	
	listener=inListener;
	needFolders = inNeedFolders;
	
	//Сразу выход на самый верх
	currentPatch=MEGA_ROOT;

	try{
		dirIcon=Image.createImage("/icons/dir.png");
	}catch(IOException e){
		dirIcon=null;
	}

	try{
		showCurrDir();
	}catch(SecurityException e){
		e.printStackTrace();
	}catch(Exception e){
		e.printStackTrace();
	}
}

//разделяет содержимое на два вектора и возвращает результат в виде массива 
//0 - элемент файлы, 1 - элемент папки
private Vector[] splitFolderAndFiles(Enumeration content){
	Vector folders = new Vector();
	Vector files = new Vector();
	
	while(content.hasMoreElements()){
		String name = (String)content.nextElement();
		if (name.charAt(name.length()-1)==SEP){ 
			//Если имя заканчивается разделителем-это папка
			folders.addElement(name);
		}else{
			//Иначе файл
			files.addElement(name);
		}
	}
	
	//Возвращаем два вектора в одном массиве
	return new Vector[] {files, folders};
}

private Vector getFolders(String patch){
	FileConnection currDir = null;
	Enumeration folderContent = null;
	//если мы на вершине иерархии получаем список логических дисков устройства
	if (MEGA_ROOT.equals(patch)){
		folderContent = FileSystemRegistry.listRoots();
	}else{//иначе получаем список по текущему пути	
		try{
			currDir=(FileConnection)Connector.open(FS_PREFIX+patch);
			folderContent=currDir.list();
		}catch(IOException ioe){
			ioe.printStackTrace();
		}
		
		try{
			if (folderContent != null){
				currDir.close();
			}
		}catch(IOException ioe){
			ioe.printStackTrace();
		}
		
	}

	//Разделяем folderContent на 2 вектора файлы и папки
	Vector[] foldersFiles = splitFolderAndFiles(folderContent);
	
	//Возвращаем только папки
	return foldersFiles[1];
}

//Производит поиск в векторе content строки searchString
private boolean existName(Vector content, String searchString){
	String name;
	for (int ch=0; ch<content.size(); ch++){
		//Получаем имя директории и переводим его в нижний регистр
		name = ((String)content.elementAt(ch)).toLowerCase();
		if (searchString.equals(name)) return true;
	}
	return false;
}

//метод проверяет наличие по пути patch необходимых папок
private boolean isMapContentFolder(String folder){
	//Получаем список директорий по пути folder
	Vector content = getFolders(folder);
	
	//Ищем в результате, все элементы массива needFolders
	for (int ch=0; ch<needFolders.length; ch++){
		//Как только мы не находим нужного имени - папка не валидная, возвращаем - Ху#
		if (!existName(content, needFolders[ch])) return false;
	}
	return true;
}


//проверяет наличие в директории субдиректорий
private boolean folderIsNotEmpty(String folder){
	//Получаем список директорий по пути folder
	Vector content = getFolders(folder);
	
	if (content.size()>0) return true;
	return false;
}

public void commandAction(Command c, Displayable d){
	//Выбираем содержимое выбранного пункта
	List curr = (List)d;
	final String selectDir = curr.getString(curr.getSelectedIndex());
	
	new Thread (new Runnable(){
		public void run(){
			//Если выбранная папка строка подъёма на уровень выше
			if(selectDir.equals(UP_DIRECTORY)){
				traverseDirectory(selectDir);
			}else if (isMapContentFolder(currentPatch+selectDir)){ //Проверяем на наличие необходимых признаков папки с картой
				//передаём этот путь обработчику
				listener.patchChoosed(FS_PREFIX+currentPatch+selectDir);
			}else if (folderIsNotEmpty(currentPatch+selectDir)){ //Есть ли в папке подпапки?
				traverseDirectory(selectDir);
			}
		}
	}).start();
}

private void showCurrDir(){
	//Удаляем все элементы текущего списка
	this.deleteAll();
	
	//Устанавливаем заголовок - текущий путь
	this.setTitle(currentPatch);
	
	//Если не самая вершина иерархии - (список логических дисков)
	if (!MEGA_ROOT.equals(currentPatch)) this.append(UP_DIRECTORY,dirIcon); //Добавляем команду выхода на уровень выше
	
	//получаем список папок
	Vector folders = getFolders(currentPatch);
	
	//Создаём список папок
	for (int ch=0; ch<folders.size(); ch++){
		this.append((String)(folders.elementAt(ch)), dirIcon);
	}
}

private void traverseDirectory(String folderName){
	//Выше MEGA_ROOT только бог
	if((currentPatch.equals(MEGA_ROOT)) && (folderName.equals(UP_DIRECTORY))) return;
	
	if (folderName.equals(UP_DIRECTORY)){ //Поднимаемся на одну директорию вверх
		//Находим последнее включение в строку символа разделителя
		int i=currentPatch.lastIndexOf(SEP,currentPatch.length()-2); //а -2 потому-что на самом деле нам нужен пред помледний разделитель!
		
		if (i!=-1){
			//Выбираем подстроку от начала до предпоследнего разделителя
			currentPatch=currentPatch.substring(0, i+1);
		} else {
			// -1 -это значит что разделитель нам не встретился
			currentPatch=MEGA_ROOT;
		}
	} else { //На одну директорию в глубь
		currentPatch=currentPatch+folderName;
	}

	//отображаем то-что получилось
	showCurrDir();
}


}
