package userinterface;

import javax.microedition.lcdui.*;
import databaseacess.DatabaseRecord;
import databaseacess.DatabaseAcess;
import databaseacess.DatabaseRecordContainer;
import message.Question;
import message.QuestionListener;


class Search extends Form implements CommandListener, ItemCommandListener, QuestionListener{
private TextField searchTextField;

private ChoiceGroup searchResultChoiceGroup;

//�������� ��������� � ������� ���������� ���������� �������...
//1-�������� �����-�������
//2-�������� ������ �������� ����� - ��� ��� ����������� �� ����
//3-����� ������ � ���� ������

private DatabaseRecordContainer searchResultContainer;

private Command goTo;
private Command startSearch;
private Command back;
private Command delete;

private UserInterface userInterface;
public Search search;

Search(UserInterface inUserInterface){
	super("Form");
	
	userInterface = inUserInterface;
	
	//��� - ��� ���������� ������ - ����� ���������� ��������
	search = this;
/////////////////////���� �����
	//���� ����� �������
	searchTextField = new TextField("�����", "", 30, TextField.ANY);
	
	//������� ������ ������ � ���� ������
	startSearch = new Command("Search", Command.OK, 0);
	
	
	//��������� � ���� ����� �������
	searchTextField.addCommand(startSearch);
	
	//���������� �����!
	searchTextField.setItemCommandListener(this);


	//������� �������� �� ���������� �����
	back = new Command("�����", Command.BACK, 0);
	
	this.addCommand(back);
	
	//��������� ���� ����� �� �����
	this.append(searchTextField);
	
	//���������� �����!
	this.setCommandListener(this);

	//� ������ ������ ����, �� ����������� ������ �� ����� ������
	searchResultChoiceGroup = null;

}

//������� �� ����� ������ ����������� ������ � ��� ������ ���� ������� �������
private void deleteSearchResult(){
	if (searchResultChoiceGroup!=null){//���� ��������� ��� ��� ������ ����� �������
		this.delete(1);
		searchResultChoiceGroup = null;
	}
}

//��������� �������� �������� �� ����������� ������ � ����������� ����������
private void searchProcess(){

	new Thread(new Runnable() {
		public void run() {
			DatabaseRecordContainer currentContainer;
			
			String query;
			//�������� ������ ��� ������ �� ����� �����
			query = searchTextField.getString();
	
			//����� � ���� ������
			currentContainer = searchInDatabase(query);

			//���� ��������� ����������� 0 ���������� ���������� ���������
			if (currentContainer.getSize() == 0){
				//���������� ��������� � ��������� ������, � ��������� ������� ������ �� ����, ����� - ������� ����� ����!
				System.out.println("���������� ��������� � ��������� ������");
				//��� ��������� �����, ���-�� ����� ��������� ������� �� ����� ������
				try{
					Thread.sleep(20);
				}catch(InterruptedException e){
				}
				userInterface.showInfoMessage("����������� 0!");
				//�������� �������!
				return;
			}
			
			searchResultContainer = currentContainer;
			
			//������� ������ ��������� �� �����
			deleteSearchResult();
	
			//��������� ������ ���������� ������
			searchResultChoiceGroup = createSearchResult(currentContainer);
	
			//���������� �� �����
			search.append(searchResultChoiceGroup);
		}
	}).start();
}

//���������� DatabaseRecord ��������� � ��������� ��������� ������ searchResultChoiceGroup
private DatabaseRecord getSelectedRecordResult(){
	//������ ����� ���������� ���������� searchResultChoiceGroup
	int selectedResult = searchResultChoiceGroup.getSelectedIndex();
	
	//������ ��������� � ���� ������ � ���� ������
	DatabaseRecord selectedRecord = searchResultContainer.getRecord(selectedResult);
	
	return selectedRecord;
}


private void goToSelectedResult(){
	DatabaseRecord selectedRecord = getSelectedRecordResult();
	userInterface.goToMap(selectedRecord.getFragment());
}


//��������������� ������� ������ ������� � ���� ������ � ���������� ���������
private DatabaseRecordContainer searchInDatabase(String query){
	DatabaseAcess databaseAcess = new DatabaseAcess();
	userInterface.showProgressIndicator(databaseAcess.getIndicator(), "�����");
	return databaseAcess.search(query);
}



//������� ������ ChoiceGroup �� ������� DatabaseRecord[] - ���������� ������ � ���� ������
private ChoiceGroup createSearchResult(DatabaseRecordContainer result){
	ChoiceGroup searchChoice;
	
	//������� ������ ������ �����������
	searchChoice = new ChoiceGroup("", Choice.POPUP);
	
	result.reset();
	
	do{
		//�� �������� DatabaseRecord ��������� ����� � ���������� � ���� ������ - searchChoice
		searchChoice.append(result.getRecord().getAdressName(), null);
	} while (result.nextRecord());//���� � ���������� ���� ��������
	
	//������ �������
	goTo = new Command("�������", Command.BACK, 0);
	delete = new Command("�������", Command.BACK, 1);
	
	//��������� ������� � �����������
	searchChoice.addCommand(goTo);
	searchChoice.addCommand(delete);
	searchChoice.setItemCommandListener(this);
	return searchChoice;
}

private void deleteRecord(){
	new Thread(new Runnable() {
		public void run() {
			//�������� ������ �� ��������� � �������� ������
			DatabaseRecord deleteRecord = getSelectedRecordResult();
	
			//������� ������ ������� � ��
			DatabaseAcess databaseAcess = new DatabaseAcess();
			
			//��������� ���������
			userInterface.showProgressIndicator(databaseAcess.getIndicator(), "��������");
			
			//��������
			databaseAcess.deleteRecord(deleteRecord);
	
			//������� ������ ��������� �� �����
			deleteSearchResult();
			
			//������� ����� �����, ������ ��������� ��������������
			searchProcess();
		}
	}).start();
	

}


////////////////////////////////////���������� ���������� QuestionListener
public void selectNo(){
	//���������� �����
	userInterface.goToBackScreen();
}

public void selectYes(){
	//���������� �����
	userInterface.goToBackScreen();
	deleteRecord();
}

///////////////////////////////////////////////////////////////////////////



public void commandAction(Command c, Item item) {
	if (c==goTo) {
		goToSelectedResult();
	} else if (c==startSearch){
		searchProcess();		
	} else if (c == delete){
		//deleteRecord();
		//�������� ������ �� ��������, ���������� ��������� ������� - � ���� ������(selectNo(), selectYes())
		userInterface.showQuestion("������� ������?", this);
	}
}



public void commandAction(Command c, Displayable d) {
	if (c==back){
		userInterface.goToBackScreen(); //������� � ����������� ������
	}
}

}