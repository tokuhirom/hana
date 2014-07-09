/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package me.geso.hana.typesafe;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import me.geso.hana.HanaSession;
import me.geso.hana.InsertStatement;
import me.geso.hana.UpdateStatement;

/**
 *
 * @author tokuhirom
 */
abstract public class AbstractTypeSafeRow {

    private final Map<String, String> columns = new HashMap<>();
    private final Set<String> dirtyColumns = new TreeSet<>();

    public String getColumn(String key) throws MissingColumnError {
        if (columns.containsKey(key)) {
            throw new MissingColumnError(key);
        }
        return columns.get(key);
    }

    public void setColumn(String key, String value) {
        this.dirtyColumns.add(key);
        this.columns.put(key, value);
    }

    public void update(HanaSession session) throws MissingColumnError, SQLException, CannotUpdateException {
        if (this.dirtyColumns.isEmpty()) {
            // No dirty columns... I don't need to send UPDATE query.
            return;
        }

        UpdateStatement update = new UpdateStatement(session, this.getTableName());
        for (String pk : this.getPrimaryKeys()) {
            String value = this.getColumn(pk);
            update.where(pk, value);
        }
        for (String column: this.dirtyColumns) {
            update.set(column, this.getColumn(column));
        }
        PreparedStatement stmt = update.prepare(session);
        session.logStatement(stmt);
        int updated = stmt.executeUpdate();
        if (updated != 1) {
            throw new CannotUpdateException(stmt);
        }
    }
    
    public void insert(HanaSession session) throws MissingColumnError, SQLException, CannotUpdateException {
        InsertStatement insert = new InsertStatement(session, this.getTableName());
        for (String column: this.dirtyColumns) {
            insert.value(column, this.getColumn(column));
        }
        PreparedStatement stmt = insert.prepare();
        session.logStatement(stmt);
        int updated = stmt.executeUpdate();
        if (updated != 1) {
            throw new CannotUpdateException(stmt);
        }
    }

    abstract public String getTableName();

    abstract public List<String> getPrimaryKeys();
}
