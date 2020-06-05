package mapvisiblecontent;


import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.*;
import programdirection.ProgramParameters;

public class SmallMapVisibleContent extends VisibleContent{

private ProgramParameters programParameters;

public SmallMapVisibleContent(Fragment inFragment){
	clearMemory();//������� ������ ������ ��� ��������
	
	//������ �� ��������� ���������
	programParameters = ProgramParameters.getProgramParameters();

	currentScreenContent = new CursorScreenContent(inFragment);
	currentMapPainter = new SmallMapPainter(currentScreenContent);
}


//���������� �������� �������� ��� �������� ����� �����
public Fragment getSmallMapLeftUpFragment(){
	return currentScreenContent.getLeftUpFragment();
}

private Fragment getCursor(){
	CursorScreenContent cursorScreenContent = (CursorScreenContent)currentScreenContent;
	Cursor currentCursor = cursorScreenContent.getCursor();
	return new Fragment(0,0, currentCursor.getDrawPositionShirina(), currentCursor.getDrawPositionVisota());
}

//���������� �������� �������� ��� �������� ������� �����
public Fragment getBigMapLeftUpFragment(){
	//������������� ������� ������� ��������� ����� ����� � �������� �������
	return CheckAndNames.smallToBigFragment(currentScreenContent.getLeftUpFragment(), getCursor());
}

}