/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package me.geso.hana;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;

/**
 *
 * @author tokuhirom
 */
public class UpdateStatement {

    private final String table;
    private final Map<String, String> set = new TreeMap<>();
    private final Map<String, String> where = new TreeMap<>();

    public UpdateStatement(HanaSession currentSession, String table) {
        this.table = table;
    }

    public void set(String column, String value) {
        set.put(column, value);
    }

    public void where(String column, String value) {
        where.put(column, value);
    }

    public PreparedStatement prepare(HanaSession session) throws SQLException {
        StringBuilder buf = new StringBuilder();
        buf.append("UPDATE ").append(session.quote(this.table)).append(" ");
        // TODO we should care the NULL value
        buf.append(set.keySet().stream().map(e -> {
            return session.quote(e) + "=?";
        }).collect(Collectors.joining(",")));
        buf.append(" WHERE ");
        buf.append(where.keySet().stream().map(e -> {
            return "(" + session.quote(e) + "=?)";
        }).collect(Collectors.joining(" AND ")));

        final PreparedStatement stmt = session.prepareStatement(table);
        int i = 1;
        for (String column : set.keySet()) {
            stmt.setString(i++, set.get(column));
        }
        for (String column : where.keySet()) {
            stmt.setString(i++, where.get(column));
        }
        return stmt;
    }

}
