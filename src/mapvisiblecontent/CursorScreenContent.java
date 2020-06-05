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

//Пересчитывает экранные позиции в объектах Fragment
//Отнимает от их высоты значение deltaMoving
protected void moveUpFragments(int delta ){
	super.moveUpFragments(delta);
	cursorMoveUp(delta);
}

//Пересчитывает экранные позиции в объектах Fragment
//Прибавляет к их высотам значение deltaMoving
protected void moveDownFragments(int delta){
	super.moveDownFragments(delta);
	cursorMoveDown(delta);
}

//Пересчитывает экранные позиции в объектах Fragment
//Отнимает от их смещения в право deltaMoving
protected void moveLeftFragments(int delta){
	super.moveLeftFragments(delta);
	cursorMoveLeft(delta);
}

//Пересчитывает экранные позиции в объектах Fragment
//Прибавляет к их смещению в право deltaMoving
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
	//Погрешности вызванные размером экрана
	int deltaVisota = 10;
	int deltaShirina = 10;
	
	//Половины от высоты и ширины размера экрана
	int halfScreenShirina = programParameters.getScreenShirina()/2;
	int halfScreenVisota = programParameters.getScreenVisota()/2;
	
	return new Cursor(halfScreenShirina, halfScreenVisota);
}



}