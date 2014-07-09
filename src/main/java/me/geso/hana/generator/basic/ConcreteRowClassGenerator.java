package me.geso.hana.generator.basic;

import me.geso.dbinspector.Table;
import me.geso.hana.generator.Configuration;
import me.geso.hana.generator.Renderer;

public class ConcreteRowClassGenerator extends Renderer {
    private final Table table;

    private final Configuration configuration;

    public ConcreteRowClassGenerator(Table table, Configuration configuration) {
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
