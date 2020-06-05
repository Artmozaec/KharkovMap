package userinterface;
import javax.microedition.lcdui.*;


public class Help extends Form implements CommandListener{

private final String text="“ут будет текст помощи“ут будет текст помощи“ут будет текст помощи“ут будет текст помощи“ут будет текст помощи“ут будет текст помощи“ут будет текст помощи“ут будет текст помощи“ут будет текст помощи“ут будет текст помощи";
private Command back;
private UserInterface userInterface;


Help(UserInterface inUserInterface){
	super("help");
	userInterface = inUserInterface;
	back = new Command("назад", Command.ITEM, 0);
	this.addCommand(back);
	this.setCommandListener(this);
	this.append(text);
}

public void commandAction(Command c, Displayable d) {
	userInterface.goToBackScreen();
}

}