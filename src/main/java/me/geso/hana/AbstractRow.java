package me.geso.hana;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import me.geso.hana.annotation.Table;

public abstract class AbstractRow {

	private final Map<String, String> extraColumns = new HashMap<>();
	protected HanaSession session;
	protected boolean inDatabase = false;
	protected Set<String> dirtyColumns = new HashSet<>();
	protected Set<String> columns = new HashSet<>();

	public static final String getTableName(Class<? extends AbstractRow> klass) {
		Class<?> k = klass;
		while (k != null) {
			Table annotation = k.getAnnotation(Table.class);
			if (annotation != null) {
				return annotation.name();
			}
			k = (Class<?>) k.getSuperclass();
		}
		throw new IllegalArgumentException("" + klass
				+ " does not annoted by @Table");
	}

	protected void addDirtyColumn(String name) {
		dirtyColumns.add(name);
	}

	protected Set<String> getDirtyColumnSet() {
		return dirtyColumns;
	}

	public boolean isModified() {
		return this.dirtyColumns.size() > 0;
	}

	public void setSession(HanaSession session) {
		this.session = session;
	}

	public HanaSession currentSession() {
		return session;
	}

	public void delete() throws SQLException, HanaException {
		for (String pk : this.getPrimaryKeys()) {
			if (!(this.columns.contains(pk) || this.dirtyColumns
					.contains(pk))) {
				throw new HanaException("The row doesn't contain primary key; " + pk);
			}
		}

		// Delete column from database.
		DeleteStatement delete = new DeleteStatement(this.session,
				this.getTableName());
		for (String column : this.getPrimaryKeys()) {
			delete.whereEq(column, this.getColumn(column));
		}
		PreparedStatement stmt = delete.prepare();
		HanaSession.logStatement(stmt);
		int deleted = stmt.executeUpdate();
		if (deleted != 1) {
			throw new HanaException("Cannot delete statement: ");
		}
	}

	public void update() throws SQLException {
		UpdateStatement update = new UpdateStatement(currentSession(), AbstractRow.getTableName(this.getClass()));
		for (String column : this.dirtyColumns) {
			update.set(column, this.getColumn(column));
		}
		PreparedStatement stmt = update.prepare(currentSession());
		HanaSession.logStatement(stmt);
		stmt.executeUpdate();
	}

	abstract protected String getColumn(String column) throws SQLException;

	abstract public String getTableName();

	public String getExtraColumn(String name) {
		return extraColumns.get(name);
	}

	abstract public List<String> getPrimaryKeys();

	public void setExtraColumn(String name, String value) {
		extraColumns.put(name, value);
	}

	abstract public void initialize(ResultSet rs) throws SQLException;

	abstract public void insert(HanaSession hanaSession) throws SQLException, HanaException;

}
