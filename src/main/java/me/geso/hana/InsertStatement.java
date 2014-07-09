package me.geso.hana;

import java.sql.Blob;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.LinkedHashMap;
import java.util.Map;

public class InsertStatement {

    private final HanaSession session;
    private final String table;
    private final Map<String, String> values = new LinkedHashMap<>();

    public InsertStatement(HanaSession session, String table) {
        this.session = session;
        this.table = table;
    }

    public InsertStatement value(String key, String value) {
        this.values.put(key, value);
        return this;
    }

    public InsertStatement value(String key, Blob value) {
        this.values.put(key, value.toString());
        return this;
    }

    public InsertStatement value(String key, long value) {
        this.values.put(key, "" + value);
        return this;
    }

    public PreparedStatement prepare() throws SQLException {
        StringBuilder buf = new StringBuilder();
        buf.append("INSERT INTO ");
        buf.append(session.quoteIdentifier(table));
        buf.append(" (");
        String[] keys = values.keySet().stream().toArray(String[]::new);
        for (int i = 0, l = keys.length; i < l; ++i) {
            buf.append(session.quoteIdentifier(keys[i]));
            if (i != l - 1) {
                buf.append(',');
            }
        }
        buf.append(") VALUES (");
        for (int i = 0, l = keys.length; i < l; ++i) {
            buf.append("?");
            if (i != l - 1) {
                buf.append(',');
            }
        }
        buf.append(")");

        PreparedStatement statement = session.getConnection()
                .prepareStatement(buf.toString());

		// String identifierQuoteString = connection.getMetaData()
        // .getIdentifierQuoteString();
        for (int i = 0, l = keys.length; i < l; ++i) {
            statement.setString(i + 1, values.get(keys[i]));
        }
        return statement;
    }

}
