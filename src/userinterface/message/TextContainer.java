package message;

import javax.microedition.lcdui.*;

class TextContainer{

//содержимое
private String content;

//Константа разделителя
private final char separator = ' ';

//Указатель
private int counter;
private int memoryCounter;

//Теущий объект шрифта для рассчёта размеров строки
private Font currentFont;

TextContainer(String inContent, Font inCurrentFont){
	//System.out.println("TextContainer >>>"+inContent);
	currentFont = inCurrentFont;
	content = inContent;
	counter = 1;
}

//Если текст присутствует
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

//создает и возвращает строку от начала до указателя и удаляет удаляет этот отрезок из содержимого
public String createString(){
	String result;
	
	result = content.substring(0, counter+1);
	
	
	content = content.substring(counter+1);
	
	//Убираем возможный пробел в начале
	content.trim();
	//Обнуляем указатель
	counter = 0;
	
	return result;
}

//Возвращает размер строки в пикселях от начала до указателя
public int getSize(){
	String str;
	
	//Формируем строку от начала до counter исключительно
	str = content.substring(0, counter);
	
	//System.out.println("str = "+str);
	return currentFont.stringWidth(str);
}

//запоминает текущую позицию указателя
public void memoryPosition(){
	memoryCounter = counter;
}


//присваивает указателю значение из памяти
public void rememberPosition(){
	counter = memoryCounter;
}


}