package me.geso.hana;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Spliterators;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public class Select<T extends AbstractRow> {

	private final String table;
	private String orderBy = null;
	private final Class<T> klass;
	private ConditionInterface condition = null;

	public Select(Class<T> klass) {
		this.table = AbstractRow.getTableName(klass);
		this.klass = klass;
	}

	public static <T extends AbstractRow> Select<T> from(Class<T> klass) {
		return new Select(klass);
	}

	public Select<T> orderBy(String orderBy) {
		this.orderBy = orderBy;
		return this;
	}

	public Select<T> where(ConditionInterface condition) {
		this.condition = condition;
		return this;
	}

	public List<T> getResultList(Connection connection) throws SQLException, HanaException {
		return this.stream(connection).collect(Collectors.toList());
	}

	public Stream<T> stream(Connection connection) throws SQLException {
		final Query query = this.build(connection);
		PreparedStatement statement = query.prepare(connection);
		ResultSet rs = statement.executeQuery();
		HanaIterator<T> iterator = new HanaIterator<>(statement, rs, klass);
		return StreamSupport.stream(
				Spliterators.spliteratorUnknownSize(iterator, 0), false);
	}

	public Query build(Connection connection) throws SQLException {
		return this.build(connection.getMetaData().getIdentifierQuoteString());
	}

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
		List<Object> params = (condition == null) ? new ArrayList<>() : condition.getParams();
		return new Query(buf.toString(), params);
	}

	public Optional<T> first(Connection connection) throws HanaException, SQLException {
		return this.stream(connection).findFirst();
	}

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
