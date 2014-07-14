/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package me.geso.hana.row;

import java.sql.SQLException;
import me.geso.hana.HanaException;
import me.geso.hana.TestBase;
import static org.junit.Assert.assertEquals;
import org.junit.Test;

/**
 *
 * @author Tokuhiro Matsuno <tokuhirom@gmail.com>
 */
public class MultiPkSampleTest extends TestBase {

	@Test
	public void testRefetch() throws Exception {
		final MultiPkSample m12 = new MultiPkSample().setId1(1).setId2(2).setTitle("12").setEmail("foo@example.com").insert(conn);
		final MultiPkSample m13 = new MultiPkSample().setId1(1).setId2(3).setTitle("13").setEmail("foo@example.com").insert(conn);
		final MultiPkSample m21 = new MultiPkSample().setId1(2).setId2(1).setTitle("21").setEmail("foo@example.com").insert(conn);
		final MultiPkSample m22 = new MultiPkSample().setId1(2).setId2(2).setTitle("22").setEmail("foo@example.com").insert(conn);
		final MultiPkSample m23 = new MultiPkSample().setId1(2).setId2(3).setTitle("23").setEmail("foo@example.com").insert(conn);
		assertEquals("12", m12.refetch(conn).get().getTitle());
		assertEquals("13", m13.refetch(conn).get().getTitle());
		assertEquals("21", m21.refetch(conn).get().getTitle());
		assertEquals("22", m22.refetch(conn).get().getTitle());
		assertEquals("23", m23.refetch(conn).get().getTitle());
	}

	@Test
	public void testDelete() throws Exception {
		final MultiPkSample m12 = new MultiPkSample().setId1(1).setId2(2).setTitle("12").setEmail("foo@example.com").insert(conn);
		final MultiPkSample m13 = new MultiPkSample().setId1(1).setId2(3).setTitle("13").setEmail("foo@example.com").insert(conn);
		final MultiPkSample m21 = new MultiPkSample().setId1(2).setId2(1).setTitle("21").setEmail("foo@example.com").insert(conn);
		final MultiPkSample m22 = new MultiPkSample().setId1(2).setId2(2).setTitle("22").setEmail("foo@example.com").insert(conn);
		final MultiPkSample m23 = new MultiPkSample().setId1(2).setId2(3).setTitle("23").setEmail("foo@example.com").insert(conn);
		m12.delete(conn);
		assertEquals(false, m12.refetch(conn).isPresent());
		assertEquals(true, m13.refetch(conn).isPresent());
		assertEquals(true, m21.refetch(conn).isPresent());
		assertEquals(true, m22.refetch(conn).isPresent());
		assertEquals(true, m23.refetch(conn).isPresent());
	}

	@Test
	public void testUpdate() throws SQLException, HanaException {
		final MultiPkSample m12 = new MultiPkSample().setId1(1).setId2(2).setTitle("12").setEmail("foo@example.com").insert(conn);
		final MultiPkSample m13 = new MultiPkSample().setId1(1).setId2(3).setTitle("13").setEmail("foo@example.com").insert(conn);
		final MultiPkSample m21 = new MultiPkSample().setId1(2).setId2(1).setTitle("21").setEmail("foo@example.com").insert(conn);
		final MultiPkSample m23 = new MultiPkSample().setId1(2).setId2(3).setTitle("23").setEmail("foo@example.com").insert(conn);
		m12.setTitle("hoge").setEmail("bar@example.com").update(conn);
		assertEquals("hoge", m12.refetch(conn).get().getTitle());
		assertEquals("bar@example.com", m12.refetch(conn).get().getEmail());
		assertEquals("13", m13.refetch(conn).get().getTitle());
		assertEquals("foo@example.com", m13.refetch(conn).get().getEmail());
		assertEquals("21", m21.refetch(conn).get().getTitle());
		assertEquals("foo@example.com", m21.refetch(conn).get().getEmail());
		assertEquals("23", m23.refetch(conn).get().getTitle());
		assertEquals("foo@example.com", m23.refetch(conn).get().getEmail());
	}
}
