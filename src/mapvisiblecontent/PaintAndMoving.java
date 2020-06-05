package mapvisiblecontent;


class PaintAndMoving{

//Ïîçèöèÿ îòğèñîâêè ıëåìåíòà íà ıêğàíå
protected int drawPositionShirina;
protected int drawPositionVisota;



PaintAndMoving(int inDrawPositionShirina, int inDrawPositionVisota){
	drawPositionShirina = inDrawPositionShirina;
	drawPositionVisota = inDrawPositionVisota;
}

/////////////////ÏÎËÓ×ÅÍÈÅ ÏÎÇÈÖÈÈ ÎÒĞÈÑÎÂÊÈ ÔĞÀÃÌÅÍÒÀ
public int getDrawPositionShirina(){
	return drawPositionShirina;
}

public int getDrawPositionVisota(){
	return drawPositionVisota;
}

/////////////////ÔÓÍÊÖÈÈ ÄÂÈÆÅÍÈß -- ÑÌÅÙÅÍÈÅ ÊÎÎĞÄÈÍÀÒ ÎÒĞÈÑÎÂÊÈ ÔĞÀÃÌÅÍÒÀ ÎÒÍÎÑÈÒÅËÜÍÎ ËÅÂÎÃÎ ÂÅĞÕÍÅÃÎ ÓÃËÀ İÊĞÀÍÀ
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