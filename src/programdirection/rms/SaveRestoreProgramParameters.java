package rms;

import userinterface.UserInterface;
import javax.microedition.rms.*;

public class SaveRestoreProgramParameters extends OneStoreOneRecord{
private static SaveRestoreProgramParameters thisInst = new SaveRestoreProgramParameters();

//Длинна хранилища
private final int dataSize = 10;

private SaveRestoreProgramParameters() {
	super("pizda");//Имя хранилища в параметре
}

//Хранилище отсутствует или запись повреждена
protected void isNotRecordStore(){
	//Восстанавливаем данные по умолчанию
	resetDefaultData();
}

//Вносит в массив data данные по умолчанию
private void resetDefaultData(){
	//Инициализируем массив данных
	data = new byte[dataSize];
	
	//Горизонтальный индекс начальной позиции
	setIndexHorizontalFirstPosition(0);
	
	//Вертикальный индекс начальной позиции
	setIndexVerticalFirstPosition(0);
	
	//Отступ начальной позиции по ширине
	setFirstPositionShirinaIdent(-10);
	
	//Отступ начальной позиции по высоте
	setFirstPositionVisotaIdent(-10);
}


//Дополнительная проверка записи хранилища на предмет сбоя, проверяем длинну записи
protected boolean checkCorrectStore(){
	RecordStore recordStore;
	boolean result = false;
	try{
		//Открыть хранилище
		recordStore = RecordStore.openRecordStore(recordStoreName, true);
		
		//Длинна должна записи соответствовать
		if (recordStore.getRecordSize(1) == dataSize) result = true;
		
		//Закрыть хранилище
		recordStore.closeRecordStore();
	} catch (RecordStoreException rse){
		UserInterface.getUserInterface().showErrorMessage(rse.getMessage());
		result = false;
	}
	return result;
}

//Переадресация saveParameters() звучит понятнее чем saveData()
public void saveParameters(){
	saveData();
}

public byte getIndexHorizontalFirstPosition(){
	return data[0];
}
public void setIndexHorizontalFirstPosition(int indexHorizontal){
	//Записываем в основной массив
	data[0] = (byte)indexHorizontal;
}


public byte getIndexVerticalFirstPosition(){
	return data[1];
}
public void setIndexVerticalFirstPosition(int indexHorizontal){
	//Записываем в основной массив
	data[1] = (byte)indexHorizontal;
}






public int getFirstPositionShirinaIdent(){
	byte[] resultArray = new byte[4];
	int result;
	
	resultArray[0] = data[2];
	resultArray[1] = data[3];
	resultArray[2] = data[4];
	resultArray[3] = data[5];
	
	result = ValueConverter.byteArrayToInt(resultArray);
	
	return result;
}

public void setFirstPositionShirinaIdent(int shirina){
	//Преобразуем int в последовательность байт
	byte[] result;
	result = ValueConverter.intToByteArray(shirina);
	
	//Записываем в основной массив
	data[2] = result[0];
	data[3] = result[1];
	data[4] = result[2];
	data[5] = result[3];
}





public int getFirstPositionVisotaIdent(){
	byte[] resultArray = new byte[4];
	int result;
	
	resultArray[0] = data[6];
	resultArray[1] = data[7];
	resultArray[2] = data[8];
	resultArray[3] = data[9];
	
	result = ValueConverter.byteArrayToInt(resultArray);
	
	return result;
}

public void setFirstPositionVisotaIdent(int visota){
	//Преобразуем int в последовательность байт
	byte[] result;
	result = ValueConverter.intToByteArray(visota);
	
	//Записываем в основной массив
	data[6] = result[0];
	data[7] = result[1];
	data[8] = result[2];
	data[9] = result[3];
}


	
public static SaveRestoreProgramParameters getInstance(){
	return thisInst;
}


}