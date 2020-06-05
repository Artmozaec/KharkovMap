package userinterface;

import javax.microedition.lcdui.*;
import java.io.*;

public class About extends Canvas{

private int screenVisota;
private int screenShirina;

private int halfScreenVisota;
private int halfScreenShirina;
private final String version = "version 1.0";

private Image aboutImage;

private UserInterface userInterface;

About(UserInterface inUserInterface){
	setFullScreenMode(true);
	userInterface = inUserInterface;
	screenVisota = getHeight();
	screenShirina = getWidth();
	
	halfScreenVisota = screenVisota/2;
	halfScreenShirina = screenShirina/2;
	
	aboutImage = createAboutImage();
}

private Image createAboutImage(){
	try{
			return Image.createImage("/about.png");
	}catch(IOException e){
		return null;
}
}
public void paint(Graphics g){
	g.setColor(Colors.BLUE);
	g.fillRect(0,0,screenShirina, screenVisota);
	g.drawImage(aboutImage, halfScreenShirina, halfScreenVisota, g.VCENTER|g.HCENTER);
	g.setColor(Colors.WHITE);
}

public void keyPressed(int keyCode) {
	userInterface.goToBackScreen();
}

}