package databaseacess;

import mapvisiblecontent.Fragment;

public class DatabaseRecord{

private Fragment fragment;
private int number;
private String adressName;

//Несет в себе фрагмент текст название улицы и номер по счету этой записи в файле
public DatabaseRecord(int inNumber, String inAdressName, Fragment inFragment){
	fragment = inFragment;
	adressName = inAdressName;
	number = inNumber;
}

public Fragment getFragment(){
	//Очень важно возвращать именно дубликат а не ссылку на объект, потому-что этот фрагмент потом изменяется при переходе на него и движении по карте
	return fragment.getDublicate();
}

public String getAdressName(){
	return adressName;
}

public int getPositionNumber(){
	return number;
}

public int getSizeRecord(){
	return 20+adressName.length();
}

}