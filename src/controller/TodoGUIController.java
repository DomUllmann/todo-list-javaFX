/**
 * Sample Skeleton for 'todoGUI.fxml' Controller Class
 */

package controller;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.stream.Collectors;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.SplitPane;
import javafx.scene.control.SplitPane.Divider;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import model.AppData;
import model.TodoTask;

public class TodoGUIController 
{
    @FXML
    private SplitPane mainPane;
	@FXML 
	private TextField descriptionText; 
	@FXML 
	private DatePicker datePicker; 
	@FXML 
	private Button addTaskButton;
	@FXML 
	private Button completeButton; 
	@FXML 
	private Button deleteButton; 
	@FXML 
	private Label errorLabel;
	@FXML 
	private ListView<TodoTask> taskList;
	@FXML 
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
	void addNewTask(ActionEvent event) 
	{
		if(addTaskValidate())
		{
			addTaskCommit();
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
		descriptionText.setText("");
		errorLabel.setText("");

		toggleButtons(list.isEmpty() && listDone.isEmpty());
		datePicker.setDisable(false);
		noDueDateCheckbox.setSelected(false);
		noDueDateCheckbox.setDisable(false);
		urgentCheckbox.setSelected(false);
		datePicker.setValue(LocalDate.now());
	}

	@FXML
	void markAsComplete(ActionEvent event) 
	{
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
				if(task.getDueDate() != null && 
				   task.getDueDate().isBefore(LocalDate.now()))
				{
					printError("Cannot mark a task as incomplete if it's past the due date");
					return;
				}
				listDone.remove(task);
				list.add(task);
				task.setCompleted(false);
				task.setCompletionDate(null);
				sortListByDate(list);
				taskList.setItems(list);
			}else
			{
				list.remove(task);
				listDone.add(task);
				task.setCompleted(true);
				task.setCompletionDate(LocalDate.now());
				sortListByDate(listDone);
				taskListDone.setItems(listDone);
			}
		}
	}

	@FXML
	void deleteTask(ActionEvent event) 
	{
		list.remove(taskList.getSelectionModel().getSelectedItem());
		listDone.remove(taskListDone.getSelectionModel().getSelectedItem());
		taskList.refresh();
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
		
		completeButton.setDisable(listDone.isEmpty());
		deleteButton.setDisable(listDone.isEmpty());
		urgentCheckbox.setDisable(true);
		taskList.getSelectionModel().clearSelection();
	}

	@FXML
	void listClicked(MouseEvent event) 
	{
		if(!list.isEmpty())
		{
			completeButton.setText("Mark as complete");
		}
		
		completeButton.setDisable(list.isEmpty());
		deleteButton.setDisable(list.isEmpty());
		urgentCheckbox.setDisable(true);
		taskListDone.getSelectionModel().clearSelection();
	}

	@FXML
	void addTaskClicked(MouseEvent event) 
	{
		urgentCheckbox.setDisable(false);
		taskList.getSelectionModel().clearSelection();
		taskListDone.getSelectionModel().clearSelection();
		
		Scene scene = (Scene) mainPane.getScene();
		
		scene.setOnKeyPressed(e -> {
		    if (e.getCode() == KeyCode.ENTER) {
		    	enterPressed();
		    }
		});
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
	
	@FXML
	void urgentSelected(ActionEvent event) 
	{
		noDueDateCheckbox.setDisable(urgentCheckbox.isSelected());
		noDueDateCheckbox.setSelected(urgentCheckbox.isSelected());
		datePicker.setDisable(urgentCheckbox.isSelected());
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
		
		loadAppData();

		// Save data when application is closed
		Runtime.getRuntime().addShutdownHook(new Thread()
		{
		    @Override
		    public void run()
		    {
		    	saveAppData();
		    }
		});
		
		taskList.getSelectionModel().clearSelection();
		taskListDone.getSelectionModel().clearSelection();
	}
	
	
	/**
	 *  Helper methods
	 */
	public void saveAppData()
	{
        OutputStream ops = null;
        ObjectOutputStream objOps = null;
        try {
        	ArrayList<TodoTask> list1 = (ArrayList<TodoTask>) list.stream().collect(Collectors.toList());
        	ArrayList<TodoTask> list2 = (ArrayList<TodoTask>) listDone.stream().collect(Collectors.toList());

            AppData data = new AppData(list1, list2);
        	
            ops = new FileOutputStream("appData.txt");
            objOps = new ObjectOutputStream(ops);
            objOps.writeObject(data);
            objOps.flush();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally{
            try{
                if(objOps != null) objOps.close();
            } catch (Exception ex){
                 
            }
        }
    }
    
    public void loadAppData()
    {
        InputStream fileIs = null;
        ObjectInputStream objIs = null;
        try {
            fileIs = new FileInputStream("appData.txt");
            objIs = new ObjectInputStream(fileIs);
            AppData data = (AppData) objIs.readObject();
            list.setAll(data.getList());
            listDone.setAll(data.getListDone());
			taskList.setItems(list);
			taskListDone.setItems(listDone);
			
        } catch (FileNotFoundException e) {
            return;
        	//e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } finally {
            try {
                if(objIs != null) objIs.close();
            } catch (Exception e){
                e.printStackTrace();
            }
        }
    }


	public void sortListByDate(ObservableList<TodoTask> list)
	{
		Collections.sort(list, new Comparator<TodoTask>() {
			@Override
			public int compare(TodoTask t1, TodoTask t2) {

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

	private void toggleButtons(boolean listsEmpty)
	{
		completeButton.setDisable(listsEmpty);
		deleteButton.setDisable(listsEmpty);
	}

	private boolean isDuplicate()
	{
		for(int i = 0; i < list.size(); i++)
		{
			if(descriptionText.getText().equals(list.get(i).getDescription()))
			{
				if(datePicker.isDisabled())
				{
					if(list.get(i).getDueDate() == null)
					{
						return true;
					}
				}else
				{
					if(list.get(i).getDueDate() != null && 
					   list.get(i).getDueDate().isEqual(datePicker.getValue()))
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

	private void enterPressed()
	{
		if(descriptionText.isFocused())
		{
			addNewTask(null);
		}
	}
}
