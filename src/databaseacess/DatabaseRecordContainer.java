package databaseacess;

public class DatabaseRecordContainer{

private DatabaseRecord container[];
private int pointer;
private int endContainer;

DatabaseRecordContainer(){
	container = new DatabaseRecord[5];
	pointer = 0;
	endContainer = 0;
}

private void extendContainer(){
	//Новый контейнер больше предыдущего в 2 раза
	DatabaseRecord[] newContainer = new DatabaseRecord[container.length*2];
	
	//Копируем в новый контейнер содержимое старого контейнера
	//System.arraycopy(Откуда, начало откуда, куда, начало куда, колличество копируемых единиц информации)
	System.arraycopy(container, 0, newContainer, 0, container.length);
	
	//Заменяем ссылку
	container = newContainer;
}


public void addRecord(DatabaseRecord inDatabaseRecord){

	//System.out.println("Record Container add record = "+inDatabaseRecord.getAdressName());
	//Если следующий добавляемый элемент будет больше размера контейнера, его нужно рсширить
	if (endContainer == container.length) extendContainer();
	
	//Добавляем элемент
	container[endContainer] = inDatabaseRecord;
	
	//Увеличиваем правую границу
	endContainer++;
}

public int getSize(){
	return endContainer;
}

public void reset(){
	pointer = 0;
}

public boolean nextRecord(){
	pointer++;
	if (pointer>endContainer-1) {//-1 Потому-что endContainer всегда на 1 больше чем есть записей, указывает на следующий пустой элесент массива куда можно внести следующую запись
		pointer = endContainer-1;
		return false;
	}
	
	return true;
}

public DatabaseRecord getRecord(){
	//System.out.println("getRecord() pointer = "+pointer);
	//System.out.println("endContainer = "+endContainer);
	return container[pointer];
}

public DatabaseRecord getRecord(int position){
	return container[position];
}

}