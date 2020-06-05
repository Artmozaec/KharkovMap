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
	//��� ��� ���� ���� ������ ����������� �� ������!!!
	notifyDestroyed(); //���������� ����������� ����������� � ����� ������
}

public void startApp(){
	ProgramParameters programParameters = ProgramParameters.getProgramParameters();
	
	//������ �� ����� ����� ����������!
	display = Display.getDisplay(this); 
	
	//������� �� ������� ����� �������������
	programParameters.setCurrentDisplay(display);
	
	//�������������� ��������� ��������� - ���������� �� �� rms
	programParameters.restoreParameters();
	
	//������� �������� ����������� �����
	programDirection = new ProgramDirection(this);
	

}

}