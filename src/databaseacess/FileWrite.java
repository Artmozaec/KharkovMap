package databaseacess;

import javax.microedition.io.*;
import javax.microedition.io.file.*;
import java.io.*;

class FileWrite extends Thread{

//Поток для чтения файла
private OutputStream fileOutStream;

//Массив записи базы данных
private byte[] recordArray;

//Если произошла ошибка
private boolean writeError;
 
FileWrite(OutputStream inFileOutStream, byte[] inRecordArray){
	fileOutStream = inFileOutStream;
	recordArray = inRecordArray;
	this.start();
	
	//Здесь системный вызывающий тред останавливается и ждёт завершения треда объекта this - то есть 
	//этого объекта
	
		try {
			this.join();
		} catch(InterruptedException e) {}	
}

//Ошибка при записи присутствовала?
public boolean writeIsOk(){
	return !writeError;
}

public void run() {
	//System.out.println("Записываем в файл >"+new String(recordArray));
	try {
		fileOutStream.write(recordArray);		
	} catch(IOException ioe) {
		System.out.println("!!!!!!!!!!!!!!!!!!!!!ОШИБКА_ЗАПИСИ_ВРЕМЕННОГО_ФАЙЛА!!!!!!!!!!!!!!!!!!!!!!!!1");
		writeError = true;
	}	
}



}