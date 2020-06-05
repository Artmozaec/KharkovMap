package userinterface;

import java.util.*;
import javax.microedition.lcdui.*;

class ScreenManager{

private Display display;

//Структура в которой содержатся ссылки на объекты Displayable - предыдущие экраны
private Vector screens;

ScreenManager(Display inDisplay){
	display = inDisplay;
	screens = new Vector();
}

//Удаляет все экраны
public void clear(){
	screens.removeAllElements();
}

//Воостанавливаем и отображаем предыдущий сохранённый эран
public void restoreSavedSvreen(){
	//Отображаем самый крайний элемент на экране
	display.setCurrent((Displayable)screens.elementAt(screens.size()-1));
	//Удаляем крайний элемент
	screens.removeElementAt(screens.size()-1);
	System.out.println("restoreSavedSvreen() size = "+screens.size());
}

private void rememberCurrentScreen(){
	//Запоминаем текущее содержимое дисплея
	screens.addElement(display.getCurrent());
	System.out.println("rememberCurrentScreen() size = "+screens.size());
}

public void showCurrent(Displayable content){
	rememberCurrentScreen();
	display.setCurrent(content);
}

}