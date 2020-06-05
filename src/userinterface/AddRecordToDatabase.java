package userinterface;

import javax.microedition.lcdui.*;
import databaseacess.DatabaseRecord;
import databaseacess.DatabaseAcess;
import mapvisiblecontent.Fragment;


//������������� UI ��� ���������� ����� ������ � ����
class AddRecordToDatabase extends Form implements CommandListener{
	
//����� �� �����
private TextField adressTextField;

private Fragment fragment;

private Command save;
private Command back;
	
private UserInterface userInterface;

AddRecordToDatabase(UserInterface inUserInterface, Fragment inFragment){
	super("add to database");
	
	userInterface = inUserInterface;
	
	//������� ����� ������� ��������
	fragment = inFragment;
	
	//������� �������� ��������
	save = new Command("���������", Command.OK, 0);
	back = new Command("�����", Command.BACK, 0);
	
	
	
	//��������� �������� � �����
	this.addCommand(save);
	this.addCommand(back);
	
	//������� ���� ����� ������
	adressTextField = new TextField("�����", "", 100, TextField.ANY);
	
	//��������� ��� � �����
	this.append(adressTextField);
	
	//������������� ���������� ������� �����
	this.setCommandListener(this);
}

private DatabaseRecord createDatabaseRecord(){
	DatabaseRecord createdDBrecord;
	
	//������� �������� � ����� ������
	String name = adressTextField.getString();
	
	//������ ������
	createdDBrecord = new DatabaseRecord(0, name, fragment);
	
	return createdDBrecord;
}

//������������ �������� ��� ���������� � ���� ������
private void saveInDatabase(){	

	//���� ���� ������ �� ��������� ��������� �� ������!
	if (adressTextField.size() == 0) {
		UserInterface.getUserInterface().showErrorMessage("������� �����!");
		return;
	}
	
	new Thread(new Runnable() {
		public void run() {
			//������ ������ �������������� � ����� ������
			DatabaseAcess databaseAcess = new DatabaseAcess();
	
			//������� ������ ������ ���� ������
			DatabaseRecord saveRecord = createDatabaseRecord();
	
			//��������� ���������
			userInterface.showProgressIndicator(databaseAcess.getIndicator(), "����������");
			
			//���������� ��� � ���� ������
			databaseAcess.addRecord(saveRecord);
	
			userInterface.goToBackScreen(); //������� � ����������� ������
			userInterface.goToBackScreen();
		}
	}).start();
}
	
public void commandAction(Command c, Displayable d) {
	if (c==save){
		saveInDatabase();
	} else if (c==back){
		userInterface.goToBackScreen(); //������� � ����������� ������
	}
}


}