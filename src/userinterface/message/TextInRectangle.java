package message;

import java.util.*;
import javax.microedition.lcdui.*;
import userinterface.Colors;

class TextInRectangle{

//private int rectangleVisota;
private int rectangleShirina;

//���������� ����� �������� �� ������
private int verticalSpace;

private Vector splitText;

//����� ������� ����� ������� ����� ���������
private Font messageFont;

TextInRectangle(String inTextContent, int inShirina){
	//rectangleVisota=inVisota;
	rectangleShirina=inShirina;
	verticalSpace = 2;
	
	//��������� ������� �����
	messageFont = Font.getFont(Font.FACE_SYSTEM, Font.STYLE_BOLD, Font.SIZE_LARGE);
	
	splitText = new Vector();
	
	//��������� ������ �� ��������� ����������� � ������������� �������
	splitToRectangleString(inTextContent);
}


//��������� ���� ������ ��������
private String oneStringCreator(TextContainer textContainer){
	boolean firstWord = false; //���� ������������ ������-�� ������ �����
	boolean endText = false;
	
	//���� ���� ������ ������ �� ����� ������ ������ �������
	while (textContainer.getSize()<rectangleShirina){
		//���� �� ���� ���������� ������ ��� ����� ��� ����� ��������� ������ ���������� ��� ������������� ���� firstWord true`
		if(textContainer.isSeparator()) {
			textContainer.memoryPosition();
			firstWord = true;
		}
		
		//System.out.println("������ = "+textContainer.getSize());
		if (!textContainer.nextChar()){ 
			endText = true;
			break;
		}
	}
	
	//���� ������� ���� ������� �����, � ����� �� ����������, ������ ���� ��� ��������� ���� � ��� ��� ����
	//���������� ������� ��������� � ����� ���������� ������������� �����
	if ((firstWord) && (!endText)) textContainer.rememberPosition();
	
	String result = textContainer.createString();
	result = result.trim();
	return result;
}


private int getDrawPositionStringX(int index){
	String str = (String)splitText.elementAt(index);
	int strLength = messageFont.stringWidth(str);
	//System.out.println("������ str =>>>>"+str+"<<<<");
	//System.out.println("rectangleShirina = "+rectangleShirina);
	//System.out.println("strLength = "+strLength);
	//System.out.println("getDrawPositionStringX >>> "+(rectangleShirina - strLength));
	return (rectangleShirina - strLength)/2;
}

private int getDrawPositionStringY(int index){
	return (messageFont.getHeight()+verticalSpace)*index;
}

private void splitToRectangleString(String text){
	//������ ������� ������
	TextContainer textContainer = new TextContainer(text, messageFont);
	String str;
	do{
		str = oneStringCreator(textContainer);
		//System.out.println("���� splitToRectangleString>>> "+str);
		splitText.addElement(str);
	} while (textContainer.textExist());
}


//���������� ���������� �� ������
public void draw(int positionX, int positionY, Graphics g){
	int drawStringX;
	int drawStringY;
	String str;
	g.setFont(messageFont);
	
	//������ ������������� �������
	g.setColor(Colors.WHITE);
	
	for(int ch =0; ch<splitText.size(); ch++){
		str = (String)splitText.elementAt(ch);
		drawStringX = getDrawPositionStringX(ch)+positionX;
		drawStringY = getDrawPositionStringY(ch)+positionY;
		g.drawString(str, drawStringX, drawStringY, g.TOP|g.LEFT);
	}
}

}