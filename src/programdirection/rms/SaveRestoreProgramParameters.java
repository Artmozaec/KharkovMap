package rms;

import userinterface.UserInterface;
import javax.microedition.rms.*;

public class SaveRestoreProgramParameters extends OneStoreOneRecord{
private static SaveRestoreProgramParameters thisInst = new SaveRestoreProgramParameters();

//������ ���������
private final int dataSize = 10;

private SaveRestoreProgramParameters() {
	super("pizda");//��� ��������� � ���������
}

//��������� ����������� ��� ������ ����������
protected void isNotRecordStore(){
	//��������������� ������ �� ���������
	resetDefaultData();
}

//������ � ������ data ������ �� ���������
private void resetDefaultData(){
	//�������������� ������ ������
	data = new byte[dataSize];
	
	//�������������� ������ ��������� �������
	setIndexHorizontalFirstPosition(0);
	
	//������������ ������ ��������� �������
	setIndexVerticalFirstPosition(0);
	
	//������ ��������� ������� �� ������
	setFirstPositionShirinaIdent(-10);
	
	//������ ��������� ������� �� ������
	setFirstPositionVisotaIdent(-10);
}


//�������������� �������� ������ ��������� �� ������� ����, ��������� ������ ������
protected boolean checkCorrectStore(){
	RecordStore recordStore;
	boolean result = false;
	try{
		//������� ���������
		recordStore = RecordStore.openRecordStore(recordStoreName, true);
		
		//������ ������ ������ ���������������
		if (recordStore.getRecordSize(1) == dataSize) result = true;
		
		//������� ���������
		recordStore.closeRecordStore();
	} catch (RecordStoreException rse){
		UserInterface.getUserInterface().showErrorMessage(rse.getMessage());
		result = false;
	}
	return result;
}

//������������� saveParameters() ������ �������� ��� saveData()
public void saveParameters(){
	saveData();
}

public byte getIndexHorizontalFirstPosition(){
	return data[0];
}
public void setIndexHorizontalFirstPosition(int indexHorizontal){
	//���������� � �������� ������
	data[0] = (byte)indexHorizontal;
}


public byte getIndexVerticalFirstPosition(){
	return data[1];
}
public void setIndexVerticalFirstPosition(int indexHorizontal){
	//���������� � �������� ������
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
	//����������� int � ������������������ ����
	byte[] result;
	result = ValueConverter.intToByteArray(shirina);
	
	//���������� � �������� ������
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
	//����������� int � ������������������ ����
	byte[] result;
	result = ValueConverter.intToByteArray(visota);
	
	//���������� � �������� ������
	data[6] = result[0];
	data[7] = result[1];
	data[8] = result[2];
	data[9] = result[3];
}


	
public static SaveRestoreProgramParameters getInstance(){
	return thisInst;
}


}