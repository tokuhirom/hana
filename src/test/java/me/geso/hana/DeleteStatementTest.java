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
		// Delete all rows
		try (HanaSession session = this.hanaSessionFactory.createSession()) {
			insertFixture(session);

			DeleteStatement delete = new DeleteStatement(session, "member");
			PreparedStatement stmt = delete.prepare();
			int affected = stmt.executeUpdate();
			assertEquals("DELETE FROM \"member\"", this.getQuery(stmt));
			assertEquals(3, affected);
			assertEquals(0, countMember(session));
		}

		// Delete selected row
		try (HanaSession session = this.hanaSessionFactory.createSession()) {
			insertFixture(session);

			DeleteStatement delete = new DeleteStatement(session, "member").whereEq("email", "fuga@example.com");
			PreparedStatement stmt = delete.prepare();
			int affected = stmt.executeUpdate();
			assertEquals("DELETE FROM \"member\" WHERE \"email\"=?", delete.renderQuery());
			assertEquals(1, affected);
			assertEquals(2, countMember(session));
		}

		// Delete with NULL value
		try (HanaSession session = this.hanaSessionFactory.createSession()) {
			insertFixture(session);

			// Member.name.eq(null)
			DeleteStatement delete = new DeleteStatement(session, "member")
					.where(eq("email", null));
			PreparedStatement stmt = delete.prepare();
			HanaSession.logStatement(stmt);
			int affected = stmt.executeUpdate();
			assertEquals("DELETE FROM \"member\" WHERE \"email\" IS NULL", delete.renderQuery());

			// Deleted one row.
			assertEquals(1, affected);
			assertEquals(2, countMember(session));

//			session.dumpTable("member", System.out);
		}
	}

	private static long countMember(final HanaSession session) throws HanaException, SQLException {
		return session.search(Member.class).count();
	}

	private void insertFixture(final HanaSession session) throws SQLException, HanaException {
		// Insert dummy data
		session.doQuery("DELETE FROM member");
		session.doQuery("INSERT INTO member (email) VALUES (NULL)");
		session.doQuery("INSERT INTO member (email) VALUES ('hoge@example.com')");
		session.doQuery("INSERT INTO member (email) VALUES ('fuga@example.com')");
		assertEquals(3, countMember(session));
	}

	private String getQuery(PreparedStatement stmt) {
		String statementText = stmt.toString();
		return statementText.substring(statementText.indexOf(": ") + 2);
	}

}
