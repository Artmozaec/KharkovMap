package mapvisiblecontent;


import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.*;


public class BigMapVisibleContent extends VisibleContent{



public BigMapVisibleContent(Fragment inFragment){
	clearMemory();//Очищаем память убивая все картинки
	//Сразу создаем наполнение
	currentScreenContent = new ScreenContent(inFragment);
	currentMapPainter = new BigMapPainter(currentScreenContent);
}

//Возвращает ключевой фрагмент для создания малой карты
public Fragment getSmallMapLeftUpFragment(){
	//Пересчитываем текущую позицию фрагмента большой карты в фрагмент малой
	return CheckAndNames.bigToSmallFragment(currentScreenContent.getLeftUpFragment());
}

//Возвращает ключевой фрагмент для создания большой карты
public Fragment getBigMapLeftUpFragment(){
	return currentScreenContent.getLeftUpFragment();
}


}