package mapvisiblecontent;

import javax.microedition.lcdui.Image;
import programdirection.ProgramParameters;

//Этот класс хранит в себе картинку и координаты её отображения относительно экрана
public class Fragment extends PaintAndMoving{

public static final int STATUS_EMPTY = 0; //Пустой
public static final int STATUS_ERROR = 1; //Ошибка чтения
public static final int STATUS_FILL = 2; //Заполненный изображением

//Индексы этой картинки
private int fileIndexHorizontal;
private int fileIndexVertical;

//Картинка наполнения
private Image content;

//текущее состояние фрагмента
private int status;

public Fragment(int inFileIndexHorizontal, 
				int inFileIndexVertical, 
				int inDrawPositionShirina, 
				int inDrawPositionVisota){
	
	super(inDrawPositionShirina, inDrawPositionVisota);
	
	fileIndexHorizontal = inFileIndexHorizontal;
	fileIndexVertical = inFileIndexVertical;
	drawPositionShirina = inDrawPositionShirina;
	drawPositionVisota = inDrawPositionVisota;
	
	status = STATUS_EMPTY; //Пустой объект	
}

//Читает картинку и устанавливает статус
public void fillFragment(){
	ImageRead reader;
	
	//Узнаем текущий путь к файлу
	String currentFilePatch = CheckAndNames.generateFilePatch(fileIndexHorizontal, fileIndexVertical);
	reader = new ImageRead(currentFilePatch);
	
	if (reader.isCorrectRead()){
		content = reader.getImage();
		status = STATUS_FILL;
	} else {
		status = STATUS_ERROR;
	}
}


public Image getContent(){
	return content;
}


public int getStatus(){
	return status;
}


/////////////////ПОЛУЧЕНИЕ ИНДЕКСОВ ФАЙЛА ФРАГМЕНТА И ЕГО ЕДЕНИЧНЫХ ИНКРЕМЕНТАЦИЙ И ДЕКРЕМЕНТПЦИЙ
public int getNameIndexHorizontal(){
	return fileIndexHorizontal;
}

public int getNameIndexVertical(){
	return fileIndexVertical;
}

public int getNextHorizontalIndexFile(){
	return fileIndexHorizontal+1;
}

public int getPervHorizontalIndexFile(){
	return fileIndexHorizontal-1;
}

public int getNextVerticalIndexFile(){
	return fileIndexVertical+1;
}

public int getPervVerticalIndexFile(){
	return fileIndexVertical-1;
}


//Создает и возвращает дубликат этого объекта
public Fragment getDublicate(){
	return new Fragment(fileIndexHorizontal, fileIndexVertical, drawPositionShirina, drawPositionVisota);
}

//Специальная функция для очистки ссылок на заключённое изображение, для последующего вызова gc()
public void killContent(){
	content = null;
}

}