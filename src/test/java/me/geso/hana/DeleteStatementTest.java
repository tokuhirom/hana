/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package me.geso.hana;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import static me.geso.hana.Condition.eq;
import me.geso.hana.row.Member;
import static org.junit.Assert.*;
import org.junit.Test;

/**
 *
 * @author tokuhirom
 */
public class DeleteStatementTest extends TestBase {

	/**
	 * Test of setComment method, of class DeleteStatement.
	 *
	 * @throws java.sql.SQLException
	 * @throws me.geso.hana.HanaException
	 */
	@Test
	public void test() throws SQLException, HanaException {
		{
			// Delete all rows
			insertFixture();

			Query query = Delete.from("member").build(conn);
			PreparedStatement stmt = query.prepare(conn);
			int affected = stmt.executeUpdate();
			assertEquals("DELETE FROM \"member\"", query.getQuery());
			assertEquals(3, affected);
			assertEquals(0, countMember());
		}

		// Delete selected row
		{
			insertFixture();

			Query query = new Delete("member").where(eq("email", "fuga@example.com")).build(conn);
			PreparedStatement stmt = query.prepare(conn);
			int affected = stmt.executeUpdate();
			assertEquals("DELETE FROM \"member\" WHERE \"email\"=?", query.getQuery());
			assertEquals(1, affected);
			assertEquals(2, countMember());
		}

		// Delete with NULL value
		{
			insertFixture();

			// Member.name.eq(null)
			Query query = new Delete("member")
					.where(eq("email", null)).build(conn);
			PreparedStatement stmt = query.prepare(conn);
			int affected = stmt.executeUpdate();
			assertEquals("DELETE FROM \"member\" WHERE \"email\" IS NULL", query.getQuery());

			// Deleted one row.
			assertEquals(1, affected);
			assertEquals(2, countMember());

//			session.dumpTable("member", System.out);
		}
	}

	private long countMember() throws HanaException, SQLException {
		return Select.from(Member.class).count(conn);
	}

	private void insertFixture() throws SQLException, HanaException {
		// Insert dummy data
		conn.prepareStatement("DELETE FROM member").executeUpdate();
		conn.prepareStatement("INSERT INTO member (email) VALUES (NULL)").executeUpdate();
		conn.prepareStatement("INSERT INTO member (email) VALUES ('hoge@example.com')").executeUpdate();
		conn.prepareStatement("INSERT INTO member (email) VALUES ('fuga@example.com')").executeUpdate();
		assertEquals(3, countMember());
	}

}
