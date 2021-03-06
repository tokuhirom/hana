/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package me.geso.hana;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import static org.junit.Assert.*;
import org.junit.Test;

/**
 *
 * @author Tokuhiro Matsuno <tokuhirom@gmail.com>
 */
public class ConditionTest extends TestBase {

	public ConditionTest() {
	}

	@Test
	public void testEq() {
		{
			System.out.println("eq");
			String column = "id";
			Object value = 5963;
			Criteria result = Condition.eq(column, value);
			assertEquals("id=?", result.getTerm(""));
			assertEquals(Arrays.asList(5963), result.getParams());
		}
		{
			System.out.println("eq");
			String column = "id";
			Object value = null;
			Criteria result = Condition.eq(column, value);
			assertEquals("id IS NULL", result.getTerm(""));
			assertEquals(Arrays.asList(), result.getParams());
		}
	}

	@Test
	public void testNe() {
		{
			String column = "id";
			Object value = 5963;
			Criteria result = Condition.ne(column, value);
			assertEquals("id!=?", result.getTerm(""));
			assertEquals(Arrays.asList(5963), result.getParams());
		}
		{
			String column = "id";
			Object value = null;
			Criteria result = Condition.ne(column, value);
			assertEquals("id IS NOT NULL", result.getTerm(""));
			assertEquals(Arrays.asList(), result.getParams());
		}
	}

	@Test
	public void testGt() {
		Criteria result = Condition.gt("id", 5963);
		assertEquals("id>?", result.getTerm(""));
		assertEquals(Arrays.asList(5963), result.getParams());
	}

	@Test
	public void testLt() {
		Criteria result = Condition.lt("id", 5963);
		assertEquals("id<?", result.getTerm(""));
		assertEquals(Arrays.asList(5963), result.getParams());
	}

	@Test
	public void testGe() {
		Criteria result = Condition.ge("id", 5963);
		assertEquals("id>=?", result.getTerm(""));
		assertEquals(Arrays.asList(5963), result.getParams());
	}

	@Test
	public void testLe() {
		Criteria result = Condition.le("id", 5963);
		assertEquals("id<=?", result.getTerm(""));
		assertEquals(Arrays.asList(5963), result.getParams());
	}

	@Test
	public void testAnd() {
		Criteria result = Condition.le("id", 5963).and(Condition.eq("id", 3));
		assertEquals("(( id<=? ) AND ( id=? ))", result.getTerm(""));
		assertEquals(Arrays.asList(5963, 3), result.getParams());
	}

	@Test
	public void testOr() {
		Criteria result = Condition.le("id", 5963).or(Condition.eq("id", 3));
		assertEquals("(( id<=? ) OR ( id=? ))", result.getTerm(""));
		assertEquals(Arrays.asList(5963, 3), result.getParams());
	}

	@Test
	public void testIn() {
		{
			System.out.println("eq");
			String column = "id";
			final List<Object> values = new ArrayList();
			values.add(5963);
			values.add(4649);
			Criteria result = Condition.in(column, values);
			assertEquals("id IN (?,?)", result.getTerm(""));
			assertEquals(values, result.getParams());
		}
		{
			System.out.println("eq");
			String column = "id";
			final List<Object> values = new ArrayList();
			Criteria result = Condition.in(column, values);
			assertEquals("1=0", result.getTerm(""));
			assertEquals(values, result.getParams());
		}
	}

	@Test
	public void testNotIn() {
		{
			System.out.println("eq");
			String column = "id";
			final List<Object> values = new ArrayList();
			values.add(5963);
			values.add(4649);
			Criteria result = Condition.not_in(column, values);
			assertEquals("id NOT IN (?,?)", result.getTerm(""));
			assertEquals(values, result.getParams());
		}
		{
			System.out.println("eq");
			String column = "id";
			final List<Object> values = new ArrayList();
			Criteria result = Condition.not_in(column, values);
			assertEquals("1=1", result.getTerm(""));
			assertEquals(values, result.getParams());
		}
	}

	@Test
	public void testLike() {
		String column = "id";
		Criteria result = Condition.like(column, "hoge");
		assertEquals("id LIKE ?", result.getTerm(""));
		assertEquals(Arrays.asList("hoge"), result.getParams());
	}

	@Test
	public void testGetTerm() {
		System.out.println("getTerm");
		Criteria instance = Condition.eq("id", 3);
		String result = instance.getTerm("");
		assertEquals("id=?", result);
	}

	@Test
	public void testGetParams() {
		Criteria instance = Condition.eq("id", 3);
		List<Object> result = instance.getParams();
		assertEquals(Arrays.asList(3), result);
	}

}
