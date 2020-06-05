package programdirection;

import javax.microedition.lcdui.*;
import javax.microedition.rms.RecordStore;
import javax.microedition.rms.RecordEnumeration;
import javax.microedition.rms.RecordStoreException;
import rms.SaveRestoreProgramParameters;
import rms.DirectoryPatch;
import mapvisiblecontent.Fragment;

public final class ProgramParameters extends Canvas{

private static ProgramParameters programParametersInstance = new ProgramParameters();

//расстояние в пикселях перемещения изображения карты по экрану
private int deltaMoving;

private int screenVisota;
private int screenShirina;

//Ссылка на текущий отображаемый объект
private Display currentDisplay;

//Ссылка на текущий элемент управления
private ProgramDirection programDirection;

//В каком состоянии отображение программы 
//0- спутник
//1- карта
//2- карта малого масштаба
private int programMode;

//Переменная для временного сохранения режима программы
private int savedProgramMode;


//Сторона изображения
private int imageSize;

//Левый верхний фрагмент экрана отображаемого при запуске мидлета
//Восстанавливается из хранилища
private Fragment firstScreenFragment;

//private final String WORK_PATCH = "file:///E:/map/";
//private final String WORK_PATCH = "file:///map/"; //Для microemulator
//private final String WORK_PATCH = "file:///root1/map/";
private String workPatch;
private final String BIGMAP_FOLDER = "map/";
private final String BIGSATELIT_FOLDER = "satelit/";
private final String SMALLMAP_FOLDER = "smallmap/";
private final String DATABASE_FOLDER = "db/";
private final String TMP_DATABASE_NAME = "db.tmp";
private final String DATABASE_NAME = "db.txt";
private final String OLD_DATABASE_NAME = "olddb.txt";
private final String FILE_TYPE_GIF = ".gif";
private final String FILE_TYPE_JPEG = ".jpg";

public static final int MODE_BIGSATELIT = 0;
public static final int MODE_BIGMAP = 1;
public static final int MODE_SMALLMAP = 2;
public static final int MODE_ENDMODES = 3;
//public static final int MODE_SATELIT_CURSOR = 2;
//public static final int MODE_MAP_CURSOR = 3;



private ProgramParameters(){
	
	setFullScreenMode(true);
	screenVisota = getHeight();
	screenShirina = getWidth();
	
	
	deltaMoving = 30;
	programMode = 0;
	imageSize = 256;//256 - пикселей
	System.out.println("screenVisota = "+screenVisota);
	System.out.println("screenShirina = "+screenShirina);
}


///////////////////////////ОСНОВНЫЕ ПУТИ


public String[] getWorkFolders(){
	return new String[]{SMALLMAP_FOLDER, DATABASE_FOLDER};
}

private String getCurrentModeFolder(){
	if (programMode == MODE_BIGSATELIT){
		return BIGSATELIT_FOLDER;
	} else 
	if (programMode == MODE_BIGMAP){
		return BIGMAP_FOLDER;
	} else 
	if (programMode == MODE_SMALLMAP){
		return SMALLMAP_FOLDER;
	}
	return null;
}

public String getCurrentModeFileType(){
	if (programMode == MODE_BIGSATELIT){
		return FILE_TYPE_JPEG;
	} else 
	if (programMode == MODE_BIGMAP){
		return FILE_TYPE_GIF;
	} else 
	if (programMode == MODE_SMALLMAP){
		return FILE_TYPE_GIF;
	}
	return null;
}


public boolean patchExist(){
	if (workPatch == null) return false;
	return true;
}

//Изменение основного пути
public void setPatch(String newPatch){
	workPatch = newPatch;
}

public String getCurrentModePatch(){
	return workPatch+getCurrentModeFolder();
}

//Путь к базе данных
public String getDatabaseDirectoryPatch(){
	return workPatch+DATABASE_FOLDER;
}

public String getOldDatabaseName(){
	return OLD_DATABASE_NAME;
}

public String getDatabaseFileName(){
	return DATABASE_NAME;
}

public String getDatabaseTempName(){
	return TMP_DATABASE_NAME;
}

//////////////////////////////////////////

/////////////////////////////////////////РАЗМЕРЫ ЭКРАНА

public int getScreenVisota(){
	 return screenVisota;
}

public int getScreenShirina(){
	 return screenShirina;
}
////////////////////////////////////////////////////

//////////////////////////////////////ОСНОВНЫЕ ССЫЛКИ
public static ProgramParameters getProgramParameters(){
	return programParametersInstance;
}

public void setCurrentDisplay(Display display){
	currentDisplay=display;
}

public Display getCurrentDisplay(){
	return currentDisplay;
}

public ProgramDirection getProgramDirection(){
	return programDirection;
}

public void setProgramDirection(ProgramDirection inProgramDirection){
	programDirection = inProgramDirection;
}
///////////////////////////////////////////////////


////////////////////////////////////ШАГ ДВИЖЕНИЯ ПО ЭКРАНУ
public void setDeltaMoving(int newDeltaMoving){
	deltaMoving = newDeltaMoving;
}

public int getDeltaMoving(){
	return deltaMoving;
}
///////////////////////////////////////////////////////////


//////////////////////////////////////ГЛОБАЛЬНЫЙ СТАТУС ПРОГРАММЫ

//Сохраняет текущий статус программы
public void saveCurrentMode(){
	savedProgramMode = programMode;
}

//Воостанавливает сохранённый режим программы
public void restoreSavedMode(){
	programMode = savedProgramMode;
}


//Переключатель режимов
public void nextModeFromBigMap(){
	programMode++;
	//Сброс на самый первый режим
	//Выбор MODE_SMALLMAP организуется не последовательным переключением режимов а отдельной клавишей "0"
	if (programMode == MODE_SMALLMAP) programMode = MODE_BIGSATELIT;
}


public void setModeSmallmap(){
	programMode = MODE_SMALLMAP;
}

public void setModeBigSatelit(){
	programMode = MODE_BIGSATELIT;
}

public void setModeBigMap(){
	programMode = MODE_BIGMAP;
}

public int getMode(){
	return programMode;
}

//////////////////////////////////////////////////////////////////


public Fragment getFirstScreenFragment(){
		return firstScreenFragment;
}

public int getImageSize(){
	return imageSize;
}


private void savePosition(SaveRestoreProgramParameters save){
	Fragment leftUp;
	byte indexHorizontal;
	byte indexVertical;
	
	int positionShirina;
	int positionVisota;
	
	//Текущий левый верхний фрагмент
	leftUp = programDirection.getCurrentLeftUpFragment();
	
	//Индекс файла по горизонтали
	indexHorizontal = (byte)leftUp.getNameIndexHorizontal();
	
	//Индекс файла по вертикали
	indexVertical = (byte)leftUp.getNameIndexVertical();
	
	//Отступ на экране по ширине
	positionShirina = leftUp.getDrawPositionShirina();
	
	//Отступ на экране по высоте
	positionVisota = leftUp.getDrawPositionVisota();
	
	
	
	//ЗАПИСЫВАЕМ
	save.setIndexHorizontalFirstPosition(indexHorizontal);
	save.setIndexVerticalFirstPosition(indexVertical);
	
	save.setFirstPositionShirinaIdent(positionShirina);
	save.setFirstPositionVisotaIdent(positionVisota);
}

private void restorePosition(SaveRestoreProgramParameters restore){

	//Создаем ключевой фрагмент
	firstScreenFragment = new Fragment(restore.getIndexHorizontalFirstPosition(),
									   restore.getIndexVerticalFirstPosition(),
									   restore.getFirstPositionShirinaIdent(),
									   restore.getFirstPositionVisotaIdent());
}



//Организует сохранение параметров программы
public void saveParameters(){
	//Создаем объект сохранения
	SaveRestoreProgramParameters save = SaveRestoreProgramParameters.getInstance();

	//Сохраняем текущую позицию отображения
	savePosition(save);
	
	//СОХРАНИТЬ!
	save.saveParameters();
}


//Сохранение рабочего пути в хранилище
public void savePatch(){
	DirectoryPatch dirPatch = DirectoryPatch.getInstance();
	dirPatch.saveWorkPatch(workPatch);
}

//Организует чтение сохраненных в хранилище параметров программы
public void restoreParameters(){
	//Создаем объект чтения параметров программы
	SaveRestoreProgramParameters restore = SaveRestoreProgramParameters.getInstance();
	
	//Читаем текущую позицию отображения
	restorePosition(restore);
	
	//Создаём объект чтения пути к файлу
	DirectoryPatch dirPatch = DirectoryPatch.getInstance();
	
	//Берём результат
	workPatch = dirPatch.getPatch();
	//Временное изменение для удобства работы! Что-бы не вызывалось постоянно окно выбора рабочей директории 
	//workPatch = "file:///root1/map/";
	//workPatch = "file:///map/";
}

public void paint(Graphics g){
}

}