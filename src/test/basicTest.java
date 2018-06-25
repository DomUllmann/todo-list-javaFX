package test;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import controller.Main;
import javafx.application.Platform;
import javafx.embed.swing.JFXPanel;
import javafx.stage.Stage;

class basicTest {

	@BeforeAll
	static void setUpBeforeClass() throws Exception {
	}

	@AfterAll
	static void tearDownAfterClass() throws Exception {
	}

	@BeforeEach
	void setUp() throws Exception {
	}

	@AfterEach
	void tearDown() throws Exception {
	}

	@Test
	void test() throws InterruptedException {
		
		/*
		System.out.println("started apppppp");
		String fxmlPath = "/gui/ToDoGUI.fxml";
		URL location = getClass().getResource(fxmlPath);
		FXMLLoader loader = new FXMLLoader(location);

		Scene scene = new Scene(loader.load());
		

		
		primaryStage.setTitle("To-Do List Application");
		primaryStage.setResizable(false);
		primaryStage.setScene(scene);
		primaryStage.getIcons().add(new Image("file:icon.jpg"));

		primaryStage.show();
	
		primaryStage.setResizable(false);
		
		
		Parent mainNode = FXMLLoader.load(Main.class.getResource("sample.fxml"));
		    stage.setScene(new Scene(mainNode));
		    stage.show();
		    stage.toFront();
		
		*/
		
		
		
		Thread thread = new Thread(new Runnable() {

			@Override
			public void run() {
				new JFXPanel(); // Initializes the JavaFx Platform
				Platform.runLater(new Runnable() {

					@Override
					public void run() {
						try {
							System.out.println("in test before main start");
							new Main().start(new Stage());
							System.out.println("in test after main start");
							

							
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} // Create and
						// initialize
						// your app.

					}
				});
			}
		});
		thread.start();// Initialize the thread
		Thread.sleep(10000); // Time to use the app, with out this, the thread
		// will be killed before you can tell.

		System.out.println("END");
		//		fail("Not yet implemented");
	}

}
