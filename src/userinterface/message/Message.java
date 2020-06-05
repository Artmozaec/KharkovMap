package message;
import java.io.*;
import javax.microedition.lcdui.*;
import userinterface.UserInterface;

public class Message extends AbstractInterfaceScreen{




private UserInterface userInterface;



private void goToNextDisplay(){
	//Назад - как-бы вперёд
	userInterface.goToBackScreen();
}

public Message(UserInterface inUserInterface, String inMessageText, int inMessageMode){
	super(inMessageText, inMessageMode);
	userInterface = inUserInterface;
}


protected void extendsPaint(Graphics g){
	
}


public void keyPressed(int keyCode) {
	goToNextDisplay();
}
}