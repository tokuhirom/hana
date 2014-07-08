package me.geso.hana;

import static org.junit.Assert.*;

import java.sql.SQLException;

import me.geso.hana.row.Member;

import org.junit.Test;

public class RowTest extends TestBase {

    @Test
    public void testInsert() throws SQLException, HanaException {
        try (HanaSession session = hanaSessionFactory.createSession()) {
            {
                Member member = new Member().setEmail("foo@example.com");
                member.insert(session);
                System.out.println(member);
                assertEquals(1, member.getId());
                assertNotEquals(0, member.getCreatedOn());
                assertEquals(1, member.refetch().get().getId());
            }

            {
                Member member = new Member().setEmail("bar@example.com");
                member.insert(session);
                System.out.println(member);
                session.dumpTable("member", System.out);
                assertEquals(2, member.getId());
                assertNotEquals(0, member.getCreatedOn());
            }
        }
    }
    
    @Test
    public void testDelete() throws Exception {
        try (HanaSession session = hanaSessionFactory.createSession()) {
            {
                Member member1 = new Member().setEmail("foo@example.com");
                Member member2 = new Member().setEmail("bar@example.com");
                member1.insert(session);
                member2.insert(session);

                assertTrue(member1.refetch().isPresent());
                assertTrue(member2.refetch().isPresent());
                
                member1.delete();
                
                assertFalse(member1.refetch().isPresent());
                assertTrue(member2.refetch().isPresent());
            }
        }
        
    }

}
