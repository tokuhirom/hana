package me.geso.hana.generator;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

import org.atteo.evo.inflector.English;

import static java.sql.Types.*;
import me.geso.dbinspector.Column;

import com.google.common.base.CaseFormat;

/**
 * Configuration for AbstractRowClassGenerator
 * 
 * @author tokuhirom
 *
 */
public class Configuration {
	private final String basePackage;
	private final String outputDirectory;

	/**
	 * basePackage is the base package name for generated classes.
	 * 
	 * @param basePackage
	 * @param outputDirectory
	 */
	public Configuration(String basePackage, String outputDirectory) {
		this.basePackage = basePackage;
		this.outputDirectory = outputDirectory;
		this.initTypemap();
	}

	public String generateBaseClass(String tableName) {
		return "me.geso.hana.AbstractRow";
	}

	public String generateAbstractPackageName() {
		return basePackage + ".abstractrow";
	}

	public String generateAbstractFullClassName(String tableName) {
		return generateAbstractPackageName() + "." + "Abstract"
				+ CamelCase(tableName);
	}

	public String generateAbstractClassName(String tableName) {
		return "Abstract" + CamelCase(tableName);
	}

	public String generateGetterName(String columnName) {
		return "get" + CamelCase(columnName);
	}

	public String generateSetterName(String columnName) {
		return "set" + CamelCase(columnName);
	}

	public String generateConcreteClassName(String tableName) {
		return CamelCase(tableName);
	}

	public String generateConcretePackageName() {
		return basePackage + ".row";
	}

	public String generateConcreteFullClassName(String tableName) {
		return generateConcretePackageName() + "." + CamelCase(tableName);
	}

	public String CamelCase(String src) {
		return CaseFormat.LOWER_UNDERSCORE.to(CaseFormat.UPPER_CAMEL, src);
	}

	// http://docs.oracle.com/javase/6/docs/technotes/guides/jdbc/getstart/mapping.html#996857
	private Map<Integer, TypeDetail> typeMap = new HashMap<>();

	protected void initTypemap() {
		type(BIT, "boolean", "rs.getBoolean(i)");
		type(BIT, "byte", "rs.getByte(i)");
		type(TINYINT, "byte", "rs.getByte(i)");
		type(SMALLINT, "short", "rs.getShort(i)");
		type(INTEGER, "long", "rs.getLong(i)");
		type(BIGINT, "long", "rs.getLong(i)");
		type(FLOAT, "double", "rs.getDouble(i)");
		type(REAL, "float", "rs.getFloat(i)");
		type(DOUBLE, "double", "rs.getDouble(i)");
		type(NUMERIC, "java.math.BigDecimal", "rs.getBigDecimal(i)");
		type(DECIMAL, "java.math.BigDecimal", "rs.getBigDecimal(i)");
		type(CHAR, "String", "rs.getString(i)");
		type(VARCHAR, "String", "rs.getString(i)");
		type(LONGVARCHAR, "String", "rs.getString(i)");
		type(DATE, null, null);
		type(TIME, null, null);
		type(TIMESTAMP, null, null);
		type(BINARY, null, null);
		type(VARBINARY, null, null);
		type(LONGVARBINARY, null, null);
		type(NULL, null, null);
		type(OTHER, null, null);
		type(JAVA_OBJECT, null, null);
		type(DISTINCT, null, null);
		type(STRUCT, null, null);
		type(ARRAY, null, null);
		type(BLOB, "Blob", "rs.getBlob(i+1)");
		type(CLOB, null, null);
		type(REF, null, null);
		type(DATALINK, null, null);
		type(BOOLEAN, null, null);
		type(ROWID, null, null);
		type(NCHAR, null, null);
		type(NVARCHAR, null, null);
		type(LONGNVARCHAR, null, null);
		type(NCLOB, null, null);
		type(SQLXML, null, null);
		type(REF_CURSOR, null, null);
		type(TIME_WITH_TIMEZONE, null, null);
		type(TIMESTAMP_WITH_TIMEZONE, null, null);
	}

	private void type(int t, String javaType, String method) {
		if (method != null) {
			typeMap.put(t, new TypeDetail(javaType, method));
		}
	}

	static class TypeDetail {
		public final String javaType;
		public final String method;

		public TypeDetail(String javaType, String method) {
			this.javaType = javaType;
			this.method = method;
		}
	}

	public String getResultSetMethod(int dataType) {
		TypeDetail typeDetail = typeMap.get(dataType);
		if (typeDetail == null) {
			throw new RuntimeException("" + dataType + " does not mapped yet.");
		}
		return typeDetail.method;
	}

	// http://docs.oracle.com/javase/6/docs/technotes/guides/jdbc/getstart/mapping.html#996857
	public String getJavaTypeName(int dataType) {
		TypeDetail typeDetail = typeMap.get(dataType);
		if (typeDetail == null) {
			throw new RuntimeException("" + dataType + " does not mapped yet.");
		}
		return typeDetail.javaType;
	}

	public void beforeInsert(Column column) {
	}

	public String generateRetrieveMethodName(String tableName) {
		return "retrieve" + CamelCase(tableName);
	}

	public String generateCountMethodName(String tableName) {
		return "count" + CamelCase(English.plural(tableName));
	}

	public Object generateSearchChildrenMethodName(String tableName) {
		return "search" + CamelCase(English.plural(tableName));
	}

	public Path outputAbstractRowFilePath(String name) {
		return Paths
				.get(outputDirectory,
						generateAbstractFullClassName(name).replace('.', '/')
								+ ".java");
	}

	public Path outputConcreteRowFilePath(String name) {
		return Paths
				.get(outputDirectory,
						generateConcreteFullClassName(name).replace('.', '/')
								+ ".java");
	}

}
