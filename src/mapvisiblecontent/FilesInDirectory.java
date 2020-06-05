package mapvisiblecontent;


final class FilesInDirectory{
private static FilesInDirectory filesInDirectoryInstance = new FilesInDirectory();

private FilesInDirectory(){
}

private final static int diapazonDirectory = 20;

private int getZonePrefixX(int xIndex){
	int xDiapazon = xIndex/diapazonDirectory;
	if (xIndex<0) xDiapazon--;
	return xDiapazon;
}

private int getZonePrefixY(int yIndex){
	int yDiapazon = yIndex/diapazonDirectory;
	if (yIndex<0) yDiapazon--;
	return yDiapazon;
}



//Функция возвращает строку имени директории в котором лежит файл с индексами в имени - переданныйми в параметре
public String getDirectroyPatch(int xIndex, int yIndex){
	int zoneX = getZonePrefixX(xIndex);
	int zoneY = getZonePrefixY(yIndex);
	return new String("x"+(zoneX+1)+"y"+(zoneY+1));
}

public static FilesInDirectory getFilesInDirectoryInstance(){
	return filesInDirectoryInstance;
}

}