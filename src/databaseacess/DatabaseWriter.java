package databaseacess;

import javax.microedition.io.*;
import javax.microedition.io.file.*;
import java.io.*;
import mapvisiblecontent.Fragment;
import userinterface.UserInterface;


class DatabaseWriter{

private FileConnection fileConnect;
private OutputStream fileOutStream;

//���� � ���������� � ������� ����� ����
private String databaseFolder;

//�������� ����� ���� ������
private String databaseName;

//�������� ���������� �����
private String tempName;

//�������� ��������� - ������� ����� ��
private String oldDatabaseName;

DatabaseWriter(String indatabaseFolder, String inTempName, String inDatabaseName, String inOldDatabaseName){
	databaseFolder = indatabaseFolder;
	tempName = inTempName;
	databaseName = inDatabaseName;
	oldDatabaseName = inOldDatabaseName;
	
	//��������� �����
	openFileAndCreateStream(databaseFolder+tempName);
}


//������� ���� ������ ���� �� ���� �������� part1 � part2
private byte[] addPart(byte[] part1, byte[] part2){
	//������ ��������������� ������� - ����� ����� �����
	byte[] result = new byte[part1.length+part2.length];
	
	//System.arraycopy(������, ������ ������, ����, ������ ����, ����������� ���������� ������ ����������)
	
	//�������� � ������������� ������ ������ �����
	System.arraycopy(part1, 0, result, 0, part1.length);
	
	//�������� � ������������� ������ ������ �����
	System.arraycopy(part2, 0, result, part1.length, part2.length);
	
	
	return result;
}

//������� ���� �� ���� ���������� � ���������
private boolean deleteFile(String fileName) {
        try {
            FileConnection fc = (FileConnection)Connector.open(fileName);
            fc.delete();
        } catch (Exception e) {
			System.out.println("׸-�� ������� �������! ������ - "+e.getMessage());
			return false;
        }
		//���� ������� ��������
		return true;
}


private boolean renameFile(String fileNamePatch, String newFileName) {
        try {
            FileConnection fc = (FileConnection)Connector.open(fileNamePatch);
            fc.rename(newFileName);
        } catch (Exception e) {
			System.out.println("׸-�� ������� ���������������! ������ - "+e.getMessage());
			return false;
        }
		return true;
}


//������� ������������ ���� � ������� �����
private void openFileAndCreateStream(String tempPatch){
	try {
		fileConnect = (FileConnection)Connector.open(tempPatch);
		
		//���� ���� ���������� �� ��� �������� ��� ���������� ���������� ���� ��������� � �������
		if (fileConnect.exists()) {
			//������� ��� ������
			deleteFile(tempPatch);
		}
		
		//������� ����� ����
		fileConnect.create();
		
		fileOutStream = fileConnect.openOutputStream();
	} catch(IOException ioe) {
		System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!������ �������� ����� ��!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
	}	
}


private void closeConnectionAndStream(){
	try {
		fileOutStream.close();
		fileConnect.close();
	} catch(IOException ioe) {}	
}

//���������� ����� ������ ��������� ������, ������� ������ ���� ����, ��������������� ��������� ����
public void finalizeRecord(){
	boolean statusError = false;
	//������� ����� ������ � ����������
	closeConnectionAndStream();
	
	//������� ������ �������� ���� ��
	if (!deleteFile(databaseFolder+oldDatabaseName)) statusError = true;
	
	//������������ ������� ���� �� � �������� ����
	if (!renameFile(databaseFolder+databaseName, oldDatabaseName)) statusError = true;
	
	//������������� ��������� ���� � ���� ��
	if (!renameFile(databaseFolder+tempName, databaseName)) statusError = true;
	
	if (statusError){
		System.out.println("!!!!!!!!!!!!!!!!!!FINALIZATION_ERROR!!!!!!!!!!!!!!!!!!!!!!!");
		UserInterface.getUserInterface().showErrorMessage("������ �����������!!!! �������� � ������� ��������");
	}
}


//���� ���� ������ �����, ������� ��������� ���� � ��������� �����
public void stopErrorRecord(){
	//������� ����� ������ � ����������
	closeConnectionAndStream();
	
	if (!deleteFile(databaseFolder+tempName)){
		//���� ������� �� ������� ������������� ���������
		UserInterface.getUserInterface().showErrorMessage("�� ������� ������� ��������� ����");
	}
	
}


//����������� ������ ������ ���������� � writeRecord � ������ ����
private byte[] getAdress(DatabaseRecord writeRecord){
	String adress;
	byte[] result = null;
	//�������� ������
	adress = writeRecord.getAdressName();
	
	try{
		result = adress.getBytes("windows-1251");
	} catch (UnsupportedEncodingException ue) {System.out.println("OSHIBKA_KODIROVKA");}
	
	//������� ������ ����
	return result;
}

private byte[] getPositions(DatabaseRecord writeRecord){
	byte[] result = null;
	String digits;
	Fragment leftUp = writeRecord.getFragment();
	
	//�������� �������������� ������ �����
	digits = leftUp.getNameIndexHorizontal()+" ";
	
	//�������� ������������ ������ �����
	digits = digits+leftUp.getNameIndexVertical()+" ";
	
	//������ ����������� �� �����������
	digits = digits+leftUp.getDrawPositionShirina()+" ";
	
	//������ ����������� �� ���������
	digits = digits+leftUp.getDrawPositionVisota()+" ";

	
	//��������� � ������ ����
	try{
		result = digits.getBytes("windows-1251");
	} catch (UnsupportedEncodingException ue) {System.out.println("����� ���������!");}
	
	return result;
}

private void writeRecordArray(byte[] writeRecord){
	FileWrite writer = new FileWrite(fileOutStream, writeRecord);
}

//�������� �������� ���� ������ writeRecord � ���� ��
public void addRecord(DatabaseRecord writeRecord){
	//System.out.println(" addRecord!!!>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>. name = "+writeRecord.getAdressName());
	//System.out.println();
	//System.out.println();
	byte[] recordArray;
	//�������� ������
	recordArray = getAdress(writeRecord);
	
	//�������� ����������� ���� ������
	recordArray = addPart(recordArray, DatabaseAcess.DATABASE_SEPARATOR);
	
	//����������� � ������ ���� ������� ����� ������ �������� ��������� � ��� ������� �� ������
	byte[] positions = getPositions(writeRecord);
	
	//��������� ���� �������
	recordArray = addPart(recordArray, positions);
	
	//��������� ������� ������
	recordArray = addPart(recordArray, DatabaseAcess.CR_LF);
	
	//�������� �� ��� ����
	writeRecordArray(recordArray);
}


}