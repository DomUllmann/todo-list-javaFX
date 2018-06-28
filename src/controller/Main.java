package controller;

import java.net.URL;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class Main extends Application 
{
	@Override
	public void start(Stage primaryStage) throws Exception 
	{
		String fxmlPath = "/gui/ToDoGUI.fxml";
		URL location = getClass().getResource(fxmlPath);
		FXMLLoader loader = new FXMLLoader(location);
		Scene scene = new Scene(loader.load());
		
		primaryStage.setTitle("To-Do List Application");
		primaryStage.setResizable(false);
		primaryStage.setScene(scene);
		primaryStage.getIcons().add(new Image("file:icon.jpg"));
		primaryStage.show();
	}

	public static void main(String[] args) 
	{
		launch(args);
	}
}
