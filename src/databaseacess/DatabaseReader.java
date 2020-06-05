package databaseacess;

import javax.microedition.io.*;
import javax.microedition.io.file.*;
import java.io.*;
import mapvisiblecontent.Fragment;


class DatabaseReader {

private FileConnection fileConnect;
private InputStream fileInStream;

//������� ��������� ������� ���� ������
private int recordCounter;

//���� � ��������� �����
private String filePatch;

//������� ����������� ������
private TextAccumulator textAccumulator;

//����� ������ �����
private boolean endFile;

//������ ��� ������� ��������� � ���������������� ����������� �� ����� ������
boolean databaseRecordError;

//������ ������ ����� ���� ������, ��� ���������� ��� �����������
boolean databaseError;

//������������ ����������� ���� � ������
private final int maxByteInRecord = 300;

public static final String databaseRecordErrorString = "������ ������ ��";


DatabaseReader(String inFilePatch){
	
	//���� �� ��
	databaseError = false;
	
	endFile = false;
	
	//������� � 0
	recordCounter =0;
	
	textAccumulator = new TextAccumulator();
	
	
	//��������� ���� � �����
	openFileAndCreateStream(inFilePatch);
	
	//���� c ������ �� � �������
	if (!databaseError){
		//��������� ����������� ������ ������� ������
		textAccumulator.addDataPart(nextDataFrame());
	}
}

//�������� �� ����� ������� ��� ������� &&
private byte[] clearSeparator(byte[] name){
	//����� ������ ����� ������ �� 2 ������� - &&(����������� � ������ ���� ������)
	byte[] newName = new byte[name.length-2];
	//System.arraycopy(������, ������ ������, ����, ������ ����, ����������� ���������� ������ ����������)	
	System.arraycopy(name, 0, newName, 0, newName.length);
	return newName;
}




//���������� ��������� ������ ������ �� �����
private byte[] nextDataFrame(){
	FileRead fileRead;
	fileRead = new FileRead(500, fileInStream);
	if (fileRead.fileIsEnd()){//���� ���� ����������
		endFile = true;
	}
	return fileRead.getDataFragment();
}


//������� �������� ���� � ������� �����
private void openFileAndCreateStream(String filePatch){
	try {
		fileConnect = (FileConnection)Connector.open(filePatch);
		fileInStream = fileConnect.openInputStream();
	} catch(IOException ioe) {
		databaseError = true;
		//System.out.println("������ �������� ������ ���� ������!!!!!");
	}	
}


//��������� �����
public void finalizeRead(){
	try {
		fileInStream.close();
		fileConnect.close();
	} catch(IOException ioe) {
		//System.out.println("������ �������� ������ ���� ������!!!!!");
	}
}

private int nextRecordCount(){
	int returnNum = recordCounter;
	recordCounter++;
	return returnNum;
}

//�������� �� ������ � ��������� ����� � �������� ��� � Sring
private String getAdress(TextAccumulator solidString){
	String result = null;
	
	//���� ���� �� ��������� ����������� ���� ������
	byte[] adress;
	
	//��������� ��������� � ������ ������, ���������� ����� ���� �� ������� ����������� ������
	while (!solidString.isRecordSeparator()){
		//�������� ��������� ������ 
		if(!solidString.nextChar()){ //���� ������ ��������� ���������� ������ ������ ���� ������ � ������� ����
			databaseRecordError = true;
			return null;
		}
		//����� �����
	}
	
	//������ � ������
	adress = solidString.getDataPart();
	
	//������� � ����� ������ ��� ������� &&
	adress = clearSeparator(adress);
	
	//������� ������
	try{
		result = new String(adress, "windows-1251");
	} catch (UnsupportedEncodingException ue) {System.out.println("OSHIBKA_KODIROVKA");}
	
	return result;
}

//
private int checkAndCreateInt(byte[] digitChar){
	//���� ������ ������� ���� ������ ��
	String digits;
	int result;
	if (digitChar.length == 0){
		databaseRecordError = true;
		return 0;
	}

	//������� ������ 
	digits = new String(digitChar);
	
	//�������� �������
	digits = digits.trim();
	
	//System.out.println("������� ����� >>>>>>>>>>>>>>>>"+digits+"<<<<<<<<<<<<<<<<<<<");
	try{
		result = Integer.parseInt(digits);
	}catch(NumberFormatException ne){
		System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!������_PARSE_INT!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
		databaseRecordError = true;
		result = 0;
	}
	
	return result;
}




//����� �� solidString ��������� ����� � ��������� ������� � ����������� ��� 
private int getNextNumber(TextAccumulator solidString){
	int result =0;
	
	//��������� ������ ���� ���� �����
	while (solidString.isDigit()){
		//�������� ��������� ������ 
		if(!solidString.nextChar()){ //���� ������ ��������� ���������� ������ ������ ���� ������ � ������� ����
			//���� � ����� ������ ����������� ������������ 13+10, ���� �� ��� ������ �� ����������
			databaseRecordError = true;
			return 0;
		}
	}
	
	//��������� ���������� ������ � ������� �����
	result = checkAndCreateInt(solidString.getDataPart());
	return result;
}

//������� �������� �� ������� byte[] � ������� �������� � ���� �������� ����� �������� ����� � �������
private Fragment createFragment(TextAccumulator solidString){
	//�������������� ������
	int indexHorizontal = getNextNumber(solidString);
	
	//������������ ������
	int indexVertical = getNextNumber(solidString);
	
	//������� ������ �������� ��������� ������������ ������ �� �����������
	int PositionShirina = getNextNumber(solidString);
	
	//������� ������ �������� ��������� ������������ ������ �� ���������
	int PositionVisota = getNextNumber(solidString);
	
	return new Fragment(indexHorizontal, indexVertical, PositionShirina, PositionVisota);
}

//�� ������� solidString ������� ������ ������ � ������ ��� �������� ������ �������� ���������
//� ����������� ��������� ������ ���� ������
private DatabaseRecord createDatabaseRecord(TextAccumulator solidString){
	String name; //����� 
	Fragment newFragment; //����� ������� �������� ��������
	int num; //����� ������ ��
	
	//���� �� ��������
	databaseRecordError = false;
	
	//������
	name = getAdress(solidString);
	//System.out.println(" name === "+name);
	//������� � ������� ���������
	newFragment = createFragment(solidString);
	
	//�������� ������� �������� �������� � ��������� ��� �� 1
	num = nextRecordCount();
	
	if (databaseRecordError) {//���� ��������� ������ ������ ��
		//System.out.println("!!!!!!!!!!!!!!!!!!!!������_������_����_������!!!!!!!!!!!!!!!!!!!!!!!!!!");
		//������� ��������� ������
		name = databaseRecordErrorString;
		
		//������ �������� ��������� ��������, ���-�� �� ���� ������ ���� ������ ������
		newFragment = new Fragment(-100, -100, 0 ,0);
	}
	
	//������� ����� ������ � ���������� �
	return new DatabaseRecord(num, name, newFragment);
}

//��������� �� ������ ������ ���� ������?
public boolean databaseIsError(){
	return databaseError;
}

//��������� �� ������ ����������� ������ ��
public boolean recordIsError(){
	return databaseRecordError;
}

//���� ��������?
public boolean fileIsEnd(){
	return endFile;
}


//���������� �� ����� ���� ������ ���� ������
public DatabaseRecord getRecord(){
	DatabaseRecord newDatabaseRecord;
	byte[] data;
	int symbolCounter=0; //������� �������� - ����������� ������ ������

	if (databaseError) return null;//���� ������ ��
	
	do{
		//���� � ������������ ������ ��� ������ 
		if (!textAccumulator.nextChar()){
			//��������� � ����������� ����� ������ ������
			data = nextDataFrame();
			textAccumulator.addDataPart(data);
		}
		
		//���� ��������� ������� �������� �������� �������� - ������ ������ ��������� �����
		if (symbolCounter>maxByteInRecord){
			//������ ����� ���� ������ ���������� ����
			//System.out.println("!!!!!!!!!!!!!!!!!!!!!!!���������_������_������_��_������_����_������!!!!!!!!!!!!!!!!!!!!!!");
			databaseError = true;
			break;
		}
		
		symbolCounter++;//��������� ������� ��������
	//���� ���� � ������������ �� ����������� ������������������ ����� ������ � ���� �� �������� ����
	} while ((!textAccumulator.isEndString()) && (!endFile));

	//�������� ������ ��� ������������ ������� DatabaseRecord
	newDatabaseRecord = createDatabaseRecord(textAccumulator.createTextAccumulator());
	//System.out.println("getRecord() = name = "+newDatabaseRecord.getAdressName());
	return newDatabaseRecord;
}



}