package mapvisiblecontent;

import javax.microedition.lcdui.Image;
import programdirection.ProgramParameters;
import userinterface.UserInterface;

//���� ����� �������� �� ���������� ������ ����������� ����������, 
//� ������� �������� ���������� ����������� �� ������
class AbstractScreenContent{

private Fragment leftUpFragment;
private Fragment rightUpFragment;
private Fragment leftDownFragment;
private Fragment rightDownFragment;

//������� �����������
private int imageSize;

//������ �� ����� � ������� �������� ��� ��������� ���������
protected ProgramParameters programParameters;

//���������� �� ������� ��������� ��������� ��� ��������
private int deltaMovingContent;


//int inBeginFileIndexHorizontal, int inBeginFileIndexVertical - ��������� ������� ������� �����
//int inCoordinateUpLeftFragmentShirina, int inCoordinateUpLeftFragmentVisota - ��������� ������� �������� ������ ���������
AbstractScreenContent(Fragment inFragment){
	
	//������ �� ��������� ���������
	programParameters = ProgramParameters.getProgramParameters();
	
	//���� ������ ��������
	deltaMovingContent = programParameters.getDeltaMoving();
	
	//������ ���� ����� ������� �����������
	imageSize = programParameters.getImageSize();

	//��������� ����� ������� ���������
	inFragment.fillFragment();
	
	//��������� ����������� ������ screenFragment
	fillScreenFragments(inFragment);
	
	//alignContentToRightSideScreen();
	//(���-��� ���� ������ ����������� �� ����� ���������� ������ �� ������� ������� ��� �������� ����� ��������� ������)
	//���� ������� �� ��������� ����� �������� ��� � ��� � � �����
	//contentAlignDownRight();

}

//��������� ������� ������� � ����� � � ���� � � ������ ������ ������� ������� ��������������
protected void contentAlignDownRight(){
	//���� ���� ����� � �����
	if (isRightOverrun()) {
		//������� � �����
		alignContentToRightSideScreen();
	}
	
	//���� ���� ����� � ����
	if (isDownOverrun()) {
		//������� � ����
		alignContentToDownSideScreen();
	}
}


//////////////������ �������
public Fragment getLeftUpFragment(){
	return leftUpFragment;
}

public Fragment getRightUpFragment(){
	return rightUpFragment;
}

public Fragment getLeftDownFragment(){
	return leftDownFragment;
}

public Fragment getRightDownFragment(){
	return rightDownFragment;
}

//���� ��� ��������� ���� ����� true �����...
public boolean allFragmentsExist(){
	if ((leftUpFragment!=null) && (rightUpFragment!=null) && (leftDownFragment!=null) && (leftDownFragment!=null)) return true;
	return false;
}


////////////////////////////�������� �� ���� ������//////////////////////////////

//����������� ���������� �� ������ ����
private void alignContentToLeftSideScreen(){
	//�������� ������ ������ �� ������ � ����
	int gap = gapValueScreenLeft();
	
	//������� ��� ��������� � ���-�� �����������
	moveLeftFragments(gap);
}

//����������� ���������� �� ������� ����
private void alignContentToRightSideScreen(){
	//�������� ������ ������ �� ������ � �����
	int gap = gapValueScreenRight();
	
	//������� ��� ��������� � ���-�� �����������
	moveRightFragments(gap);
}

//����������� ���������� �� �������� ����
private void alignContentToUpSideScreen(){
	//�������� ������ ������ �� ������ � �����
	int gap = gapValueScreenUp();
	
	//������� ��� ��������� � ���-�� �����������
	moveUpFragments(gap);
}

//����������� ���������� �� ������� ����
private void alignContentToDownSideScreen(){
	//�������� ������ ������ �� ������ � ����
	int gap = gapValueScreenDown();
	
	//������� ��� ��������� � ���-�� �����������
	moveDownFragments(gap);
}
//////////////////////////////////////////////////////////////////////////



//��������� ����-�� � ���� ������ ����� ��� �������� �����
private boolean checkExistFileForMoveUp(){

	int newIndexLeftHorizontal, newIndexLeftVertical; //���������� ������� ������
	int newIndexRightHorizontal, newIndexRightVertical; //���������� ������� ������
	
	//���������� ����� ������� ������
	newIndexLeftVertical = leftDownFragment.getNextVerticalIndexFile();//�� 1 ���� ���� ������ �������
	newIndexLeftHorizontal = leftDownFragment.getNameIndexHorizontal();
	
	newIndexRightVertical = rightDownFragment.getNextVerticalIndexFile();//�� 1 ���� ���� ������ �������
	newIndexRightHorizontal = rightDownFragment.getNameIndexHorizontal();
	
	boolean left = CheckAndNames.checkFileExist(newIndexLeftHorizontal, newIndexLeftVertical);
	boolean right = CheckAndNames.checkFileExist(newIndexRightHorizontal, newIndexRightVertical);
	
	if (left && right) return true;
	else
	return false;
}

//��������� ���� �� � ����� ������ ����� ��� �������� ����
private boolean checkExistFileForMoveDown(){

	int newIndexLeftHorizontal, newIndexLeftVertical; //���������� ������� ������
	int newIndexRightHorizontal, newIndexRightVertical; //���������� ������� ������
	
	//��������� ������� ����� ������
	
	newIndexLeftVertical = leftUpFragment.getPervVerticalIndexFile();//�� 1 ���� ������ ��������
	newIndexLeftHorizontal = leftUpFragment.getNameIndexHorizontal();
	
	newIndexRightVertical = rightUpFragment.getPervVerticalIndexFile();
	newIndexRightHorizontal = rightUpFragment.getNameIndexHorizontal();
	
	boolean left = CheckAndNames.checkFileExist(newIndexLeftHorizontal, newIndexLeftVertical);
	boolean right = CheckAndNames.checkFileExist(newIndexRightHorizontal, newIndexRightVertical);
	
	if (left && right) return true;
	else
	return false;
}

//���� �� � ����� ����������� �����
private boolean checkExistFileForMoveLeft(){
	int newIndexDownHorizontal, newIndexDownVertical; //���������� ������� ������
	int newIndexUpHorizontal, newIndexUpVertical; //���������� ������� ������
	
	//���������� ����� ������� ������
	newIndexUpHorizontal = rightUpFragment.getNextHorizontalIndexFile();
	newIndexUpVertical = rightUpFragment.getNameIndexVertical();
	
	newIndexDownHorizontal = rightDownFragment.getNextHorizontalIndexFile();
	newIndexDownVertical = rightDownFragment.getNameIndexVertical();

	boolean up = CheckAndNames.checkFileExist(newIndexUpHorizontal, newIndexUpVertical);
	boolean down = CheckAndNames.checkFileExist(newIndexDownHorizontal, newIndexDownVertical);
	
	if (up && down) return true;
	else
	return false;
}

//���� �� � ���� ����������� �����
private boolean checkExistFileForMoveRight(){
	int newIndexDownHorizontal, newIndexDownVertical; //���������� ������� ������
	int newIndexUpHorizontal, newIndexUpVertical; //���������� ������� ������
	
	//���������� ����� ������� ������
	newIndexUpHorizontal = leftUpFragment.getPervHorizontalIndexFile();
	newIndexUpVertical = leftUpFragment.getNameIndexVertical();
	
	newIndexDownHorizontal = leftDownFragment.getPervHorizontalIndexFile();
	newIndexDownVertical = leftDownFragment.getNameIndexVertical();

	boolean up = CheckAndNames.checkFileExist(newIndexUpHorizontal, newIndexUpVertical);
	boolean down = CheckAndNames.checkFileExist(newIndexDownHorizontal, newIndexDownVertical);
	
	if (up && down) return true;
	else
	return false;	
}

//��������� ��� ��������� �����(�� ���� �������� �� �� ������ ���������)
//���� ��������� ����� �� ������� ������ - �������� ���������� Screen Content ����� �������������� �����
public void moveUp(){
	moveUpFragments(deltaMovingContent);
		
	//���������� ������� �� ����� ��������� �������� �����������
	if (!screenIsFill()) {
		//���� �� �������� ������� �������� ���������� � ����
		//��������� ����-�� ����������� ����� ��� ��������
		if (checkExistFileForMoveUp()) {
			//���������
			rebuildFragmentsUp();
			//���� �� ����� ������� ���������� �� ������, ������ ������ ������ ������ ��� ������ ��������
			
			if (!screenIsFill()) {
				//��������� ���� ����������, �������� ������� � �������� ����
				alignContentToUpSideScreen();
			}
			
		} else {//������ ������ ���, ����� �����!
			//��������������� ������
			moveDownFragments(deltaMovingContent);
			UserInterface.getUserInterface().showInfoMessage("������ ���� �����");
		}
	}
}

//�������� ��� ��������� ����(�� ���� ���������� � �� ������� ���������)
//���� ��������� ����� �� ������� ������ - �������� ���������� Screen Content ����� �������������� �����
public void moveDown(){
	moveDownFragments(deltaMovingContent);
	
	//���������� ������� �� ����� ��������� �������� �����������
	if (!screenIsFill()) {
		//���� �� �������� ������� �������� ���������� � ���
		//��������� ����-�� ����������� ����� ��� ��������
		if (checkExistFileForMoveDown()) {
			//���������
			rebuildFragmentsDown();
			
			//���� �� ����� ������� ���������� �� ������, ������ ������ ������ ������ ��� ������ ��������
			if (!screenIsFill()) {
				//��������� ���� ����������, �������� ������� � ������� ����
				alignContentToDownSideScreen();
			}
			
		} else { //������ ������ ���, ����� �����!
			//��������������� ������
			moveUpFragments(deltaMovingContent);
			UserInterface.getUserInterface().showInfoMessage("������� ���� �����");
		}
	}
}

public void moveLeft(){
	moveLeftFragments(deltaMovingContent);

	//���������� ������� �� ����� ��������� �������� �����������
	if (!screenIsFill()) {
		//���� �� �������� ������� �������� ���������� � ����
		//��������� ����-�� ����������� ����� ��� ��������
		if (checkExistFileForMoveLeft()) {
			//���������
			rebuildFragmentsLeft();
			
			//���� �� ����� ������� ���������� �� ������, ������ ������ ������ ������ ��� ������ ��������
			if (!screenIsFill()) {
				//��������� ���� ����������, �������� ������� � ������ ����
				alignContentToLeftSideScreen();
			}
		} else {//������ ������ ���, ����� �����!
			//��������������� ������
			moveRightFragments(deltaMovingContent);
			UserInterface.getUserInterface().showInfoMessage("������ ���� �����");
		}
	}
}

public void moveRight(){
	moveRightFragments(deltaMovingContent);

	//���������� ������� �� ����� ��������� �������� �����������
	if (!screenIsFill()) {
		//���� �� �������� ������� �������� ���������� �����
		//��������� ����-�� ����������� ����� ��� ��������
		if (checkExistFileForMoveRight()) {
			//���������
			rebuildFragmentsRight();
			
			//���� �� ����� ������� ���������� �� ������, ������ ������ ������ ������ ��� ������ ��������
			if (!screenIsFill()) {
				//��������� ���� ����������, �������� ������� � ������� ����
				alignContentToRightSideScreen();
			}
			
		} else {//������ ������ ���, ����� �����!
			//��������������� ������
			moveLeftFragments(deltaMovingContent);
			UserInterface.getUserInterface().showInfoMessage("����� ���� ����� ����� ����� ����� ��� � ���! ����� � ������� �����, ����� � ���� � �����-�-��");
		}
	}
}

//�������� ������� ������� ����������� �� ���� ���������
public void killContent(){
	leftUpFragment.killContent();
	leftDownFragment.killContent();
	rightUpFragment.killContent();
	rightDownFragment.killContent();
	//leftUpFragment = null; - ���� ��� ����� ���� ������, ���� ���� ������� ������ �� ���� � ��� ��������
	leftDownFragment = null;
	rightUpFragment = null;
	rightDownFragment = null;
}

//������ ������ Fragment, �������� ������� ���������� ��� ���������, ���������� �� ���� ������
private Fragment createFragment(int inFileIndexHorizontal, 
								  int inFileIndexVertical, 
								  int inCoordinateFragmentShirina, 
								  int inCoordinateFragmentVisota){
	Fragment fragmentInstance;
		
	//���������� �������� ���������
	fragmentInstance = new Fragment(inFileIndexHorizontal, 
									inFileIndexVertical, 
									inCoordinateFragmentShirina, 
									inCoordinateFragmentVisota);
		
	//��������� ��������� �������� ���������
	fragmentInstance.fillFragment();	
	
	return fragmentInstance;
}


//���������� ������� ����������� ����� ������� �������� ����� ������
//���������� � ���������, �������� ������� - ����� ������� ����, �� ���� �������� ������������� ���������
//3 ��������� ������-�������, ����� ������, ������ ������
private void fillScreenFragments(Fragment inLeftUpFragment){
	//��������� �� ��������� ������ ��������
	int fileIndexHorizontal = inLeftUpFragment.getNameIndexHorizontal();
	int fileIndexVertical = inLeftUpFragment.getNameIndexVertical();
	int leftUpFragmentShirina = inLeftUpFragment.getDrawPositionShirina();
	int leftUpFragmentVisota = inLeftUpFragment.getDrawPositionVisota();
	
	//�����-������� ��� ������
	leftUpFragment = inLeftUpFragment;

	
	//������-�������
	//��������� �������� ��������� ������, � ����� �� ������ ���������
	int tempShirina;
	tempShirina = leftUpFragmentShirina+imageSize;
	rightUpFragment = createFragment(fileIndexHorizontal+1, fileIndexVertical, tempShirina, leftUpFragmentVisota);

	
	
	//�����-������
	//��������� �������� ��������� ����, �� ������ ���������
	int tempVisota;
	tempVisota = leftUpFragmentVisota+imageSize;
	leftDownFragment = createFragment(fileIndexHorizontal, fileIndexVertical+1, leftUpFragmentShirina, tempVisota);

	
	
	//������-������ 
	//��������� �������� � ������ � ����
	rightDownFragment = createFragment(fileIndexHorizontal+1, fileIndexVertical+1, tempShirina, tempVisota);
}




//������������� �������� ������� � �������� Fragment
//�������� �� �� ������ �������� deltaMoving
protected void moveUpFragments(int delta){
	leftUpFragment.moveUp(delta);
	rightUpFragment.moveUp(delta);
	leftDownFragment.moveUp(delta);
	rightDownFragment.moveUp(delta);
}

//������������� �������� ������� � �������� Fragment
//���������� � �� ������� �������� deltaMoving
protected void moveDownFragments(int delta){
	leftUpFragment.moveDown(delta);
	rightUpFragment.moveDown(delta);
	leftDownFragment.moveDown(delta);
	rightDownFragment.moveDown(delta);
}

//������������� �������� ������� � �������� Fragment
//�������� �� �� �������� � ����� deltaMoving
protected void moveLeftFragments(int delta){
	leftUpFragment.moveLeft(delta);
	rightUpFragment.moveLeft(delta);
	leftDownFragment.moveLeft(delta);
	rightDownFragment.moveLeft(delta);	
}

//������������� �������� ������� � �������� Fragment
//���������� � �� �������� � ����� deltaMoving
protected void moveRightFragments(int delta){
	leftUpFragment.moveRight(delta);
	rightUpFragment.moveRight(delta);
	leftDownFragment.moveRight(delta);
	rightDownFragment.moveRight(delta);	
}




//������� ��� ��������� ������� screenFragments �� 1 � ����
//����������� ��������� �������� �� �����
private void rebuildFragmentsUp(){
	System.out.println("REBUILD_frUP!!");
	int newIndexLeftHorizontal, newIndexLeftVertical; //���������� ������� ������
	int newIndexRightHorizontal, newIndexRightVertical; //���������� ������� ������
	
	int newPositionLeftShirina, newPositionLeftVisota; //���������� ������� ������
	int newPositionRightShirina, newPositionRightVisota;
	
	//���������� ����� ������� ������
	newIndexLeftVertical = leftDownFragment.getNextVerticalIndexFile();//�� 1 ���� ���� ������ �������
	newIndexLeftHorizontal = leftDownFragment.getNameIndexHorizontal();
	
	newIndexRightVertical = rightDownFragment.getNextVerticalIndexFile();//�� 1 ���� ���� ������ �������
	newIndexRightHorizontal = rightDownFragment.getNameIndexHorizontal();
	
	//���������� ������������ ����� ������
	newPositionLeftShirina = leftDownFragment.getDrawPositionShirina();
	//��� ��� ����� ������ �������� ������ ������ ����� ������� ��� ������� ��������� �� ����������� + ������ �����������
	//��� ��� ������ �������� ��������� ������ ������ ������� ���������
	newPositionLeftVisota = leftDownFragment.getDrawPositionVisota()+imageSize;
		
	newPositionRightShirina = rightDownFragment.getDrawPositionShirina(); 
	newPositionRightVisota = newPositionLeftVisota; //���� ��� ������������� �� ����� ������

	//��������� ������ ��������� ������� �� ����
	leftUpFragment = leftDownFragment;
	rightUpFragment = rightDownFragment;
	
	//��������� � ������ ��������� ����������� �����
	leftDownFragment = createFragment(newIndexLeftHorizontal, newIndexLeftVertical, newPositionLeftShirina, newPositionLeftVisota);
	rightDownFragment = createFragment(newIndexRightHorizontal, newIndexRightVertical, newPositionRightShirina, newPositionRightVisota);
}


//������� ��� ��������� ������� screenFragments �� 1 � ���
//����������� ��������� �������� �� �����
private void rebuildFragmentsDown(){
	System.out.println("MrebuildFragmentsDown!!!!");
	int newIndexLeftHorizontal, newIndexLeftVertical; //���������� ������� ������
	int newIndexRightHorizontal, newIndexRightVertical; //���������� ������� ������
	
	int newPositionLeftShirina, newPositionLeftVisota; //���������� ������� ������
	int newPositionRightShirina, newPositionRightVisota;
	
	//���������� ����� ������� ������
	newIndexLeftVertical = leftUpFragment.getPervVerticalIndexFile();//�� 1 ���� ������ ��������
	newIndexLeftHorizontal = leftUpFragment.getNameIndexHorizontal();
	
	newIndexRightVertical = rightUpFragment.getPervVerticalIndexFile();
	newIndexRightHorizontal = rightUpFragment.getNameIndexHorizontal();

	//���������� ������������ ����� ������
	newPositionLeftShirina = leftUpFragment.getDrawPositionShirina();
	newPositionLeftVisota = leftUpFragment.getDrawPositionVisota()-imageSize;
		
	newPositionRightShirina = rightDownFragment.getDrawPositionShirina(); 
	newPositionRightVisota = newPositionLeftVisota; //���� ��� ������������� �� ����� ������!
	
	//��������� ������� ��������� � ���
	leftDownFragment = leftUpFragment;
	rightDownFragment = rightUpFragment;
	
	//��������� � ������ ��������� ����������� �����
	leftUpFragment = createFragment(newIndexLeftHorizontal, newIndexLeftVertical, newPositionLeftShirina, newPositionLeftVisota);
	rightUpFragment = createFragment(newIndexRightHorizontal, newIndexRightVertical, newPositionRightShirina, newPositionRightVisota);
}

//������� ��� ��������� ������� screenFragments �� 1 � ����
//����������� ��������� �������� �� �����
private void rebuildFragmentsLeft(){
	int newIndexDownHorizontal, newIndexDownVertical; //���������� ������� ������
	int newIndexUpHorizontal, newIndexUpVertical; //���������� ������� ������
	
	int newPositionDownShirina, newPositionDownVisota; //���������� ������� ������
	int newPositionUpShirina, newPositionUpVisota;

	
	//���������� ����� ������� ������
	newIndexUpHorizontal = rightUpFragment.getNextHorizontalIndexFile();
	newIndexUpVertical = rightUpFragment.getNameIndexVertical();
	
	newIndexDownHorizontal = rightDownFragment.getNextHorizontalIndexFile();
	newIndexDownVertical = rightDownFragment.getNameIndexVertical();


	//���������� ������������ ����� ������
	//��� ��� ������ ������ ������� �������� ������ ����� ������� ��� �������������� �������+������ ��������
	//��� ��� �������� ������� �������� ������� �������� ���������
	newPositionUpShirina = rightUpFragment.getDrawPositionShirina()+imageSize;
	newPositionUpVisota = rightUpFragment.getDrawPositionVisota();
	
	newPositionDownVisota = rightDownFragment.getDrawPositionVisota();

	newPositionDownShirina = newPositionUpShirina; //��� ��������� �� ����� �������


	//��������� ������ ��������� � ����
	leftUpFragment = rightUpFragment;
	leftDownFragment = rightDownFragment;
	
	//��������� � ������ ��������� ����������� �����
	rightUpFragment = createFragment(newIndexUpHorizontal, newIndexUpVertical, newPositionUpShirina, newPositionUpVisota);
	rightDownFragment = createFragment(newIndexDownHorizontal, newIndexDownVertical, newPositionDownShirina, newPositionDownVisota);
}

//������� ��� ��������� ������� screenFragments �� 1 � �����
//����������� ��������� �������� �� �����
private void rebuildFragmentsRight(){
	int newIndexDownHorizontal, newIndexDownVertical; //���������� ������� ������
	int newIndexUpHorizontal, newIndexUpVertical; //���������� ������� ������
	
	int newPositionDownShirina, newPositionDownVisota; //���������� ������� ������
	int newPositionUpShirina, newPositionUpVisota;
	

	
	//���������� ����� ������� ������
	newIndexUpHorizontal = leftUpFragment.getPervHorizontalIndexFile();
	newIndexUpVertical = leftUpFragment.getNameIndexVertical();
	
	newIndexDownHorizontal = leftDownFragment.getPervHorizontalIndexFile();
	newIndexDownVertical = leftDownFragment.getNameIndexVertical();
	
	//���������� ������������ ����� ������
	
	newPositionUpVisota = leftUpFragment.getDrawPositionVisota();
	newPositionUpShirina = leftUpFragment.getDrawPositionShirina()-imageSize;
	
	newPositionDownVisota = leftDownFragment.getDrawPositionVisota();
	
	newPositionDownShirina = newPositionUpShirina; //��������� �� ����� ������
	
	
	//��������� ����� ��������� � �����
	rightUpFragment = leftUpFragment;
	rightDownFragment = leftDownFragment;
	
	//��������� � ������ ��������� ����������� �����
	leftUpFragment = createFragment(newIndexUpHorizontal, newIndexUpVertical, newPositionUpShirina, newPositionUpVisota);
	leftDownFragment = createFragment(newIndexDownHorizontal, newIndexDownVertical, newPositionDownShirina, newPositionDownVisota);
}



//�������� ������ �� ������ � �����
private int gapValueScreenRight(){
	int doubleSize = imageSize*2;
	
	//������� ������ ������� �������� ������������ ������
	int rightContentSide = leftUpFragment.getDrawPositionShirina()+doubleSize;
	
	int delta = programParameters.getScreenShirina()-rightContentSide;
	return delta;
}

//�������� ������ �� ������ � ����
private int gapValueScreenLeft(){
	return leftUpFragment.getDrawPositionShirina();
}

//�������� ������ �� ������ � �����
private int gapValueScreenUp(){
	return leftUpFragment.getDrawPositionVisota();
}

//�������� ������ �� ������ � �����
private int gapValueScreenDown(){
	int doubleSize = imageSize*2;
	
	//������� ������ ������� �������� ������������ ������
	int DownContentSide = leftUpFragment.getDrawPositionVisota()+doubleSize;
	
	int delta = programParameters.getScreenVisota()-DownContentSide;
	return delta;
}


//���������� - true, ���� �������� ��������� ������ ������ �������� ��������� ����� ������ 0, �� ������ �� ����������� ������� ������
private boolean isUpOverrun(){
	//������ ������ ����� ��������� � ������ ������
	int gap = gapValueScreenUp();
	System.out.println("isUpOverrun() - gap = "+gap);
	return (gap>0);
}

//���������� - true, ���� �������� ��������� ������ ������ �������� ��������� ����� ������ 0, �� ������ �� ����������� ������� �����
private boolean isLeftOverrun(){
	//������ ������ ����� ��������� � ����� �������� ������
	int gap = gapValueScreenLeft();
	System.out.println("isLeftOverrun() - gap = "+gap);
	return (gap>0);
}


//������ � ����
private boolean isDownOverrun(){
	//������ ������ ����� ��������� � ����� ������
	int gap = gapValueScreenDown();
	System.out.println("isDownOverrun() - gap = "+gap);
	return (gap>0);
}


//������ � �����
private boolean isRightOverrun(){
	//������ ������ ����� ��������� � ������ �������� ������
	int gap = gapValueScreenRight();
	System.out.println("isRightOverrun() - gap = "+gap);
	return (gap>0);
}

//���������� ��������� �� �������� ����� �������� ���������� �����������
private boolean screenIsFill(){
	if (isUpOverrun() || isDownOverrun() || isRightOverrun() || isLeftOverrun()){ 
		//System.out.println("screenIsThick >> vihod za granicy!!!");
		return false;
	}
	
	return true;
}


}