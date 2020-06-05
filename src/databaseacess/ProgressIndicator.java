package databaseacess;

import javax.microedition.io.*;
import javax.microedition.io.file.*;
import java.io.*;
import progressindicator.Process;
import programdirection.ProgramParameters;


class ProgressIndicator implements Process{
private int processProgress;
private int maximumValue;

ProgressIndicator(){
	reset();
}

public int getProgressValue(){
	return processProgress;
}

public int getMaximumValue(){
	return maximumValue;
}

public void addProgress(int progress){
	processProgress += progress;
}

public void reset(){
	processProgress = 0;
	//Устанавливаем конечное значение, оно равно размеру файла базы данных
	maximumValue = getDatabaseSize();
	System.out.println("maximumValue = "+maximumValue);
}

private int getDatabaseSize(){
	int dbSize=0;
	FileConnection fileConnect;
	
	ProgramParameters programParameters = ProgramParameters.getProgramParameters();
	String patch = programParameters.getDatabaseDirectoryPatch()+programParameters.getDatabaseFileName();
	try {
		fileConnect = (FileConnection)Connector.open(patch);
		dbSize = (int)fileConnect.fileSize();
	} catch(IOException ioe) {
	}
	return dbSize;
}

}