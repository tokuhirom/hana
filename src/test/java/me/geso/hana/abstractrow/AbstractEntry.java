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
@Table(name = "entry")
public abstract class AbstractEntry extends me.geso.hana.AbstractRow {
	public String getTableName() { return "entry"; }
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
			case "blog_id":
				this.blog_id = rs.getLong(i);
				break;
			case "title":
				this.title = rs.getString(i);
				break;
			case "created_on":
				this.created_on = rs.getLong(i);
				break;
			case "updated_on":
				this.updated_on = rs.getLong(i);
				break;
			default:
				this.setExtraColumn(label, rs.getString(i));
			} // switch
		} // for
	}
	@Override
	public void insert(Connection connection) throws SQLException, HanaException {
		Insert insert = Insert.into(this.getTableName());
		for (String col: dirtyColumns) {
			switch (col) {
			case "id":
				insert.value(col, this.getId());
				break;
			case "blog_id":
				insert.value(col, this.getBlogId());
				break;
			case "title":
				insert.value(col, this.getTitle());
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
	}

	@Override
	public String getColumn(String column) throws SQLException {
		switch (column) {
			case "id":
				return String.valueOf(this.id);
			case "blog_id":
				return String.valueOf(this.blog_id);
			case "title":
				return String.valueOf(this.title);
			case "created_on":
				return String.valueOf(this.created_on);
			case "updated_on":
				return String.valueOf(this.updated_on);
			default:
				return this.getExtraColumn(column);
		}
	}


	public static Optional<me.geso.hana.row.Entry> find(Connection connection, long id) throws SQLException, HanaException {
		return Select.from(me.geso.hana.row.Entry.class)
		.where(me.geso.hana.Condition.eq("id", id))
		.first(connection);
	}

	public Optional<me.geso.hana.row.Entry> refetch(Connection connection) throws SQLException, HanaException {
		return AbstractEntry.find(connection,id);
	}

	public static long count(Connection connection) throws SQLException, HanaException {
		return Select.from(AbstractEntry.class).count(connection);	}
       @Override
	public String toString() {
		return "AbstractEntry ["
			+ " id=" + id
			+ " blog_id=" + blog_id
			+ " title=" + title
			+ " created_on=" + created_on
			+ " updated_on=" + updated_on
		+ "]";
	}
	// Column: id INTEGER(10)
	private long id;

	public long getId() {
		return this.id;
	}

	public me.geso.hana.row.Entry setId(long value) {
		this.id = value;
		this.dirtyColumns.add("id");
		return (me.geso.hana.row.Entry)this;
	}

	// Column: blog_id INTEGER(10)
	private long blog_id;

	public long getBlogId() {
		return this.blog_id;
	}

	public me.geso.hana.row.Entry setBlogId(long value) {
		this.blog_id = value;
		this.dirtyColumns.add("blog_id");
		return (me.geso.hana.row.Entry)this;
	}

	// Column: title VARCHAR(255)
	private String title;

	public String getTitle() {
		return this.title;
	}

	public me.geso.hana.row.Entry setTitle(String value) {
		this.title = value;
		this.dirtyColumns.add("title");
		return (me.geso.hana.row.Entry)this;
	}

	// Column: created_on INTEGER(10)
	private long created_on=Instant.now().getEpochSecond();

	public long getCreatedOn() {
		return this.created_on;
	}

	public me.geso.hana.row.Entry setCreatedOn(long value) {
		this.created_on = value;
		this.dirtyColumns.add("created_on");
		return (me.geso.hana.row.Entry)this;
	}

	// Column: updated_on INTEGER(10)
	private long updated_on;

	public long getUpdatedOn() {
		return this.updated_on;
	}

	public me.geso.hana.row.Entry setUpdatedOn(long value) {
		this.updated_on = value;
		this.dirtyColumns.add("updated_on");
		return (me.geso.hana.row.Entry)this;
	}

	/**
	 * Seach comment related on entry.
	 * @param connection
	 * @return
	 * @throws java.sql.SQLException
	 * @throws me.geso.hana.HanaException
	 */
	public long countComments(Connection connection) throws SQLException, HanaException {
		return Select.from(me.geso.hana.row.Comment.class).where(me.geso.hana.Condition.eq("entry_id", this.id)).count(connection);
	}
	
	public Stream<me.geso.hana.row.Comment> searchComments(Connection connection) throws SQLException, HanaException {
		return Select.from(me.geso.hana.row.Comment.class).where(me.geso.hana.Condition.eq("entry_id", this.id)).stream(connection);
	}
	
	public Optional<me.geso.hana.row.Blog> retrieveBlog(Connection connection) throws SQLException, HanaException {
		return Select.from(me.geso.hana.row.Blog.class).where(me.geso.hana.Condition.eq("id", this.blog_id)).stream(connection).findFirst();
	}
	
}
