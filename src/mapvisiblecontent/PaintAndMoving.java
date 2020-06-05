package mapvisiblecontent;


class PaintAndMoving{

//������� ��������� �������� �� ������
protected int drawPositionShirina;
protected int drawPositionVisota;



PaintAndMoving(int inDrawPositionShirina, int inDrawPositionVisota){
	drawPositionShirina = inDrawPositionShirina;
	drawPositionVisota = inDrawPositionVisota;
}

/////////////////��������� ������� ��������� ���������
public int getDrawPositionShirina(){
	return drawPositionShirina;
}

public int getDrawPositionVisota(){
	return drawPositionVisota;
}

/////////////////������� �������� -- �������� ��������� ��������� ��������� ������������ ������ �������� ���� ������
public void moveUp(int delta){
	drawPositionVisota = drawPositionVisota-delta;
}

public void moveDown(int delta){
	drawPositionVisota = drawPositionVisota+delta;
}

public void moveLeft(int delta){
	drawPositionShirina = drawPositionShirina-delta;
}

public void moveRight(int delta){
	drawPositionShirina = drawPositionShirina+delta;
}


}