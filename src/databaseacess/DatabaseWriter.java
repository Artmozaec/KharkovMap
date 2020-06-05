package databaseacess;

import javax.microedition.io.*;
import javax.microedition.io.file.*;
import java.io.*;
import mapvisiblecontent.Fragment;
import userinterface.UserInterface;


class DatabaseWriter{

private FileConnection fileConnect;
private OutputStream fileOutStream;

//Путь к директории у которой лежит файл
private String databaseFolder;

//Название файла базы данных
private String databaseName;

//Название временного файла
private String tempName;

//Название архивного - старого файла БД
private String oldDatabaseName;

DatabaseWriter(String indatabaseFolder, String inTempName, String inDatabaseName, String inOldDatabaseName){
	databaseFolder = indatabaseFolder;
	tempName = inTempName;
	databaseName = inDatabaseName;
	oldDatabaseName = inOldDatabaseName;
	
	//Открываем поток
	openFileAndCreateStream(databaseFolder+tempName);
}


//Создает один массив байт из двух массивов part1 и part2
private byte[] addPart(byte[] part1, byte[] part2){
	//Длинна результирующего массива - сумма длинн обоих
	byte[] result = new byte[part1.length+part2.length];
	
	//System.arraycopy(Откуда, начало откуда, куда, начало куда, колличество копируемых единиц информации)
	
	//Копируем в результрующий массив первую часть
	System.arraycopy(part1, 0, result, 0, part1.length);
	
	//Копируем в результрующий массив вторую часть
	System.arraycopy(part2, 0, result, part1.length, part2.length);
	
	
	return result;
}

//Удаляет файл по пути переданном в параметре
private boolean deleteFile(String fileName) {
        try {
            FileConnection fc = (FileConnection)Connector.open(fileName);
            fc.delete();
        } catch (Exception e) {
			System.out.println("Чё-то нехочет удалять! Ашипка - "+e.getMessage());
			return false;
        }
		//Если удалось удаление
		return true;
}


private boolean renameFile(String fileNamePatch, String newFileName) {
        try {
            FileConnection fc = (FileConnection)Connector.open(fileNamePatch);
            fc.rename(newFileName);
        } catch (Exception e) {
			System.out.println("Чё-то нехочет переименовывать! Ашипка - "+e.getMessage());
			return false;
        }
		return true;
}


//Открыть записываемый файл и создает поток
private void openFileAndCreateStream(String tempPatch){
	try {
		fileConnect = (FileConnection)Connector.open(tempPatch);
		
		//Если файл существует то это означает что предыдущее сохранение было выполнено с ошибкой
		if (fileConnect.exists()) {
			//Удаляем его нахрен
			deleteFile(tempPatch);
		}
		
		//Создаем новый файл
		fileConnect.create();
		
		fileOutStream = fileConnect.openOutputStream();
	} catch(IOException ioe) {
		System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!ОШИБКА ОТКРЫТИЯ ПОТКА БД!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
	}	
}


private void closeConnectionAndStream(){
	try {
		fileOutStream.close();
		fileConnect.close();
	} catch(IOException ioe) {}	
}

//Нормальный конец записи закрываем потоки, удаляем старый файл базы, переименовываем временный файл
public void finalizeRecord(){
	boolean statusError = false;
	//Закрыть поток записи и соединение
	closeConnectionAndStream();
	
	//Удалить старый архивный файл БД
	if (!deleteFile(databaseFolder+oldDatabaseName)) statusError = true;
	
	//Преименовать текущий файл БД в архивный файл
	if (!renameFile(databaseFolder+databaseName, oldDatabaseName)) statusError = true;
	
	//Переименовать временный файл в файл БД
	if (!renameFile(databaseFolder+tempName, databaseName)) statusError = true;
	
	if (statusError){
		System.out.println("!!!!!!!!!!!!!!!!!!FINALIZATION_ERROR!!!!!!!!!!!!!!!!!!!!!!!");
		UserInterface.getUserInterface().showErrorMessage("Ошибка финализации!!!! операции с файлами нарушены");
	}
}


//Если была ошибка файла, удаляем временный файл и закрываем поток
public void stopErrorRecord(){
	//Закрыть поток записи и соединение
	closeConnectionAndStream();
	
	if (!deleteFile(databaseFolder+tempName)){
		//Если удалить не удалось сгенерировать сообщение
		UserInterface.getUserInterface().showErrorMessage("Не удается удалить временный файл");
	}
	
}


//Преобразует строку адреса хранящуюся в writeRecord в массив байт
private byte[] getAdress(DatabaseRecord writeRecord){
	String adress;
	byte[] result = null;
	//Получить строку
	adress = writeRecord.getAdressName();
	
	try{
		result = adress.getBytes("windows-1251");
	} catch (UnsupportedEncodingException ue) {System.out.println("OSHIBKA_KODIROVKA");}
	
	//Вернуть массив байт
	return result;
}

private byte[] getPositions(DatabaseRecord writeRecord){
	byte[] result = null;
	String digits;
	Fragment leftUp = writeRecord.getFragment();
	
	//Получаем горизонтальный индекс файла
	digits = leftUp.getNameIndexHorizontal()+" ";
	
	//Получаем вертикальный индекс файла
	digits = digits+leftUp.getNameIndexVertical()+" ";
	
	//Отступ отображения по горизонтали
	digits = digits+leftUp.getDrawPositionShirina()+" ";
	
	//Отступ отображения по вертикали
	digits = digits+leftUp.getDrawPositionVisota()+" ";

	
	//Переводим в массив байт
	try{
		result = digits.getBytes("windows-1251");
	} catch (UnsupportedEncodingException ue) {System.out.println("Бляяя кодировка!");}
	
	return result;
}

private void writeRecordArray(byte[] writeRecord){
	FileWrite writer = new FileWrite(fileOutStream, writeRecord);
}

//Записать фрагмент базы данных writeRecord в файл БД
public void addRecord(DatabaseRecord writeRecord){
	//System.out.println(" addRecord!!!>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>. name = "+writeRecord.getAdressName());
	//System.out.println();
	//System.out.println();
	byte[] recordArray;
	//Выделяем строку
	recordArray = getAdress(writeRecord);
	
	//Добавить разделитель базы данных
	recordArray = addPart(recordArray, DatabaseAcess.DATABASE_SEPARATOR);
	
	//Преобразуем в массив байт индексы файла левого верхнего фрагмента и его позиции на экране
	byte[] positions = getPositions(writeRecord);
	
	//Добавляем туда позицию
	recordArray = addPart(recordArray, positions);
	
	//Добавляем перенос строки
	recordArray = addPart(recordArray, DatabaseAcess.CR_LF);
	
	//Записать всё это дело
	writeRecordArray(recordArray);
}


}