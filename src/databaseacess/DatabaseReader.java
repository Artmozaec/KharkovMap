package databaseacess;

import javax.microedition.io.*;
import javax.microedition.io.file.*;
import java.io.*;
import mapvisiblecontent.Fragment;


class DatabaseReader {

private FileConnection fileConnect;
private InputStream fileInStream;

//счётчик прочтённых записей базы данных
private int recordCounter;

//Путь к читаемому файлу
private String filePatch;

//Текущий аккумулятор текста
private TextAccumulator textAccumulator;

//Конец чтения файла
private boolean endFile;

//ошибка при попытке разделить и проанализировать прочитанную из файла запись
boolean databaseRecordError;

//ошибка самого файла базы данных, его отсутствие или повреждение
boolean databaseError;

//Максимальное колличество байт в записи
private final int maxByteInRecord = 300;

public static final String databaseRecordErrorString = "Ошибка записи БД";


DatabaseReader(String inFilePatch){
	
	//Пока всё ок
	databaseError = false;
	
	endFile = false;
	
	//Счётчик в 0
	recordCounter =0;
	
	textAccumulator = new TextAccumulator();
	
	
	//Открываем файл и поток
	openFileAndCreateStream(inFilePatch);
	
	//Если c файлом всё в порядке
	if (!databaseError){
		//Заполняем аккумулятор первой порцией данных
		textAccumulator.addDataPart(nextDataFrame());
	}
}

//Вытирает из конца массива два символа &&
private byte[] clearSeparator(byte[] name){
	//Новый массив будет меньше на 2 символа - &&(разделители в записи базы данных)
	byte[] newName = new byte[name.length-2];
	//System.arraycopy(Откуда, начало откуда, куда, начало куда, колличество копируемых единиц информации)	
	System.arraycopy(name, 0, newName, 0, newName.length);
	return newName;
}




//Возвращает следующую порцию дпнных из файла
private byte[] nextDataFrame(){
	FileRead fileRead;
	fileRead = new FileRead(500, fileInStream);
	if (fileRead.fileIsEnd()){//Если файл закончился
		endFile = true;
	}
	return fileRead.getDataFragment();
}


//Открыть читаемый файл и создает поток
private void openFileAndCreateStream(String filePatch){
	try {
		fileConnect = (FileConnection)Connector.open(filePatch);
		fileInStream = fileConnect.openInputStream();
	} catch(IOException ioe) {
		databaseError = true;
		//System.out.println("ОШИБКА СОЗДАНИЯ ПОТОКА БАЗЫ ДАННЫХ!!!!!");
	}	
}


//Закрывает поток
public void finalizeRead(){
	try {
		fileInStream.close();
		fileConnect.close();
	} catch(IOException ioe) {
		//System.out.println("ОШИБКА ЗАКРЫТИЯ ПОТОКА БАЗЫ ДАННЫХ!!!!!");
	}
}

private int nextRecordCount(){
	int returnNum = recordCounter;
	recordCounter++;
	return returnNum;
}

//Выделяет из строки в параметре адрес и преводит его в Sring
private String getAdress(TextAccumulator solidString){
	String result = null;
	
	//цикл пока не достигнем разделителя базы данных
	byte[] adress;
	
	//Указатель находится в начале строки, сдвинаемся вперёд пока не находим разделитель записи
	while (!solidString.isRecordSeparator()){
		//сместить указатель вперед 
		if(!solidString.nextChar()){ //если строка кончилась установить ошибку записи базы данных и вернуть нулл
			databaseRecordError = true;
			return null;
		}
		//конец цикла
	}
	
	//Строка в массив
	adress = solidString.getDataPart();
	
	//Убираем в конце строки два символа &&
	adress = clearSeparator(adress);
	
	//Создаем строку
	try{
		result = new String(adress, "windows-1251");
	} catch (UnsupportedEncodingException ue) {System.out.println("OSHIBKA_KODIROVKA");}
	
	return result;
}

//
private int checkAndCreateInt(byte[] digitChar){
	//если размер массива ноль ошибка БД
	String digits;
	int result;
	if (digitChar.length == 0){
		databaseRecordError = true;
		return 0;
	}

	//создаем строку 
	digits = new String(digitChar);
	
	//Обрезать пробелы
	digits = digits.trim();
	
	//System.out.println("Создано число >>>>>>>>>>>>>>>>"+digits+"<<<<<<<<<<<<<<<<<<<");
	try{
		result = Integer.parseInt(digits);
	}catch(NumberFormatException ne){
		System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!ОШИБКА_PARSE_INT!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
		databaseRecordError = true;
		result = 0;
	}
	
	return result;
}




//Берет из solidString следующее число в текстовом формате и преобразует его 
private int getNextNumber(TextAccumulator solidString){
	int result =0;
	
	//Смещаемся вперед пока есть цифры
	while (solidString.isDigit()){
		//сместить указатель вперед 
		if(!solidString.nextChar()){ //если строка кончилась установить ошибку записи базы данных и вернуть нулл
			//Ведь в конце строки обязательно присутствует 13+10, если их нет значит БД повреждена
			databaseRecordError = true;
			return 0;
		}
	}
	
	//Проверяем переданные данные и создаем число
	result = checkAndCreateInt(solidString.getDataPart());
	return result;
}

//Создает фрагмент из массива byte[] в котором записаны в виде символов числа индексов файла и позиций
private Fragment createFragment(TextAccumulator solidString){
	//Горизонтальный индекс
	int indexHorizontal = getNextNumber(solidString);
	
	//Вертикальный индекс
	int indexVertical = getNextNumber(solidString);
	
	//Позиция левого верхнего фрагмента относительно экрана по горизонтали
	int PositionShirina = getNextNumber(solidString);
	
	//Позиция левого верхнего фрагмента относительно экрана по вертикали
	int PositionVisota = getNextNumber(solidString);
	
	return new Fragment(indexHorizontal, indexVertical, PositionShirina, PositionVisota);
}

//Из объекта solidString достаем строку адреса и данные для создания левого верхнего фрагмента
//И анализируем возможные ошибки базы данных
private DatabaseRecord createDatabaseRecord(TextAccumulator solidString){
	String name; //Адрес 
	Fragment newFragment; //Левый верхний фрагмент перехода
	int num; //Номер записи БД
	
	//Пока всё впорядке
	databaseRecordError = false;
	
	//Адресс
	name = getAdress(solidString);
	//System.out.println(" name === "+name);
	//Индексы и позиция фрагмента
	newFragment = createFragment(solidString);
	
	//Получили текущее згачение счётчика и увеличили его на 1
	num = nextRecordCount();
	
	if (databaseRecordError) {//Если произошла ошибка записи БД
		//System.out.println("!!!!!!!!!!!!!!!!!!!!ОШИБКА_ЗАПИСИ_БАЗЫ_ДААНЫХ!!!!!!!!!!!!!!!!!!!!!!!!!!");
		//Создаем ошибочную запись
		name = databaseRecordErrorString;
		
		//Создаём заведомо ошибочный фрагмент, что-бы по нему нельзя было прейти никуда
		newFragment = new Fragment(-100, -100, 0 ,0);
	}
	
	//Создаем новую запись и возвращаем её
	return new DatabaseRecord(num, name, newFragment);
}

//Произошла ли ошибка чтения базы данных?
public boolean databaseIsError(){
	return databaseError;
}

//Произошла ли ошибка распознания записи БД
public boolean recordIsError(){
	return databaseRecordError;
}

//Файл кончился?
public boolean fileIsEnd(){
	return endFile;
}


//Возвращает за вызов одну запись базы данных
public DatabaseRecord getRecord(){
	DatabaseRecord newDatabaseRecord;
	byte[] data;
	int symbolCounter=0; //Счетчик символов - ограничение длинны строки

	if (databaseError) return null;//Если ошибка БД
	
	do{
		//Если в аккумуляторе больше нет текста 
		if (!textAccumulator.nextChar()){
			//Добавляем в аккумулятор новую порцию данных
			data = nextDataFrame();
			textAccumulator.addDataPart(data);
		}
		
		//Если аварийный счётчик символов превысил значение - длинна строки превысила лимит
		if (symbolCounter>maxByteInRecord){
			//ошибка файла базы данных прекращаем цикл
			//System.out.println("!!!!!!!!!!!!!!!!!!!!!!!ПРЕВЫШЕНА_ДЛИННА_ЗАПИСИ_БД_ОШИБКА_БАЗЫ_ДАННЫХ!!!!!!!!!!!!!!!!!!!!!!");
			databaseError = true;
			break;
		}
		
		symbolCounter++;//Аварийный счётчик символов
	//цикл пока в аккумуляторе не встретилась последовательность конца строки и пока не кончился файл
	} while ((!textAccumulator.isEndString()) && (!endFile));

	//Отделяем строку для формирования объекта DatabaseRecord
	newDatabaseRecord = createDatabaseRecord(textAccumulator.createTextAccumulator());
	//System.out.println("getRecord() = name = "+newDatabaseRecord.getAdressName());
	return newDatabaseRecord;
}



}