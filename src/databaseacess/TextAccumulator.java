package databaseacess;

public class TextAccumulator{

private byte[] accumulator;
private int pointer;

//CR + LF
private final static byte CR = 13; //возврат каретки
private final static byte LF = 10; //подача строки

//Символ разделителя внутри записи
private final static byte RECORD_SPLIT = 38;

//Правые и левые границы кодов чисел
private final static byte DIGIT_0 = 48;
private final static byte DIGIT_9 = 57;
private final static byte DIGIT_MINUS = 45;

TextAccumulator(){
	//Создаем уккумулятор 
	accumulator = new byte[0];
	//Устанавливаем указатель в 0
	pointer = 0;
}

//Удаляет фрагмент аккумулятора от начала до указателя, сокращая его размер
//Устанавливает указатель в 0 - начало строки
private void deleteDataPartInPointer(){
byte[] tempAccumulator;
	//System.arraycopy(Откуда, начало откуда, куда, начало куда, колличество копируемых единиц информации)	
	//Создаем новый массив в который запишем результаты операций а его ссылку присвоим accumulator
	//длинна нового массива - длинна аккумулятора - позиция указателя
	tempAccumulator = new byte[accumulator.length-pointer];

	//Копируем в новый аккумулятор кусок старого от указателя и до конца
	System.arraycopy(accumulator, pointer, tempAccumulator, 0, tempAccumulator.length);
	
	//Обнуляем указатель
	pointer = 0;
	
	//Подставляем результат в ссылку
	accumulator = tempAccumulator;
	//System.out.println("deleteDataPartInPointer >>>>>>>  accumulat0r = "+ new String(accumulator));
}

//Дописывает данные из inDataPart в аккумулятор расширяя его
public void addDataPart(byte[] inDataPart){
byte[] tempAccumulator;

	//System.arraycopy(Откуда, начало откуда, куда, начало куда, колличество копируемых единиц информации)
	//Если длинна аккумулятора нулевая достаточно просто скоприровать ссылку
	if (accumulator.length == 0){
		accumulator = inDataPart;
		return;
	}
	

	//Создаем новый массив в который запишем результаты операций а его ссылку присвоим accumulator
	//длинна нового массива сумма длинн inDataPart и accumulator
	tempAccumulator = new byte[accumulator.length+inDataPart.length];

	//Копируем в новый аккумулятор содержимое старого
	System.arraycopy(accumulator, 0, tempAccumulator, 0, accumulator.length);

	//Копируем в новый аккумулятор содержимое входящего параметра
	System.arraycopy(inDataPart, 0, tempAccumulator, accumulator.length, inDataPart.length);
	
	//Подставляем результат в ссылку
	accumulator = tempAccumulator;
}


//Возвращает кусок строки от начала строки до указателя pointer
//B удаляет этот фрагмент из аккумулятора
public byte[] getDataPart(){
byte[] returnData; //Взвращаемая строка
	
	//System.arraycopy(Откуда, начало откуда, куда, начало куда, колличество копируемых единиц информации)
	
	//Если длинна равна 0 то обращение к массиву вызовет ошибку!	
	if (accumulator.length == 0) return new byte[0];
	
	
	//Сдвигаем указатель на следующий символ, потому-что текущий символ - разделитель
	//nextChar(); 
	pointer++;
	
	
	//Содаем возвращаемый массив размером в указатель
	returnData = new byte[pointer]; 
	
	//копируем в него данные из аккумулятора от начала до указателя
	System.arraycopy(accumulator, 0, returnData, 0, returnData.length);

	//Удаляем из уккумулятора кусок от начала до указателя
	deleteDataPartInPointer();
	
	return returnData;
}

//Находится ли указатель на последовательности симвлов && - разделитль в записи 
public boolean isRecordSeparator(){
	if (accumulator[pointer] == RECORD_SPLIT){
		//Если предыдущий символ не меньше чем 0 и если предыдущий символ равен &
		if ((pointer-1>=0) && (accumulator[pointer-1] == RECORD_SPLIT)){
			//System.out.println("isRecordSeparator() >>>>> РАЗДЕЛИТЕЛЬ БД!!!!");
			return true;
		}
	}
	//System.out.println("isRecordSeparator() >>>>> НЕ РАЗДЕЛИТЕЛЬ БД!!!!");
	return false;
}


//Находится ли указатель на символе - цифре?
public boolean isDigit(){
	//System.out.println("isDigit() >>>>>>>>>>>>> pointer = "+pointer);
	//System.out.println("isDigit() >>>>>>>>>>>>> accumulator.length = "+accumulator.length);
	//Если длинна равна 0 то обращение к массиву вызовет ошибку!	
		if (accumulator.length == 0) return false;
	//Если предыдущий символ не меньше чем 0 и если предыдущий символ цифра
		boolean digit = ((DIGIT_0 <= accumulator[pointer]) && (accumulator[pointer] <= DIGIT_9));
		boolean minus = (accumulator[pointer] == DIGIT_MINUS);
		if (digit || minus){
			//System.out.println("isDigit() >>>>> ЧИСЛО!!!!");
			return true;
		}
		//System.out.println("isDigit() >>>>> НЕ ЧИСЛО!!!!");
	return false;
}

//Является ли текущая позиция указателя концом строки
public boolean isEndString(){
	//если текущая позиция указателя равна LF - подача строки
	//System.out.println("isEndString>>>>>  accumulator.length = "+accumulator.length +" pointer = "+pointer);
	//System.out.println("isFileRead = "+ accumulator[pointer] );
	
	//Если длинна равна 0 то обращение к массиву вызовет ошибку!	
	if (accumulator.length == 0) return false;
	
	if (accumulator[pointer] == LF){
		//Если предыдущий символ не меньше чем 0 и если предыдущий символ равен возврату каретки CR
		if ((pointer-1>=0) && (accumulator[pointer-1] == CR)){
			//System.out.println("isEndString() >>>>> КОНЕЦ СТРОКИ!!!!");
			return true;
		}
	}	
	//System.out.println("isEndString() >>>>> НЕ КОНЕЦ СТРОКИ!!!!");
	return false;
}

//Возвращает вновь созданный объект из остатка
public TextAccumulator createTextAccumulator(){
	//Берём остаток данных
	byte[] data = getDataPart();
	
	
	//Создаём новый объект
	TextAccumulator newTextAccum = new TextAccumulator();
	
	//Добавляем к нему взятые данные
	newTextAccum.addDataPart(data);
	
	//возвращаем это
	return newTextAccum;
}

//сдвинуть pointer на 1 символ вперед и вернуть true если аккумулятор закончился вернуть false
public boolean nextChar(){
	//Если позиция указателя меньше чем размер аккумулятора сдвигаем укзатель вперёд и возвращаем true	
	if (pointer<accumulator.length-1){
		pointer++;
		//System.out.println("code = "+accumulator[pointer]+"nextChar() = "+(char)accumulator[pointer]);
		return true;
		
	}
	//System.out.println("nextChar() >>>>>>> больше чем размер");
	//Иначе возвращаем false	
	return false;
}

//вернуть символ на который указывает pointer
public byte getChar(){
	//Если длинна равна 0 то обращение к массиву вызовет ошибку!	
	if (accumulator.length == 0) return (byte)0;
	
	return accumulator[pointer];
}

}