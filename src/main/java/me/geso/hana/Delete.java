package me.geso.hana;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import lombok.NonNull;

public class Delete {

	private final String table;
	private ConditionInterface condition = null;
	private String comment;

	/**
	 * Create new instance.
	 *
	 * @param table
	 */
	public Delete(@NonNull String table) {
		this.table = table;
	}

	public static Delete from(String table) {
		return new Delete(table);
	}

	public void comment(String comment) {
		this.comment = comment;
	}

	public <T> Delete where(@NonNull ConditionInterface condition) throws SQLException {
		this.condition = condition;
		return this;
	}

	public Query build(Connection connection) throws SQLException {
		return this.build(connection.getMetaData().getIdentifierQuoteString());
	}

	public Query build(String identifierQuoteString) {
		final StringBuilder buf = new StringBuilder();
		if (comment != null) {
			buf.append("/* ");
			buf.append(comment);
			buf.append(" */");
		}
		buf.append("DELETE FROM ");
		buf.append(Identifier.quote(table, identifierQuoteString));
		if (condition != null) {
			buf.append(" WHERE ");
			buf.append(condition.getTerm(identifierQuoteString));
		}
		final String sql = buf.toString();
		return new Query(sql, condition != null ? condition.getParams() : new ArrayList());
	}
}
