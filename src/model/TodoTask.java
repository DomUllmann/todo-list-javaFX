package model;

import java.time.LocalDate;
import java.util.Date;

public class TodoTask {
	private LocalDate dateDue;
	private String description;
	private boolean completed;
	private boolean urgent;
	
	
	public TodoTask(String description, LocalDate date, boolean urgent)
	{
		this.description = description;
		this.dateDue = date;
		this.completed = false;
		this.urgent = urgent;
	}
	
	public boolean isUrgent() {
		return urgent;
	}

	public void setUrgent(boolean urgent) {
		this.urgent = urgent;
	}

	public LocalDate getDateDue() {
		return dateDue;
	}
	public void setDateDue(LocalDate dateDue) {
		this.dateDue = dateDue;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public boolean isCompleted() {
		return completed;
	}
	public void setCompleted(boolean completed) {
		this.completed = completed;
	}
	
	@Override
	public String toString()
	{
		String[] dateArray = null;
		if(dateDue != null)
		{
			dateArray = dateDue.toString().split("-");
		}
		return ((dateArray == null)?"------------ ": dateArray[2] + "/" + dateArray[1] + "/" + dateArray[0]) + " | " + (urgent ? "!!! ":"") + description;// + " , " + (completed ? "completed" : "not completed");
		//return (urgent ? "!!! ":"")+ description + ((dateArray == null)?"":" , before " + dateArray[2] + "/" + dateArray[1] + "/" + dateArray[0]);// + " , " + (completed ? "completed" : "not completed");
	}
	
	
}
