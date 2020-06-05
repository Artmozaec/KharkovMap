package userinterface;

import javax.microedition.lcdui.*;
import mapvisiblecontent.Fragment;

class GoToScreen extends Form implements CommandListener{
private TextField gorizontal;
private TextField vertical;
private Command ok;
private Command cancel;

private UserInterface userInterface;

GoToScreen(UserInterface inUserInterface){
	super("goToLine");

	userInterface = inUserInterface;
	
	//Поля ввода
	gorizontal = new TextField("горизонталь", "", 5, TextField.NUMERIC);
	vertical   = new TextField("вертикаль", "", 5, TextField.NUMERIC);
	
	//команды
	ok = new Command("Ok", Command.ITEM, 0);
	cancel = new Command("cancel", Command.ITEM, 0);
	
	//добавляем поля
	this.append(gorizontal);
	this.append(vertical);
	
	
	//Добавляем комманды
	this.addCommand(ok);
	this.addCommand(cancel);
	
	//Обработчик здесь!
	this.setCommandListener(this);
}

private void jump(){
	int horizontalIndex = Integer.parseInt(gorizontal.getString());
	int verticalIndex = Integer.parseInt(vertical.getString());
	
	Fragment newFragment = new Fragment(horizontalIndex, verticalIndex, 0, 0);
	//Вызываем функцию перехода, она-же и вернёт Display в нужное состояние
	
	userInterface.goToMap(newFragment);
}

public void commandAction(Command c, Displayable d) {
	if(c==ok){
		jump();
	} else if (c==cancel) {
		userInterface.goToBackScreen();
	}
	
}

}