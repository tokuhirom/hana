package me.geso.hana.generator.basic;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import me.geso.dbinspector.Column;
import me.geso.dbinspector.ExportedKey;
import me.geso.dbinspector.ImportedKey;
import me.geso.dbinspector.PrimaryKey;
import me.geso.dbinspector.Table;
import me.geso.hana.generator.Configuration;
import me.geso.hana.generator.Renderer;

public class AbstractRowClassGenerator extends Renderer {

	private final List<ColumnHook> columnHooks = new ArrayList<>();
	private final List<TableHook> beforeInsertHooks = new ArrayList<>();
	// TODO use this hook point.
	private final List<TableHook> beforeUpdateHooks = new ArrayList<>();
	private final Table table;
	private final Configuration configuration;

	public AbstractRowClassGenerator(Table table, Configuration configuration) {
		this.table = table;
		this.configuration = configuration;
	}

	// String baseClass, String packageName, String className
	@Override
	public String render() throws SQLException {
		if (table == null) {
			throw new IllegalArgumentException("Table object must not be null");
		}

		appendf("package %s;\n",
				configuration.generateAbstractPackageName());
		append("\n");
		append("import java.sql.PreparedStatement;\n");
		append("import java.sql.ResultSet;\n");
		append("import java.sql.ResultSetMetaData;\n");
		append("import java.sql.Connection;\n");
		append("import java.sql.SQLException;\n");
		append("import java.sql.Blob;\n");
		append("import java.util.List;\n");
		append("import java.util.Arrays;\n");
		append("import java.util.stream.Stream;\n");
		append("import java.util.Optional;\n");
		append("import java.time.Instant;\n");
		append("import javax.annotation.Generated;\n");
		append("\n");
		append("import me.geso.hana.annotation.Table;\n");
		append("import me.geso.hana.Insert;\n");
		append("import me.geso.hana.Criteria;\n");
		append("import me.geso.hana.HanaException;\n");
		append("import me.geso.hana.Select;\n");
		append("\n");

		appendf("@Generated(value={}, comments=\"Generated by me.geso.hana.generator.RowClassGenerator\")\n");
		appendf("@SuppressWarnings(\"unused\")\n");
		appendf("@Table(name = \"%s\")\n", table.getName());
		appendf("public abstract class %s extends %s {\n",
				configuration.generateAbstractClassName(table.getName()),
				configuration.generateBaseClass(table.getName()));

		renderGetTableName(table);
		renderGetPrimaryKeys(table);
		renderInitialize(table);
		renderInsert(table);
		if (table.getPrimaryKeys().size() > 0) {
			renderFind(table);
			renderRefetch(table);
		}
		renderCount(table);
		renderCriteria(table);
		renderSetUpdate(table);
		renderToString(table);

		for (Column column : table.getColumns()) {
			renderColumn(
					configuration.generateConcreteClassName(table.getName()),
					table,
					column);
		}
		List<ExportedKey> exportedKeys = table.getExportedKeys();
		exportedKeys.stream().filter(key -> {
			/*
			 * If the exported keys reference same table in two or more keys, we can't create the countXXX() method and
			 * searchXXX method. Because these methods conflict!
			 * 
			 * For example, there is a follow table.
			 *
			 * CREATE TABLE follow (
			 *   from_member_id INT UNSIGNED NOT NULL,
			 *   to_member_id INT UNSIGNED NOT NULL,
			 *   FOREIGN KEY(to_member_id) REFERENCES member(id) ON DELETE CASCADE,
			 *   FOREIGN KEY(from_member_id) REFERENCES member(id) ON DELETE CASCADE,
			 *   UNIQUE (from_member_id, to_member_id),
			 *   INDEX (to_member_id)
			 * );
			 *
			 * In this case, hana will create two AbstractMember#countFollows() without this filter.
			 */
			return exportedKeys.stream()
					.filter(y -> y.getPrimaryKeyTable().equals(key.getPrimaryKeyTable()))
					.count() == 1;
		}).forEach(e -> {
			renderHasManyCount(e); // countJobs
			renderHasManySearchChildren(e); // searchJobs
		});

		List<ImportedKey> importedKeys = table.getImportedKeys();
		importedKeys.stream().filter(key -> {
			/*
			 * If the exported keys reference same table in two or more keys, we can't create the createXXX() method.
			 * Because these methods conflict!
			 *
			 * For example, there is a follow table.
			 *
			 * CREATE TABLE follow (
			 *   from_member_id INT UNSIGNED NOT NULL,
			 *   to_member_id INT UNSIGNED NOT NULL,
			 *   FOREIGN KEY(to_member_id) REFERENCES member(id) ON DELETE CASCADE,
			 *   FOREIGN KEY(from_member_id) REFERENCES member(id) ON DELETE CASCADE,
			 *   UNIQUE (from_member_id, to_member_id),
			 *   INDEX (to_member_id)
			 * );
			 *
			 * In this case, hana will create two AbstractFollow#retrieveMember() without this filter.
			 */
			return importedKeys.stream()
					.filter(y -> y.getPrimaryKeyTable().equals(key.getPrimaryKeyTable()))
					.count() == 1;
		}).forEach(e -> {
			renderHasARetrieve(e); // retrieveRepository
		});

		append("}\n");
		return this.toString();
	}

	private void renderToString(Table table) throws SQLException {
		appendf("       @Override\n");
		appendf("	public String toString() {\n");
		appendf("		return \"%s [\"\n",
				configuration.generateAbstractClassName(table.getName()));
		for (Column column : table.getColumns()) {
			appendf("			+ \" %s=\" + %s\n", column.getName(), column.getName());
		}
		appendf("		+ \"]\";\n");
		appendf("	}\n");
	}

	private void renderHasManySearchChildren(ExportedKey e) {
		appendf("	public Stream<%s> %s(Connection connection) throws SQLException, HanaException {\n",
				configuration.generateConcreteFullClassName(e
						.getForeignKeyTable()),
				configuration.generateSearchChildrenMethodName(e
						.getForeignKeyTable()));
		appendf("		return Select.from(%s.class).where(me.geso.hana.Condition.eq(\"%s\", this.%s)).stream(connection);\n",
				configuration.generateConcreteFullClassName(e
						.getForeignKeyTable()),
				e.getForeignKeyColumn(),
				e.getPrimaryKeyColumn());
		appendf("	}\n");
		appendf("	\n");
	}

	private void renderHasManyCount(ExportedKey e) {
		String concreteForeignClass = configuration
				.generateConcreteFullClassName(e
						.getForeignKeyTable());
		appendf("	/**\n");
		appendf("	 * Seach %s related on %s.\n", e.getForeignKeyTable(),
				e.getPrimaryKeyTable());
		appendf("	 * @param connection\n");
		appendf("	 * @return\n");
		appendf("	 * @throws java.sql.SQLException\n");
		appendf("	 * @throws me.geso.hana.HanaException\n");
		appendf("	 */\n");
		appendf("	public long %s(Connection connection) throws SQLException, HanaException {\n",
				configuration.generateCountMethodName(e.getForeignKeyTable()));
		appendf("		return Select.from(%s.class).where(me.geso.hana.Condition.eq(\"%s\", this.%s)).count(connection);\n",
				concreteForeignClass, e.getForeignKeyColumn(),
				e.getPrimaryKeyColumn());
		appendf("	}\n");
		appendf("	\n");
	}

	private void renderHasARetrieve(ImportedKey e) {
		appendf("	public Optional<%s> %s(Connection connection) throws SQLException, HanaException {\n",
				configuration.generateConcreteFullClassName(e
						.getPrimaryKeyTable()),
				configuration
				.generateRetrieveMethodName(e.getPrimaryKeyTable()));
		appendf("		return Select.from(%s.class).where(me.geso.hana.Condition.eq(\"%s\", this.%s)).stream(connection).findFirst();\n",
				configuration.generateConcreteFullClassName(e
						.getPrimaryKeyTable()),
				e.getPrimaryKeyColumn(),
				e.getForeignKeyColumn());
		appendf("	}\n");
		appendf("	\n");
	}

	private void renderCount(Table table) throws SQLException {
		appendf("	public static long count(Connection connection) throws SQLException, HanaException {\n");
		appendf("		return Select.from(%s.class).count(connection)\n;",
				configuration.generateAbstractClassName(table.getName()));
		appendf("	}\n");
	}

	private void renderCriteria(Table table) throws SQLException {
		List<PrimaryKey> primaryKeys = table.getPrimaryKeys();
		appendf("	@Override\n");
		appendf("	public Criteria criteria() throws SQLException, HanaException {\n");
		if (primaryKeys.isEmpty()) {
			appendf("		throw new me.geso.hana.HanaNoPrimaryKeyException(\"%s doesn't have a primary key\");",
					table.getName());
		} else {
			primaryKeys.stream().map(e -> e.getColumnName()).forEach(columnName -> {
				appendf("		if (!this.%s) {\n", configuration.generateSelectedFlagName(columnName));
				appendf("				throw new HanaException(\"The row doesn't contain *selected* primary key: %s\");\n",
						columnName);
				appendf("		}\n");
			});
			appendf("\n");
			appendf("		Criteria criteria = null;\n");
			primaryKeys.stream().map(e -> e.getColumnName()).forEach(column -> {
				appendf("		criteria = me.geso.hana.Condition.and(criteria, me.geso.hana.Condition.eq(\"%s\", this.%s()));\n",
						column, configuration.generateGetterName(column));
			});
			appendf("		return criteria;\n");
		}
		appendf("	}\n");
	}

	private void renderRefetch(Table table) throws SQLException {
		String concreteClass = configuration
				.generateConcreteFullClassName(table.getName());
		appendf("	public Optional<%s> refetch(Connection connection) throws SQLException, HanaException {\n",
				concreteClass);
		appendf("		return %s.find(connection,",
				configuration.generateAbstractClassName(table.getName()));
		List<PrimaryKey> primaryKeys = table.getPrimaryKeys();
		for (int i = 0; i < primaryKeys.size(); ++i) {
			appendf("%s", primaryKeys.get(i).getColumnName());
			if (i != primaryKeys.size() - 1) {
				appendf(", ");
			}
		}
		append(");\n");
		append("	}\n");
		append("\n");
	}

	private void renderFind(Table table) throws SQLException {
		String concreteClass = configuration
				.generateConcreteFullClassName(table.getName());
		appendf("	public static Optional<%s> find(Connection connection, ", concreteClass);
		List<PrimaryKey> primaryKeys = table.getPrimaryKeys();
		for (int i = 0; i < primaryKeys.size(); ++i) {
			PrimaryKey pk = primaryKeys.get(i);
			appendf("%s %s", configuration.getJavaTypeName(pk.getDataType()),
					pk.getColumnName());
			if (i != primaryKeys.size() - 1) {
				appendf(", ");
			}
		}
		append(") throws SQLException, HanaException {\n");
		appendf("		return Select.from(%s.class)\n", concreteClass);
		for (PrimaryKey pk : table.getPrimaryKeys()) {
			appendf("		.where(me.geso.hana.Condition.eq(\"%s\", %s))\n",
					pk.getColumnName(),
					pk.getColumnName());
		}
		append("		.stream(connection).findFirst();\n");
		append("	}\n");
		append("\n");
	}

	public void renderInitialize(Table table) throws SQLException {
		append("	@Override\n");
		append("	public void initialize(ResultSet rs) throws SQLException {\n");
		append("		ResultSetMetaData meta = rs.getMetaData();\n");
		append("		int columnCount = meta.getColumnCount();\n");
		append("		for (int i=1; i<columnCount+1; ++i) {\n");
		append("			String label = meta.getColumnLabel(i);\n");
		append("			switch (label) {\n");
		table.getColumns()
				.stream()
				.forEach(
						column -> {
							appendf("			case \"%s\":\n", column.getName());
							appendf("				this.%s = %s;\n",
									column.getName(),
									configuration.getResultSetMethod(column
											.getDataType()));
							appendf("				this.%s = true;\n",
									configuration.generateSelectedFlagName(column.getName())
							);
							appendf("				break;\n");
						});
		append("			} // switch\n");
		append("		} // for\n");
		append("	}\n");
	}

	private void renderColumn(String klass, Table table, Column column) {
		String concreteClass = configuration
				.generateConcreteFullClassName(table.getName());

		String javaType = configuration.getJavaTypeName(column.getDataType());
		appendf("	// Column: %s %s(%s)\n",
				column.getName(), column.getTypeName(),
				column.getSize());
		// field
		appendf("	private %s %s",
				javaType, column.getName());
		String defaultValue = getDefaultValue(column);
		if (defaultValue != null) {
			appendf("=%s", defaultValue);
		}
		append(";\n\n");
		// dirty flag
		appendf("	private boolean %s;",
				configuration.generateDirtyFlagName(column.getName()));
		// selected flag
		appendf("	private boolean %s;",
				configuration.generateSelectedFlagName(column.getName()));

		// getter
		appendf("	public %s %s() {\n",
				javaType, configuration.generateGetterName(column.getName()));
		appendf("		return this.%s;\n",
				column.getName());
		append("	}\n\n");

		// setter
		appendf("	public %s %s(%s value) {\n",
				concreteClass,
				configuration.generateSetterName(column.getName()), javaType);
		appendf("		this.%s = value;\n",
				column.getName());
		appendf("		%s = true;\n", configuration.generateDirtyFlagName(column.getName()));
		appendf("		return (%s)this;\n", concreteClass);
		append("	}\n");
		append("\n");

		this.columnHooks.stream().forEach(hook -> {
			hook.run(this, column);
		});
	}

	private String getDefaultValue(Column column) {
		if (column.getName().equals("created_on")) {
			return "Instant.now().getEpochSecond()";
		} else {
			return null;
		}
	}

	public void addColumnHook(ColumnHook hook) {
		this.columnHooks.add(hook);
	}

	public void addBeforeInsertHook(TableHook hook) {
		this.beforeInsertHooks.add(hook);
	}

	public void addBeforeUpdateHook(TableHook hook) {
		this.beforeUpdateHooks.add(hook);
	}

	private void renderSetUpdate(Table table) throws SQLException {
		appendf("	@Override\n");
		appendf("	protected void setUpdateParameters(me.geso.hana.Update update) throws HanaException, SQLException {\n");
		table.getColumns().stream().map(column -> column.getName()).forEach(columnName -> {
			appendf("		if (%s) {\n", configuration.generateDirtyFlagName(columnName));
			appendf("			update.set(\"%s\", this.%s());\n",
					columnName,
					configuration.generateGetterName(columnName));
			appendf("		}\n");
		});
		appendf("	}\n");
	}

	@FunctionalInterface
	public static interface ColumnHook {

		public void run(AbstractRowClassGenerator generator, Column column);
	}

	@FunctionalInterface
	public static interface TableHook {

		public void run(Table table);
	}

	// getTableName
	private void renderGetTableName(Table table) {
		appendf("	public String getTableName() { return \"%s\"; }\n",
				table.getName());
	}

	// getPrimaryKeys()
	private void renderGetPrimaryKeys(Table table) throws SQLException {
		appendf("	private static final List<String> primaryKeys = Arrays.asList(");
		List<PrimaryKey> primaryKeys = table.getPrimaryKeys();
		for (int i = 0; i < primaryKeys.size(); ++i) {
			appendf("\"%s\"", primaryKeys.get(i).getColumnName());
			if (i != primaryKeys.size() - 1) {
				append(",");
			}
		}
		appendf(");\n");
		appendf("	@Override\n");
		appendf("	public List<String> getPrimaryKeys() {\n");
		appendf("		return primaryKeys;\n");
		append("	}\n\n");
	}

	private void renderInsert(Table table) throws SQLException {
		appendf("	public %s insert(Connection connection) throws SQLException, HanaException {\n",
				configuration.generateConcreteFullClassName(table.getName()));
		this.beforeInsertHooks.stream().forEach(code -> {
			code.run(table);
		});
		append("		Insert insert = Insert.into(this.getTableName());\n");
		table.getColumns().stream().map(e -> e.getName()).forEach(columnName -> {
			appendf("		if (%s) {\n", configuration.generateDirtyFlagName(columnName));
			appendf("			insert.value(\"%s\", this.%s());\n",
					columnName,
					configuration.generateGetterName(columnName));
			appendf("		}\n");
		});

		// fill auto increment value to row object.
		Optional<Column> ai = table.getColumns().stream().filter(column -> {
			return column.isAutoIncrement();
		}).findFirst();
		if (ai.isPresent()) {
			appendf("		PreparedStatement stmt = insert.build(connection).prepare(connection);\n");
			appendf("		stmt.execute();\n");
			appendf("		try (ResultSet rs = stmt.getGeneratedKeys();) {\n");
			appendf("			if (rs.next()) {\n");
			// TODO We should add test case for 64bit int.
			appendf("				this.%s(rs.getLong(1));\n", configuration.generateSetterName(ai.get().getName()));
			appendf("				this.%s = true;\n", configuration.generateSelectedFlagName(ai.get().getName()));
			appendf("			}\n");
			appendf("		}\n");
		} else {
			appendf("		PreparedStatement stmt = insert.build(connection).prepare(connection);\n");
			appendf("		stmt.executeUpdate();\n");
		}
		appendf("		return (%s)this;\n",
				configuration.generateConcreteFullClassName(table.getName()));
		append("	}\n\n");
	}

}
