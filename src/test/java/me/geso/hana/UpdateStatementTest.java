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
		{
			conn.prepareStatement("INSERT INTO member (email) VALUES (NULL)").executeUpdate();
			conn.prepareStatement("INSERT INTO member (email) VALUES ('hoge@example.com')").executeUpdate();
			conn.prepareStatement("INSERT INTO member (email) VALUES ('bar@example.com')").executeUpdate();

			Update stmt = new Update("member");
			Query query = stmt
					.set("email", "foo@example.com")
					.where(eq("email", "bar@example.com"))
					.build(conn);
			String q = query.getQuery();
			System.out.println(q);
			assertEquals("UPDATE \"member\" SET \"email\"=? WHERE \"email\"=?", q);
			assertEquals(1, query.prepare(conn).executeUpdate());
			assertEquals(0, Select.from(Member.class).where(eq("email", "bar@example.com")).count(conn));
			assertEquals(1, Select.from(Member.class).where(eq("email", "foo@example.com")).count(conn));
		}
	}

}
