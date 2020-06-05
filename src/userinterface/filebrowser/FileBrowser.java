package filebrowser;

import java.io.*;

import java.util.*;

import javax.microedition.io.*;
import javax.microedition.io.file.*;
import javax.microedition.lcdui.*;
import javax.microedition.midlet.*;

public class FileBrowser extends List implements CommandListener{

private static final String UP_DIRECTORY = "..";
private static final String MEGA_ROOT = "/";
private static final String SEP_STR = "/";
private static final char SEP = '/';
//������� ��������� � ���������� �������� �������
private static final String FS_PREFIX = "file://";

private String currentPatch;
private Command view;

private Image dirIcon;



//������ �� ���������� �������� �����
private FileBrowserListener listener;

//������ ��� ���������� �� ������� ������� ����� ����������� ��������-�� ���������� ����� ������� ��������� � ������
private String[] needFolders;



//inListener - ��������� ����������� ������
//inNeedFolders - ������ ����������� ����� ��� ����������� ����� ���������� �����
public FileBrowser(FileBrowserListener inListener, String[] inNeedFolders){
	//������ ��� List
	super(MEGA_ROOT, List.IMPLICIT);
	
	//������� ������
	view = new Command("View", Command.ITEM, 1);
	this.setSelectCommand(view);
	this.setCommandListener(this);
	
	listener=inListener;
	needFolders = inNeedFolders;
	
	//����� ����� �� ����� ����
	currentPatch=MEGA_ROOT;

	try{
		dirIcon=Image.createImage("/icons/dir.png");
	}catch(IOException e){
		dirIcon=null;
	}

	try{
		showCurrDir();
	}catch(SecurityException e){
		e.printStackTrace();
	}catch(Exception e){
		e.printStackTrace();
	}
}

//��������� ���������� �� ��� ������� � ���������� ��������� � ���� ������� 
//0 - ������� �����, 1 - ������� �����
private Vector[] splitFolderAndFiles(Enumeration content){
	Vector folders = new Vector();
	Vector files = new Vector();
	
	while(content.hasMoreElements()){
		String name = (String)content.nextElement();
		if (name.charAt(name.length()-1)==SEP){ 
			//���� ��� ������������� ������������-��� �����
			folders.addElement(name);
		}else{
			//����� ����
			files.addElement(name);
		}
	}
	
	//���������� ��� ������� � ����� �������
	return new Vector[] {files, folders};
}

private Vector getFolders(String patch){
	FileConnection currDir = null;
	Enumeration folderContent = null;
	//���� �� �� ������� �������� �������� ������ ���������� ������ ����������
	if (MEGA_ROOT.equals(patch)){
		folderContent = FileSystemRegistry.listRoots();
	}else{//����� �������� ������ �� �������� ����	
		try{
			currDir=(FileConnection)Connector.open(FS_PREFIX+patch);
			folderContent=currDir.list();
		}catch(IOException ioe){
			ioe.printStackTrace();
		}
		
		try{
			if (folderContent != null){
				currDir.close();
			}
		}catch(IOException ioe){
			ioe.printStackTrace();
		}
		
	}

	//��������� folderContent �� 2 ������� ����� � �����
	Vector[] foldersFiles = splitFolderAndFiles(folderContent);
	
	//���������� ������ �����
	return foldersFiles[1];
}

//���������� ����� � ������� content ������ searchString
private boolean existName(Vector content, String searchString){
	String name;
	for (int ch=0; ch<content.size(); ch++){
		//�������� ��� ���������� � ��������� ��� � ������ �������
		name = ((String)content.elementAt(ch)).toLowerCase();
		if (searchString.equals(name)) return true;
	}
	return false;
}

//����� ��������� ������� �� ���� patch ����������� �����
private boolean isMapContentFolder(String folder){
	//�������� ������ ���������� �� ���� folder
	Vector content = getFolders(folder);
	
	//���� � ����������, ��� �������� ������� needFolders
	for (int ch=0; ch<needFolders.length; ch++){
		//��� ������ �� �� ������� ������� ����� - ����� �� ��������, ���������� - ��#
		if (!existName(content, needFolders[ch])) return false;
	}
	return true;
}


//��������� ������� � ���������� �������������
private boolean folderIsNotEmpty(String folder){
	//�������� ������ ���������� �� ���� folder
	Vector content = getFolders(folder);
	
	if (content.size()>0) return true;
	return false;
}

public void commandAction(Command c, Displayable d){
	//�������� ���������� ���������� ������
	List curr = (List)d;
	final String selectDir = curr.getString(curr.getSelectedIndex());
	
	new Thread (new Runnable(){
		public void run(){
			//���� ��������� ����� ������ ������� �� ������� ����
			if(selectDir.equals(UP_DIRECTORY)){
				traverseDirectory(selectDir);
			}else if (isMapContentFolder(currentPatch+selectDir)){ //��������� �� ������� ����������� ��������� ����� � ������
				//������� ���� ���� �����������
				listener.patchChoosed(FS_PREFIX+currentPatch+selectDir);
			}else if (folderIsNotEmpty(currentPatch+selectDir)){ //���� �� � ����� ��������?
				traverseDirectory(selectDir);
			}
		}
	}).start();
}

private void showCurrDir(){
	//������� ��� �������� �������� ������
	this.deleteAll();
	
	//������������� ��������� - ������� ����
	this.setTitle(currentPatch);
	
	//���� �� ����� ������� �������� - (������ ���������� ������)
	if (!MEGA_ROOT.equals(currentPatch)) this.append(UP_DIRECTORY,dirIcon); //��������� ������� ������ �� ������� ����
	
	//�������� ������ �����
	Vector folders = getFolders(currentPatch);
	
	//������ ������ �����
	for (int ch=0; ch<folders.size(); ch++){
		this.append((String)(folders.elementAt(ch)), dirIcon);
	}
}

private void traverseDirectory(String folderName){
	//���� MEGA_ROOT ������ ���
	if((currentPatch.equals(MEGA_ROOT)) && (folderName.equals(UP_DIRECTORY))) return;
	
	if (folderName.equals(UP_DIRECTORY)){ //����������� �� ���� ���������� �����
		//������� ��������� ��������� � ������ ������� �����������
		int i=currentPatch.lastIndexOf(SEP,currentPatch.length()-2); //� -2 ������-��� �� ����� ���� ��� ����� ���� ��������� �����������!
		
		if (i!=-1){
			//�������� ��������� �� ������ �� �������������� �����������
			currentPatch=currentPatch.substring(0, i+1);
		} else {
			// -1 -��� ������ ��� ����������� ��� �� ����������
			currentPatch=MEGA_ROOT;
		}
	} else { //�� ���� ���������� � �����
		currentPatch=currentPatch+folderName;
	}

	//���������� ��-��� ����������
	showCurrDir();
}


}
