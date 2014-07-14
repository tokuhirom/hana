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
		// Delete column from database.
		Delete delete = Delete.from(this.getTableName());
		delete.where(this.condition());
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
		update.where(this.condition());
		PreparedStatement stmt = update.build(connection).prepare(connection);
		int affectedRows = stmt.executeUpdate();
		if (affectedRows != 1) {
			throw new HanaException("Cannot update rows!: " + affectedRows);
		}
	}

	public ConditionInterface condition() throws HanaNoPrimaryKeyException, HanaException, SQLException {
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

		// TODO We should depercate getColumn.
		ConditionInterface condition = null;
		for (String column : this.getPrimaryKeys()) {
			ConditionInterface eqExpr = eq(column, this.getColumn(column));
			if (condition == null) {
				condition = eqExpr;
			} else {
				condition = condition.and(eqExpr);
			}
		}
		return condition;
	}

	abstract protected String getColumn(String column) throws SQLException;

	abstract public String getTableName();

	abstract public List<String> getPrimaryKeys();

	abstract public void initialize(ResultSet rs) throws SQLException;
}
