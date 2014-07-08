package me.geso.hana.generator;

import java.io.IOException;
import java.sql.SQLException;

import me.geso.dbinspector.Inspector;
import me.geso.hana.TestBase;

import org.junit.Test;

public class AbstractRowClassGeneratorTest extends TestBase {

	@Test
	public void test() throws ClassNotFoundException, SQLException, IOException {
		Inspector inspector = new Inspector(conn);
		SchemaGenerator generator = new SchemaGenerator(inspector,
				new Configuration("me.geso.hana", "src/test/java"));
		generator.generateAll();

	}

}
