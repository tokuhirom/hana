/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package me.geso.hana;

import java.io.PrintStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 *
 * @author Tokuhiro Matsuno <tokuhirom@gmail.com>
 */
public class TableDumper {
	// TODO: Dump with table generator like Text::SimpleTable
	// Should we split this from Hana?

	public void dump(Connection conn, String tableName, PrintStream os) throws SQLException {
		String sql = "SELECT * FROM " + Identifier.quote(tableName, conn.getMetaData().getIdentifierQuoteString());
		PreparedStatement prepareStatement = conn.prepareStatement(sql);
		ResultSet rs = prepareStatement.executeQuery();
		int columnCount = rs.getMetaData().getColumnCount();
		// show header
		for (int i = 0; i < columnCount; ++i) {
			String label = rs.getMetaData().getColumnLabel(i + 1);
			os.print(label + " ");
		}
		os.print("\n");
		while (rs.next()) {
			for (int i = 0; i < columnCount; ++i) {
				String value = rs.getString(i + 1);
				os.print(value + " ");
			}
			os.print("\n");
		}
	}

}
