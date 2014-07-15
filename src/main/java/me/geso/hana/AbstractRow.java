package me.geso.hana;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import me.geso.hana.annotation.Table;

public abstract class AbstractRow {

	protected boolean inDatabase = false;
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
		this.setUpdateParameters(update);
		update.where(this.condition());

		Query query = update.build(connection);
		PreparedStatement stmt = query.prepare(connection);
		int affectedRows = stmt.executeUpdate();
		if (affectedRows != 1) {
			throw new HanaException("Cannot update rows!: " + affectedRows);
		}
	}

	abstract protected void setUpdateParameters(Update update) throws HanaException, SQLException;

	abstract public ConditionInterface condition() throws HanaException, SQLException;

	abstract public String getTableName();

	abstract public List<String> getPrimaryKeys();

	abstract public void initialize(ResultSet rs) throws SQLException;
}
