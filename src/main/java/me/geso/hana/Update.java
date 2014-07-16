/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package me.geso.hana;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;

/**
 *
 * @author tokuhirom
 */
public class Update {

	private final String table;
	private final Map<String, Object> set = new TreeMap<>();
	private Criteria criteria = null;

	public Update(String table) {
		this.table = table;
	}

	/**
	 * Add parameter for SET clause.
	 *
	 * @param column
	 * @param value
	 * @return Object itself. You can use method chaining.
	 */
	public Update set(String column, Object value) {
		set.put(column, value);
		return this;
	}

	/**
	 * Set the Condition to WHERE clause.
	 *
	 * @param criteria
	 * @return Object itself.
	 */
	public Update where(Criteria criteria) {
		this.criteria = criteria;
		return this;
	}

	/**
	 * Build Query object from UpdateStatement.
	 *
	 * @param connection
	 * @return
	 * @throws HanaException
	 * @throws java.sql.SQLException
	 */
	public Query build(Connection connection) throws HanaException, SQLException {
		if (set.isEmpty()) {
			throw new HanaException("no SET in UPDATE statement : " + this.table);
		}

		final String identifierQuoteString = connection.getMetaData().getIdentifierQuoteString();

		final List<Object> params = new ArrayList<>();
		final StringBuilder buf = new StringBuilder();
		buf.append("UPDATE ").append(Identifier.quote(this.table, identifierQuoteString)).append(" SET ");
		buf.append(set.keySet().stream().map(column -> {
			if (column == null) {
				return Identifier.quote(column, identifierQuoteString) + "=NULL";
			} else {
				params.add(set.get(column));
				return Identifier.quote(column, identifierQuoteString) + "=?";
			}
		}).collect(Collectors.joining(",")));
		if (criteria != null) {
			buf.append(" WHERE ");
			buf.append(criteria.getTerm(identifierQuoteString));
			params.addAll(criteria.getParams());
		}

		final String sql = buf.toString();
		return new Query(sql, params);
	}


}
