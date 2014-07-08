package me.geso.hana.generator;

import java.io.IOException;
import java.nio.file.Path;
import java.sql.SQLException;
import java.util.logging.Logger;

import com.google.common.io.Files;

import me.geso.dbinspector.Inspector;
import me.geso.dbinspector.Table;

public class SchemaGenerator {
	static final Logger logger = Logger.getLogger(SchemaGenerator.class.toString());

	private final Inspector inspector;
	private Configuration configuration;

	public SchemaGenerator(Inspector inspector, Configuration configuration) {
		this.inspector = inspector;
		this.configuration = configuration;
	}

	public void generateAll() throws SQLException, IOException {
		for (Table table : inspector.getTables()) {
			generateAbstractRow(table);
			generateConcreteRow(table);
		}
	}

	private void generateConcreteRow(Table table) throws SQLException,
			IOException {
		Path path = configuration.outputConcreteRowFilePath(table.getName());
		if (path.toFile().canRead()) {
			logger.info(path.toString() + " is already exists. Skipping...");
		} else {
			logger.info("Rendering " + path.toString());
			String code = this.renderConcreteRow(table);
			path.getParent().toFile().mkdirs();
			Files.write(code.getBytes(), path.toFile());
		}
	}

	private String renderConcreteRow(Table table) {
		return new ConcreteRowClassGenerator(table, configuration).render();
	}

	private void generateAbstractRow(Table table) throws SQLException,
			IOException {
		Path path = configuration.outputAbstractRowFilePath(table.getName());
		logger.info("Rendering " + path.toString());
		String code = this.renderAbstractRow(table);
		path.getParent().toFile().mkdirs();
		Files.write(code.getBytes(), path.toFile());
	}

	public String renderAbstractRow(Table table) throws SQLException {
		return new AbstractRowClassGenerator(table, configuration).render();
	}

}
