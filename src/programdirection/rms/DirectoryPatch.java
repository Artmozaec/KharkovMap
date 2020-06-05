package rms;
import java.io.*;
import userinterface.UserInterface;


//�������� �� ���������� - �������������� ��������� ���� � ������ �����
//� ������ ���������� ���� ���������� ����������� ����� ��������� �������� �������
public class DirectoryPatch extends OneStoreOneRecord{
private static DirectoryPatch directoryPatch = new DirectoryPatch();


public DirectoryPatch(){
	super("patch");
}

//��������� ��������� ����
public void saveWorkPatch(String patch){
	//��������� ��� � ������ ���������
	try{
		data = patch.getBytes("windows-1251");
	} catch (UnsupportedEncodingException ue) {
		System.out.println("!!!!!!!!!!!!!!!!!!!!!!saveWorkPatch -- ����� ���������!!!!!!!!!!!!!!!!!!!!!!");
	}
	//������ ������ � ���������
	saveData();
}

//��������� ���, ������ �� ������, ������...
protected void isNotRecordStore(){
}

//�������������� �������� ������ ��������� �� ������� ����,
protected boolean checkCorrectStore(){
	return true;
}

public String getPatch(){
	String patch = null;
	
	//���� ������ ������ ����������
	if (readStoreError()) return null;
	
	try{
		patch = new String(data, "windows-1251");
	} catch (UnsupportedEncodingException ue) {
		System.out.println("!!!!!!!!!!!!!!!!!!!!!!getPatch() -- ����� ���������!!!!!!!!!!!!!!!!!!!!!!");
	}
	
	return patch;
}

public static DirectoryPatch getInstance(){
	return directoryPatch;
}



}