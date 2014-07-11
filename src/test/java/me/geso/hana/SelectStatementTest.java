/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package me.geso.hana;

import java.util.Optional;
import java.util.stream.Collectors;
import static me.geso.hana.Condition.eq;
import me.geso.hana.row.Member;
import static org.junit.Assert.*;
import org.junit.Test;

/**
 *
 * @author tokuhirom
 */
public class SelectStatementTest extends TestBase {

	/**
	 * Test of getResultList method, of class SelectStatement.
	 */
	@Test
	public void testGetResultList() throws Exception {
		{
			Member m1 = new Member().setEmail("hoge@example.com");
			Member m2 = new Member().setEmail("fuga@example.com");
			m1.insert(conn);
			m2.insert(conn);

			assertEquals("fuga@example.com,hoge@example.com",
					Select.from(Member.class)
					.orderBy("email ASC")
					.stream(conn)
					.map(row -> row.getEmail())
					.collect(Collectors.joining(",")));
		}
	}

	/**
	 * Test of stream method, of class SelectStatement.
	 *
	 * @throws java.lang.Exception
	 */
	@Test
	public void testStream() throws Exception {
		{
			Member m1 = new Member().setEmail("hoge@example.com");
			Member m2 = new Member().setEmail("fuga@example.com");
			m1.insert(conn);
			m2.insert(conn);

			assertEquals("fuga@example.com,hoge@example.com",
					Select.from(Member.class)
					.orderBy("email ASC")
					.stream(conn)
					.map(row -> row.getEmail())
					.collect(Collectors.joining(",")));
		}
	}

	/**
	 * Test of first method, of class SelectStatement.
	 *
	 * @throws java.lang.Exception
	 */
	@Test
	public void testFirst() throws Exception {
		{
			Member m1 = new Member().setEmail("hoge@example.com");
			Member m2 = new Member().setEmail("fuga@example.com");
			m1.insert(conn);
			m2.insert(conn);

			Optional<Member> got = Select.from(Member.class)
					.where(eq("email", "hoge@example.com"))
					.stream(conn)
					.findFirst();
			assertTrue(got.isPresent());
			assertEquals(m1.getId(), got.get().getId());
		}
	}

	/**
	 * Test of count method, of class SelectStatement.
	 *
	 * @throws java.lang.Exception
	 */
	@Test
	public void testCount() throws Exception {
		System.out.println("count");
		{
			long result = Select.from(Member.class).count(conn);
			long expResult = 0L;
			assertEquals(expResult, result);
		}

		// has rows
		{
			new Member().setEmail("hoge@example.com").insert(conn);
			new Member().setEmail("fuga@example.com").insert(conn);

			assertEquals(2L, Select.from(Member.class).count(conn));
			assertEquals(1L, Select.from(Member.class).where(eq("email", "fuga@example.com")).count(conn));
		}
	}

}
