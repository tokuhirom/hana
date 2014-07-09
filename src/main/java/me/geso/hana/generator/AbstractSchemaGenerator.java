package me.geso.hana.generator;

import com.google.common.io.Files;
import java.io.IOException;
import java.nio.file.Path;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import me.geso.dbinspector.Inspector;
import me.geso.dbinspector.Table;

public abstract class AbstractSchemaGenerator {

    static final Logger logger = Logger.getLogger(AbstractSchemaGenerator.class.toString());

    private final Inspector inspector;

    private final Configuration configuration;

    public Configuration getConfiguration() {
	return this.configuration;
    }

    public AbstractSchemaGenerator(Inspector inspector, Configuration configuration) {
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
	    logger.log(Level.INFO, "{0} is already exists. Skipping...", path.toString());
	} else {
	    logger.log(Level.INFO, "Rendering {0}", path.toString());
	    String code = this.renderConcreteRow(table);
	    path.getParent().toFile().mkdirs();
	    Files.write(code.getBytes(), path.toFile());
	}
    }


    private void generateAbstractRow(Table table) throws SQLException,
	    IOException {
	Path path = configuration.outputAbstractRowFilePath(table.getName());
	logger.log(Level.INFO, "Rendering {0}", path.toString());
	String code = this.renderAbstractRow(table);
	path.getParent().toFile().mkdirs();
	Files.write(code.getBytes(), path.toFile());
    }

    public abstract String renderAbstractRow(Table table) throws SQLException;

    public abstract String renderConcreteRow(Table table) throws SQLException;

}
