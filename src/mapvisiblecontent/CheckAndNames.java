package mapvisiblecontent;

import javax.microedition.io.*;
import javax.microedition.io.file.*;
import java.io.*;
import programdirection.ProgramParameters;

//Здесь собраны методы-утилиты позволяющие проверять наличие одного фрагмента 
public class CheckAndNames{

//Коэффициент масщтаба карт малой и большой
private static int mapScale = 8;

//Вызывает функция проверки наличия всех четырех файлов фрагментов объекта ScreenContent
public static boolean AllFragmentExist(Fragment inFragment){
	
	//Достаем начальные индексы
	int indexHorizontal = inFragment.getNameIndexHorizontal();
	int indexVertical = inFragment.getNameIndexVertical();

	boolean leftUp = checkFileExist(indexHorizontal, indexVertical);
	boolean rightUp = checkFileExist(indexHorizontal+1, indexVertical);
	boolean leftDown = checkFileExist(indexHorizontal, indexVertical+1);
	boolean rightDown = checkFileExist(indexHorizontal+1, indexVertical+1);
	
	if ((leftUp) && (rightUp) && (leftDown) && (rightDown)){
		return true;
	} else return false;
}

//Возвращает true усли файл с индексами переданными в параметре, в текущем режиме присутствуют
public static boolean checkFileExist(int indexHorizontal, int indexVertical){
	//Получаем путь
	String filePatch = generateFilePatch(indexHorizontal, indexVertical);
	//А теперь сама провека
	return checkFileExist(filePatch);
}

//Возвращает true если по пути пререданному в параметре действительно есть файл
public static boolean checkFileExist(String filePatch){
	FileConnection fileConnect = null;
	boolean result;
	try {
		fileConnect = (FileConnection)Connector.open(filePatch);
		result = fileConnect.exists();
		fileConnect.close();
		System.out.println(">>>>>checkFileExist<<<<<< result >>>"+ filePatch +"  >>>"+ result);
	}catch(IOException ioe){
		System.out.println(">>>>>checkFileExist<<<<<<S FAILOM FIGNYA!!!");
		return false;
	}
	
	return result;
}


//Возвращает путь к файлу в зависимости от режима программы
public static String generateFilePatch(int horizontalIndex, int verticalIndex){
	StringBuffer resultStr = new StringBuffer();
	ProgramParameters programParameters = ProgramParameters.getProgramParameters();
	
	//Узнаем основной префикс - путь к папке где расположены подпапки в которых изображения
	resultStr.append(programParameters.getCurrentModePatch());
	
	//Уточняем в какой подпапке расположены индексы файла
	resultStr.append(FilesInDirectory.getFilesInDirectoryInstance().getDirectroyPatch(horizontalIndex, verticalIndex));
	
	//и добавляем слеш
	resultStr.append("/");
	
	
	//Добавляем имя файла
	resultStr.append("x"+horizontalIndex+"_y"+verticalIndex);
	
	
	//Добавляем расширение - gif или jpg
	resultStr.append(programParameters.getCurrentModeFileType());
	
	return resultStr.toString();
}





public static Fragment smallToBigFragment(Fragment inFragment, Fragment cursor){
	ProgramParameters programParameters = ProgramParameters.getProgramParameters();
	
	//Получаем размер стороны изображения - фрагмента
	int fragmentSize = programParameters.getImageSize();
	
	
	//Половины от высоты и ширины размера экрана
	int halfScreenShirina = programParameters.getScreenShirina()/2;
	int halfScreenVisota = programParameters.getScreenVisota()/2;
	
	//находим текущую позицию курсора относительно всей  малой карты
	//для этого узнаем  индекс текущего левого верхнего фрагмента умножаем на размер фрагмента 
	//складываем с позицией отрисовки (по модулю) 
	//и складываем это все с позицией отрисовки курсора
	int realPixelPositionVisota = inFragment.getNameIndexVertical()*fragmentSize+Math.abs(inFragment.getDrawPositionVisota()) + cursor.getDrawPositionVisota();
	int realPixelPositionShirina = inFragment.getNameIndexHorizontal()*fragmentSize+Math.abs(inFragment.getDrawPositionShirina()) + cursor.getDrawPositionShirina();
	
	//результат умножаем на коэффициент соотношения карт и получаем размер на большой карте
	realPixelPositionVisota = realPixelPositionVisota * mapScale;
	realPixelPositionShirina = realPixelPositionShirina * mapScale;
	

	//Поделив общее колличество пикселей на размер одного фрагмента мы узнаем номер изображения-фрагмента
	int fileIndexHorizontal = realPixelPositionShirina/fragmentSize;
	int fileIndexVertical = realPixelPositionVisota/fragmentSize;

	int newPositionShirina =fileIndexHorizontal*fragmentSize - realPixelPositionShirina;
	int newPositionVisota = fileIndexVertical*fragmentSize - realPixelPositionVisota;
	
	
	//прибавляем к позициям и высоты и ширины фрагмента половину размера экрана по вертикали и горизонтали соответственно получив искомую точку на карте ровно по центру экрана
	
	newPositionShirina += halfScreenShirina;
	newPositionVisota += halfScreenVisota;
	
	//при этом если горизонталь или вертикаль становиться больше нуля то индекс файла уменьшается на единицу в нужном направлении а позиция откровки уменьшается на ширину фрагмента

	if (newPositionShirina>0){
		fileIndexHorizontal--;
		newPositionShirina -= fragmentSize;
	}
	
	if (newPositionVisota>0){
		fileIndexVertical--;
		newPositionVisota -= fragmentSize;
	}
	
	return new Fragment(fileIndexHorizontal,
						fileIndexVertical,
						newPositionShirina,
						newPositionVisota);

}

public static Fragment bigToSmallFragment(Fragment inFragment){
	ProgramParameters programParameters = ProgramParameters.getProgramParameters();
	
	//Получаем размер стороны изображения - фрагмента
	int fragmentSize = programParameters.getImageSize();
	
	//Половины от высоты и ширины размера экрана
	int halfScreenShirina = programParameters.getScreenShirina()/2;
	int halfScreenVisota = programParameters.getScreenVisota()/2;
	
	//определить в пикселях реальное положение левого верхнего угла относительно всей карты + размеры серединки экрана
	int realPixelPositionVisota = inFragment.getNameIndexVertical()*fragmentSize+Math.abs(inFragment.getDrawPositionVisota())+halfScreenVisota;
	int realPixelPositionShirina = inFragment.getNameIndexHorizontal()*fragmentSize+Math.abs(inFragment.getDrawPositionShirina())+halfScreenShirina;
	
	//поделить полученное значение на коэффициент mapScale и мы узнаем позиции в пикселях на уменьшенной карте
	realPixelPositionVisota = realPixelPositionVisota/mapScale;
	realPixelPositionShirina = realPixelPositionShirina/mapScale;
	
	//Поделив общее колличество пикселей на размер одного фрагмента мы узнаем номер изображения-фрагмента
	int fileIndexHorizontal = realPixelPositionShirina/fragmentSize;
	int fileIndexVertical = realPixelPositionVisota/fragmentSize;

	int newPositionShirina =fileIndexHorizontal*fragmentSize - realPixelPositionShirina;
	int newPositionVisota = fileIndexVertical*fragmentSize - realPixelPositionVisota;
	
	
	//прибавляем к позициям и высоты и ширины фрагмента половину размера экрана по вертикали и горизонтали соответственно получив искомую точку на карте ровно по центру экрана
	
	newPositionShirina += halfScreenShirina;
	newPositionVisota += halfScreenVisota;
	
	//при этом если горизонталь или вертикаль становиться больше нуля то индекс файла уменьшается на единицу в нужном направлении а позиция откровки уменьшается на ширину фрагмента

	if (newPositionShirina>0){
		fileIndexHorizontal--;
		newPositionShirina -= fragmentSize;
	}
	
	if (newPositionVisota>0){
		fileIndexVertical--;
		newPositionVisota -= fragmentSize;
	}
	
	return new Fragment(fileIndexHorizontal,
						fileIndexVertical,
						newPositionShirina,
						newPositionVisota);
}




}