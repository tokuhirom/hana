package me.geso.hana.abstractrow;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Blob;
import java.util.List;
import java.util.Arrays;
import java.util.stream.Stream;
import java.util.Optional;
import java.time.Instant;
import javax.annotation.Generated;

import me.geso.hana.annotation.Table;
import me.geso.hana.Insert;
import me.geso.hana.ConditionInterface;
import me.geso.hana.HanaException;
import me.geso.hana.Select;

@Generated(value={}, comments="Generated by me.geso.hana.generator.RowClassGenerator")
@SuppressWarnings("unused")
@Table(name = "follow")
public abstract class AbstractFollow extends me.geso.hana.AbstractRow {
	public String getTableName() { return "follow"; }
	private static final List<String> primaryKeys = Arrays.asList();
	@Override
	public List<String> getPrimaryKeys() {
		return primaryKeys;
	}

	@Override
	public void initialize(ResultSet rs) throws SQLException {
		ResultSetMetaData meta = rs.getMetaData();
		int columnCount = meta.getColumnCount();
		for (int i=1; i<columnCount+1; ++i) {
			String label = meta.getColumnLabel(i);
			switch (label) {
			case "from_member_id":
				this.from_member_id = rs.getLong(i);
				break;
			case "to_member_id":
				this.to_member_id = rs.getLong(i);
				break;
			case "created_on":
				this.created_on = rs.getLong(i);
				break;
			} // switch
		} // for
	}
	public me.geso.hana.row.Follow insert(Connection connection) throws SQLException, HanaException {
		Insert insert = Insert.into(this.getTableName());
		for (String col: dirtyColumns) {
			switch (col) {
			case "from_member_id":
				insert.value(col, this.getFromMemberId());
				break;
			case "to_member_id":
				insert.value(col, this.getToMemberId());
				break;
			case "created_on":
				insert.value(col, this.getCreatedOn());
				break;
			}
		}
		PreparedStatement stmt = insert.build(connection).prepare(connection);
		stmt.executeUpdate();
		columns.addAll(dirtyColumns);
		dirtyColumns.clear();
		return (me.geso.hana.row.Follow)this;
	}

	@Override
	public String getColumn(String column) throws SQLException, HanaException {
		switch (column) {
			case "from_member_id":
				return String.valueOf(this.from_member_id);
			case "to_member_id":
				return String.valueOf(this.to_member_id);
			case "created_on":
				return String.valueOf(this.created_on);
		default:
			throw new HanaException("Unknown column: " + column);
		}
	}


	public static long count(Connection connection) throws SQLException, HanaException {
		return Select.from(AbstractFollow.class).count(connection)
;	}
	@Override
	public ConditionInterface condition() throws SQLException, HanaException {
		throw new me.geso.hana.HanaNoPrimaryKeyException("follow doesn't have a primary key");	}
       @Override
	public String toString() {
		return "AbstractFollow ["
			+ " from_member_id=" + from_member_id
			+ " to_member_id=" + to_member_id
			+ " created_on=" + created_on
		+ "]";
	}
	// Column: from_member_id INTEGER(10)
	private long from_member_id;

	public long getFromMemberId() {
		return this.from_member_id;
	}

	public me.geso.hana.row.Follow setFromMemberId(long value) {
		this.from_member_id = value;
		this.dirtyColumns.add("from_member_id");
		return (me.geso.hana.row.Follow)this;
	}

	// Column: to_member_id INTEGER(10)
	private long to_member_id;

	public long getToMemberId() {
		return this.to_member_id;
	}

	public me.geso.hana.row.Follow setToMemberId(long value) {
		this.to_member_id = value;
		this.dirtyColumns.add("to_member_id");
		return (me.geso.hana.row.Follow)this;
	}

	// Column: created_on INTEGER(10)
	private long created_on=Instant.now().getEpochSecond();

	public long getCreatedOn() {
		return this.created_on;
	}

	public me.geso.hana.row.Follow setCreatedOn(long value) {
		this.created_on = value;
		this.dirtyColumns.add("created_on");
		return (me.geso.hana.row.Follow)this;
	}

}
