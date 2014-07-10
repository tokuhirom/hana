package me.geso.hana;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import java.util.Spliterators;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import me.geso.hana.annotation.Table;

public class SelectStatement<T extends AbstractRow> {

	private final String table;
	private String orderBy = null;
	private final Class<T> klass;
	private ConditionInterface condition = null;
	private final HanaSession session;

	public SelectStatement(HanaSession hanaSession, Class<T> klass) {
		this.session = hanaSession;
		this.table = this.getTableName(klass);
		this.klass = klass;
	}

	@SuppressWarnings("unchecked")
	private String getTableName(Class<T> klass) {
		Class<T> k = klass;
		while (k != null) {
			Table annotation = k.getAnnotation(Table.class);
			if (annotation != null) {
				return annotation.name();
			}
			k = (Class<T>) k.getSuperclass();
		}
		throw new IllegalArgumentException("" + klass
				+ " does not annoted by @Table");
	}

	public SelectStatement<T> orderBy(String orderBy) {
		this.orderBy = orderBy;
		return this;
	}

	public SelectStatement<T> where(ConditionInterface condition) {
		this.condition = condition;
		return this;
	}

	public List<T> getResultList() throws SQLException, HanaException {
		return this.stream().collect(Collectors.toList());
	}

	public Stream<T> stream() throws SQLException {
		final String sql = buildQuery();
		PreparedStatement statement = this.prepare(sql);
		ResultSet rs = statement.executeQuery();
		HanaIterator<T> iterator = new HanaIterator<>(statement, rs, klass);
		return StreamSupport.stream(
				Spliterators.spliteratorUnknownSize(iterator, 0), false);
	}

	private PreparedStatement prepare(String sql) throws SQLException {
		PreparedStatement statement = this.session.getConnection()
				.prepareStatement(sql);
		int parameterIndex = 1;
		if (condition != null) {
			for (Object param : condition.getParams()) {
				statement.setObject(parameterIndex++, param);
			}
		}
		return statement;
	}

	private String buildQuery() {
		StringBuilder buf = new StringBuilder();
		buf.append("SELECT * FROM ");
		buf.append(this.session.quoteIdentifier(table));
		if (condition != null) {
			buf.append(" WHERE ");
			buf.append(condition.getTerm(session.getIdentifierQuoteString()));
		}
		if (orderBy != null) {
			buf.append(" ORDER BY ");
			buf.append(orderBy);
		}
		return new String(buf);
	}

	public Optional<T> first() throws HanaException, SQLException {
		List<T> list = this.getResultList();
		if (list.size() > 0) {
			return Optional.of(list.get(0));
		} else {
			return Optional.empty();
		}
	}

	public long count() throws SQLException, HanaException {
		final String sql = "SELECT COUNT(*) FROM (" + this.buildQuery() + ")";
		PreparedStatement statement = this.prepare(sql);
		HanaSession.logStatement(statement);
		ResultSet rs = statement.executeQuery();
		if (rs.next()) {
			return rs.getLong(1);
		} else {
			throw new HanaException("Cannot get count by : " + sql);
		}
	}

}
