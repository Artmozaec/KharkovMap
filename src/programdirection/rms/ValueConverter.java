package rms;

import userinterface.UserInterface;

public class ValueConverter{

public static byte[] intToByteArray(int value){
	byte[] result = new byte[4];

	result[0]= (byte)(value);
	result[1]= (byte)(value>>8);
	result[2]= (byte)(value>>16);
	result[3]= (byte)(value>>24);
	
	return result;
}

public static int byteArrayToInt(byte[] valueArray){
	int result;
	System.out.println("ValueConverter.byteArrayToInt() >>> valueArray.length = "+valueArray.length);
	if (valueArray.length !=4) {
		UserInterface.getUserInterface().showErrorMessage("ValueConverter.byteArrayToInt() Не верная длинна массива!!!");
		return 0;
	}
	result =   (((valueArray[0])     & (0XFF)) | 
				((valueArray[1] <<8) & (0XFF00)) | 
				((valueArray[2]<<16) & (0XFF0000)) | 
				((valueArray[3]<<24) & (0XFF000000)));
	
	return result;
}


}