package me.geso.hana;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Spliterators;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;
import lombok.ToString;

	// TODO pager support
@ToString
public class Select<T extends AbstractRow> {

	private final String table;
	private String orderBy = null;
	private Long limit = null;
	private final Class<T> klass;
	private ConditionInterface condition = null;

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
		if (this.condition != null) {
			this.condition = this.condition.and(condition);
		} else {
			this.condition = condition;
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
		if (condition != null) {
			buf.append(" WHERE ");
			buf.append(condition.getTerm(identifierQuoteString));
		}
		if (orderBy != null) {
			buf.append(" ORDER BY ");
			buf.append(orderBy);
		}
		if (limit != null) {
			buf.append(" LIMIT ");
			buf.append(limit);
		}
		List<Object> params = (condition == null) ? new ArrayList<>() : condition.getParams();
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
