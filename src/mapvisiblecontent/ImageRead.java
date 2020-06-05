package mapvisiblecontent;

import javax.microedition.lcdui.Image;
import javax.microedition.io.*;
import javax.microedition.io.file.*;
import java.io.*;

class ImageRead extends Thread{
private Image currentImg;
private String filePatch;

//Указывает на ошибку чтения файла
private boolean errorStatus = false;

ImageRead(String inFilePatch){
	
	filePatch = inFilePatch;
	System.out.println("put k faily >>>>>>>>>"+filePatch);
	this.start();
	//Здесь системный вызывающий тред останавливается и ждёт завершения треда объекта this - то есть 
	//этого объекта

	
		try {
			this.join();
		} catch(InterruptedException e) {}	
}

public Image getImage(){
	return currentImg;
}



//Сразу после чтения файла позволяет узнать прочёлся ли он вообще, или может его и небыло или другая какая фигня
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
		
		//Закрываем поток и соединение
		fileInStream.close();
		fileConnect.close();
	} catch(IOException ioe) {
		//Нужный файл не-был найден!
		System.out.println("S FAILOM FIGNYA!!!");
		currentImg = null;
		errorStatus = true;
	}	
}

}