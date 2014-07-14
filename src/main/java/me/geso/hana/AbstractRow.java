package me.geso.hana;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import static me.geso.hana.Condition.eq;
import me.geso.hana.annotation.Table;

public abstract class AbstractRow {

	private final Map<String, String> extraColumns = new HashMap<>();
	protected boolean inDatabase = false;
	protected Set<String> dirtyColumns = new HashSet<>();
	protected Set<String> columns = new HashSet<>();

	/**
	 * Get a table name from the row class.
	 *
	 * @param klass Class object for detecting table name.
	 * @return Table name for <i>klass</i>.
	 */
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

	public void delete(Connection connection) throws SQLException, HanaException {
		List<String> primaryKeys = this.getPrimaryKeys();
		if (primaryKeys.isEmpty()) {
			throw new HanaNoPrimaryKeyException("" + AbstractRow.getTableName(this.getClass()) + " does not have a primary keys. You can't delete this row from row object.");
		}
		for (String pk : primaryKeys) {
			if (!(this.columns.contains(pk) || this.dirtyColumns
					.contains(pk))) {
				throw new HanaException("The row doesn't contain primary key; " + pk);
			}
		}

		// Delete column from database.
		Delete delete = Delete.from(this.getTableName());
		for (String column : this.getPrimaryKeys()) {
			delete.where(eq(column, this.getColumn(column)));
		}
		PreparedStatement stmt = delete.build(connection).prepare(connection);
		int deleted = stmt.executeUpdate();
		if (deleted != 1) {
			throw new HanaException("Cannot delete statement: ");
		}
	}

	public void update(Connection connection) throws SQLException, HanaException {
		List<String> primaryKeys = this.getPrimaryKeys();
		if (primaryKeys.isEmpty()) {
			throw new HanaNoPrimaryKeyException("" + AbstractRow.getTableName(this.getClass()) + " does not have a primary keys. You can't delete this row from row object.");
		}

		// TODO test multiple column pk.
		Update update = new Update(AbstractRow.getTableName(this.getClass()));
		for (String column : this.dirtyColumns) {
			update.set(column, this.getColumn(column));
		}
		// TODO set WHERE clause.
		PreparedStatement stmt = update.build(connection).prepare(connection);
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
}
