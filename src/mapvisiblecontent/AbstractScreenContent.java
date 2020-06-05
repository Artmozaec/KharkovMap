package mapvisiblecontent;

import javax.microedition.lcdui.Image;
import programdirection.ProgramParameters;
import userinterface.UserInterface;

//Этот класс отвечает за наполнение экрана фрагментами изобажений, 
//и рассчёт смещения фрагментов изображений по экрану
class AbstractScreenContent{

private Fragment leftUpFragment;
private Fragment rightUpFragment;
private Fragment leftDownFragment;
private Fragment rightDownFragment;

//Сторона изображения
private int imageSize;

//Ссылка на класс в котором хранятся все параметры программы
protected ProgramParameters programParameters;

//расстояние на которое смещаются фрагменты при движении
private int deltaMovingContent;


//int inBeginFileIndexHorizontal, int inBeginFileIndexVertical - начальные индексы первого файла
//int inCoordinateUpLeftFragmentShirina, int inCoordinateUpLeftFragmentVisota - начальные отступы верхнего левого фрагмента
AbstractScreenContent(Fragment inFragment){
	
	//ссылка на параметры программы
	programParameters = ProgramParameters.getProgramParameters();
	
	//берём размер смещения
	deltaMovingContent = programParameters.getDeltaMoving();
	
	//Узнаем чему равна сторона изображения
	imageSize = programParameters.getImageSize();

	//заполняем левый верхний картинкой
	inFragment.fillFragment();
	
	//Заполняем фрагментами массив screenFragment
	fillScreenFragments(inFragment);
	
	//alignContentToRightSideScreen();
	//(так-как база данных создавалась на малом разрешении экрана на крупных экранах при переходе иогут возникать зазоры)
	//Если контент не покрывает экран смещеаем его в низ и в право
	//contentAlignDownRight();

}

//Проверяет наличие зазоров с права и с низу и в случае зазора смещает контент соответственно
protected void contentAlignDownRight(){
	//Если есть зазор с права
	if (isRightOverrun()) {
		//смещаем в право
		alignContentToRightSideScreen();
	}
	
	//Если есть зазор с низу
	if (isDownOverrun()) {
		//Смещаем к низу
		alignContentToDownSideScreen();
	}
}


//////////////ОТДАЮТ КОНТЕНТ
public Fragment getLeftUpFragment(){
	return leftUpFragment;
}

public Fragment getRightUpFragment(){
	return rightUpFragment;
}

public Fragment getLeftDownFragment(){
	return leftDownFragment;
}

public Fragment getRightDownFragment(){
	return rightDownFragment;
}

//Если все фрагменты есть тогда true иначе...
public boolean allFragmentsExist(){
	if ((leftUpFragment!=null) && (rightUpFragment!=null) && (leftDownFragment!=null) && (leftDownFragment!=null)) return true;
	return false;
}


////////////////////////////РАВНЕНИЕ ПО КРАЮ ЭКРАНА//////////////////////////////

//Выравнивает содержимое по левому краю
private void alignContentToLeftSideScreen(){
	//получаем размер зазора на экране с лева
	int gap = gapValueScreenLeft();
	
	//смещаем все фрагменты в том-же направлении
	moveLeftFragments(gap);
}

//Выравнивает содержимое по правому краю
private void alignContentToRightSideScreen(){
	//получаем размер зазора на экране с права
	int gap = gapValueScreenRight();
	
	//смещаем все фрагменты в том-же направлении
	moveRightFragments(gap);
}

//Выравнивает содержимое по верхнему краю
private void alignContentToUpSideScreen(){
	//получаем размер зазора на экране с верху
	int gap = gapValueScreenUp();
	
	//смещаем все фрагменты в том-же направлении
	moveUpFragments(gap);
}

//Выравнивает содержимое по нижнему краю
private void alignContentToDownSideScreen(){
	//получаем размер зазора на экране с низу
	int gap = gapValueScreenDown();
	
	//смещаем все фрагменты в том-же направлении
	moveDownFragments(gap);
}
//////////////////////////////////////////////////////////////////////////



//Проверяет есть-ли с низу нужные файлы для смещения вверх
private boolean checkExistFileForMoveUp(){

	int newIndexLeftHorizontal, newIndexLeftVertical; //Рассчётные индексы файлов
	int newIndexRightHorizontal, newIndexRightVertical; //Рассчётные индексы файлов
	
	//рассчитать новые индексы файлов
	newIndexLeftVertical = leftDownFragment.getNextVerticalIndexFile();//На 1 файл ниже самого нижнего
	newIndexLeftHorizontal = leftDownFragment.getNameIndexHorizontal();
	
	newIndexRightVertical = rightDownFragment.getNextVerticalIndexFile();//На 1 файл ниже самого нижнего
	newIndexRightHorizontal = rightDownFragment.getNameIndexHorizontal();
	
	boolean left = CheckAndNames.checkFileExist(newIndexLeftHorizontal, newIndexLeftVertical);
	boolean right = CheckAndNames.checkFileExist(newIndexRightHorizontal, newIndexRightVertical);
	
	if (left && right) return true;
	else
	return false;
}

//Проверяет есть ли с верху нужные файлы для смещения вниз
private boolean checkExistFileForMoveDown(){

	int newIndexLeftHorizontal, newIndexLeftVertical; //Рассчётные индексы файлов
	int newIndexRightHorizontal, newIndexRightVertical; //Рассчётные индексы файлов
	
	//вычисляем индексы новых файлов
	
	newIndexLeftVertical = leftUpFragment.getPervVerticalIndexFile();//На 1 выше самого верхнего
	newIndexLeftHorizontal = leftUpFragment.getNameIndexHorizontal();
	
	newIndexRightVertical = rightUpFragment.getPervVerticalIndexFile();
	newIndexRightHorizontal = rightUpFragment.getNameIndexHorizontal();
	
	boolean left = CheckAndNames.checkFileExist(newIndexLeftHorizontal, newIndexLeftVertical);
	boolean right = CheckAndNames.checkFileExist(newIndexRightHorizontal, newIndexRightVertical);
	
	if (left && right) return true;
	else
	return false;
}

//Есть ли с права необходимые файлы
private boolean checkExistFileForMoveLeft(){
	int newIndexDownHorizontal, newIndexDownVertical; //Рассчётные индексы файлов
	int newIndexUpHorizontal, newIndexUpVertical; //Рассчётные индексы файлов
	
	//рассчитать новые индексы файлов
	newIndexUpHorizontal = rightUpFragment.getNextHorizontalIndexFile();
	newIndexUpVertical = rightUpFragment.getNameIndexVertical();
	
	newIndexDownHorizontal = rightDownFragment.getNextHorizontalIndexFile();
	newIndexDownVertical = rightDownFragment.getNameIndexVertical();

	boolean up = CheckAndNames.checkFileExist(newIndexUpHorizontal, newIndexUpVertical);
	boolean down = CheckAndNames.checkFileExist(newIndexDownHorizontal, newIndexDownVertical);
	
	if (up && down) return true;
	else
	return false;
}

//Есть ли с лева необходимые файлы
private boolean checkExistFileForMoveRight(){
	int newIndexDownHorizontal, newIndexDownVertical; //Рассчётные индексы файлов
	int newIndexUpHorizontal, newIndexUpVertical; //Рассчётные индексы файлов
	
	//рассчитать новые индексы файлов
	newIndexUpHorizontal = leftUpFragment.getPervHorizontalIndexFile();
	newIndexUpVertical = leftUpFragment.getNameIndexVertical();
	
	newIndexDownHorizontal = leftDownFragment.getPervHorizontalIndexFile();
	newIndexDownVertical = leftDownFragment.getNameIndexVertical();

	boolean up = CheckAndNames.checkFileExist(newIndexUpHorizontal, newIndexUpVertical);
	boolean down = CheckAndNames.checkFileExist(newIndexDownHorizontal, newIndexDownVertical);
	
	if (up && down) return true;
	else
	return false;	
}

//Поднимает все фрагменты вверх(то есть отнимает от их высоты отрисовки)
//Если результат вышел за пределы экрана - изменяет содержимое Screen Content читая дополнительные файлы
public void moveUp(){
	moveUpFragments(deltaMovingContent);
		
	//Определить заполен ли экран полностью текущими фрагментами
	if (!screenIsFill()) {
		//Если не заполнен вызвать смещение фрагментов в верх
		//Проверяем есть-ли необходимые файлы для смещения
		if (checkExistFileForMoveUp()) {
			//Смещаемся
			rebuildFragmentsUp();
			//Если всё равно остался промежуток на экране, значит размер экрана больше чем размер контента
			
			if (!screenIsFill()) {
				//Устраняем этот промежуток, прижимая контент к верхнему краю
				alignContentToUpSideScreen();
			}
			
		} else {//файлов нужных нет, конец карты!
			//Восстанавливаем отступ
			moveDownFragments(deltaMovingContent);
			UserInterface.getUserInterface().showInfoMessage("Нижний край карты");
		}
	}
}

//Опускает все фрагменты вниз(то есть прибавляет к их высотам отрисовки)
//Если результат вышел за пределы экрана - изменяет содержимое Screen Content читая дополнительные файлы
public void moveDown(){
	moveDownFragments(deltaMovingContent);
	
	//Определить заполен ли экран полностью текущими фрагментами
	if (!screenIsFill()) {
		//Если не заполнен вызвать смещение фрагментов в низ
		//Проверяем есть-ли необходимые файлы для смещения
		if (checkExistFileForMoveDown()) {
			//Смещаемся
			rebuildFragmentsDown();
			
			//Если всё равно остался промежуток на экране, значит размер экрана больше чем размер контента
			if (!screenIsFill()) {
				//Устраняем этот промежуток, прижимая контент к нижнему краю
				alignContentToDownSideScreen();
			}
			
		} else { //файлов нужных нет, конец карты!
			//Восстанавливаем отступ
			moveUpFragments(deltaMovingContent);
			UserInterface.getUserInterface().showInfoMessage("Верхний край карты");
		}
	}
}

public void moveLeft(){
	moveLeftFragments(deltaMovingContent);

	//Определить заполен ли экран полностью текущими фрагментами
	if (!screenIsFill()) {
		//Если не заполнен вызвать смещение фрагментов в лево
		//Проверяем есть-ли необходимые файлы для смещения
		if (checkExistFileForMoveLeft()) {
			//Смещаемся
			rebuildFragmentsLeft();
			
			//Если всё равно остался промежуток на экране, значит размер экрана больше чем размер контента
			if (!screenIsFill()) {
				//Устраняем этот промежуток, прижимая контент к левому краю
				alignContentToLeftSideScreen();
			}
		} else {//файлов нужных нет, конец карты!
			//Восстанавливаем отступ
			moveRightFragments(deltaMovingContent);
			UserInterface.getUserInterface().showInfoMessage("Правый край карты");
		}
	}
}

public void moveRight(){
	moveRightFragments(deltaMovingContent);

	//Определить заполен ли экран полностью текущими фрагментами
	if (!screenIsFill()) {
		//Если не заполнен вызвать смещение фрагментов вверх
		//Проверяем есть-ли необходимые файлы для смещения
		if (checkExistFileForMoveRight()) {
			//Смещаемся
			rebuildFragmentsRight();
			
			//Если всё равно остался промежуток на экране, значит размер экрана больше чем размер контента
			if (!screenIsFill()) {
				//Устраняем этот промежуток, прижимая контент к правому краю
				alignContentToRightSideScreen();
			}
			
		} else {//файлов нужных нет, конец карты!
			//Восстанавливаем отступ
			moveLeftFragments(deltaMovingContent);
			UserInterface.getUserInterface().showInfoMessage("Левый край карты блядь нахуй ебать его в рот! Какая в пиздень карта, нахуй Я ебал б пизде-е-ец");
		}
	}
}

//Вызывает функции очистки изображения во всех фраментах
public void killContent(){
	leftUpFragment.killContent();
	leftDownFragment.killContent();
	rightUpFragment.killContent();
	rightDownFragment.killContent();
	//leftUpFragment = null; - ведь нам нужен этот объект, хотя какя разница ссылка на него и так остается
	leftDownFragment = null;
	rightUpFragment = null;
	rightDownFragment = null;
}

//Создаёт объект Fragment, вызывает функцию заполнения его картинкой, возвращает на него ссылку
private Fragment createFragment(int inFileIndexHorizontal, 
								  int inFileIndexVertical, 
								  int inCoordinateFragmentShirina, 
								  int inCoordinateFragmentVisota){
	Fragment fragmentInstance;
		
	//Собственно создание фрагмента
	fragmentInstance = new Fragment(inFileIndexHorizontal, 
									inFileIndexVertical, 
									inCoordinateFragmentShirina, 
									inCoordinateFragmentVisota);
		
	//Заполняем созданный фрагмент картинкой
	fragmentInstance.fillFragment();	
	
	return fragmentInstance;
}


//Заполнение массива фрагментами левый верхний фрагмент имеет индекс
//переданный в параметре, экранная позиция - левый верхний угол, от него начинают выстраиваться остальные
//3 фрагмента правый-верхний, левый нижний, правый нижний
private void fillScreenFragments(Fragment inLeftUpFragment){
	//выгребаем из фрагмента нужные значения
	int fileIndexHorizontal = inLeftUpFragment.getNameIndexHorizontal();
	int fileIndexVertical = inLeftUpFragment.getNameIndexVertical();
	int leftUpFragmentShirina = inLeftUpFragment.getDrawPositionShirina();
	int leftUpFragmentVisota = inLeftUpFragment.getDrawPositionVisota();
	
	//Левый-верхний уже создан
	leftUpFragment = inLeftUpFragment;

	
	//Правый-верхний
	//Следующий фрагмент находится дальше, в право на ширину фрагмента
	int tempShirina;
	tempShirina = leftUpFragmentShirina+imageSize;
	rightUpFragment = createFragment(fileIndexHorizontal+1, fileIndexVertical, tempShirina, leftUpFragmentVisota);

	
	
	//Левый-нижний
	//Следующий фрагмент находится ниже, на высоту фрагмента
	int tempVisota;
	tempVisota = leftUpFragmentVisota+imageSize;
	leftDownFragment = createFragment(fileIndexHorizontal, fileIndexVertical+1, leftUpFragmentShirina, tempVisota);

	
	
	//Правый-нижний 
	//Следующий фрагмент и леевее и ниже
	rightDownFragment = createFragment(fileIndexHorizontal+1, fileIndexVertical+1, tempShirina, tempVisota);
}




//Пересчитывает экранные позиции в объектах Fragment
//Отнимает от их высоты значение deltaMoving
protected void moveUpFragments(int delta){
	leftUpFragment.moveUp(delta);
	rightUpFragment.moveUp(delta);
	leftDownFragment.moveUp(delta);
	rightDownFragment.moveUp(delta);
}

//Пересчитывает экранные позиции в объектах Fragment
//Прибавляет к их высотам значение deltaMoving
protected void moveDownFragments(int delta){
	leftUpFragment.moveDown(delta);
	rightUpFragment.moveDown(delta);
	leftDownFragment.moveDown(delta);
	rightDownFragment.moveDown(delta);
}

//Пересчитывает экранные позиции в объектах Fragment
//Отнимает от их смещения в право deltaMoving
protected void moveLeftFragments(int delta){
	leftUpFragment.moveLeft(delta);
	rightUpFragment.moveLeft(delta);
	leftDownFragment.moveLeft(delta);
	rightDownFragment.moveLeft(delta);	
}

//Пересчитывает экранные позиции в объектах Fragment
//Прибавляет к их смещению в право deltaMoving
protected void moveRightFragments(int delta){
	leftUpFragment.moveRight(delta);
	rightUpFragment.moveRight(delta);
	leftDownFragment.moveRight(delta);
	rightDownFragment.moveRight(delta);	
}




//Смещает все фрагменты массива screenFragments на 1 в верх
//Недостающие фрагменты читаются из файла
private void rebuildFragmentsUp(){
	System.out.println("REBUILD_frUP!!");
	int newIndexLeftHorizontal, newIndexLeftVertical; //Рассчётные индексы файлов
	int newIndexRightHorizontal, newIndexRightVertical; //Рассчётные индексы файлов
	
	int newPositionLeftShirina, newPositionLeftVisota; //Рассчётные позиции файлов
	int newPositionRightShirina, newPositionRightVisota;
	
	//рассчитать новые индексы файлов
	newIndexLeftVertical = leftDownFragment.getNextVerticalIndexFile();//На 1 файл ниже самого нижнего
	newIndexLeftHorizontal = leftDownFragment.getNameIndexHorizontal();
	
	newIndexRightVertical = rightDownFragment.getNextVerticalIndexFile();//На 1 файл ниже самого нижнего
	newIndexRightHorizontal = rightDownFragment.getNameIndexHorizontal();
	
	//Рассчитать расположения новых файлов
	newPositionLeftShirina = leftDownFragment.getDrawPositionShirina();
	//Так как левый нижний фрагмент сейчас станет левым верхним его позиция отрисовки по горизонтали + размер изображения
	//как раз станут позицией отрисовки нового левого нижнего фрагмента
	newPositionLeftVisota = leftDownFragment.getDrawPositionVisota()+imageSize;
		
	newPositionRightShirina = rightDownFragment.getDrawPositionShirina(); 
	newPositionRightVisota = newPositionLeftVisota; //Ведь они располагаются на одной высоте

	//Перенести нижние фрагменты массива на верх
	leftUpFragment = leftDownFragment;
	rightUpFragment = rightDownFragment;
	
	//Прочитать в нижние фрагменты недостающие файлы
	leftDownFragment = createFragment(newIndexLeftHorizontal, newIndexLeftVertical, newPositionLeftShirina, newPositionLeftVisota);
	rightDownFragment = createFragment(newIndexRightHorizontal, newIndexRightVertical, newPositionRightShirina, newPositionRightVisota);
}


//Смещает все фрагменты массива screenFragments на 1 в низ
//Недостающие фрагменты читаются из файла
private void rebuildFragmentsDown(){
	System.out.println("MrebuildFragmentsDown!!!!");
	int newIndexLeftHorizontal, newIndexLeftVertical; //Рассчётные индексы файлов
	int newIndexRightHorizontal, newIndexRightVertical; //Рассчётные индексы файлов
	
	int newPositionLeftShirina, newPositionLeftVisota; //Рассчётные позиции файлов
	int newPositionRightShirina, newPositionRightVisota;
	
	//рассчитать новые индексы файлов
	newIndexLeftVertical = leftUpFragment.getPervVerticalIndexFile();//На 1 выше самого верхнего
	newIndexLeftHorizontal = leftUpFragment.getNameIndexHorizontal();
	
	newIndexRightVertical = rightUpFragment.getPervVerticalIndexFile();
	newIndexRightHorizontal = rightUpFragment.getNameIndexHorizontal();

	//Рассчитать расположения новых файлов
	newPositionLeftShirina = leftUpFragment.getDrawPositionShirina();
	newPositionLeftVisota = leftUpFragment.getDrawPositionVisota()-imageSize;
		
	newPositionRightShirina = rightDownFragment.getDrawPositionShirina(); 
	newPositionRightVisota = newPositionLeftVisota; //Ведь они распологаются на одной высоте!
	
	//Перенесли верхние фрагменты в низ
	leftDownFragment = leftUpFragment;
	rightDownFragment = rightUpFragment;
	
	//Прочитать в нижние фрагменты недостающие файлы
	leftUpFragment = createFragment(newIndexLeftHorizontal, newIndexLeftVertical, newPositionLeftShirina, newPositionLeftVisota);
	rightUpFragment = createFragment(newIndexRightHorizontal, newIndexRightVertical, newPositionRightShirina, newPositionRightVisota);
}

//Смещает все фрагменты массива screenFragments на 1 в лево
//Недостающие фрагменты читаются из файла
private void rebuildFragmentsLeft(){
	int newIndexDownHorizontal, newIndexDownVertical; //Рассчётные индексы файлов
	int newIndexUpHorizontal, newIndexUpVertical; //Рассчётные индексы файлов
	
	int newPositionDownShirina, newPositionDownVisota; //Рассчётные позиции файлов
	int newPositionUpShirina, newPositionUpVisota;

	
	//рассчитать новые индексы файлов
	newIndexUpHorizontal = rightUpFragment.getNextHorizontalIndexFile();
	newIndexUpVertical = rightUpFragment.getNameIndexVertical();
	
	newIndexDownHorizontal = rightDownFragment.getNextHorizontalIndexFile();
	newIndexDownVertical = rightDownFragment.getNameIndexVertical();


	//Рассчитать расположения новых файлов
	//Так как сейчас правый верхний фрагмент станет левым верхнем его горизонтальная позиция+размер картинки
	//как раз является началом будущего правого верхнего фрагмента
	newPositionUpShirina = rightUpFragment.getDrawPositionShirina()+imageSize;
	newPositionUpVisota = rightUpFragment.getDrawPositionVisota();
	
	newPositionDownVisota = rightDownFragment.getDrawPositionVisota();

	newPositionDownShirina = newPositionUpShirina; //Они находятся на одной позиции


	//Перенесли правые фрагменты в лево
	leftUpFragment = rightUpFragment;
	leftDownFragment = rightDownFragment;
	
	//Прочитать в нижние фрагменты недостающие файлы
	rightUpFragment = createFragment(newIndexUpHorizontal, newIndexUpVertical, newPositionUpShirina, newPositionUpVisota);
	rightDownFragment = createFragment(newIndexDownHorizontal, newIndexDownVertical, newPositionDownShirina, newPositionDownVisota);
}

//Смещает все фрагменты массива screenFragments на 1 в право
//Недостающие фрагменты читаются из файла
private void rebuildFragmentsRight(){
	int newIndexDownHorizontal, newIndexDownVertical; //Рассчётные индексы файлов
	int newIndexUpHorizontal, newIndexUpVertical; //Рассчётные индексы файлов
	
	int newPositionDownShirina, newPositionDownVisota; //Рассчётные позиции файлов
	int newPositionUpShirina, newPositionUpVisota;
	

	
	//рассчитать новые индексы файлов
	newIndexUpHorizontal = leftUpFragment.getPervHorizontalIndexFile();
	newIndexUpVertical = leftUpFragment.getNameIndexVertical();
	
	newIndexDownHorizontal = leftDownFragment.getPervHorizontalIndexFile();
	newIndexDownVertical = leftDownFragment.getNameIndexVertical();
	
	//Рассчитать расположения новых файлов
	
	newPositionUpVisota = leftUpFragment.getDrawPositionVisota();
	newPositionUpShirina = leftUpFragment.getDrawPositionShirina()-imageSize;
	
	newPositionDownVisota = leftDownFragment.getDrawPositionVisota();
	
	newPositionDownShirina = newPositionUpShirina; //Находятся на одном уровне
	
	
	//Перенесли левые фрагменты в право
	rightUpFragment = leftUpFragment;
	rightDownFragment = leftDownFragment;
	
	//Прочитать в нижние фрагменты недостающие файлы
	leftUpFragment = createFragment(newIndexUpHorizontal, newIndexUpVertical, newPositionUpShirina, newPositionUpVisota);
	leftDownFragment = createFragment(newIndexDownHorizontal, newIndexDownVertical, newPositionDownShirina, newPositionDownVisota);
}



//величина зазора на экране с права
private int gapValueScreenRight(){
	int doubleSize = imageSize*2;
	
	//позиция правой стороны контента относительно экрана
	int rightContentSide = leftUpFragment.getDrawPositionShirina()+doubleSize;
	
	int delta = programParameters.getScreenShirina()-rightContentSide;
	return delta;
}

//величина зазора на экране с лева
private int gapValueScreenLeft(){
	return leftUpFragment.getDrawPositionShirina();
}

//величина зазора на экране с верху
private int gapValueScreenUp(){
	return leftUpFragment.getDrawPositionVisota();
}

//величина зазора на экране с внизу
private int gapValueScreenDown(){
	int doubleSize = imageSize*2;
	
	//позиция нижней стороны контента относительно экрана
	int DownContentSide = leftUpFragment.getDrawPositionVisota()+doubleSize;
	
	int delta = programParameters.getScreenVisota()-DownContentSide;
	return delta;
}


//возвращает - true, если значения координат высоты левого верхнего фрагмента стали больше 0, На экране не заполненная область сверху
private boolean isUpOverrun(){
	//размер зазора между контентом и верхом экрана
	int gap = gapValueScreenUp();
	System.out.println("isUpOverrun() - gap = "+gap);
	return (gap>0);
}

//возвращает - true, если значения координат ширины левого верхнего фрагмента стали больше 0, На экране не заполненная область слева
private boolean isLeftOverrun(){
	//размер зазора между контентом и левой стороной экрана
	int gap = gapValueScreenLeft();
	System.out.println("isLeftOverrun() - gap = "+gap);
	return (gap>0);
}


//выступ с низу
private boolean isDownOverrun(){
	//размер зазора между контентом и низом экрана
	int gap = gapValueScreenDown();
	System.out.println("isDownOverrun() - gap = "+gap);
	return (gap>0);
}


//выступ с права
private boolean isRightOverrun(){
	//размер зазора между контентом и правой стороной экрана
	int gap = gapValueScreenRight();
	System.out.println("isRightOverrun() - gap = "+gap);
	return (gap>0);
}

//Определяет полностью ли заполнен экран текущими смещёнными фрагментами
private boolean screenIsFill(){
	if (isUpOverrun() || isDownOverrun() || isRightOverrun() || isLeftOverrun()){ 
		//System.out.println("screenIsThick >> vihod za granicy!!!");
		return false;
	}
	
	return true;
}


}