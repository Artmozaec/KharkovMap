package databaseacess;

public class TextAccumulator{

private byte[] accumulator;
private int pointer;

//CR + LF
private final static byte CR = 13; //������� �������
private final static byte LF = 10; //������ ������

//������ ����������� ������ ������
private final static byte RECORD_SPLIT = 38;

//������ � ����� ������� ����� �����
private final static byte DIGIT_0 = 48;
private final static byte DIGIT_9 = 57;
private final static byte DIGIT_MINUS = 45;

TextAccumulator(){
	//������� ����������� 
	accumulator = new byte[0];
	//������������� ��������� � 0
	pointer = 0;
}

//������� �������� ������������ �� ������ �� ���������, �������� ��� ������
//������������� ��������� � 0 - ������ ������
private void deleteDataPartInPointer(){
byte[] tempAccumulator;
	//System.arraycopy(������, ������ ������, ����, ������ ����, ����������� ���������� ������ ����������)	
	//������� ����� ������ � ������� ������� ���������� �������� � ��� ������ �������� accumulator
	//������ ������ ������� - ������ ������������ - ������� ���������
	tempAccumulator = new byte[accumulator.length-pointer];

	//�������� � ����� ����������� ����� ������� �� ��������� � �� �����
	System.arraycopy(accumulator, pointer, tempAccumulator, 0, tempAccumulator.length);
	
	//�������� ���������
	pointer = 0;
	
	//����������� ��������� � ������
	accumulator = tempAccumulator;
	//System.out.println("deleteDataPartInPointer >>>>>>>  accumulat0r = "+ new String(accumulator));
}

//���������� ������ �� inDataPart � ����������� �������� ���
public void addDataPart(byte[] inDataPart){
byte[] tempAccumulator;

	//System.arraycopy(������, ������ ������, ����, ������ ����, ����������� ���������� ������ ����������)
	//���� ������ ������������ ������� ���������� ������ ������������ ������
	if (accumulator.length == 0){
		accumulator = inDataPart;
		return;
	}
	

	//������� ����� ������ � ������� ������� ���������� �������� � ��� ������ �������� accumulator
	//������ ������ ������� ����� ����� inDataPart � accumulator
	tempAccumulator = new byte[accumulator.length+inDataPart.length];

	//�������� � ����� ����������� ���������� �������
	System.arraycopy(accumulator, 0, tempAccumulator, 0, accumulator.length);

	//�������� � ����� ����������� ���������� ��������� ���������
	System.arraycopy(inDataPart, 0, tempAccumulator, accumulator.length, inDataPart.length);
	
	//����������� ��������� � ������
	accumulator = tempAccumulator;
}


//���������� ����� ������ �� ������ ������ �� ��������� pointer
//B ������� ���� �������� �� ������������
public byte[] getDataPart(){
byte[] returnData; //����������� ������
	
	//System.arraycopy(������, ������ ������, ����, ������ ����, ����������� ���������� ������ ����������)
	
	//���� ������ ����� 0 �� ��������� � ������� ������� ������!	
	if (accumulator.length == 0) return new byte[0];
	
	
	//�������� ��������� �� ��������� ������, ������-��� ������� ������ - �����������
	//nextChar(); 
	pointer++;
	
	
	//������ ������������ ������ �������� � ���������
	returnData = new byte[pointer]; 
	
	//�������� � ���� ������ �� ������������ �� ������ �� ���������
	System.arraycopy(accumulator, 0, returnData, 0, returnData.length);

	//������� �� ������������ ����� �� ������ �� ���������
	deleteDataPartInPointer();
	
	return returnData;
}

//��������� �� ��������� �� ������������������ ������� && - ���������� � ������ 
public boolean isRecordSeparator(){
	if (accumulator[pointer] == RECORD_SPLIT){
		//���� ���������� ������ �� ������ ��� 0 � ���� ���������� ������ ����� &
		if ((pointer-1>=0) && (accumulator[pointer-1] == RECORD_SPLIT)){
			//System.out.println("isRecordSeparator() >>>>> ����������� ��!!!!");
			return true;
		}
	}
	//System.out.println("isRecordSeparator() >>>>> �� ����������� ��!!!!");
	return false;
}


//��������� �� ��������� �� ������� - �����?
public boolean isDigit(){
	//System.out.println("isDigit() >>>>>>>>>>>>> pointer = "+pointer);
	//System.out.println("isDigit() >>>>>>>>>>>>> accumulator.length = "+accumulator.length);
	//���� ������ ����� 0 �� ��������� � ������� ������� ������!	
		if (accumulator.length == 0) return false;
	//���� ���������� ������ �� ������ ��� 0 � ���� ���������� ������ �����
		boolean digit = ((DIGIT_0 <= accumulator[pointer]) && (accumulator[pointer] <= DIGIT_9));
		boolean minus = (accumulator[pointer] == DIGIT_MINUS);
		if (digit || minus){
			//System.out.println("isDigit() >>>>> �����!!!!");
			return true;
		}
		//System.out.println("isDigit() >>>>> �� �����!!!!");
	return false;
}

//�������� �� ������� ������� ��������� ������ ������
public boolean isEndString(){
	//���� ������� ������� ��������� ����� LF - ������ ������
	//System.out.println("isEndString>>>>>  accumulator.length = "+accumulator.length +" pointer = "+pointer);
	//System.out.println("isFileRead = "+ accumulator[pointer] );
	
	//���� ������ ����� 0 �� ��������� � ������� ������� ������!	
	if (accumulator.length == 0) return false;
	
	if (accumulator[pointer] == LF){
		//���� ���������� ������ �� ������ ��� 0 � ���� ���������� ������ ����� �������� ������� CR
		if ((pointer-1>=0) && (accumulator[pointer-1] == CR)){
			//System.out.println("isEndString() >>>>> ����� ������!!!!");
			return true;
		}
	}	
	//System.out.println("isEndString() >>>>> �� ����� ������!!!!");
	return false;
}

//���������� ����� ��������� ������ �� �������
public TextAccumulator createTextAccumulator(){
	//���� ������� ������
	byte[] data = getDataPart();
	
	
	//������ ����� ������
	TextAccumulator newTextAccum = new TextAccumulator();
	
	//��������� � ���� ������ ������
	newTextAccum.addDataPart(data);
	
	//���������� ���
	return newTextAccum;
}

//�������� pointer �� 1 ������ ������ � ������� true ���� ����������� ���������� ������� false
public boolean nextChar(){
	//���� ������� ��������� ������ ��� ������ ������������ �������� �������� ����� � ���������� true	
	if (pointer<accumulator.length-1){
		pointer++;
		//System.out.println("code = "+accumulator[pointer]+"nextChar() = "+(char)accumulator[pointer]);
		return true;
		
	}
	//System.out.println("nextChar() >>>>>>> ������ ��� ������");
	//����� ���������� false	
	return false;
}

//������� ������ �� ������� ��������� pointer
public byte getChar(){
	//���� ������ ����� 0 �� ��������� � ������� ������� ������!	
	if (accumulator.length == 0) return (byte)0;
	
	return accumulator[pointer];
}

}