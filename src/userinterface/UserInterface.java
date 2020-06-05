package userinterface;
import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.*;
import programdirection.ProgramParameters;
import mapvisiblecontent.Fragment;
import message.Message;
import filebrowser.FileBrowserListener;
import filebrowser.FileBrowser;
import progressindicator.Process;
import progressindicator.ProgressIndicator;
import message.QuestionListener;
import message.Question;
//�� ����� ������ ��������� � ������������ �������� �������� ����������������� ����������. ��� �� �� ���������� ���������� ������ ������� ��� �.
//��� ������ ������ ����� ����� ������ ���� �� ��������� ��������� ���������� ������ � ����. ��� ����� ���������� ������� ������ �� ������������ ������ ������ ������ ����� �������� � ���� ������



public final class UserInterface{
private static UserInterface userInterfaceInstance = new UserInterface();

private UserInterfaceListener userInterfaceListener;

//������ �� ����� ������ � ���� ������ - ��� �� ������������ ��������� ���������� ������
private Search search;

private ScreenManager screenManager;


private UserInterface(){
	ProgramParameters programParameters = ProgramParameters.getProgramParameters();
	
	//������� �������� ��������
	screenManager = new ScreenManager(programParameters.getCurrentDisplay());
}

public void changeUserInterfaceListener(UserInterfaceListener inListener){
	userInterfaceListener = inListener;
}

public static UserInterface getUserInterface(){
	return userInterfaceInstance;
}

//////////////////////////////////////////����� ���������////////////////////////////////

public void showErrorMessage(String message){
	Message mes = new Message(this, message, Message.MODE_ERROR);

	//���������� ���������
	screenManager.showCurrent(mes);
}

public void showInfoMessage(String message){
	Message mes = new Message(this, message, Message.MODE_INFO);

	//���������� ���������
	screenManager.showCurrent(mes);
}
////////////////////////////////////////////////////////////////////////////////////////

public void showQuestion(String questionText, QuestionListener questionListener){
	Question question = new Question(questionText, questionListener);
	
	screenManager.showCurrent(question);
}

public void showFileBrowser(FileBrowserListener listener){
	FileBrowser fileBrowser;
	ProgramParameters programParameters = ProgramParameters.getProgramParameters();
	fileBrowser = new FileBrowser(listener, programParameters.getWorkFolders());
	screenManager.showCurrent(fileBrowser);
}

public void showProgressIndicator(Process inProcess, String name){
	//����� �������� ������ 
	ProgressIndicator progressIndicator = new ProgressIndicator(this, inProcess, name);
	
	//����������
	screenManager.showCurrent(progressIndicator.getDisplay());

}

//���������� ����� ������
public void showSearch(){
	//���������� �� ������ �����?
	if (search == null){ //�� ����������
		//������� ����� ������
		search = new Search(this);
	} 
	
	//����������
	screenManager.showCurrent(search);
}

public void exitProgram(){
	userInterfaceListener.exitProgram();
}

public void goToBackScreen(){
	screenManager.restoreSavedSvreen();
}

//����������� ������� � ����� 
public void goToMap(Fragment inFragment){
	//������� �������� �������
	screenManager.clear();
	
	//�������
	userInterfaceListener.goToFragment(inFragment);
}

//���������� ����� ����������
public void showAddRecord(){
	AddRecordToDatabase addRecordToDatabase = new AddRecordToDatabase(this,  userInterfaceListener.getCurrentLeftUpFragment());
	
	
	//���������� ��� �� ������
	screenManager.showCurrent(addRecordToDatabase);
}

public void ShowGoToScreen(){
	GoToScreen goToScreen;
	goToScreen = new GoToScreen(this);
	
	//���������� ��� �� ������
	screenManager.showCurrent(goToScreen);
}


public void showAbout(){
	About about;
	//������ ����
	about = new About(this);
	
	screenManager.showCurrent(about);
}

public void showHelp(){
	Help help;
	help = new Help(this);
	screenManager.showCurrent(help);
}

public void showMenu(){
	Menu currentMenu;
	
	//������� ����
	currentMenu = new Menu(this);

	//���������� ���� �� ������
	screenManager.showCurrent(currentMenu);
}


}
