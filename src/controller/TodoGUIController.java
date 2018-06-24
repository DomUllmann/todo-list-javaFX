/**
 * Sample Skeleton for 'todoGUI.fxml' Controller Class
 */

package controller;

import java.time.LocalDate;
import java.util.Collections;
import java.util.Comparator;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.SplitPane;
import javafx.scene.control.SplitPane.Divider;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import model.TodoTask;

public class TodoGUIController {

	@FXML // fx:id="descriptionText"
	private TextField descriptionText; 

	@FXML // fx:id="datePicker"
	private DatePicker datePicker; 

	@FXML // fx:id="addTaskButton"
	private Button addTaskButton;

	@FXML // fx:id="completeButton"
	private Button completeButton; // Value injected by FXMLLoader

	@FXML // fx:id="deleteButton"
	private Button deleteButton; // Value injected by FXMLLoader

	@FXML // fx:id="errorLabel"
	private Label errorLabel; // Value injected by FXMLLoader

	@FXML // fx:id="taskList"
	private ListView<TodoTask> taskList;

	@FXML // fx:id="taskListDone"
	private ListView<TodoTask> taskListDone;

	@FXML
	private CheckBox noDueDateCheckbox;

	@FXML
	private CheckBox urgentCheckbox;

	@FXML
	private SplitPane splitPane;

	ObservableList<TodoTask> list = FXCollections.observableArrayList();
	ObservableList<TodoTask> listDone = FXCollections.observableArrayList();

	@FXML
	void addNewTask(ActionEvent event) {
		if(addTaskValidate())
		{
			addTaskCommit();
		}
	}

	@FXML
	void markAsComplete(ActionEvent event) {

		TodoTask task = null;

		if(taskList.isFocused())
		{
			task = taskList.getSelectionModel().getSelectedItem();
		}else if(taskListDone.isFocused())
		{
			task = taskListDone.getSelectionModel().getSelectedItem();
		}

		if(task != null)
		{
			if(task.isCompleted())
			{
				listDone.remove(task);
				list.add(task);
				task.setCompleted(false);
				sortListByDate(list);
				taskList.setItems(list);
			}else
			{
				list.remove(task);
				listDone.add(task);
				task.setCompleted(true);
				sortListByDate(listDone);
				taskListDone.setItems(listDone);
			}
		}
	}

	@FXML
	void deleteTask(ActionEvent event) {
		list.remove(taskList.getSelectionModel().getSelectedItem());
		taskList.refresh();

		listDone.remove(taskListDone.getSelectionModel().getSelectedItem());
		taskListDone.refresh();
		toggleButtons(list.isEmpty() && listDone.isEmpty());
		taskList.getSelectionModel().clearSelection();
		taskListDone.getSelectionModel().clearSelection();
	}

	@FXML
	void doneListClicked(MouseEvent event) 
	{
		if(!listDone.isEmpty())
		{
			completeButton.setText("Mark as incomplete");
		}
		taskList.getSelectionModel().clearSelection();
		urgentCheckbox.setDisable(true);
	}

	@FXML
	void listClicked(MouseEvent event) 
	{
		if(!list.isEmpty())
		{
			completeButton.setText("Mark as complete");
		}
		taskListDone.getSelectionModel().clearSelection();
		urgentCheckbox.setDisable(true);
	}

	@FXML
	void addTaskClicked(MouseEvent event) 
	{
		urgentCheckbox.setDisable(false);
		taskList.getSelectionModel().clearSelection();
		taskListDone.getSelectionModel().clearSelection();
	}

	@FXML
	void noDueDateSelected(ActionEvent event) 
	{
		if(noDueDateCheckbox.isSelected())
		{
			datePicker.setDisable(true);
		}else
		{
			datePicker.setDisable(false);
		}
	}

	private boolean addTaskValidate()
	{
		// Check for invalid input
		if( descriptionText.getText().equals("") )
		{
			printError("Cannot create an empty task");
			return false;
		}else if( datePicker.getValue().isBefore(LocalDate.now()) )
		{
			printError("Cannot create a task in the past");
			return false;
		}

		// Check for duplicate tasks
		if(isDuplicate())
		{
			printError("Cannot create duplicate tasks");
			return false;
		}
		return true;
	}

	private void addTaskCommit()
	{

		list.add(new TodoTask(descriptionText.getText(), 
				(noDueDateCheckbox.isSelected() ? null : datePicker.getValue()), 
				urgentCheckbox.isSelected()));
		sortListByDate(list);
		taskList.setItems(list);

		char[] arr = descriptionText.getText().toCharArray();
		descriptionText.setText("");

		toggleButtons(list.isEmpty() && listDone.isEmpty());
		datePicker.setDisable(false);
		noDueDateCheckbox.setSelected(false);
		urgentCheckbox.setSelected(false);

		datePicker.setValue(LocalDate.now());
	}

	public void sortListByDate(ObservableList<TodoTask> list)
	{
		Collections.sort(list, new Comparator<TodoTask>() {
			@Override
			public int compare(TodoTask t1, TodoTask t2) {

				if( t1.getDateDue() == null )
				{
					return 1;
				}else if( t2.getDateDue() == null )
				{
					return -1;
				}

				if( t1.getDateDue().isAfter(t2.getDateDue()) || 
						t1.getDateDue().isEqual(t2.getDateDue()) )
				{
					return 1;
				}
				if( t1.getDateDue().isBefore(t2.getDateDue()) )
				{
					return -1;
				}
				return 0;
			}
		});
	}

	private void toggleButtons(boolean listsEmpty)
	{
		completeButton.setDisable(listsEmpty);
		deleteButton.setDisable(listsEmpty);
	}

	private boolean isDuplicate()
	{
		// Iterate over the arraylist to find if there are any duplicate tasks
		for(int i = 0; i < list.size(); i++)
		{
			if(descriptionText.getText().equals(list.get(i).getDescription()))
			{
				
				if(datePicker.isDisabled())
				{
					if(list.get(i).getDateDue() == null)
					{
						return true;
					}
				}else
				{
					if(list.get(i).getDateDue() != null && 
					   list.get(i).getDateDue().isEqual(datePicker.getValue()))
					{
						return true;
					}
				}
			}
		}
		return false;
	}

	private void printError(String text)
	{
		errorLabel.setText(text);
		errorLabel.setTextFill(Color.RED);
	}


	@FXML
	public void initialize()
	{
		datePicker.setValue(LocalDate.now());
		toggleButtons(list.isEmpty() && listDone.isEmpty());

		Divider divider = splitPane.getDividers().get(0);
		divider.positionProperty().addListener(new ChangeListener<Number>()      
		{             
			@Override 
			public void changed( ObservableValue<? extends Number> observable, Number oldvalue, Number newvalue )
			{
				divider.setPosition(0.5);
			}
		});        
	}
}
