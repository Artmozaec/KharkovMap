package rms;

import javax.microedition.rms.*;
import userinterface.UserInterface;


//����� ������� ��������� ������� ����������� ������������� ������ ��������� � ����� ������� 
//��� ������ � ������� ��������� ����������� �������� ������� ��������� 
//�������� ����������� � ��� ������ ��������� �������� ������


abstract class OneStoreOneRecord {


//��� ��������� � ������� �������� ���� ������
public String recordStoreName;

//������ � ������� ����������� ���������� ������
public byte[] data = null;

//��������� ������ ������ ���������
private boolean errorReadStore;


OneStoreOneRecord(String inStoreName){
	recordStoreName = inStoreName;
	errorReadStore = false;
	//������������-�� ��������� 
	if (recordStoreCheck()){ //������������
		System.out.println("Hranilishe est");
		//��������� ���������� � �� ������ � parameters
		restoreData();
	} else {
		//��������� ���, ��������� ����-������ �������� ������������� � �����������
		errorReadStore = true;
		System.out.println("Hranilisha net");
		isNotRecordStore();
	}
}

//� ������ ���������� ���������� ��� ���������� ��������� ����������� ���� ��������
protected abstract void isNotRecordStore();

protected boolean readStoreError(){
	return errorReadStore;
}

private void deleteStore(){
	try{
		//������� ���������
		RecordStore.deleteRecordStore(recordStoreName);
	} catch( RecordStoreNotFoundException e ){
		// ��� ������ ���������
		System.out.println("deleteStore() >>>> netHranilisha");
	} catch( RecordStoreException e ){
		// ��������� �������
		System.out.println("deleteStore() >>>> hranilishe otkrito");
	}
}

//������� ������ ���������
//��������� � rms ������ ���������� data � ��������� ���������
public void saveData(){
	RecordStore recordStore;
	try{
		//������� ������ ���������
		deleteStore();
		
		//������� �����
		recordStore = RecordStore.openRecordStore(recordStoreName, true);
	
		//�������� � ���� ������
		recordStore.addRecord(data, 0, data.length);
		
		//������� ���������
		recordStore.closeRecordStore();
	} catch (RecordStoreException rse){
		System.out.println("saveParameters >>>>>>>>>>> FIGNYA!!!");
		UserInterface.getUserInterface().showErrorMessage(rse.getMessage());
	}
}

//��������� � data �� rms ������
private void restoreData(){
	RecordStore recordStore;
	try{
		//������� ���������
		recordStore = RecordStore.openRecordStore(recordStoreName, true);
		System.out.println("otkrili hranilishe recordStore = "+recordStore);
		//��������� �� ���� ������
		data = recordStore.getRecord(1);
		System.out.println("prochitali hranilishe ");
		//������� ���������
		recordStore.closeRecordStore();
		System.out.println("zakryli hranilishe ");
	} catch (RecordStoreException rse){
		errorReadStore = true;
		UserInterface.getUserInterface().showErrorMessage(rse.getMessage());
	}
}

private boolean existRecordName(String[] stores){
	for(int ch=0; ch<stores.length; ch++){
		System.out.println("store = "+stores[ch]);
		if (recordStoreName.equals(stores[ch])) return true;
	}
	return false;
}


//� ������ ���������� �������� ������������ ������ ����������� �� ������
protected abstract boolean checkCorrectStore();

//���������� ����-�� ��������� ��� ������ �� ���� ������
private boolean recordStoreCheck(){
	String[] stores;

	//�������� ������ ��������
	stores = RecordStore.listRecordStores();
	
	//��� ������ ���������� ������ ���!
	if (stores == null) return false;

	//���� �� � ������ ������ ��� ���������?
	if (!existRecordName(stores)) return false;
	
	//��������� ����,,, �� ������������� �� ��� ����������� ������������ ����������� ������?
	if (checkCorrectStore()) return true;

	//��������� ����� ��������� ��� �� ����������!
	return false;
}

}