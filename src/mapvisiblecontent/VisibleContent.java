package mapvisiblecontent;

import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.*;
import programdirection.ProgramParameters;


public abstract class VisibleContent{

//�� ����� ������ ��� ���������� BigMapVisibleContent � SmallMapVisibleContent
//��� �������� ������� ������� ������ ��� ������������ ������, ������� ���������� currentScreenContent ���� �� ����!
protected static AbstractScreenContent currentScreenContent;
protected Displayable currentMapPainter;

protected abstract Fragment getSmallMapLeftUpFragment();

protected abstract Fragment getBigMapLeftUpFragment();



//���������� ���������� ������
public Displayable getContent(){
	return currentMapPainter;
}

//������� ������ �� ����������� ScreenContent ��� ������������ �������, �������� �������� ������������ � ���������� � ��������� ������� ������
protected void clearMemory(){
	if (currentScreenContent != null){
		Runtime rt = Runtime.getRuntime();
		System.out.println("svobodnoy pamiatyi = "+rt.freeMemory());
		currentScreenContent.killContent();
		currentScreenContent = null;
		System.out.println("svobodnoy pamiatyi = "+rt.freeMemory());
		rt.gc();
	}
}

}