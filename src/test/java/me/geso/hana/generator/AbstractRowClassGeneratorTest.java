package me.geso.hana.generator;

import java.io.IOException;
import java.sql.SQLException;
import me.geso.dbinspector.Inspector;
import me.geso.hana.TestBase;
import me.geso.hana.generator.basic.RowClassSchemaGenerator;
import org.junit.Test;

public class AbstractRowClassGeneratorTest extends TestBase {

    @Test
    public void test() throws ClassNotFoundException, SQLException, IOException {
	Inspector inspector = new Inspector(conn);
	Configuration configuration = new Configuration("me.geso.hana", "src/test/java");
	RowClassSchemaGenerator generator = new RowClassSchemaGenerator(inspector,
		configuration);
	generator.generateAll();

    }

}
