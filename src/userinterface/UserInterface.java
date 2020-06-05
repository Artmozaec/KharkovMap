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
//Из этого класса создаются и отображаются основные элементы пользовательского интерфейса. Так же он возвращает управление классу харьков мап и.
//При поиске нужной улицы очень удобно было бы оставлять результат последнего поиска в окне. Для этого необходимо хранить ссылку на используемый объект поиска ссылка будет хранится в этом классе



public final class UserInterface{
private static UserInterface userInterfaceInstance = new UserInterface();

private UserInterfaceListener userInterfaceListener;

//Ссылка на форму поиска в базе данных - что бы запоминались последнии результаты поиска
private Search search;

private ScreenManager screenManager;


private UserInterface(){
	ProgramParameters programParameters = ProgramParameters.getProgramParameters();
	
	//Создаем менеджер дисплеев
	screenManager = new ScreenManager(programParameters.getCurrentDisplay());
}

public void changeUserInterfaceListener(UserInterfaceListener inListener){
	userInterfaceListener = inListener;
}

public static UserInterface getUserInterface(){
	return userInterfaceInstance;
}

//////////////////////////////////////////ВЫЗОВ СООБЩЕНИЙ////////////////////////////////

public void showErrorMessage(String message){
	Message mes = new Message(this, message, Message.MODE_ERROR);

	//Отображаем сообщение
	screenManager.showCurrent(mes);
}

public void showInfoMessage(String message){
	Message mes = new Message(this, message, Message.MODE_INFO);

	//Отображаем сообщение
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
	//Содаём основной объект 
	ProgressIndicator progressIndicator = new ProgressIndicator(this, inProcess, name);
	
	//Показываем
	screenManager.showCurrent(progressIndicator.getDisplay());

}

//Показывает форму поиска
public void showSearch(){
	//Проводился ли вообще поиск?
	if (search == null){ //Не проводился
		//Создаем форму поиска
		search = new Search(this);
	} 
	
	//Показываем
	screenManager.showCurrent(search);
}

public void exitProgram(){
	userInterfaceListener.exitProgram();
}

public void goToBackScreen(){
	screenManager.restoreSavedSvreen();
}

//Осуевстляет переход к карте 
public void goToMap(Fragment inFragment){
	//очищаем менеджер экранов
	screenManager.clear();
	
	//переход
	userInterfaceListener.goToFragment(inFragment);
}

//Показывает форму добавления
public void showAddRecord(){
	AddRecordToDatabase addRecordToDatabase = new AddRecordToDatabase(this,  userInterfaceListener.getCurrentLeftUpFragment());
	
	
	//отображаем его на диплее
	screenManager.showCurrent(addRecordToDatabase);
}

public void ShowGoToScreen(){
	GoToScreen goToScreen;
	goToScreen = new GoToScreen(this);
	
	//отображаем его на диплее
	screenManager.showCurrent(goToScreen);
}


public void showAbout(){
	About about;
	//Создаём окно
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
	
	//создаем меню
	currentMenu = new Menu(this);

	//отображаем меню на диплее
	screenManager.showCurrent(currentMenu);
}


}
