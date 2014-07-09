/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package me.geso.hana.typesafe.generator;

import me.geso.dbinspector.Table;
import me.geso.hana.generator.Configuration;
import me.geso.hana.generator.Renderer;

/**
 *
 * @author tokuhirom
 */
public class ConcreteTypeSafeRowGenerator extends Renderer {

    private final Table table;
    private final Configuration configuration;

    public ConcreteTypeSafeRowGenerator(Table table, Configuration configuration) {
	this.table = table;
	this.configuration = configuration;
    }

    @Override
    public String render() {
	String tableName = table.getName();

	appendf("package %s;\n", configuration.generateConcretePackageName());
	appendf("\n");
	appendf("import %s;\n",
		configuration.generateAbstractFullClassName(tableName));
	appendf("\n");
	appendf("public class %s extends %s {",
		configuration.generateConcreteClassName(tableName),
		configuration.generateAbstractClassName(tableName));
	appendf("\n");
	appendf("}\n");
	appendf("\n");
	return this.toString();
    }

}
