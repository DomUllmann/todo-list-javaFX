package model;

import java.io.Serializable;
import java.time.LocalDate;


@SuppressWarnings("serial")
public class TodoTask implements Serializable 
{
	private LocalDate dueDate;
	private LocalDate completionDate;
	private String description;
	private boolean completed;
	private boolean urgent;
	
	public TodoTask(String description, LocalDate date, boolean urgent)
	{
		this.description = description;
		this.dueDate = date;
		this.completionDate = null;
		this.completed = false;
		this.urgent = urgent;
	}
	
	@Override
	public String toString()
	{
		String[] dateArray = null;
		String[] completionDateArray = null;
		if(dueDate != null)
		{
			dateArray = dueDate.toString().split("-");
		}
		if(completionDate != null)
		{
			completionDateArray = completionDate.toString().split("-");
		}
		
		if(urgent)
		{
			if(completed)
			{
				return completionDateArray[2] + "/" + completionDateArray[1] + "/" + completionDateArray[0] +
					   " | URGENT | " + description;
			}else
			{
				return "URGENT | " + description;
			}
		}else
		{
			if(completed)
			{
				return completionDateArray[2] + "/" + completionDateArray[1] + "/" + completionDateArray[0] + 
					   " | "+ description + ((dateArray == null)?"": "   (due " + 
				       dateArray[2] + "/" + dateArray[1] + "/" + dateArray[0] + ")");
			}else
			{
				return ((dateArray == null)?"": dateArray[2] + "/" + dateArray[1] + "/" + dateArray[0] + " | ") +
					   description;
			}
		}
	}
	
	public LocalDate getCompletionDate()
	{
		return completionDate;
	}

	public void setCompletionDate(LocalDate completionDate) 
	{
		this.completionDate = completionDate;
	}

	public boolean isUrgent() 
	{
		return urgent;
	}

	public void setUrgent(boolean urgent) 
	{
		this.urgent = urgent;
	}

	public LocalDate getDueDate() 
	{
		return dueDate;
	}
	
	public void setDueDate(LocalDate dueDate)
	{
		this.dueDate = dueDate;
	}
	
	public String getDescription()
	{
		return description;
	}
	
	public void setDescription(String description)
	{
		this.description = description;
	}
	
	public boolean isCompleted()
	{
		return completed;
	}
	
	public void setCompleted(boolean completed)
	{
		this.completed = completed;
	}
}
