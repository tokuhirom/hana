package me.geso.hana;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import lombok.NonNull;

public class DeleteStatement {

	private final HanaSession session;

	private final String table;
	private Condition condition = null;
	private String comment;

	/**
	 * Create new instance.
	 *
	 * @param session
	 * @param table
	 */
	public DeleteStatement(HanaSession session, String table) {
		this.session = session;
		this.table = table;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public PreparedStatement prepare() throws SQLException, HanaException {
		final String sql = renderQuery();

		PreparedStatement stmt = session.getConnection()
				.prepareStatement(sql);
		int parameterIndex = 1;
		if (condition != null) {
			for (Object value : condition.getParams()) {
				stmt.setObject(parameterIndex++, value);
			}
		}

		return stmt;
	}

	String renderQuery() {
		StringBuilder buf = new StringBuilder();
		if (comment != null) {
			buf.append("/* ");
			buf.append(comment);
			buf.append(" */");
		}
		buf.append("DELETE FROM ");
		buf.append(session.quoteIdentifier(table));
		if (condition != null) {
			buf.append(" WHERE ");
			buf.append(condition.getTerm());
		}
		return buf.toString();
	}

	public <T> DeleteStatement whereEq(String column, T value) throws SQLException {
		return this.where(Condition.eq(column, value));
	}

	public <T> DeleteStatement where(@NonNull Condition<T> condition) throws SQLException {
		this.condition = condition;
		return this;
	}
}
