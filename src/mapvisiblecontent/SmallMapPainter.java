package mapvisiblecontent;

import javax.microedition.lcdui.*;
import java.io.*;
import java.util.*;
import programdirection.ProgramParameters;
import userinterface.UserInterface;
import userinterface.Colors;

class SmallMapPainter extends Canvas{
private CursorScreenContent scrContent;
private ProgramParameters programParameters;

//Сканкоды джойстика
private static final int JOYSTICK_UP = -1;
private static final int JOYSTICK_DOWN = -2;
private static final int JOYSTICK_LEFT = -3;
private static final int JOYSTICK_RIGHT = -4;
private static final int JOYSTICK_CENTER = -5;

//Объект курсора
private Cursor cursor;

private int screenVisota;
private int screenShirina;

//Колличество пикселей на которые будет смещатся курсор
private final int cursorDeltaMoving = 10;

SmallMapPainter(AbstractScreenContent inScrContent){
	//Убрать эту хрень!!!!
	programParameters = ProgramParameters.getProgramParameters();
	
	setFullScreenMode(true);
	
	screenVisota = getHeight();
	screenShirina = getWidth();

	scrContent = (CursorScreenContent)inScrContent;
	
	//Получаем объект курсора
	cursor = scrContent.getCursor();
}


public void paintFragment(Graphics g, Fragment fragment){
	g.drawImage(fragment.getContent(), //Картинка
				fragment.getDrawPositionShirina(), //Позиция относительно левого края экрана
				fragment.getDrawPositionVisota(), //Позиция относительно верхнего края экрана
				0);
}

private void paintMapCursor(Graphics g){
	g.setColor(Colors.RED);

	//drawLine(начало ширина, высота,     конец ширина, высота)
	//Рисуем роризонтальную линию
	g.drawLine(0, cursor.getDrawPositionVisota(), screenShirina, cursor.getDrawPositionVisota());
	
	//Рисуем вертикальную линию
	g.drawLine(cursor.getDrawPositionShirina(), 0, cursor.getDrawPositionShirina(), screenVisota);
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
	
	//рисуем курсор
	paintMapCursor(g);
}

//Проверяет находится ли курсор в пределах экрана
private boolean cursorPositionInScreen(){
	//Если высота или ширина позиции курсора меньше чем начало экрана
	if (cursor.getDrawPositionVisota()<0) return false;
	if (cursor.getDrawPositionShirina()<0) return false;
	
	//Если высота или ширина позиции курсора больше чем размеры экрана
	if (cursor.getDrawPositionShirina()>screenShirina) return false;
	if (cursor.getDrawPositionVisota()>screenVisota) return false;
	return true;
}


private void cursorMoveUp(){
	//Смещаем позицию отрисовки курсора в нужном направлении
	scrContent.cursorMoveUp(cursorDeltaMoving);
	//Если теперь курсор находится за пределами экрана смещаем в противоположную сторону весь контент, что-бы восстановить его видимость в пределах экрана
	if (!cursorPositionInScreen()) scrContent.moveDown();
}

private void cursorMoveDown(){
	//Смещаем позицию отрисовки курсора в нужном направлении
	scrContent.cursorMoveDown(cursorDeltaMoving);
	//Если теперь курсор находится за пределами экрана смещаем в противоположную сторону весь контент, что-бы восстановить его видимость в пределах экрана
	if (!cursorPositionInScreen()) scrContent.moveUp();
}

private void cursorMoveLeft(){
	//Смещаем позицию отрисовки курсора в нужном направлении
	scrContent.cursorMoveLeft(cursorDeltaMoving);
	//Если теперь курсор находится за пределами экрана смещаем в противоположную сторону весь контент, что-бы восстановить его видимость в пределах экрана
	if (!cursorPositionInScreen()) scrContent.moveRight();
}

private void cursorMoveRight(){
	//Смещаем позицию отрисовки курсора в нужном направлении
	scrContent.cursorMoveRight(cursorDeltaMoving);
	//Если теперь курсор находится за пределами экрана смещаем в противоположную сторону весь контент, что-бы восстановить его видимость в пределах экрана
	if (!cursorPositionInScreen()) scrContent.moveLeft();
}

//Позиционируем курсор в центер экрана
private void cursorInCenterScreen(){
	cursor.changePositionVisota(screenVisota/2);
	cursor.changePositionShirina(screenShirina/2);
}

private void cursorMoving(int course){
	//Если курсор за пределами экрана тогда выход
	if (!cursorPositionInScreen()){ 
	System.out.println("КУРСОР ЗА ПРЕДЕЛАМИ!!!");
	return;
	}
	switch (course){
		case JOYSTICK_UP:{
			cursorMoveUp();
		}
		break;
		
		case JOYSTICK_DOWN:{
			cursorMoveDown();
		}
		break;
		
		
		case JOYSTICK_LEFT:{
			cursorMoveLeft();
		}
		break;
		
		
		case JOYSTICK_RIGHT:{
			cursorMoveRight();
		}
		break;

	}
}



public void keyPressed(int keyCode) {
	//Управление инвертировано - потому - что изначально для простоты понимания
	//представлялось что карта двигается под неподвижным экраном, потому-что так и происходит на самом деле, координаты фрагментов
	//смещаются относительно неподвижного экрана. Нам-же удобней что-бы экран двигался над неподвижной картой
	System.out.println("keyCode = "+keyCode);
	//УПРАВЛЕНИЕ КУРСОРОМ
	cursorMoving(keyCode);
	
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
		
		case KEY_NUM0:{ //Возврат в режим MAP
			programParameters.getProgramDirection().bigMapMode();
			return;
		}
		
		case KEY_STAR:{ //Вызов меню
			UserInterface.getUserInterface().showMenu();
			return;
		}

		
		case JOYSTICK_CENTER:{
			cursorInCenterScreen();
		}
	}
	
	repaint();

}


}