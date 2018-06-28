package test;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import model.TodoTask;

public class TestHarness
{
	private ArrayList<TodoTask> list;
	private ArrayList<TodoTask> listDone;
	private String errorLabel;
	private boolean noDueDateSelected;
	private boolean urgentSelected;
	
	public TestHarness()
	{
		reset();
	}
	
	public void reset()
	{
		list = new ArrayList<TodoTask>();
		listDone = new ArrayList<TodoTask>();
		errorLabel = "";
		noDueDateSelected = false;
		urgentSelected = false;		
	}
	
	public void addNewTask(TodoTask task)
	{
		if(addTaskValidate(task))
		{
			addTaskCommit(task);
		}
	}
	
	private boolean addTaskValidate(TodoTask task)
	{
		// Check for invalid input
		if( task.getDescription().equals("") )
		{
			errorLabel = "Cannot create an empty task";
			return false;
		}else if( task.getDueDate().isBefore(LocalDate.now()) )
		{
			errorLabel = "Cannot create a task in the past";
			return false;
		}

		// Check for duplicate tasks
		if(isDuplicate(task))
		{
			errorLabel = "Cannot create duplicate tasks";
			return false;
		}
		return true;
	}

	private void addTaskCommit(TodoTask task)
	{
		list.add(task);
		sortListByDate(list);

		noDueDateSelected = false;
		urgentSelected = false;
		errorLabel = "";
	}
	
	private boolean isDuplicate(TodoTask task)
	{
		for(int i = 0; i < list.size(); i++)
		{
			if(task.getDescription().equals(list.get(i).getDescription()))
			{
				if(noDueDateSelected)
				{
					if(list.get(i).getDueDate() == null)
					{
						return true;
					}
				}else
				{
					if(list.get(i).getDueDate() != null && 
					   list.get(i).getDueDate().isEqual(task.getDueDate()))
					{
						return true;
					}
				}
			}
		}
		return false;
	}
	
	public void sortListByDate(ArrayList<TodoTask> list)
	{
		Collections.sort(list, new Comparator<TodoTask>()
		{
			@Override
			public int compare(TodoTask t1, TodoTask t2) 
			{
				if(t1.isUrgent())
				{
					return -1;
				}else if(t2.isUrgent())
				{
					return 1;
				}
				
				if( t1.getDueDate() == null )
				{
					return 1;
				}else if( t2.getDueDate() == null )
				{
					return -1;
				}

				if( t1.getDueDate().isAfter(t2.getDueDate()) || 
						t1.getDueDate().isEqual(t2.getDueDate()) )
				{
					return 1;
				}
				if( t1.getDueDate().isBefore(t2.getDueDate()) )
				{
					return -1;
				}
				return 0;
			}
		});
	}
	
	
	public void deleteTask(int position, boolean deleteCompletedTask)
	{
		if(deleteCompletedTask)
		{
			if(listDone.size() == 0)
			{
				return;
			}

			listDone.remove(position);
		}else
		{
			if(list.size() == 0)
			{
				return;
			}
			list.remove(position);
		}
	}
	
	public void markTaskAsComplete(int position, boolean markAsComplete)
	{
		TodoTask task = null;
		if(markAsComplete)
		{
			if(list.size() == 0)
			{
				return;
			}
			task = list.get(position);
			task.setCompleted(true);
			task.setCompletionDate(LocalDate.now());
			listDone.add(task);
			list.remove(position);
			sortListByDate(listDone);
		}else
		{
			if(listDone.size() == 0)
			{
				return;
			}
			task = listDone.get(position);
			
			if(task.getDueDate() != null && 
			   task.getDueDate().isBefore(LocalDate.now()))
			{
				errorLabel = "Cannot mark a task as incomplete if it's past the due date";
				return;
			}
			task.setCompleted(false);
			task.setCompletionDate(null);
			listDone.remove(task);
			list.add(task);
			sortListByDate(list);
		}
	}

	public ArrayList<TodoTask> getList() {
		return list;
	}

	public void setList(ArrayList<TodoTask> list) {
		this.list = list;
	}

	public ArrayList<TodoTask> getListDone() {
		return listDone;
	}

	public void setListDone(ArrayList<TodoTask> listDone) {
		this.listDone = listDone;
	}

	public String getErrorLabel() {
		return errorLabel;
	}

	public void setErrorLabel(String errorLabel) {
		this.errorLabel = errorLabel;
	}

	public boolean isNoDueDateSelected() {
		return noDueDateSelected;
	}

	public void setNoDueDateSelected(boolean noDueDateSelected) {
		this.noDueDateSelected = noDueDateSelected;
	}

	public boolean isUrgentSelected() {
		return urgentSelected;
	}

	public void setUrgentSelected(boolean urgentSelected) {
		this.urgentSelected = urgentSelected;
		noDueDateSelected = urgentSelected;
	}
	
}
