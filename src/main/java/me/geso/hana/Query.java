/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package me.geso.hana;

import java.sql.PreparedStatement;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import lombok.Getter;
import lombok.ToString;

/**
 *
 * @author Tokuhiro Matsuno <tokuhirom@gmail.com>
 */
@ToString
public class Query {

	@Getter
	private final String query;

	@Getter
	private final List<Object> params;

	Query(String query, List<Object> params) {
		this.query = query;
		this.params = params;
	}

	/**
	 * Create prepared statement from query object.
	 *
	 * @param connection
	 * @return
	 * @throws SQLException
	 */
	public PreparedStatement prepare(Connection connection) throws SQLException {
		PreparedStatement stmt = connection.prepareStatement(query);
		int parameterIndex = 1;
		for (Object param : params) {
			stmt.setObject(parameterIndex++, param);
		}
		return stmt;
	}
}
