package me.geso.hana.abstractrow;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Blob;
import java.util.List;
import java.util.Arrays;
import java.util.Optional;
import java.time.Instant;
import javax.annotation.Generated;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import me.geso.hana.HanaSession;
import me.geso.hana.annotation.Table;
import me.geso.hana.InsertStatement;
import me.geso.hana.HanaException;

@Generated(value={}, comments="Generated by me.geso.hana.generator.RowClassGenerator", date="2014-07-10T02:36:23.130Z")
@SuppressWarnings("unused")
@Table(name = "comment")
public abstract class AbstractComment extends me.geso.hana.AbstractRow {
	static final Logger logger = LoggerFactory.getLogger(AbstractComment.class);	public String getTableName() { return "comment"; }
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
			case "entry_id":
				this.entry_id = rs.getLong(i);
				break;
			case "body":
				this.body = rs.getString(i);
				break;
			case "data":
				this.data = rs.getBlob(i+1);
				break;
			case "created_on":
				this.created_on = rs.getLong(i);
				break;
			default:
				this.setExtraColumn(label, rs.getString(i));
			} // switch
		} // for
	}
	@Override
	public void insert(HanaSession hanaSession) throws SQLException, HanaException {
		InsertStatement insert = new InsertStatement(hanaSession, this.getTableName());
		for (String col: dirtyColumns) {
			switch (col) {
			case "id":
				insert.value(col, this.getId());
				break;
			case "entry_id":
				insert.value(col, this.getEntryId());
				break;
			case "body":
				insert.value(col, this.getBody());
				break;
			case "data":
				insert.value(col, this.getData());
				break;
			case "created_on":
				insert.value(col, this.getCreatedOn());
				break;
			}
		}
		PreparedStatement stmt = insert.prepare();
		HanaSession.logStatement(stmt);
		stmt.execute();
		try (ResultSet rs = stmt.getGeneratedKeys();) {
			if (rs.next()) {
				this.setId(rs.getInt(1));
			}
		}
		columns.addAll(dirtyColumns);
		dirtyColumns.clear();
		this.session = hanaSession;
	}

	@Override
	public String getColumn(String column) throws SQLException {
		switch (column) {
			case "id":
				return String.valueOf(this.id);
			case "entry_id":
				return String.valueOf(this.entry_id);
			case "body":
				return String.valueOf(this.body);
			case "data":
				return String.valueOf(this.data);
			case "created_on":
				return String.valueOf(this.created_on);
			default:
				return this.getExtraColumn(column);
		}
	}


	public Optional<me.geso.hana.row.Comment> find(long id) throws SQLException, HanaException {
		return currentSession().search(me.geso.hana.row.Comment.class)
		.where(me.geso.hana.Condition.eq("id", id))
		.first();
	}

	public Optional<me.geso.hana.row.Comment> refetch() throws SQLException, HanaException {
		return this.find(id);
	}

       @Override
	public String toString() {
		return "AbstractComment ["
			+ " id=" + id
			+ " entry_id=" + entry_id
			+ " body=" + body
			+ " data=" + data
			+ " created_on=" + created_on
		+ "]";
	}
	// Column: id INTEGER(10)
	private long id;

	public long getId() {
		return this.id;
	}

	public me.geso.hana.row.Comment setId(long value) {
		this.id = value;
		this.dirtyColumns.add("id");
		return (me.geso.hana.row.Comment)this;
	}

	// Column: entry_id INTEGER(10)
	private long entry_id;

	public long getEntryId() {
		return this.entry_id;
	}

	public me.geso.hana.row.Comment setEntryId(long value) {
		this.entry_id = value;
		this.dirtyColumns.add("entry_id");
		return (me.geso.hana.row.Comment)this;
	}

	// Column: body VARCHAR(255)
	private String body;

	public String getBody() {
		return this.body;
	}

	public me.geso.hana.row.Comment setBody(String value) {
		this.body = value;
		this.dirtyColumns.add("body");
		return (me.geso.hana.row.Comment)this;
	}

	// Column: data BLOB(2147483647)
	private Blob data;

	public Blob getData() {
		return this.data;
	}

	public me.geso.hana.row.Comment setData(Blob value) {
		this.data = value;
		this.dirtyColumns.add("data");
		return (me.geso.hana.row.Comment)this;
	}

	// Column: created_on INTEGER(10)
	private long created_on=Instant.now().getEpochSecond();

	public long getCreatedOn() {
		return this.created_on;
	}

	public me.geso.hana.row.Comment setCreatedOn(long value) {
		this.created_on = value;
		this.dirtyColumns.add("created_on");
		return (me.geso.hana.row.Comment)this;
	}

	public Optional<me.geso.hana.row.Entry> retrieveEntry() throws SQLException, HanaException {
		return this.currentSession().search(me.geso.hana.row.Entry.class).where(me.geso.hana.Condition.eq("id", this.entry_id)).first();
	}
	
}
