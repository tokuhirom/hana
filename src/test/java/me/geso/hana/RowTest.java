package me.geso.hana;

import static org.junit.Assert.*;

import java.sql.SQLException;

import me.geso.hana.row.Member;

import org.junit.Test;

public class RowTest extends TestBase {

	@Test
	public void testInsert() throws SQLException, HanaException {
		{
			{
				Member member = new Member().setEmail("foo@example.com");
				member.insert(conn);
				System.out.println(member);
				assertEquals(1, member.getId());
				assertNotEquals(0, member.getCreatedOn());
				assertEquals(1, member.refetch(conn).get().getId());
			}

			{
				Member member = new Member().setEmail("bar@example.com");
				member.insert(conn);
				System.out.println(member);
				new TableDumper().dump(conn, "member", System.out);
				assertEquals(2, member.getId());
				assertNotEquals(0, member.getCreatedOn());
			}
		}
	}

	@Test
	public void testDelete() throws Exception {
		{
			{
				Member member1 = new Member().setEmail("foo@example.com");
				Member member2 = new Member().setEmail("bar@example.com");
				member1.insert(conn);
				member2.insert(conn);

				assertTrue(member1.refetch(conn).isPresent());
				assertTrue(member2.refetch(conn).isPresent());

				member1.delete(conn);

				assertFalse(member1.refetch(conn).isPresent());
				assertTrue(member2.refetch(conn).isPresent());
			}
		}

	}

	@Test
	public void testCount() throws Exception {
		assertEquals(0, Member.count(conn));
		new Member().setEmail(null).insert(conn);
		assertEquals(1, Member.count(conn));
		new Member().setEmail("hoge@gmail.com").insert(conn);
		assertEquals(2, Member.count(conn));
	}

}
