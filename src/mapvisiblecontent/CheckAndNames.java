package mapvisiblecontent;

import javax.microedition.io.*;
import javax.microedition.io.file.*;
import java.io.*;
import programdirection.ProgramParameters;

//����� ������� ������-������� ����������� ��������� ������� ������ ��������� 
public class CheckAndNames{

//����������� �������� ���� ����� � �������
private static int mapScale = 8;

//�������� ������� �������� ������� ���� ������� ������ ���������� ������� ScreenContent
public static boolean AllFragmentExist(Fragment inFragment){
	
	//������� ��������� �������
	int indexHorizontal = inFragment.getNameIndexHorizontal();
	int indexVertical = inFragment.getNameIndexVertical();

	boolean leftUp = checkFileExist(indexHorizontal, indexVertical);
	boolean rightUp = checkFileExist(indexHorizontal+1, indexVertical);
	boolean leftDown = checkFileExist(indexHorizontal, indexVertical+1);
	boolean rightDown = checkFileExist(indexHorizontal+1, indexVertical+1);
	
	if ((leftUp) && (rightUp) && (leftDown) && (rightDown)){
		return true;
	} else return false;
}

//���������� true ���� ���� � ��������� ����������� � ���������, � ������� ������ ������������
public static boolean checkFileExist(int indexHorizontal, int indexVertical){
	//�������� ����
	String filePatch = generateFilePatch(indexHorizontal, indexVertical);
	//� ������ ���� �������
	return checkFileExist(filePatch);
}

//���������� true ���� �� ���� ������������ � ��������� ������������� ���� ����
public static boolean checkFileExist(String filePatch){
	FileConnection fileConnect = null;
	boolean result;
	try {
		fileConnect = (FileConnection)Connector.open(filePatch);
		result = fileConnect.exists();
		fileConnect.close();
		System.out.println(">>>>>checkFileExist<<<<<< result >>>"+ filePatch +"  >>>"+ result);
	}catch(IOException ioe){
		System.out.println(">>>>>checkFileExist<<<<<<S FAILOM FIGNYA!!!");
		return false;
	}
	
	return result;
}


//���������� ���� � ����� � ����������� �� ������ ���������
public static String generateFilePatch(int horizontalIndex, int verticalIndex){
	StringBuffer resultStr = new StringBuffer();
	ProgramParameters programParameters = ProgramParameters.getProgramParameters();
	
	//������ �������� ������� - ���� � ����� ��� ����������� �������� � ������� �����������
	resultStr.append(programParameters.getCurrentModePatch());
	
	//�������� � ����� �������� ����������� ������� �����
	resultStr.append(FilesInDirectory.getFilesInDirectoryInstance().getDirectroyPatch(horizontalIndex, verticalIndex));
	
	//� ��������� ����
	resultStr.append("/");
	
	
	//��������� ��� �����
	resultStr.append("x"+horizontalIndex+"_y"+verticalIndex);
	
	
	//��������� ���������� - gif ��� jpg
	resultStr.append(programParameters.getCurrentModeFileType());
	
	return resultStr.toString();
}





public static Fragment smallToBigFragment(Fragment inFragment, Fragment cursor){
	ProgramParameters programParameters = ProgramParameters.getProgramParameters();
	
	//�������� ������ ������� ����������� - ���������
	int fragmentSize = programParameters.getImageSize();
	
	
	//�������� �� ������ � ������ ������� ������
	int halfScreenShirina = programParameters.getScreenShirina()/2;
	int halfScreenVisota = programParameters.getScreenVisota()/2;
	
	//������� ������� ������� ������� ������������ ����  ����� �����
	//��� ����� ������  ������ �������� ������ �������� ��������� �������� �� ������ ��������� 
	//���������� � �������� ��������� (�� ������) 
	//� ���������� ��� ��� � �������� ��������� �������
	int realPixelPositionVisota = inFragment.getNameIndexVertical()*fragmentSize+Math.abs(inFragment.getDrawPositionVisota()) + cursor.getDrawPositionVisota();
	int realPixelPositionShirina = inFragment.getNameIndexHorizontal()*fragmentSize+Math.abs(inFragment.getDrawPositionShirina()) + cursor.getDrawPositionShirina();
	
	//��������� �������� �� ����������� ����������� ���� � �������� ������ �� ������� �����
	realPixelPositionVisota = realPixelPositionVisota * mapScale;
	realPixelPositionShirina = realPixelPositionShirina * mapScale;
	

	//������� ����� ����������� �������� �� ������ ������ ��������� �� ������ ����� �����������-���������
	int fileIndexHorizontal = realPixelPositionShirina/fragmentSize;
	int fileIndexVertical = realPixelPositionVisota/fragmentSize;

	int newPositionShirina =fileIndexHorizontal*fragmentSize - realPixelPositionShirina;
	int newPositionVisota = fileIndexVertical*fragmentSize - realPixelPositionVisota;
	
	
	//���������� � �������� � ������ � ������ ��������� �������� ������� ������ �� ��������� � ����������� �������������� ������� ������� ����� �� ����� ����� �� ������ ������
	
	newPositionShirina += halfScreenShirina;
	newPositionVisota += halfScreenVisota;
	
	//��� ���� ���� ����������� ��� ��������� ����������� ������ ���� �� ������ ����� ����������� �� ������� � ������ ����������� � ������� �������� ����������� �� ������ ���������

	if (newPositionShirina>0){
		fileIndexHorizontal--;
		newPositionShirina -= fragmentSize;
	}
	
	if (newPositionVisota>0){
		fileIndexVertical--;
		newPositionVisota -= fragmentSize;
	}
	
	return new Fragment(fileIndexHorizontal,
						fileIndexVertical,
						newPositionShirina,
						newPositionVisota);

}

public static Fragment bigToSmallFragment(Fragment inFragment){
	ProgramParameters programParameters = ProgramParameters.getProgramParameters();
	
	//�������� ������ ������� ����������� - ���������
	int fragmentSize = programParameters.getImageSize();
	
	//�������� �� ������ � ������ ������� ������
	int halfScreenShirina = programParameters.getScreenShirina()/2;
	int halfScreenVisota = programParameters.getScreenVisota()/2;
	
	//���������� � �������� �������� ��������� ������ �������� ���� ������������ ���� ����� + ������� ��������� ������
	int realPixelPositionVisota = inFragment.getNameIndexVertical()*fragmentSize+Math.abs(inFragment.getDrawPositionVisota())+halfScreenVisota;
	int realPixelPositionShirina = inFragment.getNameIndexHorizontal()*fragmentSize+Math.abs(inFragment.getDrawPositionShirina())+halfScreenShirina;
	
	//�������� ���������� �������� �� ����������� mapScale � �� ������ ������� � �������� �� ����������� �����
	realPixelPositionVisota = realPixelPositionVisota/mapScale;
	realPixelPositionShirina = realPixelPositionShirina/mapScale;
	
	//������� ����� ����������� �������� �� ������ ������ ��������� �� ������ ����� �����������-���������
	int fileIndexHorizontal = realPixelPositionShirina/fragmentSize;
	int fileIndexVertical = realPixelPositionVisota/fragmentSize;

	int newPositionShirina =fileIndexHorizontal*fragmentSize - realPixelPositionShirina;
	int newPositionVisota = fileIndexVertical*fragmentSize - realPixelPositionVisota;
	
	
	//���������� � �������� � ������ � ������ ��������� �������� ������� ������ �� ��������� � ����������� �������������� ������� ������� ����� �� ����� ����� �� ������ ������
	
	newPositionShirina += halfScreenShirina;
	newPositionVisota += halfScreenVisota;
	
	//��� ���� ���� ����������� ��� ��������� ����������� ������ ���� �� ������ ����� ����������� �� ������� � ������ ����������� � ������� �������� ����������� �� ������ ���������

	if (newPositionShirina>0){
		fileIndexHorizontal--;
		newPositionShirina -= fragmentSize;
	}
	
	if (newPositionVisota>0){
		fileIndexVertical--;
		newPositionVisota -= fragmentSize;
	}
	
	return new Fragment(fileIndexHorizontal,
						fileIndexVertical,
						newPositionShirina,
						newPositionVisota);
}




}