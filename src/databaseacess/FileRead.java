package databaseacess;

import javax.microedition.io.*;
import javax.microedition.io.file.*;
import java.io.*;

class FileRead extends Thread{
private boolean endFile;

//������ � ������� ���������� ������ �����
private byte[] dataFragment;

//����� ��� ������ �����
private InputStream fileInStream;

FileRead(int dataFragmentSize, InputStream inFileInStream){
	endFile = false;
	fileInStream = inFileInStream;
	dataFragment = new byte[dataFragmentSize];
	this.start();
	
	//����� ��������� ���������� ���� ��������������� � ��� ���������� ����� ������� this - �� ���� 
	//����� �������
	
		try {
			this.join();
		} catch(InterruptedException e) {}	
}


private byte [] truncateDataFragment(int newSize){
	byte[] newDataFragment;
	
	newDataFragment = new byte[newSize];
	
	//�������� � ���� ������ �� ������� �������
	//System.arraycopy(������, ������ ������, ����, ������ ����, ����������� ���������� ������ ����������)
	System.arraycopy(dataFragment, 0, newDataFragment, 0, newDataFragment.length);
	
	return newDataFragment;
}

public byte[] getDataFragment(){
	return dataFragment;
}

//���� ����������?
public boolean fileIsEnd(){
	return endFile;
}

public void run() {
	int result;
	try {
			result = fileInStream.read(dataFragment);
			if (result == -1){//���� ������ ��������
				endFile = true;
				return;
			}
			
			if (result<dataFragment.length){//���� ��� ��������� ������ � ������ ������ ��� ������ ������� ������
				//��� ����� �������� �� ������ �������
				dataFragment = truncateDataFragment(result);
			}
			//System.out.println("��������� - "+ new String(dataFragment));
	} catch(IOException ioe) {}	
}

}


