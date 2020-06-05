package databaseacess;

import mapvisiblecontent.Fragment;

public class DatabaseRecord{

private Fragment fragment;
private int number;
private String adressName;

//����� � ���� �������� ����� �������� ����� � ����� �� ����� ���� ������ � �����
public DatabaseRecord(int inNumber, String inAdressName, Fragment inFragment){
	fragment = inFragment;
	adressName = inAdressName;
	number = inNumber;
}

public Fragment getFragment(){
	//����� ����� ���������� ������ �������� � �� ������ �� ������, ������-��� ���� �������� ����� ���������� ��� �������� �� ���� � �������� �� �����
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