package me.geso.hana;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class DeleteStatement extends AbstractStatement {
	
	private final String table;
	private final List<String> where = new ArrayList<>();
	private final List<String> values = new ArrayList<>();
	private String comment;
	
	public DeleteStatement(HanaSession hanaSession, String table) {
		super(hanaSession);
		this.table = table;
	}
	
	public void setComment(String comment) {
		this.comment = comment;
	}
	
	public PreparedStatement createPreparedStatement() throws SQLException {
		StringBuilder buf = new StringBuilder();
		if (comment != null) {
			buf.append("/* ");
			buf.append(comment);
			buf.append(" */");
		}
		buf.append("DELETE FROM ");
		buf.append(quote(table));
		if (where.size() > 0) {
			buf.append(" WHERE ");
			where.stream().forEach((w) -> {
				buf.append(w);
			});
		}
		String sql = buf.toString();
		
		PreparedStatement stmt = session.getConnection()
				.prepareStatement(sql);
		for (int i = 0; i < values.size(); ++i) {
			stmt.setString(i + 1, values.get(i));
		}
		
		return stmt;
	}
	
	public void where(String column, String value) {
		where.add(quote(column) + "=?");
		values.add(value);
	}
}
