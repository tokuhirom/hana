/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package me.geso.hana;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
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
	private Condition condition = null;

	public UpdateStatement(HanaSession currentSession, String table) {
		this.table = table;
	}

	public void set(String column, String value) {
		set.put(column, value);
	}

	public void where(Condition condition) {
		this.condition = condition;
	}

	public PreparedStatement prepare(HanaSession session) throws SQLException {
		final List<String> params = new ArrayList<>();
		final StringBuilder buf = new StringBuilder();
		buf.append("UPDATE ").append(session.quoteIdentifier(this.table)).append(" ");
		buf.append(set.keySet().stream().map(column -> {
			if (column == null) {
				return session.quoteIdentifier(column) + "=NULL";
			} else {
				params.add(set.get(column));
				return session.quoteIdentifier(column) + "=?";
			}
		}).collect(Collectors.joining(",")));
		if (condition != null) {
			buf.append(" WHERE ");
			buf.append(condition.getTerm());
		}

		final String sql = buf.toString();
		final PreparedStatement stmt = session.prepareStatement(sql);

		int parameterIndex = 1;
		for (String val : params) {
			stmt.setString(parameterIndex++, val);
		}

		return stmt;
	}

}
