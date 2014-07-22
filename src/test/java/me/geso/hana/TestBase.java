package me.geso.hana;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import org.junit.After;
import org.junit.Before;

public class TestBase {

	protected Connection conn;

	@Before
	public void setup() throws SQLException, ClassNotFoundException, IOException {
		Class.forName("org.h2.Driver");
		conn = DriverManager
				.getConnection("jdbc:h2:mem:test;DATABASE_TO_UPPER=FALSE;TRACE_LEVEL_SYSTEM_OUT=0");
		String schema = new String(Files.readAllBytes(Paths.get("src/test/resources/schema.sql"
		)));
		conn.prepareStatement(schema).executeUpdate();
		conn.prepareStatement("DELETE FROM member").executeUpdate();
	}

	@After
	public void tearDown() throws SQLException {
		conn.close();
	}

}
