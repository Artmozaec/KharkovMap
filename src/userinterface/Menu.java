package userinterface;
import javax.microedition.lcdui.*;


public class Menu extends List implements CommandListener{

private Command ok;
private UserInterface userInterface;


private static final String[] commandArrayMap = new String[]{
	"Поиск",
	"Добавить",
	"Перейти",
	"О программе",
	"Помощь",
	"Назад",
	"Выход"	
};

Menu (UserInterface inUserInterface){
	//Создаем меню
	super("kharkovMap", 
							List.IMPLICIT, 
							commandArrayMap,
							null);
	
	//создаем команду 
	ok = new Command("Ok", Command.ITEM, 0);
	this.addCommand(ok);
	this.setCommandListener(this);
	
	userInterface = inUserInterface;

}

public void commandAction(Command c, Displayable d) {
	int selectNumber = this.getSelectedIndex();
	String selectCommand = this.getString(selectNumber);
	
	if (selectCommand.equals("Выход")) {
		userInterface.exitProgram();
	} else if (selectCommand.equals("Перейти")){
			userInterface.ShowGoToScreen();
	} else if (selectCommand.equals("Назад")){
			userInterface.goToBackScreen(); //Переход к предыдущему экрану - то есть назад к карте
	} else if (selectCommand.equals("Поиск")){
			userInterface.showSearch();
	} else if (selectCommand.equals("О программе")){
			userInterface.showAbout();
	} else if (selectCommand.equals("Помощь")){
			userInterface.showHelp();
	} else if (selectCommand.equals("Добавить")){
			userInterface.showAddRecord();
	}
}

}