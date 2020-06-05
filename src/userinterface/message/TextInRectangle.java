package message;

import java.util.*;
import javax.microedition.lcdui.*;
import userinterface.Colors;

class TextInRectangle{

//private int rectangleVisota;
private int rectangleShirina;

//Расстояние между строками по высоте
private int verticalSpace;

private Vector splitText;

//шрифт которым будет написан текст сообщения
private Font messageFont;

TextInRectangle(String inTextContent, int inShirina){
	//rectangleVisota=inVisota;
	rectangleShirina=inShirina;
	verticalSpace = 2;
	
	//Формируем текущий шрифт
	messageFont = Font.getFont(Font.FACE_SYSTEM, Font.STYLE_BOLD, Font.SIZE_LARGE);
	
	splitText = new Vector();
	
	//Разделяем строку на подстроки умещающиеся в прямоугольной области
	splitToRectangleString(inTextContent);
}


//формирует одну строку контента
private String oneStringCreator(TextContainer textContainer){
	boolean firstWord = false; //Ключ определяющий прошло-ли первое слово
	boolean endText = false;
	
	//цикл пока длинна строки не будет больше ширины области
	while (textContainer.getSize()<rectangleShirina){
		//если на пути встретился пробел это место где можно разорвать строку запоминаем его устанавливаем ключ firstWord true`
		if(textContainer.isSeparator()) {
			textContainer.memoryPosition();
			firstWord = true;
		}
		
		//System.out.println("Длиина = "+textContainer.getSize());
		if (!textContainer.nextChar()){ 
			endText = true;
			break;
		}
	}
	
	//Если включен флаг первого слова, и текст не закончился, значит одно или несколько слов у нас уже есть
	//Возвращаем позицию указателя к концу последнего обнаруженного слова
	if ((firstWord) && (!endText)) textContainer.rememberPosition();
	
	String result = textContainer.createString();
	result = result.trim();
	return result;
}


private int getDrawPositionStringX(int index){
	String str = (String)splitText.elementAt(index);
	int strLength = messageFont.stringWidth(str);
	//System.out.println("строка str =>>>>"+str+"<<<<");
	//System.out.println("rectangleShirina = "+rectangleShirina);
	//System.out.println("strLength = "+strLength);
	//System.out.println("getDrawPositionStringX >>> "+(rectangleShirina - strLength));
	return (rectangleShirina - strLength)/2;
}

private int getDrawPositionStringY(int index){
	return (messageFont.getHeight()+verticalSpace)*index;
}

private void splitToRectangleString(String text){
	//создаём контент текста
	TextContainer textContainer = new TextContainer(text, messageFont);
	String str;
	do{
		str = oneStringCreator(textContainer);
		//System.out.println("Цикл splitToRectangleString>>> "+str);
		splitText.addElement(str);
	} while (textContainer.textExist());
}


//отображает содержимое на экране
public void draw(int positionX, int positionY, Graphics g){
	int drawStringX;
	int drawStringY;
	String str;
	g.setFont(messageFont);
	
	//Рисуем прямоугольник области
	g.setColor(Colors.WHITE);
	
	for(int ch =0; ch<splitText.size(); ch++){
		str = (String)splitText.elementAt(ch);
		drawStringX = getDrawPositionStringX(ch)+positionX;
		drawStringY = getDrawPositionStringY(ch)+positionY;
		g.drawString(str, drawStringX, drawStringY, g.TOP|g.LEFT);
	}
}

}