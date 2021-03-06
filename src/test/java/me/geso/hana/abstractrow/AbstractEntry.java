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
import me.geso.hana.Criteria;
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
				this._HaNa_selected_id = true;
				break;
			case "blog_id":
				this.blog_id = rs.getLong(i);
				this._HaNa_selected_blog_id = true;
				break;
			case "title":
				this.title = rs.getString(i);
				this._HaNa_selected_title = true;
				break;
			case "created_on":
				this.created_on = rs.getLong(i);
				this._HaNa_selected_created_on = true;
				break;
			case "updated_on":
				this.updated_on = rs.getLong(i);
				this._HaNa_selected_updated_on = true;
				break;
			} // switch
		} // for
	}
	public me.geso.hana.row.Entry insert(Connection connection) throws SQLException, HanaException {
		Insert insert = Insert.into(this.getTableName());
		if (_HaNa_dirty_id) {
			insert.value("id", this.getId());
		}
		if (_HaNa_dirty_blog_id) {
			insert.value("blog_id", this.getBlogId());
		}
		if (_HaNa_dirty_title) {
			insert.value("title", this.getTitle());
		}
		if (_HaNa_dirty_created_on) {
			insert.value("created_on", this.getCreatedOn());
		}
		if (_HaNa_dirty_updated_on) {
			insert.value("updated_on", this.getUpdatedOn());
		}
		PreparedStatement stmt = insert.build(connection).prepare(connection);
		stmt.execute();
		try (ResultSet rs = stmt.getGeneratedKeys();) {
			if (rs.next()) {
				this.setId(rs.getLong(1));
				this._HaNa_selected_id = true;
			}
		}
		return (me.geso.hana.row.Entry)this;
	}

	public static Optional<me.geso.hana.row.Entry> find(Connection connection, long id) throws SQLException, HanaException {
		return Select.from(me.geso.hana.row.Entry.class)
		.where(me.geso.hana.Condition.eq("id", id))
		.stream(connection).findFirst();
	}

	public Optional<me.geso.hana.row.Entry> refetch(Connection connection) throws SQLException, HanaException {
		return AbstractEntry.find(connection,id);
	}

	public static long count(Connection connection) throws SQLException, HanaException {
		return Select.from(AbstractEntry.class).count(connection)
;	}
	@Override
	public Criteria criteria() throws SQLException, HanaException {
		if (!this._HaNa_selected_id) {
				throw new HanaException("The row doesn't contain *selected* primary key: id");
		}

		Criteria criteria = null;
		criteria = me.geso.hana.Condition.and(criteria, me.geso.hana.Condition.eq("id", this.getId()));
		return criteria;
	}
	@Override
	protected void setUpdateParameters(me.geso.hana.Update update) throws HanaException, SQLException {
		if (_HaNa_dirty_id) {
			update.set("id", this.getId());
		}
		if (_HaNa_dirty_blog_id) {
			update.set("blog_id", this.getBlogId());
		}
		if (_HaNa_dirty_title) {
			update.set("title", this.getTitle());
		}
		if (_HaNa_dirty_created_on) {
			update.set("created_on", this.getCreatedOn());
		}
		if (_HaNa_dirty_updated_on) {
			update.set("updated_on", this.getUpdatedOn());
		}
	}
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

	private boolean _HaNa_dirty_id;	private boolean _HaNa_selected_id;	public long getId() {
		return this.id;
	}

	public me.geso.hana.row.Entry setId(long value) {
		this.id = value;
		_HaNa_dirty_id = true;
		return (me.geso.hana.row.Entry)this;
	}

	// Column: blog_id INTEGER(10)
	private long blog_id;

	private boolean _HaNa_dirty_blog_id;	private boolean _HaNa_selected_blog_id;	public long getBlogId() {
		return this.blog_id;
	}

	public me.geso.hana.row.Entry setBlogId(long value) {
		this.blog_id = value;
		_HaNa_dirty_blog_id = true;
		return (me.geso.hana.row.Entry)this;
	}

	// Column: title VARCHAR(255)
	private String title;

	private boolean _HaNa_dirty_title;	private boolean _HaNa_selected_title;	public String getTitle() {
		return this.title;
	}

	public me.geso.hana.row.Entry setTitle(String value) {
		this.title = value;
		_HaNa_dirty_title = true;
		return (me.geso.hana.row.Entry)this;
	}

	// Column: created_on INTEGER(10)
	private long created_on=Instant.now().getEpochSecond();

	private boolean _HaNa_dirty_created_on;	private boolean _HaNa_selected_created_on;	public long getCreatedOn() {
		return this.created_on;
	}

	public me.geso.hana.row.Entry setCreatedOn(long value) {
		this.created_on = value;
		_HaNa_dirty_created_on = true;
		return (me.geso.hana.row.Entry)this;
	}

	// Column: updated_on INTEGER(10)
	private long updated_on;

	private boolean _HaNa_dirty_updated_on;	private boolean _HaNa_selected_updated_on;	public long getUpdatedOn() {
		return this.updated_on;
	}

	public me.geso.hana.row.Entry setUpdatedOn(long value) {
		this.updated_on = value;
		_HaNa_dirty_updated_on = true;
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
