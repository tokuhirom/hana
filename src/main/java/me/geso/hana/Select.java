package me.geso.hana;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Spliterators;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;
import lombok.ToString;

// TODO pager support
// TODO support innerJoin?
// TODO support leftOuterJoin?
@ToString
public class Select<T extends AbstractRow> implements Cloneable {

	private final String table;
	private String orderBy = null;
	private Long limit = null;
	private Long offset = null;
	private final Class<T> klass;
	private ConditionInterface where = null;
	private ConditionInterface having = null;

	Select(Class<T> klass) {
		this.table = AbstractRow.getTableName(klass);
		this.klass = klass;
	}

	/**
	 * Create new Select object.
	 *
	 * <pre>
	 * 	Select.from(Member.class)
	 * 		.where(eq("id", id))
	 * 		.stream(connection)
	 *		.collect(Collections.toList());
	 * </pre>
	 *
	 * @param <T> The row object.
	 * @param klass Class object for row class.
	 * @return Object itself.
	 */
	public static <T extends AbstractRow> Select<T> from(Class<T> klass) {
		return new Select(klass);
	}

	/**
	 * Set ORDER BY clause.
	 *
	 * @param orderBy
	 * @return
	 */
	public Select<T> orderBy(String orderBy) {
		this.orderBy = orderBy;
		return this;
	}

	/**
	 * Set LIMIT clause.
	 *
	 * @param limit
	 * @return self
	 */
	public Select<T> limit(long limit) {
		this.limit = limit;
		return this;
	}

	/**
	 * Set OFFSET clause.
	 *
	 * @param offset
	 * @return self
	 */
	public Select<T> offset(long offset) {
		this.offset = offset;
		return this;
	}

	/**
	 * Set condition to WHERE clause. If the statement has a where clause, it makes AND clause.
	 *
	 * <pre>
	 * 	Select.from("member").where(eq("email", email)).where(eq("status", 2)).build(conn)
	 * </pre> makes
	 * <pre>
	 * 	SELECT * FROM member WHERE email=? AND status=?
	 * </pre>
	 *
	 * @param condition
	 * @return
	 */
	public Select<T> where(ConditionInterface condition) {
		if (this.where != null) {
			this.where = this.where.and(condition);
		} else {
			this.where = condition;
		}
		return this;
	}

	// TODO support `HAVING COUNT(*) > 3`... In current implementation, eq() quotes automatically.
	/**
	 * Set HAVING clause.
	 *
	 * If the object has HAVING clause already, Hana make these 2 condition in AND expression.
	 *
	 * <pre>
	 * 	Select.from("member").having(eq("email", email)).having(eq("status", 2)).build(conn)
	 * </pre> makes
	 * <pre>
	 * 	SELECT * FROM member HAVING email=? AND status=?
	 * </pre>
	 *
	 * @param condition
	 * @return
	 */
	public Select<T> having(ConditionInterface condition) {
		if (this.having != null) {
			this.having = this.having.and(condition);
		} else {
			this.having = condition;
		}
		return this;
	}

	/**
	 * Create Java8 Stream from Select statement. This method executes SQL in the database.
	 *
	 * @param connection
	 * @return the stream.
	 * @throws SQLException
	 */
	public Stream<T> stream(Connection connection) throws SQLException {
		final Query query = this.build(connection);
		PreparedStatement statement = query.prepare(connection);
		ResultSet rs = statement.executeQuery();
		HanaIterator<T> iterator = new HanaIterator<>(statement, rs, klass);
		return StreamSupport.stream(
				Spliterators.spliteratorUnknownSize(iterator, 0), false);
	}

	/**
	 * Build query object from the Select object.
	 *
	 * @param connection
	 * @return
	 * @throws SQLException
	 */
	public Query build(Connection connection) throws SQLException {
		return this.build(connection.getMetaData().getIdentifierQuoteString());
	}

	public Page<T> paginate(long currentPage, long entriesPerPage, Connection connection) throws SQLException {
		List<T> rows = this.limit(entriesPerPage + 1)
				.offset(entriesPerPage * (currentPage - 1))
				.stream(connection).collect(Collectors.toList());

		boolean hasNext = false;

		if (rows.size() == entriesPerPage + 1) {
			// there is next page.
			rows.remove(rows.size() - 1);
			hasNext = true;
		}

		return new Page<T>(entriesPerPage, currentPage, hasNext, rows);
	}

	/**
	 * I *don't* recommend to use this method. This method makes your application slow.
	 *
	 * @param currentPage
	 * @param entriesPerPage
	 * @param connection
	 * @return
	 * @throws SQLException
	 * @throws me.geso.hana.HanaException
	 */
	public PageWithTotalEntries<T> paginateWithTotalEntries(long currentPage, long entriesPerPage, Connection connection) throws SQLException, HanaException {
		long totalEntries = this.count(connection);
		List<T> rows = this.limit(entriesPerPage)
				.offset(entriesPerPage * (currentPage - 1))
				.stream(connection).collect(Collectors.toList());

		return new PageWithTotalEntries<>(totalEntries, entriesPerPage, currentPage, rows);
	}

	/**
	 * Build query object from the Select object.
	 *
	 * @param identifierQuoteString
	 * @return
	 */
	public Query build(String identifierQuoteString) {
		StringBuilder buf = new StringBuilder();
		buf.append("SELECT * FROM ");
		buf.append(Identifier.quote(table, identifierQuoteString));
		if (where != null) {
			buf.append(" WHERE ");
			buf.append(where.getTerm(identifierQuoteString));
		}
		if (orderBy != null) {
			buf.append(" ORDER BY ");
			buf.append(orderBy);
		}
		if (having != null) {
			buf.append(" HAVING ");
			buf.append(having.getTerm(identifierQuoteString));
		}
		// It may not work on oracle. But I don't have a Oracle server! Patches welcome.
		if (limit != null) {
			buf.append(" LIMIT ");
			buf.append(limit);
		}
		if (offset != null) {
			buf.append(" OFFSET ");
			buf.append(offset);
		}
		List<Object> params = new ArrayList<>();
		if (where != null) {
			params.addAll(where.getParams());
		}
		if (having != null) {
			params.addAll(having.getParams());
		}
		return new Query(buf.toString(), params);
	}

	/**
	 * Count rows selected by this Select object.
	 *
	 * @param connection
	 * @return The number of selectable rows.
	 * @throws SQLException
	 * @throws HanaException
	 */
	public long count(Connection connection) throws SQLException, HanaException {
		final Query query = this.build(connection);
		final String sql = "SELECT COUNT(*) FROM (" + query.getQuery() + ")";
		PreparedStatement statement = new Query(sql, query.getParams()).prepare(connection);
		ResultSet rs = statement.executeQuery();
		if (rs.next()) {
			return rs.getLong(1);
		} else {
			throw new HanaException("Cannot get count by : " + sql);
		}
	}


}
