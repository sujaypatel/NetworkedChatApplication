package edu.uic.cs342.project5.models.test;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import edu.uic.cs342.project5.models.RummyModel;

public class RummyModelTest {

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void ConstructorTest() {
		RummyModel rummyModelTest2 = new RummyModel(2);
		RummyModel rummyModelTest4 = new RummyModel(4);
		RummyModel rummyModelTest6 = new RummyModel(6);
		
		assertEquals(rummyModelTest2.getPlayers().size(), 2);
		assertEquals(rummyModelTest2.getDeck().size(), 52-(10*2));
		
		assertEquals(rummyModelTest4.getPlayers().size(), 4);
		assertEquals(rummyModelTest4.getDeck().size(), 52-(7*4));
		
		assertEquals(rummyModelTest6.getPlayers().size(), 6);		
		assertEquals(rummyModelTest6.getDeck().size(), 52-(6*6));
		assertEquals(rummyModelTest6.getPlayers().get(5).getId(), 5);
		for (int i=0; i<6; i++)
			System.out.println(rummyModelTest6.getPlayers().get(i).getName() + ": " + rummyModelTest6.getPlayers().get(i));
		
	}
}
