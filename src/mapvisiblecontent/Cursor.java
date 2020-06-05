package mapvisiblecontent;

class Cursor extends PaintAndMoving{


Cursor(int inDrawPositionShirina, int inDrawPositionVisota){
	super (inDrawPositionShirina, inDrawPositionVisota);
}


public void changePositionVisota(int newPosition){
	drawPositionVisota=newPosition;
}

public void changePositionShirina(int newPosition){
	drawPositionShirina=newPosition;
}

}