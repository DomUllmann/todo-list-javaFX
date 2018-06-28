package test;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDate;
import java.util.Random;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import model.TodoTask;

class TestBasicFunctionality {
private static TestHarness app;
private static Random r;
	
	@BeforeAll
	static void setUpBeforeClass() throws Exception {
		app = new TestHarness();
		r = new Random();
	}

	@AfterAll
	static void tearDownAfterClass() throws Exception {
	}

	@BeforeEach
	void setUp() throws Exception {
		app.addNewTask(new TodoTask("Task1", LocalDate.now(), false));
		app.addNewTask(new TodoTask("Task2", LocalDate.now(), false));
		app.addNewTask(new TodoTask("Task3", LocalDate.now(), false));
		app.addNewTask(new TodoTask("Task4", LocalDate.now(), false));
		app.addNewTask(new TodoTask("Task5", LocalDate.now(), false));
	}

	@AfterEach
	void tearDown() throws Exception {
		app.reset();
	}

	@Test
	void testAddTasks() {
		System.out.println("testAddTasks start");
		app.reset();
		assertEquals(0, app.getList().size());
		app.addNewTask(new TodoTask("Task1", LocalDate.now(), false));
		app.addNewTask(new TodoTask("Task2", LocalDate.now(), false));
		app.addNewTask(new TodoTask("Task3", LocalDate.now(), false));
		app.addNewTask(new TodoTask("Task4", LocalDate.now(), false));
		app.addNewTask(new TodoTask("Task5", LocalDate.now(), false));
		assertEquals(5, app.getList().size());
		System.out.println("testAddTasks end");

	}
	
	@Test
	void testRemoveTasks() {

		System.out.println("testRemoveTasks start");

		assertEquals(5, app.getList().size());

		app.deleteTask(r.nextInt(app.getList().size()), false);
		app.deleteTask(r.nextInt(app.getList().size()), false);
		app.deleteTask(r.nextInt(app.getList().size()), false);
		app.deleteTask(r.nextInt(app.getList().size()), false);
		
		
		
		assertEquals(1, app.getList().size());
		
		System.out.println("testRemoveTasks end");

	}
	
	@Test
	void testMarkTasksAsCompleteAndIncomplete() {

		System.out.println("testMarkTasksAsCompleteAndIncomplete start");

		assertEquals(5, app.getList().size());
		assertEquals(0, app.getListDone().size());
		
		app.markTaskAsComplete(r.nextInt(app.getList().size()), true);
		app.markTaskAsComplete(r.nextInt(app.getList().size()), true);
		app.markTaskAsComplete(r.nextInt(app.getList().size()), true);

		assertEquals(2, app.getList().size());
		assertEquals(3, app.getListDone().size());
		
		app.markTaskAsComplete(r.nextInt(app.getListDone().size()), false);
		app.markTaskAsComplete(r.nextInt(app.getListDone().size()), false);

		assertEquals(4, app.getList().size());
		assertEquals(1, app.getListDone().size());
		
		
		System.out.println("testMarkTasksAsCompleteAndIncomplete end");

	}
	
	
	
	
	

}
