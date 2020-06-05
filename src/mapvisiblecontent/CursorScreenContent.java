package mapvisiblecontent;


class CursorScreenContent extends AbstractScreenContent{


private static Cursor cursor;

CursorScreenContent(Fragment inFragment){
	super(inFragment);
	cursor =  createCursor();
	contentAlignDownRight();
}

public Cursor getCursor(){
	return cursor;
}

//������������� �������� ������� � �������� Fragment
//�������� �� �� ������ �������� deltaMoving
protected void moveUpFragments(int delta ){
	super.moveUpFragments(delta);
	cursorMoveUp(delta);
}

//������������� �������� ������� � �������� Fragment
//���������� � �� ������� �������� deltaMoving
protected void moveDownFragments(int delta){
	super.moveDownFragments(delta);
	cursorMoveDown(delta);
}

//������������� �������� ������� � �������� Fragment
//�������� �� �� �������� � ����� deltaMoving
protected void moveLeftFragments(int delta){
	super.moveLeftFragments(delta);
	cursorMoveLeft(delta);
}

//������������� �������� ������� � �������� Fragment
//���������� � �� �������� � ����� deltaMoving
protected void moveRightFragments(int delta){
	super.moveRightFragments(delta);
	cursorMoveRight(delta);
}

public void cursorMoveUp(int delta){
	cursor.moveUp(delta);
}

public void cursorMoveDown(int delta){
	cursor.moveDown(delta);
}

public void cursorMoveRight(int delta){
	cursor.moveRight(delta);
}

public void cursorMoveLeft(int delta){
	cursor.moveLeft(delta);
}




private Cursor createCursor(){
	//����������� ��������� �������� ������
	int deltaVisota = 10;
	int deltaShirina = 10;
	
	//�������� �� ������ � ������ ������� ������
	int halfScreenShirina = programParameters.getScreenShirina()/2;
	int halfScreenVisota = programParameters.getScreenVisota()/2;
	
	return new Cursor(halfScreenShirina, halfScreenVisota);
}



}