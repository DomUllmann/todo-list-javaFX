/**
 * Sample Skeleton for 'todoGUI.fxml' Controller Class
 */

package application;

import java.time.LocalDate;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
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

    @FXML // fx:id="errorLabel"
    private Label errorLabel; // Value injected by FXMLLoader
    
    @FXML // fx:id="taskList"
    private ListView<TodoTask> taskList;
    
    ObservableList<TodoTask> list = FXCollections.observableArrayList();

    @FXML
    void addNewTask(ActionEvent event) {
    	if(addTaskValidate())
    	{
    		addTaskCommit();
    	}
    }
    
    @FXML
    void markAsComplete(ActionEvent event) {

    }
    
    private boolean addTaskValidate()
    {
    	// Check for invalid input
    	if(descriptionText.getText().equals("") || datePicker.getValue().isBefore(LocalDate.now()))
    	{
    		return false;
    	}
    	
    	// Check for duplicate tasks
    	if(isDuplicate())
    	{
    		errorLabel.setText("Cannot add duplicate todos");
    		errorLabel.setTextFill(Color.RED);
    		return false;
    	}
    	return true;
    }
    
    private void addTaskCommit()
    {
    	System.out.println("DO STUFF");
    	list.add(new TodoTask(descriptionText.getText(), datePicker.getValue()));
    	taskList.setItems(list);
    }
    
    private boolean isDuplicate()
    {
    	// Iterate over the arraylist to find if there are any duplicate tasks
    	for(int i = 0; i < list.size(); i++)
    	{
    		if(descriptionText.getText().equals(list.get(i).getDescription()) && 
    		   datePicker.getValue().isEqual(list.get(i).getDateDue()))
    		{
    			return true;
    		}
    	}
    	return false;
    }

    @FXML
    public void initialize()
    {
    	datePicker.setValue(LocalDate.now());
    	descriptionText.setFocusTraversable(false);
    	datePicker.setFocusTraversable(false);
    	addTaskButton.setFocusTraversable(false);
    }
    
}
