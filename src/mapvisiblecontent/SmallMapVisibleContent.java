package mapvisiblecontent;


import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.*;
import programdirection.ProgramParameters;

public class SmallMapVisibleContent extends VisibleContent{

private ProgramParameters programParameters;

public SmallMapVisibleContent(Fragment inFragment){
	clearMemory();//Очищаем память убивая все картинки
	
	//ссылка на параметры программы
	programParameters = ProgramParameters.getProgramParameters();

	currentScreenContent = new CursorScreenContent(inFragment);
	currentMapPainter = new SmallMapPainter(currentScreenContent);
}


//Возвращает ключевой фрагмент для создания малой карты
public Fragment getSmallMapLeftUpFragment(){
	return currentScreenContent.getLeftUpFragment();
}

private Fragment getCursor(){
	CursorScreenContent cursorScreenContent = (CursorScreenContent)currentScreenContent;
	Cursor currentCursor = cursorScreenContent.getCursor();
	return new Fragment(0,0, currentCursor.getDrawPositionShirina(), currentCursor.getDrawPositionVisota());
}

//Возвращает ключевой фрагмент для создания большой карты
public Fragment getBigMapLeftUpFragment(){
	//Пересчитываем текущую позицию фрагмента малой карты в фрагмент большой
	return CheckAndNames.smallToBigFragment(currentScreenContent.getLeftUpFragment(), getCursor());
}

}