/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package me.geso.hana.typesafe.generator;

import me.geso.dbinspector.Inspector;
import me.geso.dbinspector.Table;
import me.geso.hana.TestBase;
import me.geso.hana.generator.Configuration;
import static org.junit.Assert.*;
import org.junit.Test;

/**
 *
 * @author tokuhirom
 */
public class TypeSafeRowGeneratorTest extends TestBase {

    /**
     * Test of render method, of class TypeSafeRowGenerator.
     */
    @Test
    public void testRender() throws Exception {
	System.out.println("render");
	Inspector inspector = new Inspector(conn);
	Table table = inspector.getTable("member").get();
	Configuration configuration = new Configuration("me.geso.hana.typesafe.db", "src/test/java");
	TypeSafeSchemaGenerator generator = new TypeSafeSchemaGenerator(inspector, configuration);
	generator.generateAll();
    }

}
