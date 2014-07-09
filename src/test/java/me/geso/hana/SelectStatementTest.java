/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package me.geso.hana;

import java.util.Optional;
import java.util.stream.Collectors;
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
		try (HanaSession session = this.hanaSessionFactory.createSession()) {
			Member m1 = new Member().setEmail("hoge@example.com");
			Member m2 = new Member().setEmail("fuga@example.com");
			session.insert(m1);
			session.insert(m2);

			assertEquals("fuga@example.com,hoge@example.com",
					session
					.search(Member.class)
					.orderBy("email ASC")
					.getResultList()
					.stream()
					.map(row -> row.getEmail())
					.collect(Collectors.joining(",")));
		}
	}

	/**
	 * Test of stream method, of class SelectStatement.
	 * @throws java.lang.Exception
	 */
	@Test
	public void testStream() throws Exception {
		try (HanaSession session = this.hanaSessionFactory.createSession()) {
			Member m1 = new Member().setEmail("hoge@example.com");
			Member m2 = new Member().setEmail("fuga@example.com");
			session.insert(m1);
			session.insert(m2);

			assertEquals("fuga@example.com,hoge@example.com",
					session
					.search(Member.class)
					.orderBy("email ASC")
					.stream()
					.map(row -> row.getEmail())
					.collect(Collectors.joining(",")));
		}
	}

	/**
	 * Test of first method, of class SelectStatement.
	 * @throws java.lang.Exception
	 */
	@Test
	public void testFirst() throws Exception {
		try (HanaSession session = this.hanaSessionFactory.createSession()) {
			Member m1 = new Member().setEmail("hoge@example.com");
			Member m2 = new Member().setEmail("fuga@example.com");
			session.insert(m1);
			session.insert(m2);

			Optional<Member> got = session.search(Member.class).where("email=?", "hoge@example.com").first();
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
		try (HanaSession session = this.hanaSessionFactory.createSession()) {
			SelectStatement stmt = new SelectStatement(session, Member.class);
			long expResult = 0L;
			long result = stmt.count();
			assertEquals(expResult, result);
		}

		// has rows
		try (HanaSession session = this.hanaSessionFactory.createSession()) {
			session.insert(new Member().setEmail("hoge@example.com"));
			session.insert(new Member().setEmail("fuga@example.com"));

			assertEquals(2L, session.search(Member.class).count());
			assertEquals(1L, session.search(Member.class).where("email=?", "fuga@example.com").count());
		}
	}

}
