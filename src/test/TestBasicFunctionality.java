package test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Random;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import model.TodoTask;

class TestBasicFunctionality
{
	private static TestHarness app;
	private static Random random;

	@BeforeAll
	static void setUpBeforeClass() throws Exception
	{
		app = new TestHarness();
		random = new Random();
	}

	@AfterAll
	static void tearDownAfterClass() throws Exception 
	{
		app = null;
	}

	@BeforeEach
	void setUp() throws Exception 
	{
		app.addNewTask(new TodoTask("Task1", LocalDate.now().plusDays(3), false));
		app.addNewTask(new TodoTask("Task2", LocalDate.now().plusDays(21), false));
		app.addNewTask(new TodoTask("Task3", LocalDate.now().plusDays(5), false));
		app.addNewTask(new TodoTask("Task4", LocalDate.now().plusWeeks(3), false));
		app.addNewTask(new TodoTask("Task5", LocalDate.now(), false));
		app.sortListByDate(app.getList());
	}

	@AfterEach
	void tearDown() throws Exception
	{
		app.reset();
	}

	@Test
	void testAddTasks()
	{
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
	void testRemoveTasks() 
	{
		System.out.println("testRemoveTasks start");

		assertEquals(5, app.getList().size());

		app.deleteTask(random.nextInt(app.getList().size()), false);
		app.deleteTask(random.nextInt(app.getList().size()), false);
		app.deleteTask(random.nextInt(app.getList().size()), false);
		app.deleteTask(random.nextInt(app.getList().size()), false);

		assertEquals(1, app.getList().size());

		System.out.println("testRemoveTasks end");
	}

	@Test
	void testMarkTasksAsCompleteAndIncomplete() 
	{
		System.out.println("testMarkTasksAsCompleteAndIncomplete start");

		assertEquals(5, app.getList().size());
		assertEquals(0, app.getListDone().size());

		app.markTaskAsComplete(random.nextInt(app.getList().size()), true);
		app.markTaskAsComplete(random.nextInt(app.getList().size()), true);
		app.markTaskAsComplete(random.nextInt(app.getList().size()), true);

		assertEquals(2, app.getList().size());
		assertEquals(3, app.getListDone().size());

		app.markTaskAsComplete(random.nextInt(app.getListDone().size()), false);
		app.markTaskAsComplete(random.nextInt(app.getListDone().size()), false);

		assertEquals(4, app.getList().size());
		assertEquals(1, app.getListDone().size());

		System.out.println("testMarkTasksAsCompleteAndIncomplete end");
	}


	@Test
	void testDeleteCompleteTasks() 
	{
		System.out.println("testDeleteCompleteTasks start");

		assertEquals(5, app.getList().size());
		assertEquals(0, app.getListDone().size());

		app.markTaskAsComplete(random.nextInt(app.getList().size()), true);
		app.markTaskAsComplete(random.nextInt(app.getList().size()), true);
		app.markTaskAsComplete(random.nextInt(app.getList().size()), true);

		assertEquals(2, app.getList().size());
		assertEquals(3, app.getListDone().size());

		app.deleteTask(random.nextInt(app.getListDone().size()), true);
		app.deleteTask(random.nextInt(app.getListDone().size()), true);

		assertEquals(2, app.getList().size());
		assertEquals(1, app.getListDone().size());

		System.out.println("testDeleteCompleteTasks end");
	}

	@Test
	void testAddNullTask()
	{
		System.out.println("testAddNullTask start");

		assertTrue(app.getErrorLabel().equals(""));
		assertEquals(5, app.getList().size());

		app.addNewTask(new TodoTask("", LocalDate.now(), false));

		assertTrue(app.getErrorLabel().equals("Cannot create an empty task"));
		assertEquals(5, app.getList().size());

		System.out.println("testAddNullTask end");
	}

	@Test
	void testAddTaskInPast()
	{
		System.out.println("testAddTaskInPast start");

		assertTrue(app.getErrorLabel().equals(""));
		assertEquals(5, app.getList().size());

		app.addNewTask(new TodoTask("Task6", LocalDate.now().minusDays(1), false));

		assertTrue(app.getErrorLabel().equals("Cannot create a task in the past"));
		assertEquals(5, app.getList().size());

		System.out.println("testAddTaskInPast end");
	}

	@Test
	void testAddDuplicateTask()
	{
		System.out.println("testAddDuplicateTask start");
		app.reset();

		assertTrue(app.getErrorLabel().equals(""));
		assertEquals(0, app.getList().size());

		app.addNewTask(new TodoTask("Task1", LocalDate.now(), false));

		assertEquals(1, app.getList().size());
		assertTrue(app.getErrorLabel().equals(""));

		app.addNewTask(new TodoTask("Task1", LocalDate.now(), false));

		assertEquals(1, app.getList().size());
		assertTrue(app.getErrorLabel().equals("Cannot create duplicate tasks"));

		app.addNewTask(new TodoTask("Task2", LocalDate.now(), false));

		assertEquals(2, app.getList().size());
		assertTrue(app.getErrorLabel().equals(""));

		System.out.println("testAddDuplicateTask end");
	}

	/**
	 * This verifies that it is not permitted to mark a complete task as incomplete, if it's due date is in the past
	 */
	@Test
	void testMarkingCompleteTaskFromPastAsIncomplete()
	{
		System.out.println("testMarkingCompleteTaskFromPastAsIncomplete start");

		// Create a complete task with due date in the past
		ArrayList<TodoTask> listDone = app.getListDone();
		listDone.add(new TodoTask("Task1", LocalDate.now().minusWeeks(1), false));
		app.setListDone(listDone);

		assertEquals(5, app.getList().size());
		assertEquals(1, app.getListDone().size());

		app.markTaskAsComplete(0, false);

		assertTrue(app.getErrorLabel().equals("Cannot mark a task as incomplete if it's past the due date"));
		assertEquals(5, app.getList().size());
		assertEquals(1, app.getListDone().size());

		System.out.println("testMarkingCompleteTaskFromPastAsIncomplete end");
	}


	@Test
	void testCheckSorting()
	{
		System.out.println("testCheckSorting start");

		for(int i = 0 ; i < app.getList().size() ; i++)
		{
			if(i == 0)
			{
				continue;
			}
			assertFalse(app.getList().get(i).getDueDate().isBefore(app.getList().get(i-1).getDueDate()));
		}

		System.out.println("testCheckSorting end");
	}
}