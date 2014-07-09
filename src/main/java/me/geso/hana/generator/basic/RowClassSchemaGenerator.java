/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package me.geso.hana.generator.basic;

import java.sql.SQLException;
import me.geso.dbinspector.Inspector;
import me.geso.dbinspector.Table;
import me.geso.hana.generator.AbstractSchemaGenerator;
import me.geso.hana.generator.Configuration;

/**
 *
 * @author tokuhirom
 */
public class RowClassSchemaGenerator extends AbstractSchemaGenerator {

    public RowClassSchemaGenerator(Inspector inspector, Configuration configuration) {
	super(inspector, configuration);
    }

    @Override
    public String renderConcreteRow(Table table) throws SQLException {
	return new ConcreteRowClassGenerator(table, this.getConfiguration()).render();
    }

    @Override
    public String renderAbstractRow(Table table) throws SQLException {
	return new AbstractRowClassGenerator(table, this.getConfiguration()).render();
    }

}
