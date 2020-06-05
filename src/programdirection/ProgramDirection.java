package programdirection;


import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.*;
import userinterface.UserInterface;
import mapvisiblecontent.BigMapVisibleContent;
import mapvisiblecontent.VisibleContent;
import mapvisiblecontent.SmallMapVisibleContent;
import mapvisiblecontent.Fragment;
import mapvisiblecontent.CheckAndNames;
import javax.microedition.midlet.MIDlet;
import filebrowser.FileBrowserListener;
import userinterface.UserInterfaceListener;

public class ProgramDirection implements FileBrowserListener, UserInterfaceListener{
private Display display;
private UserInterface userInterface;
private VisibleContent visibleContent;
private KharkovMap midlet;
private ProgramParameters programParameters;
private Fragment defaultFragment = new Fragment(5, 5, -10, -10);

public ProgramDirection(KharkovMap inMidl){
	midlet = inMidl;
	programParameters = ProgramParameters.getProgramParameters();
	
	//инициализируем интерфейс пользователя
	userInterface = UserInterface.getUserInterface();
	
	
	//Обработчик событий интерфейса здесь!
	userInterface.changeUserInterfaceListener(this);
	
	
	//ссылка на сюда стала ГЛОБАЛЬНОЙ!
	programParameters.setProgramDirection(this);
		
	//Дисплей выковыриваем
	display = programParameters.getCurrentDisplay(); 
	
	
	//Получаем позицию левого верхнего фрагмента последней сохраненной позиции
	Fragment initFragment = programParameters.getFirstScreenFragment();
	
	//Проверяем корректность текущего пути
		if (!goToStartFragment(initFragment)){
			//Путь неверный! Создаём окно выбора!
			userInterface.showFileBrowser(this);
		}
}

private boolean goToStartFragment(Fragment inFragment){
	//проверяем задан ли путь
	//Прочитался-ли путь из хранилища?
	if (!programParameters.patchExist()) return false;
	
	//попытка показать карту поиск доступного режима
	if (goToAvailableMode(inFragment)) return true;
	
	//сброс индексов фрагмента в ноль и снова поиск режима
	if (goToAvailableMode(defaultFragment)) return true;
	
	//Ни в одном режиме нет доступных фрагментов
	return false;
}



//Обработчик выбора директории в окне выбора
public void patchChoosed(String patch){
	System.out.println("!!!!!!!!!!!!!!fileChoosed!!!!!!!!!!!!!!"+patch);
	//Формируем начальный ключ-фрагмент в самом верхнем-левом углу карты
	Fragment initFragment = defaultFragment;
	
	//Меняем глобальный путь
	programParameters.setPatch(patch);
	
	//Проверяем наличие фрагментов - истинность пути
	if (! goToStartFragment(initFragment)){
		//Нужных фрагментов нет, опять вызываем обзор директорий
		userInterface.showFileBrowser(this);
	} else {
		//Запоминаем путь в хранилище
		programParameters.savePatch();
		//Фрагменты есть, отображаем карту
		createAndShowBigMapContent(initFragment);
	}
}




//Проверяет корректность пути и соответствие индексов в фрагменте реальным файлам
private boolean checkValidCurrentPatch(Fragment initFragment){
	//Прочитался-ли путь из хранилища?
	if (!programParameters.patchExist()) return false;
	
	//Соответствуют-ли индексы фрагмента реальным файлам?
	if (!CheckAndNames.AllFragmentExist(initFragment)) return false;
	
	return true;
}

private void createAndShowBigMapContent(Fragment initFragment){
	//Создаем объект карты
	visibleContent = new BigMapVisibleContent(initFragment);
	//Отображаем объект на экране
	showScreenContent();
}


private void createAndShowSmallMapContent(Fragment initFragment){
	//Создаем объект карты
	visibleContent = new SmallMapVisibleContent(initFragment);
	//Отображаем объект на экране
	showScreenContent();
}


private Fragment getKeyFragmentFromBigMap(){
	Fragment leftUp = null;
	//Если большая карта
	if ((programParameters.getMode() == ProgramParameters.MODE_BIGSATELIT) || 
		(programParameters.getMode() == ProgramParameters.MODE_BIGMAP)){
		leftUp = ((BigMapVisibleContent)visibleContent).getBigMapLeftUpFragment();
	} else { //Если малая карта
		leftUp = ((SmallMapVisibleContent)visibleContent).getBigMapLeftUpFragment();
	}
	return leftUp;
}

private Fragment getKeyFragmentFromSmallMap(){
	Fragment leftUp = null;
	//Если большая карта
	if ((programParameters.getMode() == ProgramParameters.MODE_BIGSATELIT) || 
		(programParameters.getMode() == ProgramParameters.MODE_BIGMAP)){
		leftUp = ((BigMapVisibleContent)visibleContent).getSmallMapLeftUpFragment();
	} else { //Если малая карта
		leftUp = ((SmallMapVisibleContent)visibleContent).getSmallMapLeftUpFragment();
	}
	return leftUp;
}


public void exitProgram(){
	//Сохраняем текущую позицию экрана
	programParameters.saveParameters();
	System.out.println("exit");
	//Собс-но выход
	
	midlet.destroyApp(true);
}

public void bigMapMode(){
	//Получаем левый верхний фрагмент текущего объекта отображения, 
	//это ключ к мозайке составления другого объекта отображения
	Fragment leftUpFragment = getKeyFragmentFromBigMap();
	
	//Пытаемся создать контент и отобразить на экране
	if (!goToBigMap(leftUpFragment)){
		//Не удалось
		userInterface.showErrorMessage("Нужные фрагменты большой карты отсутствуют!");
	}
}

private boolean goToBigMap(Fragment inFragment){
	//Запоминаем прежний режим
	programParameters.saveCurrentMode();
	
	//Меняем глобальный режим от него зависит выбор пути к файлам при создании объекта отображения
	programParameters.setModeBigMap();
	
	//Если все фрагменты карты присутствуют - делаем переход
	if (CheckAndNames.AllFragmentExist(inFragment)){
		//Создаем объект bigMap
		//Отображаем объект на экране
		createAndShowBigMapContent(inFragment);
		return true;
	} else { //Фрагмент малой большой отсутсвует, переход не удался
		//Восстанавливаем сохранённый режим
		programParameters.restoreSavedMode();
		return false;
	}
}




public void bigSatelitMode(){
	//Получаем левый верхний фрагмент текущего объекта отображения, 
	//это ключ к мозайке составления другого объекта отображения
	Fragment leftUpFragment = getKeyFragmentFromBigMap();
	
	//Пытаемся создать контент и отобразить на экране
	if (!goToBigSatelit(leftUpFragment)){
		//Не удалось
		userInterface.showErrorMessage("Нужные фрагменты большой карты отсутствуют!");
	}
}

private boolean goToBigSatelit(Fragment inFragment){
	//Запоминаем прежний режим
	programParameters.saveCurrentMode();
	
	//Меняем глобальный режим от него зависит выбор пути к файлам при создании объекта отображения
	programParameters.setModeBigSatelit();
	
	//Если все фрагменты карты присутствуют - делаем переход
	if (CheckAndNames.AllFragmentExist(inFragment)){
		//Создаем объект bigMap
		//Отображаем объект на экране
		createAndShowBigMapContent(inFragment);
		return true;
	} else { //Фрагмент малой большой отсутсвует, переход не удался
		//Восстанавливаем сохранённый режим
		programParameters.restoreSavedMode();
		return false;
	}
}


public void smallMapMode(){
	//Получаем левый верхний фрагмент текущего объекта отображения, 
	//это ключ к мозайке составления другого объекта отображения
	Fragment leftUpFragment = getKeyFragmentFromSmallMap();
	if (!goToSmallMap(leftUpFragment)){
		userInterface.showErrorMessage("Нужные фрагменты малой карты отсутствуют!");
	}
}

private boolean goToSmallMap(Fragment inFragment){
	//Запоминаем прежний режим
	programParameters.saveCurrentMode();
	
	//Меняем глобальный режим от него зависит выбор пути к файлам при создании объекта отображения
	programParameters.setModeSmallmap();
	
	//Если все фрагменты уменьшенной карты присутствуют - делаем переход
	if (CheckAndNames.AllFragmentExist(inFragment)){
		//Создаем объект smallMap
		//Отображаем объект на экране
		createAndShowSmallMapContent(inFragment);
		return true;
	} else { //Фрагмент малой карты отсутсвует, переход не удался
		//Восстанавливаем сохранённый режим
		programParameters.restoreSavedMode();
		return false;
	}
}



public void bigModeChanger(){
	if ((programParameters.getMode() == ProgramParameters.MODE_BIGSATELIT)){ 
		bigMapMode();
		return;
	}
	if ((programParameters.getMode() == ProgramParameters.MODE_BIGMAP)){
		bigSatelitMode();
		return;
	}
}


public void goToFragment(Fragment inFragment){
	//Если режим малой карты
	if (programParameters.getMode() == ProgramParameters.MODE_SMALLMAP){
		//Меняем ключевой фрагмент
		Fragment smallMapFragment = CheckAndNames.bigToSmallFragment(inFragment);
		//Попытка перейти
		if (goToSmallMap(smallMapFragment)) return;
		
	} else {//Если режим большой карты
		//Попытка перейти
		if (goToBigMap(inFragment)) return;
		
	}
	
	//Попытка перейти была неудачной, начинаем искать доступный режим
	if (goToAvailableMode(inFragment)) return;
	
	//Переход невозможен ни в одном из режимов!
	userInterface.showErrorMessage("Нужные фрагменты отсутствуют!");
}

//Ищем допустимый режим
private boolean goToAvailableMode(Fragment inFragment){
	//Запоминаем текущий режим
	programParameters.saveCurrentMode();
	
	//Режим большой карты
	programParameters.setModeBigMap();
	
	//Проверяем, если фрагменты есть тогда переходим
	if (CheckAndNames.AllFragmentExist(inFragment)){
		createAndShowBigMapContent(inFragment);
		return true;
	}	
	
	//Режим спутника
	programParameters.setModeBigSatelit();
	
	//Проверяем, если фрагменты есть тогда переходим
	if (CheckAndNames.AllFragmentExist(inFragment)){
		createAndShowBigMapContent(inFragment);
		return true;
	}
	
	//Режим малой карты
	programParameters.setModeSmallmap();
	
	//Переводим фрагмент в координаты малой карты
	Fragment smallMapFragment = CheckAndNames.bigToSmallFragment(inFragment);
	
	//Проверяем, если фрагменты есть тогда переходим
	if (CheckAndNames.AllFragmentExist(smallMapFragment)){
		createAndShowSmallMapContent(smallMapFragment);
		return true;
	}
	
	//Восстанавливаем сохранённый режим
	programParameters.restoreSavedMode();
	
	return false;
}

public void showScreenContent(){
	display.setCurrent(visibleContent.getContent());
}

//Текущий левый верхний фрагмент, возвращаемый из этого класса пока, нужен лишь для добавления в базу данных
public Fragment getCurrentLeftUpFragment(){
	System.out.println("getCurrentLeftUpFragment");
	return getKeyFragmentFromBigMap();
}

}