/*
 * The MIT License
 *
 * Copyright 2014 Tokuhiro Matsuno <tokuhirom@gmail.com>.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package me.geso.hana.generator;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DriverManager;
import me.geso.dbinspector.Inspector;
import me.geso.hana.generator.basic.RowClassSchemaGenerator;

/**
 *
 * @author Tokuhiro Matsuno <tokuhirom@gmail.com>
 */
public class CLI {

	public static void main(String[] args) throws Exception {
		if (args.length != 4) {
			System.out.println("Usage: CLI dsn user pass schema.sql");
			return;
		}

		Class.forName("org.h2.Driver");

		Connection conn = DriverManager
				.getConnection(args[0], args[1], args[2]);
		String schema = new String(Files.readAllBytes(Paths.get(args[3]
		)));
		conn.prepareStatement(schema).executeUpdate();

		Inspector inspector = new Inspector(conn);
		Configuration configuration = new Configuration("me.geso.hana", "src/test/java");
		RowClassSchemaGenerator generator = new RowClassSchemaGenerator(inspector,
				configuration);
		generator.generateAll();

	}
}
