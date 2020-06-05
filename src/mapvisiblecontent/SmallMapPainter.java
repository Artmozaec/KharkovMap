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

//�������� ���������
private static final int JOYSTICK_UP = -1;
private static final int JOYSTICK_DOWN = -2;
private static final int JOYSTICK_LEFT = -3;
private static final int JOYSTICK_RIGHT = -4;
private static final int JOYSTICK_CENTER = -5;

//������ �������
private Cursor cursor;

private int screenVisota;
private int screenShirina;

//����������� �������� �� ������� ����� �������� ������
private final int cursorDeltaMoving = 10;

SmallMapPainter(AbstractScreenContent inScrContent){
	//������ ��� �����!!!!
	programParameters = ProgramParameters.getProgramParameters();
	
	setFullScreenMode(true);
	
	screenVisota = getHeight();
	screenShirina = getWidth();

	scrContent = (CursorScreenContent)inScrContent;
	
	//�������� ������ �������
	cursor = scrContent.getCursor();
}


public void paintFragment(Graphics g, Fragment fragment){
	g.drawImage(fragment.getContent(), //��������
				fragment.getDrawPositionShirina(), //������� ������������ ������ ���� ������
				fragment.getDrawPositionVisota(), //������� ������������ �������� ���� ������
				0);
}

private void paintMapCursor(Graphics g){
	g.setColor(Colors.RED);

	//drawLine(������ ������, ������,     ����� ������, ������)
	//������ �������������� �����
	g.drawLine(0, cursor.getDrawPositionVisota(), screenShirina, cursor.getDrawPositionVisota());
	
	//������ ������������ �����
	g.drawLine(cursor.getDrawPositionShirina(), 0, cursor.getDrawPositionShirina(), screenVisota);
}

public void paint(Graphics g){
	//���� �� ��� ��������� ������������ ����� �����!
	if (!scrContent.allFragmentsExist()) return;
	
	System.out.println("PAINT");
	//���������� ����� ������� ��������
	paintFragment(g, scrContent.getLeftUpFragment());
	
		
	//���������� ������ ������� ��������
	paintFragment(g, scrContent.getRightUpFragment());
	
	
	//���������� ����� ������ ��������
	paintFragment(g, scrContent.getLeftDownFragment());
	
	
	//�������� ������ ������ ��������
	paintFragment(g, scrContent.getRightDownFragment());
	
	//������ ������
	paintMapCursor(g);
}

//��������� ��������� �� ������ � �������� ������
private boolean cursorPositionInScreen(){
	//���� ������ ��� ������ ������� ������� ������ ��� ������ ������
	if (cursor.getDrawPositionVisota()<0) return false;
	if (cursor.getDrawPositionShirina()<0) return false;
	
	//���� ������ ��� ������ ������� ������� ������ ��� ������� ������
	if (cursor.getDrawPositionShirina()>screenShirina) return false;
	if (cursor.getDrawPositionVisota()>screenVisota) return false;
	return true;
}


private void cursorMoveUp(){
	//������� ������� ��������� ������� � ������ �����������
	scrContent.cursorMoveUp(cursorDeltaMoving);
	//���� ������ ������ ��������� �� ��������� ������ ������� � ��������������� ������� ���� �������, ���-�� ������������ ��� ��������� � �������� ������
	if (!cursorPositionInScreen()) scrContent.moveDown();
}

private void cursorMoveDown(){
	//������� ������� ��������� ������� � ������ �����������
	scrContent.cursorMoveDown(cursorDeltaMoving);
	//���� ������ ������ ��������� �� ��������� ������ ������� � ��������������� ������� ���� �������, ���-�� ������������ ��� ��������� � �������� ������
	if (!cursorPositionInScreen()) scrContent.moveUp();
}

private void cursorMoveLeft(){
	//������� ������� ��������� ������� � ������ �����������
	scrContent.cursorMoveLeft(cursorDeltaMoving);
	//���� ������ ������ ��������� �� ��������� ������ ������� � ��������������� ������� ���� �������, ���-�� ������������ ��� ��������� � �������� ������
	if (!cursorPositionInScreen()) scrContent.moveRight();
}

private void cursorMoveRight(){
	//������� ������� ��������� ������� � ������ �����������
	scrContent.cursorMoveRight(cursorDeltaMoving);
	//���� ������ ������ ��������� �� ��������� ������ ������� � ��������������� ������� ���� �������, ���-�� ������������ ��� ��������� � �������� ������
	if (!cursorPositionInScreen()) scrContent.moveLeft();
}

//������������� ������ � ������ ������
private void cursorInCenterScreen(){
	cursor.changePositionVisota(screenVisota/2);
	cursor.changePositionShirina(screenShirina/2);
}

private void cursorMoving(int course){
	//���� ������ �� ��������� ������ ����� �����
	if (!cursorPositionInScreen()){ 
	System.out.println("������ �� ���������!!!");
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
	//���������� ������������� - ������ - ��� ���������� ��� �������� ���������
	//�������������� ��� ����� ��������� ��� ����������� �������, ������-��� ��� � ���������� �� ����� ����, ���������� ����������
	//��������� ������������ ������������ ������. ���-�� ������� ���-�� ����� �������� ��� ����������� ������
	System.out.println("keyCode = "+keyCode);
	//���������� ��������
	cursorMoving(keyCode);
	
	switch (keyCode){
		case KEY_NUM2:{ //�����
			//scrContent.moveUp();
			scrContent.moveDown();
		}
		break;
		
		case KEY_NUM8:{ //����
			//scrContent.moveDown();
			scrContent.moveUp();
		}
		break;
		
		case KEY_NUM6:{ //�����
			//scrContent.moveRight();
			scrContent.moveLeft();
		}
		break;
		
		case KEY_NUM4:{ //����
			//scrContent.moveLeft();
			scrContent.moveRight();
		}
		break;
		
		case KEY_NUM0:{ //������� � ����� MAP
			programParameters.getProgramDirection().bigMapMode();
			return;
		}
		
		case KEY_STAR:{ //����� ����
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