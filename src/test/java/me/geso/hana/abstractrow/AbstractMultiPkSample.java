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
@Table(name = "multi_pk_sample")
public abstract class AbstractMultiPkSample extends me.geso.hana.AbstractRow {
	public String getTableName() { return "multi_pk_sample"; }
	private static final List<String> primaryKeys = Arrays.asList("id1","id2");
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
			case "id1":
				this.id1 = rs.getLong(i);
				break;
			case "id2":
				this.id2 = rs.getLong(i);
				break;
			case "title":
				this.title = rs.getString(i);
				break;
			case "email":
				this.email = rs.getString(i);
				break;
			} // switch
		} // for
	}
	public me.geso.hana.row.MultiPkSample insert(Connection connection) throws SQLException, HanaException {
		Insert insert = Insert.into(this.getTableName());
		for (String col: dirtyColumns) {
			switch (col) {
			case "id1":
				insert.value(col, this.getId1());
				break;
			case "id2":
				insert.value(col, this.getId2());
				break;
			case "title":
				insert.value(col, this.getTitle());
				break;
			case "email":
				insert.value(col, this.getEmail());
				break;
			}
		}
		PreparedStatement stmt = insert.build(connection).prepare(connection);
		stmt.executeUpdate();
		columns.addAll(dirtyColumns);
		dirtyColumns.clear();
		return (me.geso.hana.row.MultiPkSample)this;
	}

	@Override
	public String getColumn(String column) throws SQLException {
		switch (column) {
			case "id1":
				return String.valueOf(this.id1);
			case "id2":
				return String.valueOf(this.id2);
			case "title":
				return String.valueOf(this.title);
			case "email":
				return String.valueOf(this.email);
		}
	}


	public static Optional<me.geso.hana.row.MultiPkSample> find(Connection connection, long id1, long id2) throws SQLException, HanaException {
		return Select.from(me.geso.hana.row.MultiPkSample.class)
		.where(me.geso.hana.Condition.eq("id1", id1))
		.where(me.geso.hana.Condition.eq("id2", id2))
		.stream(connection).findFirst();
	}

	public Optional<me.geso.hana.row.MultiPkSample> refetch(Connection connection) throws SQLException, HanaException {
		return AbstractMultiPkSample.find(connection,id1, id2);
	}

	public static long count(Connection connection) throws SQLException, HanaException {
		return Select.from(AbstractMultiPkSample.class).count(connection);	}
       @Override
	public String toString() {
		return "AbstractMultiPkSample ["
			+ " id1=" + id1
			+ " id2=" + id2
			+ " title=" + title
			+ " email=" + email
		+ "]";
	}
	// Column: id1 INTEGER(10)
	private long id1;

	public long getId1() {
		return this.id1;
	}

	public me.geso.hana.row.MultiPkSample setId1(long value) {
		this.id1 = value;
		this.dirtyColumns.add("id1");
		return (me.geso.hana.row.MultiPkSample)this;
	}

	// Column: id2 INTEGER(10)
	private long id2;

	public long getId2() {
		return this.id2;
	}

	public me.geso.hana.row.MultiPkSample setId2(long value) {
		this.id2 = value;
		this.dirtyColumns.add("id2");
		return (me.geso.hana.row.MultiPkSample)this;
	}

	// Column: title VARCHAR(255)
	private String title;

	public String getTitle() {
		return this.title;
	}

	public me.geso.hana.row.MultiPkSample setTitle(String value) {
		this.title = value;
		this.dirtyColumns.add("title");
		return (me.geso.hana.row.MultiPkSample)this;
	}

	// Column: email VARCHAR(255)
	private String email;

	public String getEmail() {
		return this.email;
	}

	public me.geso.hana.row.MultiPkSample setEmail(String value) {
		this.email = value;
		this.dirtyColumns.add("email");
		return (me.geso.hana.row.MultiPkSample)this;
	}

}
