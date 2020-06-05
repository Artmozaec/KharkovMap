package programdirection;
import javax.microedition.lcdui.*;
import javax.microedition.midlet.MIDlet;
import programdirection.ProgramDirection;
import programdirection.ProgramParameters;

public class KharkovMap extends MIDlet{

private ProgramDirection programDirection;
private Display display;


public void pauseApp() {
}

public void destroyApp(boolean dede){
	//¬от без этой хери мидлет закрыватьс€ не желает!!!
	notifyDestroyed(); //”ведомл€ет программное обеспечение о конце работы
}

public void startApp(){
	ProgramParameters programParameters = ProgramParameters.getProgramParameters();
	
	//ссылка на экран стала √ЋќЅјЋ№Ќќ…!
	display = Display.getDisplay(this); 
	
	//—ссылка на дисплей стала общедоступной
	programParameters.setCurrentDisplay(display);
	
	//»нициализируем настройки программы - вычитываем их из rms
	programParameters.restoreParameters();
	
	//—оздаем основной управл€ющий класс
	programDirection = new ProgramDirection(this);
	

}

}