package mapvisiblecontent;

import javax.microedition.lcdui.Image;
import javax.microedition.io.*;
import javax.microedition.io.file.*;
import java.io.*;

class ImageRead extends Thread{
private Image currentImg;
private String filePatch;

//��������� �� ������ ������ �����
private boolean errorStatus = false;

ImageRead(String inFilePatch){
	
	filePatch = inFilePatch;
	System.out.println("put k faily >>>>>>>>>"+filePatch);
	this.start();
	//����� ��������� ���������� ���� ��������������� � ��� ���������� ����� ������� this - �� ���� 
	//����� �������

	
		try {
			this.join();
		} catch(InterruptedException e) {}	
}

public Image getImage(){
	return currentImg;
}



//����� ����� ������ ����� ��������� ������ �������� �� �� ������, ��� ����� ��� � ������ ��� ������ ����� �����
public boolean isCorrectRead(){
	return (!errorStatus);
}

public void run() {
	FileConnection fileConnect = null;
	InputStream fileInStream = null;
	System.out.println("Nachali!!!! >>>>>>>>"+filePatch);
	try {
		fileConnect = (FileConnection)Connector.open(filePatch);
		System.out.println("Otkrili soedinenie");
		fileInStream = fileConnect.openInputStream();
		System.out.println("Otkrili potok");
		currentImg = Image.createImage(fileInStream);
		System.out.println("prochitali fail!!!!");
		
		//��������� ����� � ����������
		fileInStream.close();
		fileConnect.close();
	} catch(IOException ioe) {
		//������ ���� ��-��� ������!
		System.out.println("S FAILOM FIGNYA!!!");
		currentImg = null;
		errorStatus = true;
	}	
}

}