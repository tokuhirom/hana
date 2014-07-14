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
import me.geso.hana.HanaException;
import me.geso.hana.Select;

@Generated(value={}, comments="Generated by me.geso.hana.generator.RowClassGenerator")
@SuppressWarnings("unused")
@Table(name = "member")
public abstract class AbstractMember extends me.geso.hana.AbstractRow {
	public String getTableName() { return "member"; }
	private static final List<String> primaryKeys = Arrays.asList("id");
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
			case "id":
				this.id = rs.getLong(i);
				break;
			case "email":
				this.email = rs.getString(i);
				break;
			case "created_on":
				this.created_on = rs.getLong(i);
				break;
			case "updated_on":
				this.updated_on = rs.getLong(i);
				break;
			} // switch
		} // for
	}
	public me.geso.hana.row.Member insert(Connection connection) throws SQLException, HanaException {
		Insert insert = Insert.into(this.getTableName());
		for (String col: dirtyColumns) {
			switch (col) {
			case "id":
				insert.value(col, this.getId());
				break;
			case "email":
				insert.value(col, this.getEmail());
				break;
			case "created_on":
				insert.value(col, this.getCreatedOn());
				break;
			case "updated_on":
				insert.value(col, this.getUpdatedOn());
				break;
			}
		}
		PreparedStatement stmt = insert.build(connection).prepare(connection);
		stmt.execute();
		try (ResultSet rs = stmt.getGeneratedKeys();) {
			if (rs.next()) {
				this.setId(rs.getInt(1));
			}
		}
		columns.addAll(dirtyColumns);
		dirtyColumns.clear();
		return (me.geso.hana.row.Member)this;
	}

	@Override
	public String getColumn(String column) throws SQLException {
		switch (column) {
			case "id":
				return String.valueOf(this.id);
			case "email":
				return String.valueOf(this.email);
			case "created_on":
				return String.valueOf(this.created_on);
			case "updated_on":
				return String.valueOf(this.updated_on);
		}
	}


	public static Optional<me.geso.hana.row.Member> find(Connection connection, long id) throws SQLException, HanaException {
		return Select.from(me.geso.hana.row.Member.class)
		.where(me.geso.hana.Condition.eq("id", id))
		.stream(connection).findFirst();
	}

	public Optional<me.geso.hana.row.Member> refetch(Connection connection) throws SQLException, HanaException {
		return AbstractMember.find(connection,id);
	}

	public static long count(Connection connection) throws SQLException, HanaException {
		return Select.from(AbstractMember.class).count(connection);	}
       @Override
	public String toString() {
		return "AbstractMember ["
			+ " id=" + id
			+ " email=" + email
			+ " created_on=" + created_on
			+ " updated_on=" + updated_on
		+ "]";
	}
	// Column: id BIGINT(19)
	private long id;

	public long getId() {
		return this.id;
	}

	public me.geso.hana.row.Member setId(long value) {
		this.id = value;
		this.dirtyColumns.add("id");
		return (me.geso.hana.row.Member)this;
	}

	// Column: email VARCHAR(255)
	private String email;

	public String getEmail() {
		return this.email;
	}

	public me.geso.hana.row.Member setEmail(String value) {
		this.email = value;
		this.dirtyColumns.add("email");
		return (me.geso.hana.row.Member)this;
	}

	// Column: created_on INTEGER(10)
	private long created_on=Instant.now().getEpochSecond();

	public long getCreatedOn() {
		return this.created_on;
	}

	public me.geso.hana.row.Member setCreatedOn(long value) {
		this.created_on = value;
		this.dirtyColumns.add("created_on");
		return (me.geso.hana.row.Member)this;
	}

	// Column: updated_on INTEGER(10)
	private long updated_on;

	public long getUpdatedOn() {
		return this.updated_on;
	}

	public me.geso.hana.row.Member setUpdatedOn(long value) {
		this.updated_on = value;
		this.dirtyColumns.add("updated_on");
		return (me.geso.hana.row.Member)this;
	}

}
