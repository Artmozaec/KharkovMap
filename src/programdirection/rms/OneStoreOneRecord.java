package rms;

import javax.microedition.rms.*;
import userinterface.UserInterface;


//класс который реализует удобную возможность использовани€ одного хранилища с одной записью 
//дл€ работы с данными реализует возможности проверки наличи€ хранилища 
//проверки присутстви€ в нем записи изменени€ хранимых данных


abstract class OneStoreOneRecord {


//»м€ хранилища в котором хранитс€ одна запись
public String recordStoreName;

//ћассив в который переноситс€ содержимое записи
public byte[] data = null;

//»ндикатор ошибки чтени€ хранилища
private boolean errorReadStore;


OneStoreOneRecord(String inStoreName){
	recordStoreName = inStoreName;
	errorReadStore = false;
	//ѕрисутствует-ли хранилище 
	if (recordStoreCheck()){ //ѕрисутствует
		System.out.println("Hranilishe est");
		//—читываем записсаные в нЄм данные в parameters
		restoreData();
	} else {
		//’ранилища нет, выполн€ем каие-нибудь действи€ реализованные в наследниках
		errorReadStore = true;
		System.out.println("Hranilisha net");
		isNotRecordStore();
	}
}

//¬ каждой конкретной реализации при отсутствии хранилища выполн€ютс€ свои действи€
protected abstract void isNotRecordStore();

protected boolean readStoreError(){
	return errorReadStore;
}

private void deleteStore(){
	try{
		//”далить хранилище
		RecordStore.deleteRecordStore(recordStoreName);
	} catch( RecordStoreNotFoundException e ){
		// нет такого хранилища
		System.out.println("deleteStore() >>>> netHranilisha");
	} catch( RecordStoreException e ){
		// хранилище открыто
		System.out.println("deleteStore() >>>> hranilishe otkrito");
	}
}

//”дал€ет старое хранилище
//—охран€ет в rms пам€ти содержимое data и закрывает хранилище
public void saveData(){
	RecordStore recordStore;
	try{
		//”далить старое хранилище
		deleteStore();
		
		//—оздать новое
		recordStore = RecordStore.openRecordStore(recordStoreName, true);
	
		//«аписать в него данные
		recordStore.addRecord(data, 0, data.length);
		
		//«акрыть хранилище
		recordStore.closeRecordStore();
	} catch (RecordStoreException rse){
		System.out.println("saveParameters >>>>>>>>>>> FIGNYA!!!");
		UserInterface.getUserInterface().showErrorMessage(rse.getMessage());
	}
}

//«агружает в data из rms данные
private void restoreData(){
	RecordStore recordStore;
	try{
		//ќткрыть хранилище
		recordStore = RecordStore.openRecordStore(recordStoreName, true);
		System.out.println("otkrili hranilishe recordStore = "+recordStore);
		//ѕрочитать из него данные
		data = recordStore.getRecord(1);
		System.out.println("prochitali hranilishe ");
		//«акрыть хранилище
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


//¬ каждой реализации проверка корректности записи выполн€етс€ по своему
protected abstract boolean checkCorrectStore();

//ќпредел€ет есть-ли хранилище дл€ чтени€ из него данных
private boolean recordStoreCheck(){
	String[] stores;

	//ѕолучить список хранилищ
	stores = RecordStore.listRecordStores();
	
	//ќно иногда возвращало именно это!
	if (stores == null) return false;

	//≈сть ли в списке нужное нам хранилище?
	if (!existRecordName(stores)) return false;
	
	//’ранилище есть,,, но соответствует ли оно требовани€м определ€емым наслениками класса?
	if (checkCorrectStore()) return true;

	//’ранилище имеет требуемое им€ но повреждено!
	return false;
}

}