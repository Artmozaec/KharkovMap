package programdirection;


import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.*;
import userinterface.UserInterface;
import mapvisiblecontent.BigMapVisibleContent;
import mapvisiblecontent.VisibleContent;
import mapvisiblecontent.SmallMapVisibleContent;
import mapvisiblecontent.Fragment;
import mapvisiblecontent.CheckAndNames;
import javax.microedition.midlet.MIDlet;
import filebrowser.FileBrowserListener;
import userinterface.UserInterfaceListener;

public class ProgramDirection implements FileBrowserListener, UserInterfaceListener{
private Display display;
private UserInterface userInterface;
private VisibleContent visibleContent;
private KharkovMap midlet;
private ProgramParameters programParameters;
private Fragment defaultFragment = new Fragment(5, 5, -10, -10);

public ProgramDirection(KharkovMap inMidl){
	midlet = inMidl;
	programParameters = ProgramParameters.getProgramParameters();
	
	//�������������� ��������� ������������
	userInterface = UserInterface.getUserInterface();
	
	
	//���������� ������� ���������� �����!
	userInterface.changeUserInterfaceListener(this);
	
	
	//������ �� ���� ����� ����������!
	programParameters.setProgramDirection(this);
		
	//������� ������������
	display = programParameters.getCurrentDisplay(); 
	
	
	//�������� ������� ������ �������� ��������� ��������� ����������� �������
	Fragment initFragment = programParameters.getFirstScreenFragment();
	
	//��������� ������������ �������� ����
		if (!goToStartFragment(initFragment)){
			//���� ��������! ������ ���� ������!
			userInterface.showFileBrowser(this);
		}
}

private boolean goToStartFragment(Fragment inFragment){
	//��������� ����� �� ����
	//����������-�� ���� �� ���������?
	if (!programParameters.patchExist()) return false;
	
	//������� �������� ����� ����� ���������� ������
	if (goToAvailableMode(inFragment)) return true;
	
	//����� �������� ��������� � ���� � ����� ����� ������
	if (goToAvailableMode(defaultFragment)) return true;
	
	//�� � ����� ������ ��� ��������� ����������
	return false;
}



//���������� ������ ���������� � ���� ������
public void patchChoosed(String patch){
	System.out.println("!!!!!!!!!!!!!!fileChoosed!!!!!!!!!!!!!!"+patch);
	//��������� ��������� ����-�������� � ����� �������-����� ���� �����
	Fragment initFragment = defaultFragment;
	
	//������ ���������� ����
	programParameters.setPatch(patch);
	
	//��������� ������� ���������� - ���������� ����
	if (! goToStartFragment(initFragment)){
		//������ ���������� ���, ����� �������� ����� ����������
		userInterface.showFileBrowser(this);
	} else {
		//���������� ���� � ���������
		programParameters.savePatch();
		//��������� ����, ���������� �����
		createAndShowBigMapContent(initFragment);
	}
}




//��������� ������������ ���� � ������������ �������� � ��������� �������� ������
private boolean checkValidCurrentPatch(Fragment initFragment){
	//����������-�� ���� �� ���������?
	if (!programParameters.patchExist()) return false;
	
	//�������������-�� ������� ��������� �������� ������?
	if (!CheckAndNames.AllFragmentExist(initFragment)) return false;
	
	return true;
}

private void createAndShowBigMapContent(Fragment initFragment){
	//������� ������ �����
	visibleContent = new BigMapVisibleContent(initFragment);
	//���������� ������ �� ������
	showScreenContent();
}


private void createAndShowSmallMapContent(Fragment initFragment){
	//������� ������ �����
	visibleContent = new SmallMapVisibleContent(initFragment);
	//���������� ������ �� ������
	showScreenContent();
}


private Fragment getKeyFragmentFromBigMap(){
	Fragment leftUp = null;
	//���� ������� �����
	if ((programParameters.getMode() == ProgramParameters.MODE_BIGSATELIT) || 
		(programParameters.getMode() == ProgramParameters.MODE_BIGMAP)){
		leftUp = ((BigMapVisibleContent)visibleContent).getBigMapLeftUpFragment();
	} else { //���� ����� �����
		leftUp = ((SmallMapVisibleContent)visibleContent).getBigMapLeftUpFragment();
	}
	return leftUp;
}

private Fragment getKeyFragmentFromSmallMap(){
	Fragment leftUp = null;
	//���� ������� �����
	if ((programParameters.getMode() == ProgramParameters.MODE_BIGSATELIT) || 
		(programParameters.getMode() == ProgramParameters.MODE_BIGMAP)){
		leftUp = ((BigMapVisibleContent)visibleContent).getSmallMapLeftUpFragment();
	} else { //���� ����� �����
		leftUp = ((SmallMapVisibleContent)visibleContent).getSmallMapLeftUpFragment();
	}
	return leftUp;
}


public void exitProgram(){
	//��������� ������� ������� ������
	programParameters.saveParameters();
	System.out.println("exit");
	//����-�� �����
	
	midlet.destroyApp(true);
}

public void bigMapMode(){
	//�������� ����� ������� �������� �������� ������� �����������, 
	//��� ���� � ������� ����������� ������� ������� �����������
	Fragment leftUpFragment = getKeyFragmentFromBigMap();
	
	//�������� ������� ������� � ���������� �� ������
	if (!goToBigMap(leftUpFragment)){
		//�� �������
		userInterface.showErrorMessage("������ ��������� ������� ����� �����������!");
	}
}

private boolean goToBigMap(Fragment inFragment){
	//���������� ������� �����
	programParameters.saveCurrentMode();
	
	//������ ���������� ����� �� ���� ������� ����� ���� � ������ ��� �������� ������� �����������
	programParameters.setModeBigMap();
	
	//���� ��� ��������� ����� ������������ - ������ �������
	if (CheckAndNames.AllFragmentExist(inFragment)){
		//������� ������ bigMap
		//���������� ������ �� ������
		createAndShowBigMapContent(inFragment);
		return true;
	} else { //�������� ����� ������� ����������, ������� �� ������
		//��������������� ���������� �����
		programParameters.restoreSavedMode();
		return false;
	}
}




public void bigSatelitMode(){
	//�������� ����� ������� �������� �������� ������� �����������, 
	//��� ���� � ������� ����������� ������� ������� �����������
	Fragment leftUpFragment = getKeyFragmentFromBigMap();
	
	//�������� ������� ������� � ���������� �� ������
	if (!goToBigSatelit(leftUpFragment)){
		//�� �������
		userInterface.showErrorMessage("������ ��������� ������� ����� �����������!");
	}
}

private boolean goToBigSatelit(Fragment inFragment){
	//���������� ������� �����
	programParameters.saveCurrentMode();
	
	//������ ���������� ����� �� ���� ������� ����� ���� � ������ ��� �������� ������� �����������
	programParameters.setModeBigSatelit();
	
	//���� ��� ��������� ����� ������������ - ������ �������
	if (CheckAndNames.AllFragmentExist(inFragment)){
		//������� ������ bigMap
		//���������� ������ �� ������
		createAndShowBigMapContent(inFragment);
		return true;
	} else { //�������� ����� ������� ����������, ������� �� ������
		//��������������� ���������� �����
		programParameters.restoreSavedMode();
		return false;
	}
}


public void smallMapMode(){
	//�������� ����� ������� �������� �������� ������� �����������, 
	//��� ���� � ������� ����������� ������� ������� �����������
	Fragment leftUpFragment = getKeyFragmentFromSmallMap();
	if (!goToSmallMap(leftUpFragment)){
		userInterface.showErrorMessage("������ ��������� ����� ����� �����������!");
	}
}

private boolean goToSmallMap(Fragment inFragment){
	//���������� ������� �����
	programParameters.saveCurrentMode();
	
	//������ ���������� ����� �� ���� ������� ����� ���� � ������ ��� �������� ������� �����������
	programParameters.setModeSmallmap();
	
	//���� ��� ��������� ����������� ����� ������������ - ������ �������
	if (CheckAndNames.AllFragmentExist(inFragment)){
		//������� ������ smallMap
		//���������� ������ �� ������
		createAndShowSmallMapContent(inFragment);
		return true;
	} else { //�������� ����� ����� ����������, ������� �� ������
		//��������������� ���������� �����
		programParameters.restoreSavedMode();
		return false;
	}
}



public void bigModeChanger(){
	if ((programParameters.getMode() == ProgramParameters.MODE_BIGSATELIT)){ 
		bigMapMode();
		return;
	}
	if ((programParameters.getMode() == ProgramParameters.MODE_BIGMAP)){
		bigSatelitMode();
		return;
	}
}


public void goToFragment(Fragment inFragment){
	//���� ����� ����� �����
	if (programParameters.getMode() == ProgramParameters.MODE_SMALLMAP){
		//������ �������� ��������
		Fragment smallMapFragment = CheckAndNames.bigToSmallFragment(inFragment);
		//������� �������
		if (goToSmallMap(smallMapFragment)) return;
		
	} else {//���� ����� ������� �����
		//������� �������
		if (goToBigMap(inFragment)) return;
		
	}
	
	//������� ������� ���� ���������, �������� ������ ��������� �����
	if (goToAvailableMode(inFragment)) return;
	
	//������� ���������� �� � ����� �� �������!
	userInterface.showErrorMessage("������ ��������� �����������!");
}

//���� ���������� �����
private boolean goToAvailableMode(Fragment inFragment){
	//���������� ������� �����
	programParameters.saveCurrentMode();
	
	//����� ������� �����
	programParameters.setModeBigMap();
	
	//���������, ���� ��������� ���� ����� ���������
	if (CheckAndNames.AllFragmentExist(inFragment)){
		createAndShowBigMapContent(inFragment);
		return true;
	}	
	
	//����� ��������
	programParameters.setModeBigSatelit();
	
	//���������, ���� ��������� ���� ����� ���������
	if (CheckAndNames.AllFragmentExist(inFragment)){
		createAndShowBigMapContent(inFragment);
		return true;
	}
	
	//����� ����� �����
	programParameters.setModeSmallmap();
	
	//��������� �������� � ���������� ����� �����
	Fragment smallMapFragment = CheckAndNames.bigToSmallFragment(inFragment);
	
	//���������, ���� ��������� ���� ����� ���������
	if (CheckAndNames.AllFragmentExist(smallMapFragment)){
		createAndShowSmallMapContent(smallMapFragment);
		return true;
	}
	
	//��������������� ���������� �����
	programParameters.restoreSavedMode();
	
	return false;
}

public void showScreenContent(){
	display.setCurrent(visibleContent.getContent());
}

//������� ����� ������� ��������, ������������ �� ����� ������ ����, ����� ���� ��� ���������� � ���� ������
public Fragment getCurrentLeftUpFragment(){
	System.out.println("getCurrentLeftUpFragment");
	return getKeyFragmentFromBigMap();
}

}