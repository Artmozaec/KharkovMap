package progressindicator;

//import programdirection.ProgramParameters;
import javax.microedition.lcdui.*;
import userinterface.UserInterface;

public class ProgressIndicator extends Thread{
private Process process;
private int maxProgressValue;
private int waitingTime = 10;
private PaintProgressIndicator paintProgressIndicator;

private UserInterface userInterface;


public ProgressIndicator(UserInterface inUserInterface, Process inProcess, String processName){
	process = inProcess;
	maxProgressValue = inProcess.getMaximumValue();
	userInterface = inUserInterface;
	
	//Создать объект отображения
	paintProgressIndicator = new PaintProgressIndicator(processName);
	
	//Запускаем цикл (run)
	this.start();
}

private int getPercent(int inValue){
	return (inValue*100)/maxProgressValue;
}

//Возвращает отрисовываемый экран с индикатором прогресса
public Displayable getDisplay(){
	return (Displayable)paintProgressIndicator;
}

public void run(){
	int currentValue;
	int currentPercent;
	do{
		//получить текущее значение прогресса процесса
		currentValue = process.getProgressValue();
		//System.out.println("currentValue = "+currentValue);
		
		//найти процент от максимального значения процесса
		currentPercent = getPercent(currentValue);
		//System.out.println("currentPercent = "+currentPercent);
		
		//отрисовать экран
		paintProgressIndicator.refresh(currentPercent);
		
		//ждать нужное время
		try{
			this.sleep(waitingTime);
		}catch(InterruptedException e){
			System.out.println("!!!!!!!!!!!ОШИБКА ОЖИДАНИЯ ProgressIndicator!!!!!!!!!!!!!!1");
		}
		
	}while(currentValue<maxProgressValue);
	
	System.out.println("Индикатор возвращает экран!!");
	//Возвращаем предыдущий дсиплей
	userInterface.goToBackScreen();
}

}