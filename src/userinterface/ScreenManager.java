package userinterface;

import java.util.*;
import javax.microedition.lcdui.*;

class ScreenManager{

private Display display;

//��������� � ������� ���������� ������ �� ������� Displayable - ���������� ������
private Vector screens;

ScreenManager(Display inDisplay){
	display = inDisplay;
	screens = new Vector();
}

//������� ��� ������
public void clear(){
	screens.removeAllElements();
}

//��������������� � ���������� ���������� ���������� ����
public void restoreSavedSvreen(){
	//���������� ����� ������� ������� �� ������
	display.setCurrent((Displayable)screens.elementAt(screens.size()-1));
	//������� ������� �������
	screens.removeElementAt(screens.size()-1);
	System.out.println("restoreSavedSvreen() size = "+screens.size());
}

private void rememberCurrentScreen(){
	//���������� ������� ���������� �������
	screens.addElement(display.getCurrent());
	System.out.println("rememberCurrentScreen() size = "+screens.size());
}

public void showCurrent(Displayable content){
	rememberCurrentScreen();
	display.setCurrent(content);
}

}