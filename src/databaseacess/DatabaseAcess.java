package databaseacess;


import mapvisiblecontent.Fragment;
import userinterface.UserInterface;
import programdirection.ProgramParameters;
import progressindicator.Process;


public class DatabaseAcess{

private ProgramParameters programParameters;

//чтение - запись в БД
private DatabaseReader databaseReader;
private DatabaseWriter databaseWriter;

//Основные пути к БД
private String databaseFolder;
private String tempName;
private String databaseName;
private String oldDatabaseName;

//Текущий индикатор прогресса
private ProgressIndicator progressIndicator;

//Разделитель БД
public static final byte[] DATABASE_SEPARATOR = new byte[]{38, 38}; 

//Перенос строки
public static final byte[] CR_LF = new byte[]{13, 10};

public DatabaseAcess (){
	programParameters = ProgramParameters.getProgramParameters();
	
	//Получаем пути и имена основных файлов
	databaseFolder = programParameters.getDatabaseDirectoryPatch();
	tempName = programParameters.getDatabaseTempName();
	databaseName = programParameters.getDatabaseFileName();
	oldDatabaseName = programParameters.getOldDatabaseName();
	
	//Сразу создаём индикатор
	progressIndicator = new ProgressIndicator();
}

public Process getIndicator(){
	return (Process)progressIndicator;
}

private boolean substringIsExist(DatabaseRecord currentDBRecord, String substring){
	//Получили адресс
	String name = currentDBRecord.getAdressName();
	int searchPosition;
	
	//Так как методы поиска подстроки в строке работают с учетом регистра
	//Переводим обе строки в один регистр
	name = name.toLowerCase();
	substring = substring.toLowerCase();
	
	
	//Возвращает первое вхождение подстроки в строку
	searchPosition = name.indexOf(substring);
	
	if (searchPosition == -1){//Нет подстроки в строке
		return false;
	} else {//Подстрока обнаружена
		return true;
	}
}



public DatabaseRecordContainer search(String query){
	//Обнуляем индикатор
	progressIndicator.reset();
	
	//Создать контейнер 
	DatabaseRecordContainer container = new DatabaseRecordContainer();
	
	//Создать объект чтения базы данных - он читает базу и возвращает нам по 1 записи
	databaseReader = new DatabaseReader(databaseFolder+databaseName);
	
	//цикл пока не кончился файл и пока не произошла ошибка чтения файла БД
	while (true) {
		
		//получить запись
		DatabaseRecord record = databaseReader.getRecord();
		
		//Добавляем прогресс
		progressIndicator.addProgress(record.getSizeRecord());
		
		//цикл пока не кончился файл или пока не произошла ошибка чтения файла БД
		if ((databaseReader.fileIsEnd()) || (databaseReader.databaseIsError())) break;
		
		
		if ((substringIsExist(record, query)) || //Проверить есть - ли в текущем фрагменте искомая подстрока
			(substringIsExist(record, DatabaseReader.databaseRecordErrorString)))//На случай если в базе данных ошибка её должно быть видно, при любом поиске							)
		{
			//если есть добавляем эту запись в контейнер
			container.addRecord(record);
		}	
	}
	//System.out.println("search - возвращаем результат!");
	
	//Закрываем поток чтения
	databaseReader.finalizeRead();
	
	return container;
}


private void stopErrorProcess(){
		//Закрываем запись
		databaseWriter.stopErrorRecord();
			
		//Закрываем чтение
		databaseReader.finalizeRead();
			
		//Показываем сообщение
		UserInterface.getUserInterface().showErrorMessage("Ошибка файла БД! запись не удалась");
}

public void addRecord(DatabaseRecord addDatabaseRecord){
	//Обнуляем индикатор
	progressIndicator.reset();
	
	//создать объект записи БД
	databaseWriter = new DatabaseWriter(databaseFolder, tempName, databaseName, oldDatabaseName);
	
	//Создать объект чтения БД
	databaseReader = new DatabaseReader(databaseFolder+databaseName);
	
	//С начала переписываем во временный файл все записи базы данных
	while (true) {
		//получить запись
		DatabaseRecord record = databaseReader.getRecord();
		
		//Добавить прогресса
		progressIndicator.addProgress(record.getSizeRecord());
		
		//цикл пока не кончился файл или пока не произошла ошибка чтения файла БД
		if ((databaseReader.fileIsEnd()) || (databaseReader.databaseIsError())) break;
				
		//Если не произошла ошибка распознания записи БД
		if (!(databaseReader.recordIsError())){
			//добавляем её в новый файл
			databaseWriter.addRecord(record);
		} else {
			//System.out.println("!!!!!!!!!!!!!!!!!addRecord ОШИБКА ЗАПИСИ БД! !!!!!!!!!!!!!!!!!!!!"+record.getPositionNumber());
		}
	}
	
	//Если случилась ошибка файла БД
	if (databaseReader.databaseIsError()){
		//Всё прекращаем! БД повреждена!
		stopErrorProcess();
		
		//Выходим нахер
		return;
	}
	
	//Теперь добавляем новую запись
	databaseWriter.addRecord(addDatabaseRecord);
	
	//Финализируем запись - закрываем потоки переименовываем временный файл
	databaseReader.finalizeRead();
	databaseWriter.finalizeRecord();

	//Показать сообщение
	//UserInterface.getUserInterface().showInfoMessage("Новая запись успешно добавлена");//че-то не показывается
}


public void deleteRecord(DatabaseRecord deleteRecord){
	//Обнуляем индикатор
	progressIndicator.reset();
	
	//создать объект записи БД
	databaseWriter = new DatabaseWriter(databaseFolder, tempName, databaseName, oldDatabaseName);
	
	//Создать объект чтения БД
	databaseReader = new DatabaseReader(databaseFolder+databaseName);
	
	//Номер записи который при записи нужно пропустить... То-есть удалить
	int passRecordNumber = deleteRecord.getPositionNumber();
	//System.out.println("Удаляемая запись номер >>>>>>"+passRecordNumber);
	
	boolean databaseError;
	boolean thisDeleteNumber;
	while (true) {
		//получить запись
		DatabaseRecord record = databaseReader.getRecord();
		
		//Добавить прогресса
		progressIndicator.addProgress(record.getSizeRecord());
		
		
		//цикл пока не кончился файл или пока не произошла ошибка чтения файла БД
		if ((databaseReader.fileIsEnd()) || (databaseReader.databaseIsError())) break;
		

		
		//ошибка распознания записи БД
		databaseError = databaseReader.recordIsError();
		
		//Это удаляемая запись?
		thisDeleteNumber = (passRecordNumber == record.getPositionNumber());
		
		//Если не удаляемая запись и не ошибка распознания записи БД
		if ((!thisDeleteNumber) && (!databaseError)) {	
			//добавляем её в новый файл
			databaseWriter.addRecord(record);
		} else {
			//System.out.println("!!!!!!!!!!!!!!!!!!deleteRecord >>> Удалено или ошибка!!!!!!!!!!!!!!!!!!!!"+record.getPositionNumber());
		}
	}	
	
	//Если случилась ошибка файла БД
	if (databaseReader.databaseIsError()){
		//Всё прекращаем! БД повреждена!
		stopErrorProcess();
			
		//Выходим нахер
		return;
	}
	
	//Финализируем запись - закрываем потоки переименовываем временный файл
	databaseReader.finalizeRead();
	databaseWriter.finalizeRecord();
}



public void replaceRecord(DatabaseRecord replaceDatabaseRecord){
}

}