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

//Основная структура в которой содержатся результаты запроса...
//1-Название улицы-объекта
//2-название левого верхнего файла - где это расположено на крте
//3-номер записи в базе данных

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
	
	//Это - для вложенного класса - треда индикатора процесса
	search = this;
/////////////////////Поле ввода
	//Поле ввода запроса
	searchTextField = new TextField("ПОИСК", "", 30, TextField.ANY);
	
	//Команда начала поиска в базе данных
	startSearch = new Command("Search", Command.OK, 0);
	
	
	//Добавляем к полю ввода команду
	searchTextField.addCommand(startSearch);
	
	//Обработчик здесь!
	searchTextField.setItemCommandListener(this);


	//Команда возврата на предыдущий экран
	back = new Command("Назад", Command.BACK, 0);
	
	this.addCommand(back);
	
	//Добавляем поле ввода на форму
	this.append(searchTextField);
	
	//Обработчик здесь!
	this.setCommandListener(this);

	//С начала список пуст, он заполняется только во время поиска
	searchResultChoiceGroup = null;

}

//Удаляет из формы список результатов поиска в том случае если таковой имеется
private void deleteSearchResult(){
	if (searchResultChoiceGroup!=null){//Если результат уже был создан тогда удаляем
		this.delete(1);
		searchResultChoiceGroup = null;
	}
}

//Выполняет основные действия по организации поиска и отображения результата
private void searchProcess(){

	new Thread(new Runnable() {
		public void run() {
			DatabaseRecordContainer currentContainer;
			
			String query;
			//Получаем строку для поиска из формы ввода
			query = searchTextField.getString();
	
			//Поиск в базе данных
			currentContainer = searchInDatabase(query);

			//Если найденных результатов 0 выкидываем выкидываем сообщение
			if (currentContainer.getSize() == 0){
				//Отобразить сообщение о неудачном поиске, в параметре передаём ссылку на сюда, когда - возврат будет сюда!
				System.out.println("Отобразить сообщение о неудачном поиске");
				//Ждём некоторое время, что-бы успел поменятся дисплей на форму поиска
				try{
					Thread.sleep(20);
				}catch(InterruptedException e){
				}
				userInterface.showInfoMessage("результатов 0!");
				//Покинуть функцию!
				return;
			}
			
			searchResultContainer = currentContainer;
			
			//Удаляем старый результат из формы
			deleteSearchResult();
	
			//Сорздание списка результата поиска
			searchResultChoiceGroup = createSearchResult(currentContainer);
	
			//Результаты на экран
			search.append(searchResultChoiceGroup);
		}
	}).start();
}

//Возвращает DatabaseRecord связанный с выбранным элементом списка searchResultChoiceGroup
private DatabaseRecord getSelectedRecordResult(){
	//Узнаем номер выбранного результата searchResultChoiceGroup
	int selectedResult = searchResultChoiceGroup.getSelectedIndex();
	
	//Достаём связанную с меню запись в базе данных
	DatabaseRecord selectedRecord = searchResultContainer.getRecord(selectedResult);
	
	return selectedRecord;
}


private void goToSelectedResult(){
	DatabaseRecord selectedRecord = getSelectedRecordResult();
	userInterface.goToMap(selectedRecord.getFragment());
}


//Непосредственно создает объект запроса в базу данных и возвращает результат
private DatabaseRecordContainer searchInDatabase(String query){
	DatabaseAcess databaseAcess = new DatabaseAcess();
	userInterface.showProgressIndicator(databaseAcess.getIndicator(), "поиск");
	return databaseAcess.search(query);
}



//Создает объект ChoiceGroup из массива DatabaseRecord[] - результата поиска в базе данных
private ChoiceGroup createSearchResult(DatabaseRecordContainer result){
	ChoiceGroup searchChoice;
	
	//Создаем объект списка результатов
	searchChoice = new ChoiceGroup("", Choice.POPUP);
	
	result.reset();
	
	do{
		//Из текущего DatabaseRecord извлекаем адрес и записываем в меню выбора - searchChoice
		searchChoice.append(result.getRecord().getAdressName(), null);
	} while (result.nextRecord());//Пока в контейнере есть элементы
	
	//Создаём команды
	goTo = new Command("Перейти", Command.BACK, 0);
	delete = new Command("Удалить", Command.BACK, 1);
	
	//Добавляем команды к результатам
	searchChoice.addCommand(goTo);
	searchChoice.addCommand(delete);
	searchChoice.setItemCommandListener(this);
	return searchChoice;
}

private void deleteRecord(){
	new Thread(new Runnable() {
		public void run() {
			//Получаем ссылку на вубранную к удалению запись
			DatabaseRecord deleteRecord = getSelectedRecordResult();
	
			//Создаем объект доступа к БД
			DatabaseAcess databaseAcess = new DatabaseAcess();
			
			//Индикатор прогресса
			userInterface.showProgressIndicator(databaseAcess.getIndicator(), "удаление");
			
			//Удаляемс
			databaseAcess.deleteRecord(deleteRecord);
	
			//Удаляем старый результат из формы
			deleteSearchResult();
			
			//создаем новый поиск, старый результат недействителен
			searchProcess();
		}
	}).start();
	

}


////////////////////////////////////Реализация интерфейса QuestionListener
public void selectNo(){
	//Возвращаем экран
	userInterface.goToBackScreen();
}

public void selectYes(){
	//Возвращаем экран
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
		//Вызываем запрос на удаление, реализация слушателя запроса - в этом классе(selectNo(), selectYes())
		userInterface.showQuestion("Удалить запись?", this);
	}
}



public void commandAction(Command c, Displayable d) {
	if (c==back){
		userInterface.goToBackScreen(); //Переход к предыдущему экрану
	}
}

}