/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package me.geso.hana;

import java.sql.PreparedStatement;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author tokuhirom
 */
public class DeleteStatementTest extends TestBase {
	
	public DeleteStatementTest() {
	}
	
	@BeforeClass
	public static void setUpClass() {
	}
	
	@AfterClass
	public static void tearDownClass() {
	}
	
	@Before
	public void setUp() {
	}
	
	@After
	public void tearDown() {
	}

	/**
	 * Test of setComment method, of class DeleteStatement.
	 */
	@Test
	public void testSetComment() {
		System.out.println("setComment");
		String comment = "";
		DeleteStatement instance = null;
		instance.setComment(comment);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of createPreparedStatement method, of class DeleteStatement.
	 */
	@Test
	public void testCreatePreparedStatement() throws Exception {
		System.out.println("createPreparedStatement");
		DeleteStatement instance = null;
		PreparedStatement expResult = null;
		PreparedStatement result = instance.createPreparedStatement();
		assertEquals(expResult, result);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of where method, of class DeleteStatement.
	 */
	@Test
	public void testWhere() throws Exception {
		System.out.println("where");
		String column = "";
		String value = "";
		DeleteStatement instance = null;
		instance.where(column, value);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}
	
}
