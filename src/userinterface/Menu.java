package userinterface;
import javax.microedition.lcdui.*;


public class Menu extends List implements CommandListener{

private Command ok;
private UserInterface userInterface;


private static final String[] commandArrayMap = new String[]{
	"�����",
	"��������",
	"�������",
	"� ���������",
	"������",
	"�����",
	"�����"	
};

Menu (UserInterface inUserInterface){
	//������� ����
	super("kharkovMap", 
							List.IMPLICIT, 
							commandArrayMap,
							null);
	
	//������� ������� 
	ok = new Command("Ok", Command.ITEM, 0);
	this.addCommand(ok);
	this.setCommandListener(this);
	
	userInterface = inUserInterface;

}

public void commandAction(Command c, Displayable d) {
	int selectNumber = this.getSelectedIndex();
	String selectCommand = this.getString(selectNumber);
	
	if (selectCommand.equals("�����")) {
		userInterface.exitProgram();
	} else if (selectCommand.equals("�������")){
			userInterface.ShowGoToScreen();
	} else if (selectCommand.equals("�����")){
			userInterface.goToBackScreen(); //������� � ����������� ������ - �� ���� ����� � �����
	} else if (selectCommand.equals("�����")){
			userInterface.showSearch();
	} else if (selectCommand.equals("� ���������")){
			userInterface.showAbout();
	} else if (selectCommand.equals("������")){
			userInterface.showHelp();
	} else if (selectCommand.equals("��������")){
			userInterface.showAddRecord();
	}
}

}