/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package me.geso.hana.row;

import java.sql.SQLException;
import me.geso.hana.HanaException;
import me.geso.hana.HanaNoPrimaryKeyException;
import me.geso.hana.TestBase;
import org.junit.Assert;
import static org.junit.Assert.assertTrue;
import org.junit.Test;

/**
 *
 * @author Tokuhiro Matsuno <tokuhirom@gmail.com>
 */
public class FollowTest extends TestBase {

	@Test
	public void testDelete() throws SQLException, HanaException {
		Member m1 = new Member().setEmail("foo@example.com").insert(conn);
		Assert.assertNotNull(m1);
		Member m2 = new Member().setEmail("foo@example.com").insert(conn);
		Assert.assertNotNull(m2);
		Follow follow = new Follow()
				.setFromMemberId(m1.getId())
				.setToMemberId(m2.getId())
				.insert(conn);

		assertThrows(HanaNoPrimaryKeyException.class, () -> {
			follow.delete(conn);
		});
	}

	@Test
	public void testUpdate() throws SQLException, HanaException {
		Member m1 = new Member().setEmail("foo@example.com").insert(conn);
		Assert.assertNotNull(m1);
		Member m2 = new Member().setEmail("bar@example.com").insert(conn);
		Member m3 = new Member().setEmail("baz@example.com").insert(conn);
		Assert.assertNotNull(m2);
		Follow follow = new Follow()
				.setFromMemberId(m1.getId())
				.setToMemberId(m2.getId())
				.insert(conn);

		assertThrows(HanaNoPrimaryKeyException.class, () -> {
			follow.setFromMemberId(m3.getId()).update(conn);
		});
	}

	public static void assertThrows(Class<? extends Exception> exceptionClass, Code code) {
		boolean thrown = false;
		try {
			code.run();
		} catch (Exception ex) {
			System.out.println(ex);
			assertTrue(exceptionClass.isInstance(ex));
			thrown = true;
		}
		assertTrue(thrown);
	}

	@FunctionalInterface
	public interface Code {

		public void run() throws Exception;
	}
}
