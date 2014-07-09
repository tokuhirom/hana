/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package me.geso.hana.typesafe.generator;

import java.sql.SQLException;
import java.util.stream.Collectors;
import me.geso.dbinspector.Column;
import me.geso.dbinspector.Table;
import me.geso.hana.generator.Configuration;
import me.geso.hana.generator.Renderer;

/**
 *
 * @author tokuhirom
 */
public class AbstractTypeSafeRowGenerator extends Renderer {

    private final Table table;
    private final Configuration conf;

    public AbstractTypeSafeRowGenerator(Table table, Configuration conf) {
	this.table = table;
	this.conf = conf;
    }

    public String render() throws SQLException {
	String tableName = table.getName();

	appendfln("package %s;", conf.generateAbstractPackageName());
	appendfln("import java.util.List;");
	appendfln("import java.util.Arrays;");
	appendfln("import me.geso.hana.typesafe.StringColumn;");
	appendfln("import me.geso.hana.typesafe.LongColumn;");
	appendfln("import me.geso.hana.typesafe.BlobColumn;");
	appendfln("import me.geso.hana.typesafe.AbstractTypeSafeRow;");
	appendfln("import %s;", conf.generateConcreteFullClassName(tableName));

	appendfln("public class %s extends AbstractTypeSafeRow {",
		conf.generateAbstractClassName(tableName));

	// getTableName
	appendfln("\tprivate static final String tableName = \"%s\";", tableName);
	appendfln("\t@Override");
	appendfln("\tpublic String getTableName() {");
	appendfln("\t\treturn tableName;");
	appendfln("\t}");
	appendfln("");

	// getPrimaryKeys
	appendf("\tprivate static final List<String> primaryKeys = Arrays.asList(");
	append(table.getPrimaryKeys().stream().map(c -> {
	    return "\"" + c.getColumnName() + "\"";
	}).collect(Collectors.joining(",")));
	append(");\n");
	appendfln("\t@Override");
	appendfln("\tpublic List<String> getPrimaryKeys() {");
	appendfln("\t\treturn primaryKeys;");
	appendfln("\t}");
	appendfln("");

	// column instance variables
	for (Column column : table.getColumns()) {
	    String type = columnType(column);
	    appendfln("\tpublic %s %s=new %s(\"%s\", \"%s\");",
		    type, column.getName(), type, tableName, column.getName());
	}
	// Getter method
	for (Column column : table.getColumns()) {
	    String type = columnType(column);
	    appendfln("\tpublic String %s() {",
		    column.getName());
	    appendfln("\t\treturn this.getColumn(\"%s\");", column.getName());
	    appendfln("\t}");
	}
	// Setter method
	for (Column column : table.getColumns()) {
	    String type = columnType(column);
	    appendfln("\tpublic %s %s(String value) {",
		    conf.generateConcreteClassName(tableName),
		    conf.generateSetterName(column.getName())
	    );
	    appendfln("\t\tthis.setColumn(\"%s\", value);", column.getName());
	    appendfln("\t\treturn (%s)this;", conf.generateConcreteClassName(tableName));
	    appendfln("\t}");
	}
	appendfln("}");

	return this.toString();
    }


    public String columnType(Column column) {
	switch (column.getDataType()) {
	    case java.sql.Types.INTEGER:
		return "LongColumn";
	    case java.sql.Types.BIGINT:
		return "LongColumn";
	    case java.sql.Types.VARCHAR:
		return "StringColumn";
	    case java.sql.Types.BLOB:
		return "BlobColumn";
	    default:
		throw new RuntimeException("Unknown column type : " + column.getName());
	}
    }

}
