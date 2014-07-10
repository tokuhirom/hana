/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package me.geso.hana;

import static me.geso.hana.Condition.eq;
import me.geso.hana.row.Member;
import static org.junit.Assert.*;
import org.junit.Test;

/**
 *
 * @author Tokuhiro Matsuno <tokuhirom@gmail.com>
 */
public class UpdateStatementTest extends TestBase {

	public UpdateStatementTest() {
	}

	@Test
	public void testPrepare() throws Exception {
		System.out.println("prepare");
		try (HanaSession session = this.hanaSessionFactory.createSession()) {
			session.doQuery("INSERT INTO member (email) VALUES (NULL)");
			session.doQuery("INSERT INTO member (email) VALUES ('hoge@example.com')");
			session.doQuery("INSERT INTO member (email) VALUES ('bar@example.com')");

			UpdateStatement stmt = new UpdateStatement(session, "member");
			Query query = stmt
					.set("email", "foo@example.com")
					.where(eq("email", "bar@example.com"))
					.build(session);
			String q = query.getQuery();
			System.out.println(q);
			assertEquals("UPDATE \"member\" SET \"email\"=? WHERE \"email\"=?", q);
			assertEquals(1, query.prepare(session).executeUpdate());
			assertEquals(0, session.search(Member.class).where(eq("email", "bar@example.com")).count());
			assertEquals(1, session.search(Member.class).where(eq("email", "foo@example.com")).count());
		}
	}

}
