package userinterface;

import javax.microedition.lcdui.*;
import databaseacess.DatabaseRecord;
import databaseacess.DatabaseAcess;
import mapvisiblecontent.Fragment;


//Предоставляет UI для оформления ввода данных в базу
class AddRecordToDatabase extends Form implements CommandListener{
	
//Адрес на карте
private TextField adressTextField;

private Fragment fragment;

private Command save;
private Command back;
	
private UserInterface userInterface;

AddRecordToDatabase(UserInterface inUserInterface, Fragment inFragment){
	super("add to database");
	
	userInterface = inUserInterface;
	
	//текущий левый верхний фрагмент
	fragment = inFragment;
	
	//Создаем основные комманды
	save = new Command("Сохранить", Command.OK, 0);
	back = new Command("Назад", Command.BACK, 0);
	
	
	
	//Добавляем комманды в форму
	this.addCommand(save);
	this.addCommand(back);
	
	//создаем поле ввода адреса
	adressTextField = new TextField("АДРЕС", "", 100, TextField.ANY);
	
	//Добавляем его в форму
	this.append(adressTextField);
	
	//Устанавливаем обработчик событий здесь
	this.setCommandListener(this);
}

private DatabaseRecord createDatabaseRecord(){
	DatabaseRecord createdDBrecord;
	
	//Достаем введённый в яорму адресс
	String name = adressTextField.getString();
	
	//Создаём объект
	createdDBrecord = new DatabaseRecord(0, name, fragment);
	
	return createdDBrecord;
}

//Координирует действия для сохранения в базе данных
private void saveInDatabase(){	

	//Если поле адреса не заполнено сообщение об ошибке!
	if (adressTextField.size() == 0) {
		UserInterface.getUserInterface().showErrorMessage("Введите адрес!");
		return;
	}
	
	new Thread(new Runnable() {
		public void run() {
			//Создаём объект взаимодействия с базой данных
			DatabaseAcess databaseAcess = new DatabaseAcess();
	
			//Создаем объект записи базы данных
			DatabaseRecord saveRecord = createDatabaseRecord();
	
			//Индикатор прогресса
			userInterface.showProgressIndicator(databaseAcess.getIndicator(), "сохранение");
			
			//Записываем его в базу данных
			databaseAcess.addRecord(saveRecord);
	
			userInterface.goToBackScreen(); //Переход к предыдущему экрану
			userInterface.goToBackScreen();
		}
	}).start();
}
	
public void commandAction(Command c, Displayable d) {
	if (c==save){
		saveInDatabase();
	} else if (c==back){
		userInterface.goToBackScreen(); //Переход к предыдущему экрану
	}
}


}