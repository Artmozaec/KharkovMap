package databaseacess;


import mapvisiblecontent.Fragment;
import userinterface.UserInterface;
import programdirection.ProgramParameters;
import progressindicator.Process;


public class DatabaseAcess{

private ProgramParameters programParameters;

//������ - ������ � ��
private DatabaseReader databaseReader;
private DatabaseWriter databaseWriter;

//�������� ���� � ��
private String databaseFolder;
private String tempName;
private String databaseName;
private String oldDatabaseName;

//������� ��������� ���������
private ProgressIndicator progressIndicator;

//����������� ��
public static final byte[] DATABASE_SEPARATOR = new byte[]{38, 38}; 

//������� ������
public static final byte[] CR_LF = new byte[]{13, 10};

public DatabaseAcess (){
	programParameters = ProgramParameters.getProgramParameters();
	
	//�������� ���� � ����� �������� ������
	databaseFolder = programParameters.getDatabaseDirectoryPatch();
	tempName = programParameters.getDatabaseTempName();
	databaseName = programParameters.getDatabaseFileName();
	oldDatabaseName = programParameters.getOldDatabaseName();
	
	//����� ������ ���������
	progressIndicator = new ProgressIndicator();
}

public Process getIndicator(){
	return (Process)progressIndicator;
}

private boolean substringIsExist(DatabaseRecord currentDBRecord, String substring){
	//�������� ������
	String name = currentDBRecord.getAdressName();
	int searchPosition;
	
	//��� ��� ������ ������ ��������� � ������ �������� � ������ ��������
	//��������� ��� ������ � ���� �������
	name = name.toLowerCase();
	substring = substring.toLowerCase();
	
	
	//���������� ������ ��������� ��������� � ������
	searchPosition = name.indexOf(substring);
	
	if (searchPosition == -1){//��� ��������� � ������
		return false;
	} else {//��������� ����������
		return true;
	}
}



public DatabaseRecordContainer search(String query){
	//�������� ���������
	progressIndicator.reset();
	
	//������� ��������� 
	DatabaseRecordContainer container = new DatabaseRecordContainer();
	
	//������� ������ ������ ���� ������ - �� ������ ���� � ���������� ��� �� 1 ������
	databaseReader = new DatabaseReader(databaseFolder+databaseName);
	
	//���� ���� �� �������� ���� � ���� �� ��������� ������ ������ ����� ��
	while (true) {
		
		//�������� ������
		DatabaseRecord record = databaseReader.getRecord();
		
		//��������� ��������
		progressIndicator.addProgress(record.getSizeRecord());
		
		//���� ���� �� �������� ���� ��� ���� �� ��������� ������ ������ ����� ��
		if ((databaseReader.fileIsEnd()) || (databaseReader.databaseIsError())) break;
		
		
		if ((substringIsExist(record, query)) || //��������� ���� - �� � ������� ��������� ������� ���������
			(substringIsExist(record, DatabaseReader.databaseRecordErrorString)))//�� ������ ���� � ���� ������ ������ � ������ ���� �����, ��� ����� ������							)
		{
			//���� ���� ��������� ��� ������ � ���������
			container.addRecord(record);
		}	
	}
	//System.out.println("search - ���������� ���������!");
	
	//��������� ����� ������
	databaseReader.finalizeRead();
	
	return container;
}


private void stopErrorProcess(){
		//��������� ������
		databaseWriter.stopErrorRecord();
			
		//��������� ������
		databaseReader.finalizeRead();
			
		//���������� ���������
		UserInterface.getUserInterface().showErrorMessage("������ ����� ��! ������ �� �������");
}

public void addRecord(DatabaseRecord addDatabaseRecord){
	//�������� ���������
	progressIndicator.reset();
	
	//������� ������ ������ ��
	databaseWriter = new DatabaseWriter(databaseFolder, tempName, databaseName, oldDatabaseName);
	
	//������� ������ ������ ��
	databaseReader = new DatabaseReader(databaseFolder+databaseName);
	
	//� ������ ������������ �� ��������� ���� ��� ������ ���� ������
	while (true) {
		//�������� ������
		DatabaseRecord record = databaseReader.getRecord();
		
		//�������� ���������
		progressIndicator.addProgress(record.getSizeRecord());
		
		//���� ���� �� �������� ���� ��� ���� �� ��������� ������ ������ ����� ��
		if ((databaseReader.fileIsEnd()) || (databaseReader.databaseIsError())) break;
				
		//���� �� ��������� ������ ����������� ������ ��
		if (!(databaseReader.recordIsError())){
			//��������� � � ����� ����
			databaseWriter.addRecord(record);
		} else {
			//System.out.println("!!!!!!!!!!!!!!!!!addRecord ������ ������ ��! !!!!!!!!!!!!!!!!!!!!"+record.getPositionNumber());
		}
	}
	
	//���� ��������� ������ ����� ��
	if (databaseReader.databaseIsError()){
		//�� ����������! �� ����������!
		stopErrorProcess();
		
		//������� �����
		return;
	}
	
	//������ ��������� ����� ������
	databaseWriter.addRecord(addDatabaseRecord);
	
	//������������ ������ - ��������� ������ ��������������� ��������� ����
	databaseReader.finalizeRead();
	databaseWriter.finalizeRecord();

	//�������� ���������
	//UserInterface.getUserInterface().showInfoMessage("����� ������ ������� ���������");//��-�� �� ������������
}


public void deleteRecord(DatabaseRecord deleteRecord){
	//�������� ���������
	progressIndicator.reset();
	
	//������� ������ ������ ��
	databaseWriter = new DatabaseWriter(databaseFolder, tempName, databaseName, oldDatabaseName);
	
	//������� ������ ������ ��
	databaseReader = new DatabaseReader(databaseFolder+databaseName);
	
	//����� ������ ������� ��� ������ ����� ����������... ��-���� �������
	int passRecordNumber = deleteRecord.getPositionNumber();
	//System.out.println("��������� ������ ����� >>>>>>"+passRecordNumber);
	
	boolean databaseError;
	boolean thisDeleteNumber;
	while (true) {
		//�������� ������
		DatabaseRecord record = databaseReader.getRecord();
		
		//�������� ���������
		progressIndicator.addProgress(record.getSizeRecord());
		
		
		//���� ���� �� �������� ���� ��� ���� �� ��������� ������ ������ ����� ��
		if ((databaseReader.fileIsEnd()) || (databaseReader.databaseIsError())) break;
		

		
		//������ ����������� ������ ��
		databaseError = databaseReader.recordIsError();
		
		//��� ��������� ������?
		thisDeleteNumber = (passRecordNumber == record.getPositionNumber());
		
		//���� �� ��������� ������ � �� ������ ����������� ������ ��
		if ((!thisDeleteNumber) && (!databaseError)) {	
			//��������� � � ����� ����
			databaseWriter.addRecord(record);
		} else {
			//System.out.println("!!!!!!!!!!!!!!!!!!deleteRecord >>> ������� ��� ������!!!!!!!!!!!!!!!!!!!!"+record.getPositionNumber());
		}
	}	
	
	//���� ��������� ������ ����� ��
	if (databaseReader.databaseIsError()){
		//�� ����������! �� ����������!
		stopErrorProcess();
			
		//������� �����
		return;
	}
	
	//������������ ������ - ��������� ������ ��������������� ��������� ����
	databaseReader.finalizeRead();
	databaseWriter.finalizeRecord();
}



public void replaceRecord(DatabaseRecord replaceDatabaseRecord){
}

}