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

//���������� � �������� ����������� ����������� ����� �� ������
private int deltaMoving;

private int screenVisota;
private int screenShirina;

//������ �� ������� ������������ ������
private Display currentDisplay;

//������ �� ������� ������� ����������
private ProgramDirection programDirection;

//� ����� ��������� ����������� ��������� 
//0- �������
//1- �����
//2- ����� ������ ��������
private int programMode;

//���������� ��� ���������� ���������� ������ ���������
private int savedProgramMode;


//������� �����������
private int imageSize;

//����� ������� �������� ������ ������������� ��� ������� �������
//����������������� �� ���������
private Fragment firstScreenFragment;

//private final String WORK_PATCH = "file:///E:/map/";
//private final String WORK_PATCH = "file:///map/"; //��� microemulator
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
	imageSize = 256;//256 - ��������
	System.out.println("screenVisota = "+screenVisota);
	System.out.println("screenShirina = "+screenShirina);
}


///////////////////////////�������� ����


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

//��������� ��������� ����
public void setPatch(String newPatch){
	workPatch = newPatch;
}

public String getCurrentModePatch(){
	return workPatch+getCurrentModeFolder();
}

//���� � ���� ������
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

/////////////////////////////////////////������� ������

public int getScreenVisota(){
	 return screenVisota;
}

public int getScreenShirina(){
	 return screenShirina;
}
////////////////////////////////////////////////////

//////////////////////////////////////�������� ������
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


////////////////////////////////////��� �������� �� ������
public void setDeltaMoving(int newDeltaMoving){
	deltaMoving = newDeltaMoving;
}

public int getDeltaMoving(){
	return deltaMoving;
}
///////////////////////////////////////////////////////////


//////////////////////////////////////���������� ������ ���������

//��������� ������� ������ ���������
public void saveCurrentMode(){
	savedProgramMode = programMode;
}

//��������������� ���������� ����� ���������
public void restoreSavedMode(){
	programMode = savedProgramMode;
}


//������������� �������
public void nextModeFromBigMap(){
	programMode++;
	//����� �� ����� ������ �����
	//����� MODE_SMALLMAP ������������ �� ���������������� ������������� ������� � ��������� �������� "0"
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
	
	//������� ����� ������� ��������
	leftUp = programDirection.getCurrentLeftUpFragment();
	
	//������ ����� �� �����������
	indexHorizontal = (byte)leftUp.getNameIndexHorizontal();
	
	//������ ����� �� ���������
	indexVertical = (byte)leftUp.getNameIndexVertical();
	
	//������ �� ������ �� ������
	positionShirina = leftUp.getDrawPositionShirina();
	
	//������ �� ������ �� ������
	positionVisota = leftUp.getDrawPositionVisota();
	
	
	
	//����������
	save.setIndexHorizontalFirstPosition(indexHorizontal);
	save.setIndexVerticalFirstPosition(indexVertical);
	
	save.setFirstPositionShirinaIdent(positionShirina);
	save.setFirstPositionVisotaIdent(positionVisota);
}

private void restorePosition(SaveRestoreProgramParameters restore){

	//������� �������� ��������
	firstScreenFragment = new Fragment(restore.getIndexHorizontalFirstPosition(),
									   restore.getIndexVerticalFirstPosition(),
									   restore.getFirstPositionShirinaIdent(),
									   restore.getFirstPositionVisotaIdent());
}



//���������� ���������� ���������� ���������
public void saveParameters(){
	//������� ������ ����������
	SaveRestoreProgramParameters save = SaveRestoreProgramParameters.getInstance();

	//��������� ������� ������� �����������
	savePosition(save);
	
	//���������!
	save.saveParameters();
}


//���������� �������� ���� � ���������
public void savePatch(){
	DirectoryPatch dirPatch = DirectoryPatch.getInstance();
	dirPatch.saveWorkPatch(workPatch);
}

//���������� ������ ����������� � ��������� ���������� ���������
public void restoreParameters(){
	//������� ������ ������ ���������� ���������
	SaveRestoreProgramParameters restore = SaveRestoreProgramParameters.getInstance();
	
	//������ ������� ������� �����������
	restorePosition(restore);
	
	//������ ������ ������ ���� � �����
	DirectoryPatch dirPatch = DirectoryPatch.getInstance();
	
	//���� ���������
	workPatch = dirPatch.getPatch();
	//��������� ��������� ��� �������� ������! ���-�� �� ���������� ��������� ���� ������ ������� ���������� 
	//workPatch = "file:///root1/map/";
	//workPatch = "file:///map/";
}

public void paint(Graphics g){
}

}