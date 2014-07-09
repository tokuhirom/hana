/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package me.geso.hana.typesafe;

import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import me.geso.hana.HanaSession;
import static me.geso.hana.typesafe.OrderType.*;

/**
 *
 * @author tokuhirom
 */
public class Member extends AbstractTypeSafeRow {

    // Table definition
    private static final String tableName = "member";
    private static final List<String> primaryKeys = Arrays.asList("id");
    public static final LongPredicate<Member> id = new LongPredicate<>("member", "id");
    public static final StringColumn<Member> name = new StringColumn<>("member", "name");
    public static final StringColumn<Member> email = new StringColumn<>("member", "email");

    public Member setName(String name) {
        this.setColumn("name", name);
        return this;
    }

    public String name() {
        return this.getColumn("name");
    }

    @Override
    public String getTableName() {
        return tableName;
    }

    @Override
    public List<String> getPrimaryKeys() {
        return primaryKeys;
    }

    public static void main(String[] args) throws SQLException, MissingColumnError, CannotUpdateException {
        HanaSession session = null;
        {
            List<Member> members = Member.name.is("yappo")
                    .search(session)
                    .collect(Collectors.toList());
        }
        {
            List<Member> members = Member.name.is("yappo")
                    .order_by(Member.id, ASC)
                    .search(session)
                    .collect(Collectors.toList());
        }
        {
            List<Member> members = Member.name.like("yappo")
                    .order_by(Member.id, DESC)
                    .search(session)
                    .collect(Collectors.toList());
        }
        {
            List<Member> members = Member.name.like("yappo")
                    .order_by(Member.id, ASC)
                    .count(session)
                    .collect(Collectors.toList());
        }
        {
            Member.name.like("yappo").and(
                    Member.email.is("yappo@shibuya.pl")
            )
                    .order_by(Member.id, DESC)
                    .count(session)
                    .forEach(row -> System.out.println(row.name()));
        }
        {
            Member member = new Member();
            member.setName("yappo").update(session);
        }
        {
            Member member = new Member();
            member.setName("yappo").insert(session);
        }
    }
}
