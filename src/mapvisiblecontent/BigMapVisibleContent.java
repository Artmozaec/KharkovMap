package mapvisiblecontent;


import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.*;


public class BigMapVisibleContent extends VisibleContent{



public BigMapVisibleContent(Fragment inFragment){
	clearMemory();//������� ������ ������ ��� ��������
	//����� ������� ����������
	currentScreenContent = new ScreenContent(inFragment);
	currentMapPainter = new BigMapPainter(currentScreenContent);
}

//���������� �������� �������� ��� �������� ����� �����
public Fragment getSmallMapLeftUpFragment(){
	//������������� ������� ������� ��������� ������� ����� � �������� �����
	return CheckAndNames.bigToSmallFragment(currentScreenContent.getLeftUpFragment());
}

//���������� �������� �������� ��� �������� ������� �����
public Fragment getBigMapLeftUpFragment(){
	return currentScreenContent.getLeftUpFragment();
}


}