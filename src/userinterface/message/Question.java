package message;
import java.io.*;
import javax.microedition.lcdui.*;
import userinterface.UserInterface;
import userinterface.Colors;

public class Question extends AbstractInterfaceScreen{

private int commandsFieldVisota;
private Font commandsFont;
private QuestionListener questionListener;
private final int commandTextIdent = 1;

private static final String YES ="да";
private static final String NO ="нет";
private final static int LEFT_KEY = -6;
private final static int RIGHT_KEY = -7;

public Question(String questionText, QuestionListener inQuestionListener){
	super(questionText, MODE_QUESTION);
	questionListener = inQuestionListener;
	
	//Шрифт списка команд
	commandsFont = Font.getFont(Font.FACE_SYSTEM, Font.STYLE_BOLD, Font.SIZE_LARGE);
	
	//Высота поля для комманд
	commandsFieldVisota = commandsFont.getHeight()+(commandTextIdent*2);
}

protected void extendsPaint(Graphics g){
	int positionCommandFieldVisota = getScreenVisota()-commandsFieldVisota;
	g.setColor(Colors.YELLOW);
	g.fillRect(0, positionCommandFieldVisota, getScreenShirina(), getScreenVisota());
	g.setColor(Colors.WHITE);
	g.drawString(YES, commandTextIdent, (positionCommandFieldVisota-commandTextIdent), g.TOP|g.LEFT);
	g.drawString(NO, getScreenShirina()-commandTextIdent, (positionCommandFieldVisota-commandTextIdent), g.TOP|g.RIGHT);
}



public void keyPressed(int keyCode) {
	if (keyCode == LEFT_KEY){
		questionListener.selectYes();
		return;
	}
	if (keyCode == RIGHT_KEY){
		questionListener.selectNo();
		return;
	}
}

}