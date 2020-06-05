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
	
	//������� ������ �����������
	paintProgressIndicator = new PaintProgressIndicator(processName);
	
	//��������� ���� (run)
	this.start();
}

private int getPercent(int inValue){
	return (inValue*100)/maxProgressValue;
}

//���������� �������������� ����� � ����������� ���������
public Displayable getDisplay(){
	return (Displayable)paintProgressIndicator;
}

public void run(){
	int currentValue;
	int currentPercent;
	do{
		//�������� ������� �������� ��������� ��������
		currentValue = process.getProgressValue();
		//System.out.println("currentValue = "+currentValue);
		
		//����� ������� �� ������������� �������� ��������
		currentPercent = getPercent(currentValue);
		//System.out.println("currentPercent = "+currentPercent);
		
		//���������� �����
		paintProgressIndicator.refresh(currentPercent);
		
		//����� ������ �����
		try{
			this.sleep(waitingTime);
		}catch(InterruptedException e){
			System.out.println("!!!!!!!!!!!������ �������� ProgressIndicator!!!!!!!!!!!!!!1");
		}
		
	}while(currentValue<maxProgressValue);
	
	System.out.println("��������� ���������� �����!!");
	//���������� ���������� �������
	userInterface.goToBackScreen();
}

}