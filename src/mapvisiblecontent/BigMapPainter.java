package mapvisiblecontent;

import javax.microedition.lcdui.*;
import java.io.*;
import java.util.*;
import programdirection.ProgramParameters;
import userinterface.UserInterface;

class BigMapPainter extends Canvas{
private ScreenContent scrContent;

BigMapPainter(AbstractScreenContent inScrContent){
	setFullScreenMode(true);
	scrContent = (ScreenContent)inScrContent;
}


public void paintFragment(Graphics g, Fragment fragment){
	g.drawImage(fragment.getContent(), //Картинка
				fragment.getDrawPositionShirina(), //Позиция относительно левого края экрана
				fragment.getDrawPositionVisota(), //Позиция относительно верхнего края экрана
				0);
}

public void paint(Graphics g){
	
	//Если не все фрагменты присутствуют тогда выход!
	if (!scrContent.allFragmentsExist()) return;
	System.out.println("PAINT");

	
	
	//Нарисовать левый верхний фрагмент
	paintFragment(g, scrContent.getLeftUpFragment());
	
		
	//Нарисовать правый верхний фрагмент
	paintFragment(g, scrContent.getRightUpFragment());
	
	
	//Нарисовать левый нижний фрагмент
	paintFragment(g, scrContent.getLeftDownFragment());
	
	
	//Получить правый нижний фрагмент
	paintFragment(g, scrContent.getRightDownFragment());
}

public void keyPressed(int keyCode) {
	//Управление инвертировано - потому - что изначально для простоты понимания
	//представлялось что карта двигается под неподвижным экраном, потому-что так и происходит на самом деле, координаты фрагментов
	//смещаются относительно неподвижного экрана. Нам-же удобней что-бы экран двигался над неподвижной картой
	switch (keyCode){
		case KEY_NUM2:{ //Вверх
			//scrContent.moveUp();
			scrContent.moveDown();
		}
		break;
		
		case KEY_NUM8:{ //вниз
			//scrContent.moveDown();
			scrContent.moveUp();
		}
		break;
		
		case KEY_NUM6:{ //право
			//scrContent.moveRight();
			scrContent.moveLeft();
		}
		break;
		
		case KEY_NUM4:{ //Лево
			//scrContent.moveLeft();
			scrContent.moveRight();
		}
		break;
		
		case KEY_NUM0:{ //SMALLMAP - карта в меньшем масштабе
			ProgramParameters.getProgramParameters().getProgramDirection().smallMapMode();
			System.out.println("KEY_NUM0");
			return;
		}
		
		
		case KEY_STAR:{ //Вызов меню
			UserInterface.getUserInterface().showMenu();
			return;
		}
		
		
		case KEY_POUND:{ //Смена вида
			ProgramParameters.getProgramParameters().getProgramDirection().bigModeChanger();
			return;
		}
		
	}
	repaint();

}


}