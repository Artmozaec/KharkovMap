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
	
	//���� �����
	gorizontal = new TextField("�����������", "", 5, TextField.NUMERIC);
	vertical   = new TextField("���������", "", 5, TextField.NUMERIC);
	
	//�������
	ok = new Command("Ok", Command.ITEM, 0);
	cancel = new Command("cancel", Command.ITEM, 0);
	
	//��������� ����
	this.append(gorizontal);
	this.append(vertical);
	
	
	//��������� ��������
	this.addCommand(ok);
	this.addCommand(cancel);
	
	//���������� �����!
	this.setCommandListener(this);
}

private void jump(){
	int horizontalIndex = Integer.parseInt(gorizontal.getString());
	int verticalIndex = Integer.parseInt(vertical.getString());
	
	Fragment newFragment = new Fragment(horizontalIndex, verticalIndex, 0, 0);
	//�������� ������� ��������, ���-�� � ����� Display � ������ ���������
	
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