/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package me.geso.hana;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;
import static me.geso.hana.Condition.eq;
import static me.geso.hana.Condition.gt;
import me.geso.hana.row.Member;
import static org.junit.Assert.*;
import org.junit.Test;
import static org.assertj.core.api.Assertions.assertThat;  // main one


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
	public void testLimit() throws SQLException {
		assertEquals("SELECT * FROM \"member\" LIMIT 30",
				Select.from(Member.class)
				.limit(30)
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
	public void testHaving() throws SQLException {
		assertEquals("SELECT * FROM \"member\"", Select.from(Member.class).build(conn).getQuery());
		assertEquals("SELECT * FROM \"member\" WHERE \"status\"=? HAVING \"id\"=?",
				Select.from(Member.class)
				.where(eq("status", 3))
				.having(eq("id", 1))
				.build(conn)
				.getQuery());
		assertEquals("SELECT * FROM \"member\" HAVING \"id\"=?",
				Select.from(Member.class)
				.having(eq("id", 1))
				.build(conn)
				.getQuery());
		assertEquals("SELECT * FROM \"member\" HAVING (( \"id\"=? ) AND ( \"status\">? ))",
				Select.from(Member.class)
				.having(eq("id", 1))
				.having(gt("status", 3))
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

	@Test
	public void testOffset() throws SQLException {
		assertEquals("SELECT * FROM \"member\" LIMIT 30 OFFSET 10",
				Select.from(Member.class)
				.limit(30)
				.offset(10)
				.build(conn)
				.getQuery());
	}

	@Test
	public void testPaginate() throws Exception {
		IntStream.rangeClosed(1, 10).forEach(i -> {
			try {
				new Member().setEmail("" + i).insert(conn);
			} catch (SQLException | HanaException ex) {
				throw new RuntimeException(ex);
			}
		});
		{
			Page<Member> page1 = Select.from(Member.class).orderBy("id").paginate(1, 3, conn);
			assertThat(page1.getCurrentPage()).isEqualTo(1);
			assertThat(page1.getEntriesPerPage()).isEqualTo(3);
			assertThat(page1.getRows().stream().map(member -> member.getEmail()).collect(Collectors.joining(",")))
					.isEqualTo("1,2,3");
		}
		{
			Page<Member> page = Select.from(Member.class).orderBy("id").paginate(2, 3, conn);
			assertThat(page.getCurrentPage()).isEqualTo(2);
			assertThat(page.getEntriesPerPage()).isEqualTo(3);
			assertThat(page.getRows().stream().map(member -> member.getEmail()).collect(Collectors.joining(",")))
					.isEqualTo("4,5,6");
		}
		{
			Page<Member> page = Select.from(Member.class).orderBy("id").paginate(3, 3, conn);
			assertThat(page.getCurrentPage()).isEqualTo(3);
			assertThat(page.getEntriesPerPage()).isEqualTo(3);
			assertThat(page.getRows().stream().map(member -> member.getEmail()).collect(Collectors.joining(",")))
					.isEqualTo("7,8,9");
		}
		{
			Page<Member> page = Select.from(Member.class).orderBy("id").paginate(4, 3, conn);
			assertThat(page.getCurrentPage()).isEqualTo(4);
			assertThat(page.getEntriesPerPage()).isEqualTo(3);
			assertThat(page.getRows().stream().map(member -> member.getEmail()).collect(Collectors.joining(",")))
					.isEqualTo("10");
		}
		{
			Page<Member> page = Select.from(Member.class).orderBy("id").paginate(5, 3, conn);
			assertThat(page.getCurrentPage()).isEqualTo(5);
			assertThat(page.getEntriesPerPage()).isEqualTo(3);
			assertThat(page.getRows().stream().map(member -> member.getEmail()).collect(Collectors.joining(",")))
					.isEqualTo("");
		}
	}

	@Test
	public void testPaginateWithTotalEntries() throws Exception {
		IntStream.rangeClosed(1, 10).forEach(i -> {
			try {
				new Member().setEmail("" + i).insert(conn);
			} catch (SQLException | HanaException ex) {
				throw new RuntimeException(ex);
			}
		});
		{
			PageWithTotalEntries<Member> page = Select.from(Member.class).orderBy("id").paginateWithTotalEntries(1, 3, conn);
			assertThat(page.getTotalEntries()).isEqualTo(10);
			assertThat(page.getCurrentPage()).isEqualTo(1);
			assertThat(page.getEntriesPerPage()).isEqualTo(3);
			assertThat(page.getRows().stream().map(member -> member.getEmail()).collect(Collectors.joining(",")))
					.isEqualTo("1,2,3");
		}
		{
			PageWithTotalEntries<Member> page = Select.from(Member.class).orderBy("id").paginateWithTotalEntries(2, 3, conn);
			assertThat(page.getTotalEntries()).isEqualTo(10);
			assertThat(page.getCurrentPage()).isEqualTo(2);
			assertThat(page.getEntriesPerPage()).isEqualTo(3);
			assertThat(page.getRows().stream().map(member -> member.getEmail()).collect(Collectors.joining(",")))
					.isEqualTo("4,5,6");
		}
		{
			PageWithTotalEntries<Member> page = Select.from(Member.class).orderBy("id").paginateWithTotalEntries(3, 3, conn);
			assertThat(page.getTotalEntries()).isEqualTo(10);
			assertThat(page.getCurrentPage()).isEqualTo(3);
			assertThat(page.getEntriesPerPage()).isEqualTo(3);
			assertThat(page.getRows().stream().map(member -> member.getEmail()).collect(Collectors.joining(",")))
					.isEqualTo("7,8,9");
		}
		{
			PageWithTotalEntries<Member> page = Select.from(Member.class).orderBy("id").paginateWithTotalEntries(4, 3, conn);
			assertThat(page.getTotalEntries()).isEqualTo(10);
			assertThat(page.getCurrentPage()).isEqualTo(4);
			assertThat(page.getEntriesPerPage()).isEqualTo(3);
			assertThat(page.getRows().stream().map(member -> member.getEmail()).collect(Collectors.joining(",")))
					.isEqualTo("10");
		}
		{
			PageWithTotalEntries<Member> page = Select.from(Member.class).orderBy("id").paginateWithTotalEntries(5, 3, conn);
			assertThat(page.getTotalEntries()).isEqualTo(10);
			assertThat(page.getCurrentPage()).isEqualTo(5);
			assertThat(page.getEntriesPerPage()).isEqualTo(3);
			assertThat(page.getRows().stream().map(member -> member.getEmail()).collect(Collectors.joining(",")))
					.isEqualTo("");
		}
	}

}
