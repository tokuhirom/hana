/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package me.geso.hana;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.stream.Stream;
import static me.geso.hana.Condition.eq;
import static me.geso.hana.Condition.gt;
import me.geso.hana.row.Member;
import static org.junit.Assert.*;
import org.junit.Test;

/**
 *
 * @author Tokuhiro Matsuno <tokuhirom@gmail.com>
 */
public class SelectTest extends TestBase {

	public SelectTest() {
	}

	@Test
	public void testFrom() {
		assertTrue(Select.from(Member.class) instanceof Select);
	}

	@Test
	public void testOrderBy() throws SQLException {
		assertEquals("SELECT * FROM \"member\" ORDER BY id DESC",
				Select.from(Member.class)
				.orderBy("id DESC")
				.build(conn)
				.getQuery());
	}

	@Test
	public void testWhere() throws SQLException {
		assertEquals("SELECT * FROM \"member\"", Select.from(Member.class).build(conn).getQuery());
		assertEquals("SELECT * FROM \"member\" WHERE \"id\"=?",
				Select.from(Member.class)
				.where(eq("id", 1))
				.build(conn)
				.getQuery());
		assertEquals("SELECT * FROM \"member\" WHERE (( \"id\"=? ) AND ( \"status\">? ))",
				Select.from(Member.class)
				.where(eq("id", 1))
				.where(gt("status", 3))
				.build(conn)
				.getQuery());
	}

	@Test
	public void testCount() throws Exception {
		new Member().setEmail("foo@example.com").insert(conn);
		assertEquals(1, Select.from(Member.class).count(conn));
		new Member().setEmail("bar@example.com").insert(conn);
		assertEquals(2, Select.from(Member.class).count(conn));
	}

}
