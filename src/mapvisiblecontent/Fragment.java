package mapvisiblecontent;

import javax.microedition.lcdui.Image;
import programdirection.ProgramParameters;

//���� ����� ������ � ���� �������� � ���������� � ����������� ������������ ������
public class Fragment extends PaintAndMoving{

public static final int STATUS_EMPTY = 0; //������
public static final int STATUS_ERROR = 1; //������ ������
public static final int STATUS_FILL = 2; //����������� ������������

//������� ���� ��������
private int fileIndexHorizontal;
private int fileIndexVertical;

//�������� ����������
private Image content;

//������� ��������� ���������
private int status;

public Fragment(int inFileIndexHorizontal, 
				int inFileIndexVertical, 
				int inDrawPositionShirina, 
				int inDrawPositionVisota){
	
	super(inDrawPositionShirina, inDrawPositionVisota);
	
	fileIndexHorizontal = inFileIndexHorizontal;
	fileIndexVertical = inFileIndexVertical;
	drawPositionShirina = inDrawPositionShirina;
	drawPositionVisota = inDrawPositionVisota;
	
	status = STATUS_EMPTY; //������ ������	
}

//������ �������� � ������������� ������
public void fillFragment(){
	ImageRead reader;
	
	//������ ������� ���� � �����
	String currentFilePatch = CheckAndNames.generateFilePatch(fileIndexHorizontal, fileIndexVertical);
	reader = new ImageRead(currentFilePatch);
	
	if (reader.isCorrectRead()){
		content = reader.getImage();
		status = STATUS_FILL;
	} else {
		status = STATUS_ERROR;
	}
}


public Image getContent(){
	return content;
}


public int getStatus(){
	return status;
}


/////////////////��������� �������� ����� ��������� � ��� ��������� ������������� � �������������
public int getNameIndexHorizontal(){
	return fileIndexHorizontal;
}

public int getNameIndexVertical(){
	return fileIndexVertical;
}

public int getNextHorizontalIndexFile(){
	return fileIndexHorizontal+1;
}

public int getPervHorizontalIndexFile(){
	return fileIndexHorizontal-1;
}

public int getNextVerticalIndexFile(){
	return fileIndexVertical+1;
}

public int getPervVerticalIndexFile(){
	return fileIndexVertical-1;
}


//������� � ���������� �������� ����� �������
public Fragment getDublicate(){
	return new Fragment(fileIndexHorizontal, fileIndexVertical, drawPositionShirina, drawPositionVisota);
}

//����������� ������� ��� ������� ������ �� ����������� �����������, ��� ������������ ������ gc()
public void killContent(){
	content = null;
}

}