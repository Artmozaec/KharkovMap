package message;

import javax.microedition.lcdui.*;

class TextContainer{

//����������
private String content;

//��������� �����������
private final char separator = ' ';

//���������
private int counter;
private int memoryCounter;

//������ ������ ������ ��� �������� �������� ������
private Font currentFont;

TextContainer(String inContent, Font inCurrentFont){
	//System.out.println("TextContainer >>>"+inContent);
	currentFont = inCurrentFont;
	content = inContent;
	counter = 1;
}

//���� ����� ������������
public boolean textExist(){
	//System.out.println("textExist");
	return (content.length()>0);
}

public boolean nextChar(){
	if (counter == content.length()-1) return false;
	counter++;
	return true;
}

public boolean isSeparator(){
	//System.out.println(" isSeparator >>>> counter = "+counter +" content.length() = "+content.length());
	//System.out.println(content.charAt(counter));
	if (content.charAt(counter) == separator) return true;
	return false;
}

//������� � ���������� ������ �� ������ �� ��������� � ������� ������� ���� ������� �� �����������
public String createString(){
	String result;
	
	result = content.substring(0, counter+1);
	
	
	content = content.substring(counter+1);
	
	//������� ��������� ������ � ������
	content.trim();
	//�������� ���������
	counter = 0;
	
	return result;
}

//���������� ������ ������ � �������� �� ������ �� ���������
public int getSize(){
	String str;
	
	//��������� ������ �� ������ �� counter �������������
	str = content.substring(0, counter);
	
	//System.out.println("str = "+str);
	return currentFont.stringWidth(str);
}

//���������� ������� ������� ���������
public void memoryPosition(){
	memoryCounter = counter;
}


//����������� ��������� �������� �� ������
public void rememberPosition(){
	counter = memoryCounter;
}


}