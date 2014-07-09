package me.geso.hana;

import java.io.PrintStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import lombok.Getter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HanaSession implements AutoCloseable {

    final static Logger logger = LoggerFactory.getLogger(HanaSession.class);

    @Getter
    private final Connection connection;
	private final String identifierQuoteString;

    public HanaSession(HanaSessionFactory hana) throws SQLException {
        this.connection = DriverManager.getConnection(hana.getUrl(),
                hana.getUser(), hana.getPassword());
		this.identifierQuoteString = connection.getMetaData().getIdentifierQuoteString();
    }

    @Override
    public void close() throws SQLException {
        if (this.connection != null) {
            this.connection.close();
        }
    }

    public int doQuery(String query) throws SQLException {
        PreparedStatement statement = this.connection.prepareStatement(query);
        statement.execute();
        return statement.getUpdateCount();
    }

    public <T extends AbstractRow> SelectStatement<T> search(Class<T> klass) {
        return new SelectStatement<>(this, klass);
    }

    public DeleteStatement delete(String tableName) {
        return new DeleteStatement(this, tableName);
    }

    public InsertStatement insert(String table) throws SQLException {
        return new InsertStatement(this, table);
    }

    public void insert(AbstractRow row) throws SQLException, HanaException {
        row.insert(this);
    }

    // TODO: Dump with table generator like Text::SimpleTable
    public void dumpTable(String tableName, PrintStream os) throws SQLException {
        String sql = "SELECT * FROM " + quoteIdentifier(tableName);
        PreparedStatement prepareStatement = this.connection
                .prepareStatement(sql);
        ResultSet rs = prepareStatement.executeQuery();
        int columnCount = rs.getMetaData().getColumnCount();
        // show header
        for (int i = 0; i < columnCount; ++i) {
            String label = rs.getMetaData().getColumnLabel(i + 1);
            os.print(label + " ");
        }
        os.print("\n");
        while (rs.next()) {
            for (int i = 0; i < columnCount; ++i) {
                String value = rs.getString(i + 1);
                os.print(value + " ");
            }
            os.print("\n");
        }
    }

    public static void logStatement(PreparedStatement stmt) {
        logger.info(stmt.toString());
    }

    public String quoteIdentifier(String ident) {
        return Identifier.quote(ident, identifierQuoteString);
    }

    public PreparedStatement prepareStatement(String sql) throws SQLException {
        return this.getConnection().prepareStatement(sql);
    }
}
