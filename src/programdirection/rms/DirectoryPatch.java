package rms;
import java.io.*;
import userinterface.UserInterface;


//Отвесает за сохранение - восстановление основного пути к файлам карты
//В случае отсутчтвия пути организует отображение древа каталогов файловой системы
public class DirectoryPatch extends OneStoreOneRecord{
private static DirectoryPatch directoryPatch = new DirectoryPatch();


public DirectoryPatch(){
	super("patch");
}

//Сохраняем выбранный путь
public void saveWorkPatch(String patch){
	//Переводим его в нужную кодировку
	try{
		data = patch.getBytes("windows-1251");
	} catch (UnsupportedEncodingException ue) {
		System.out.println("!!!!!!!!!!!!!!!!!!!!!!saveWorkPatch -- Бляяя кодировка!!!!!!!!!!!!!!!!!!!!!!");
	}
	//делаем запись в хранилище
	saveData();
}

//Хранилища нет, ничего не делаем, похрен...
protected void isNotRecordStore(){
}

//Дополнительная проверка записи хранилища на предмет сбоя,
protected boolean checkCorrectStore(){
	return true;
}

public String getPatch(){
	String patch = null;
	
	//Если данные небыли прочитанны
	if (readStoreError()) return null;
	
	try{
		patch = new String(data, "windows-1251");
	} catch (UnsupportedEncodingException ue) {
		System.out.println("!!!!!!!!!!!!!!!!!!!!!!getPatch() -- Бляяя кодировка!!!!!!!!!!!!!!!!!!!!!!");
	}
	
	return patch;
}

public static DirectoryPatch getInstance(){
	return directoryPatch;
}



}