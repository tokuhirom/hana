package me.geso.hana;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class Insert {

	private final String table;
	private final Map<String, Object> values = new LinkedHashMap<>();

	public Insert(String table) {
		this.table = table;
	}

	public static Insert into(String table) {
		return new Insert(table);
	}

	public Insert value(String key, Object value) {
		this.values.put(key, value);
		return this;
	}

	public Query build(Connection connection) throws SQLException {
		return this.build(connection.getMetaData().getIdentifierQuoteString());
	}

	public Query build(String identifierQuoteString) throws SQLException {
		final StringBuilder buf = new StringBuilder();
		buf.append("INSERT INTO ");
		buf.append(Identifier.quote(table, identifierQuoteString));
		buf.append(" (");
		String[] keys = values.keySet().stream().toArray(String[]::new);
		for (int i = 0, l = keys.length; i < l; ++i) {
			buf.append(Identifier.quote(keys[i], identifierQuoteString));
			if (i != l - 1) {
				buf.append(',');
			}
		}
		buf.append(") VALUES (");
		for (int i = 0, l = keys.length; i < l; ++i) {
			buf.append("?");
			if (i != l - 1) {
				buf.append(',');
			}
		}
		buf.append(")");

		final String sql = buf.toString();
		return new Query(sql, values.values().stream().collect(Collectors.toList()));
	}

}
