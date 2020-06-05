package progressindicator;


import java.io.*;
import javax.microedition.lcdui.*;


class PaintProgressIndicator extends Canvas{
//private ProgressIndicator progressIndicator;
private int procentBar;

private int screenVisota;
private int screenShirina;
private int barLength;

private static final int RED = 0xFF0000;
private static final int BLACK = 0x000000;
private static final int WHITE = 0xFFFFFF;

//Ўирина отступа по бокам полоски 
private static final int sideBarIdent = 5;

private int barDrawVisota;

private static final int barVisota = 5;
private boolean firstDraw;
private String processName;

private static Font processNameFont;
private Image logo;
//»нициирует перерисовку экрана, и линии прогресса в соответствии со значением inProcentBar
public void refresh(int inProcentBar){
	procentBar = procentToBarLength(inProcentBar);
	repaint();
}


private int procentToBarLength(int procent){
	return (barLength*procent)/100;
}

PaintProgressIndicator(String inProcessName){
	setFullScreenMode(true);
	screenVisota = getHeight();
	screenShirina = getWidth();
	firstDraw = true;
	barLength = screenShirina - (sideBarIdent*2);
	processName = inProcessName;
	barDrawVisota = (screenVisota/2)+(screenVisota/4)+(screenVisota/6);
	processNameFont = Font.getFont(Font.FACE_SYSTEM, Font.STYLE_BOLD, Font.SIZE_LARGE);
	
	try{
		logo=Image.createImage("/logo.png");
	}catch(IOException e){
		logo=null;
	}

}

private void drawProcessName(Graphics g){
	g.setFont(processNameFont);
	g.setColor(WHITE);
	
	int pocessNameVisota =  barDrawVisota -3;
	int processNameShirina = (screenShirina - processNameFont.stringWidth(processName))/2;
	g.drawString(processName, processNameShirina, pocessNameVisota, g.BOTTOM|g.LEFT);
}

private void drawLogo(Graphics g){
	g.setColor(WHITE);
	g.fillRect(0,0,screenShirina, screenVisota/2);
	
	g.setColor(BLACK);
	g.fillRect(0,screenVisota/2,screenShirina, screenVisota);
	
	int drawImageVisota = (screenVisota/2) - (logo.getHeight()/2)-16;
	int drawImageShirina = (screenShirina - logo.getWidth())/2;
	g.drawImage(logo,drawImageShirina,drawImageVisota, 0);
	
	g.setColor(WHITE);
	g.drawRect(sideBarIdent-1, barDrawVisota-1, barLength+1, barVisota+1);
	drawProcessName(g);
}

public void paint(Graphics g){
	drawLogo(g);
	g.setColor(WHITE);
	g.fillRect(sideBarIdent, barDrawVisota, procentBar, barVisota);
}

}