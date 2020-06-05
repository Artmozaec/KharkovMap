package databaseacess;

import javax.microedition.io.*;
import javax.microedition.io.file.*;
import java.io.*;

class FileWrite extends Thread{

//����� ��� ������ �����
private OutputStream fileOutStream;

//������ ������ ���� ������
private byte[] recordArray;

//���� ��������� ������
private boolean writeError;
 
FileWrite(OutputStream inFileOutStream, byte[] inRecordArray){
	fileOutStream = inFileOutStream;
	recordArray = inRecordArray;
	this.start();
	
	//����� ��������� ���������� ���� ��������������� � ��� ���������� ����� ������� this - �� ���� 
	//����� �������
	
		try {
			this.join();
		} catch(InterruptedException e) {}	
}

//������ ��� ������ ��������������?
public boolean writeIsOk(){
	return !writeError;
}

public void run() {
	//System.out.println("���������� � ���� >"+new String(recordArray));
	try {
		fileOutStream.write(recordArray);		
	} catch(IOException ioe) {
		System.out.println("!!!!!!!!!!!!!!!!!!!!!������_������_����������_�����!!!!!!!!!!!!!!!!!!!!!!!!1");
		writeError = true;
	}	
}



}