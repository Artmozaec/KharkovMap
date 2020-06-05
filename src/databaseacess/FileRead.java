package databaseacess;

import javax.microedition.io.*;
import javax.microedition.io.file.*;
import java.io.*;

class FileRead extends Thread{
private boolean endFile;

//Массив в который происходит чтение файла
private byte[] dataFragment;

//Поток для чтения файла
private InputStream fileInStream;

FileRead(int dataFragmentSize, InputStream inFileInStream){
	endFile = false;
	fileInStream = inFileInStream;
	dataFragment = new byte[dataFragmentSize];
	this.start();
	
	//Здесь системный вызывающий тред останавливается и ждёт завершения треда объекта this - то есть 
	//этого объекта
	
		try {
			this.join();
		} catch(InterruptedException e) {}	
}


private byte [] truncateDataFragment(int newSize){
	byte[] newDataFragment;
	
	newDataFragment = new byte[newSize];
	
	//Копируем в него данные из старого массива
	//System.arraycopy(Откуда, начало откуда, куда, начало куда, колличество копируемых единиц информации)
	System.arraycopy(dataFragment, 0, newDataFragment, 0, newDataFragment.length);
	
	return newDataFragment;
}

public byte[] getDataFragment(){
	return dataFragment;
}

//Файл закончился?
public boolean fileIsEnd(){
	return endFile;
}

public void run() {
	int result;
	try {
			result = fileInStream.read(dataFragment);
			if (result == -1){//файл вообще кончился
				endFile = true;
				return;
			}
			
			if (result<dataFragment.length){//Если это последнее чтение и данных меньше чем размер массива буяера
				//его нужно обрезать до нового размера
				dataFragment = truncateDataFragment(result);
			}
			//System.out.println("Прочитали - "+ new String(dataFragment));
	} catch(IOException ioe) {}	
}

}


