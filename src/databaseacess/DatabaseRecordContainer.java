package databaseacess;

public class DatabaseRecordContainer{

private DatabaseRecord container[];
private int pointer;
private int endContainer;

DatabaseRecordContainer(){
	container = new DatabaseRecord[5];
	pointer = 0;
	endContainer = 0;
}

private void extendContainer(){
	//����� ��������� ������ ����������� � 2 ����
	DatabaseRecord[] newContainer = new DatabaseRecord[container.length*2];
	
	//�������� � ����� ��������� ���������� ������� ����������
	//System.arraycopy(������, ������ ������, ����, ������ ����, ����������� ���������� ������ ����������)
	System.arraycopy(container, 0, newContainer, 0, container.length);
	
	//�������� ������
	container = newContainer;
}


public void addRecord(DatabaseRecord inDatabaseRecord){

	//System.out.println("Record Container add record = "+inDatabaseRecord.getAdressName());
	//���� ��������� ����������� ������� ����� ������ ������� ����������, ��� ����� ��������
	if (endContainer == container.length) extendContainer();
	
	//��������� �������
	container[endContainer] = inDatabaseRecord;
	
	//����������� ������ �������
	endContainer++;
}

public int getSize(){
	return endContainer;
}

public void reset(){
	pointer = 0;
}

public boolean nextRecord(){
	pointer++;
	if (pointer>endContainer-1) {//-1 ������-��� endContainer ������ �� 1 ������ ��� ���� �������, ��������� �� ��������� ������ ������� ������� ���� ����� ������ ��������� ������
		pointer = endContainer-1;
		return false;
	}
	
	return true;
}

public DatabaseRecord getRecord(){
	//System.out.println("getRecord() pointer = "+pointer);
	//System.out.println("endContainer = "+endContainer);
	return container[pointer];
}

public DatabaseRecord getRecord(int position){
	return container[position];
}

}