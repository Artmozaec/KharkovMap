package message;
import java.io.*;
import javax.microedition.lcdui.*;
import userinterface.UserInterface;
import userinterface.Colors;

abstract class AbstractInterfaceScreen extends Canvas{

private Image alertImage;

private int screenVisota;
private int screenShirina;

private int halfScreenVisota;
private int halfScreenShirina;

private int positionImageVisota;
private int positionTextFieldVisota;
private int textFieldVisota;




private TextInRectangle textField;
//Отступ по бокам рамки текстового сообщения
private int textIdent;

public static final int MODE_ERROR=0;
public static final int MODE_INFO=1;
public static final int MODE_QUESTION=2;


AbstractInterfaceScreen(String inMessageContent, int mode){
	setFullScreenMode(true);
	
	screenVisota = getHeight();
	screenShirina = getWidth();
	
	halfScreenVisota = screenVisota/2;
	halfScreenShirina = screenShirina/2;
	
	alertImage = createAlertImage(mode);
	
	//Позиция отрисовки изображения
	positionImageVisota = (screenShirina - alertImage.getWidth())/3;
	
	//Позиция отрисовки поля текста
	positionTextFieldVisota = positionImageVisota+alertImage.getHeight()+5;

	// отступ с боку рамки текста 1/20 экрана
	textIdent = screenShirina/20;
	
	//Создаём объект текстового наполнения
	textField = new TextInRectangle(inMessageContent, screenShirina-textIdent*2);
}

private void drawAlertImage(Graphics g){
	g.drawImage(alertImage, halfScreenShirina, positionImageVisota, g.TOP|g.HCENTER);
}


protected int getScreenVisota(){
	return screenVisota;
}

protected int getScreenShirina(){
	return screenShirina;
}

private Image createAlertImage(int mode){
	try{
		if (mode == MODE_ERROR){ 
			return Image.createImage("/krest.png");
		} else if (mode == MODE_INFO){
			return Image.createImage("/vosklicanie.png");
		} else if (mode == MODE_QUESTION){
			return Image.createImage("/quest.png");
		}
	}catch(IOException e){
		
	}
	return null;
}

protected abstract void extendsPaint(Graphics g);

public void paint(Graphics g){
	System.out.println("MESSAGE PAINT");
	g.setColor(Colors.BLACK);
	g.fillRect(0,0,screenShirina, screenVisota);
	drawAlertImage(g);
	
	//Рисуем текст сообщения
	textField.draw(textIdent, positionTextFieldVisota, g);
	
	extendsPaint(g);
}


}