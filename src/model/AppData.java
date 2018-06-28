package model;

import java.io.Serializable;
import java.util.ArrayList;

@SuppressWarnings("serial")
public class AppData implements Serializable
{
	private ArrayList<TodoTask> list;
	private ArrayList<TodoTask> listDone;
	
	public AppData(ArrayList<TodoTask> list, ArrayList<TodoTask> listDone)
	{
		this.list = list;
		this.listDone = listDone;
	}

	public ArrayList<TodoTask> getList() 
	{
		return list;
	}

	public ArrayList<TodoTask> getListDone() 
	{
		return listDone;
	}
}
