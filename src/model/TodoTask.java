package model;

import java.time.LocalDate;
import java.util.Date;

public class TodoTask {
	private LocalDate dateDue;
	private String description;
	private boolean completed;
	
	
	public TodoTask(String description, LocalDate date)
	{
		this.description = description;
		this.dateDue = date;
		this.completed = false;
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
		String[] dateArray = dateDue.toString().split("-");
		return description + ((dateDue == null)?"":" , before " + dateArray[2] + "/" + dateArray[1] + "/" + dateArray[0]);// + " , " + (completed ? "completed" : "not completed");
	}
	
	
}
