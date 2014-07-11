/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package me.geso.hana;

import java.sql.Connection;
import java.sql.SQLException;
import lombok.NonNull;

/**
 *
 * @author Tokuhiro Matsuno <tokuhirom@gmail.com>
 */
public class Functions {

	/**
	 * Create new DeleteStatement instance from table name.
	 *
	 * @param tableName
	 * @return
	 */
	public static Delete DELETE(String tableName) {
		return new Delete(tableName);
	}

	/**
	 * Create new InsertStatement instance from table name.
	 *
	 * @param table
	 * @return
	 * @throws SQLException
	 */
	public Insert INSERT(@NonNull final String table) throws SQLException {
		return new Insert(table);
	}

	/**
	 * Create new SelectStatement object from Row object.
	 *
	 * @param <T>
	 * @param klass
	 * @return
	 */
	public static <T extends AbstractRow> Select<T> SELECT(Class<T> klass) {
		return new Select<>(klass);
	}

}
