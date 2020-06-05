package mapvisiblecontent;

import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.*;
import programdirection.ProgramParameters;


public abstract class VisibleContent{

//От этого класса два наследника BigMapVisibleContent и SmallMapVisibleContent
//Оба вызывают функцию очистки памяти при переключении режима, поэтому переменная currentScreenContent одна на всех!
protected static AbstractScreenContent currentScreenContent;
protected Displayable currentMapPainter;

protected abstract Fragment getSmallMapLeftUpFragment();

protected abstract Fragment getBigMapLeftUpFragment();



//возвращает содержимое экрана
public Displayable getContent(){
	return currentMapPainter;
}

//Очищает память от содержимого ScreenContent При переключении режимов, вытирает картинки содержащиеся в фрагментах и запускает сборщик мусора
protected void clearMemory(){
	if (currentScreenContent != null){
		Runtime rt = Runtime.getRuntime();
		System.out.println("svobodnoy pamiatyi = "+rt.freeMemory());
		currentScreenContent.killContent();
		currentScreenContent = null;
		System.out.println("svobodnoy pamiatyi = "+rt.freeMemory());
		rt.gc();
	}
}

}