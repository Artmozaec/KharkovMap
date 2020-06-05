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
	g.drawImage(fragment.getContent(), //��������
				fragment.getDrawPositionShirina(), //������� ������������ ������ ���� ������
				fragment.getDrawPositionVisota(), //������� ������������ �������� ���� ������
				0);
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
}

public void keyPressed(int keyCode) {
	//���������� ������������� - ������ - ��� ���������� ��� �������� ���������
	//�������������� ��� ����� ��������� ��� ����������� �������, ������-��� ��� � ���������� �� ����� ����, ���������� ����������
	//��������� ������������ ������������ ������. ���-�� ������� ���-�� ����� �������� ��� ����������� ������
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
		
		case KEY_NUM0:{ //SMALLMAP - ����� � ������� ��������
			ProgramParameters.getProgramParameters().getProgramDirection().smallMapMode();
			System.out.println("KEY_NUM0");
			return;
		}
		
		
		case KEY_STAR:{ //����� ����
			UserInterface.getUserInterface().showMenu();
			return;
		}
		
		
		case KEY_POUND:{ //����� ����
			ProgramParameters.getProgramParameters().getProgramDirection().bigModeChanger();
			return;
		}
		
	}
	repaint();

}


}