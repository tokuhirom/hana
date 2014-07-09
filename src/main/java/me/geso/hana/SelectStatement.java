package me.geso.hana;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Spliterators;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import me.geso.hana.annotation.Table;

public class SelectStatement<T extends AbstractRow> extends AbstractStatement {

    private final String table;
    private String orderBy = null;
    private final Class<T> klass;
    private final List<String> where = new ArrayList<>();
    private final List<String> binds = new ArrayList<>();

    public SelectStatement(HanaSession hanaSession, Class<T> klass) {
        super(hanaSession);
        this.table = this.getTableName(klass);
        this.klass = klass;
    }

    @SuppressWarnings("unchecked")
    private String getTableName(Class<T> klass) {
        Class<T> k = klass;
        while (k != null) {
            Table annotation = k.getAnnotation(Table.class);
            if (annotation != null) {
                return annotation.name();
            }
            k = (Class<T>) k.getSuperclass();
        }
        throw new IllegalArgumentException("" + klass
                + " does not annoted by @Table");
    }

    public SelectStatement<T> orderBy(String orderBy) {
        this.orderBy = orderBy;
        return this;
    }

    public SelectStatement<T> where(String string, long id) {
        this.where.add(string);
        this.binds.add("" + id);
        return this;
    }

    public SelectStatement<T> where(String string, String value) {
        this.where.add(string);
        this.binds.add(value);
        return this;
    }

    public List<T> getResultList() throws SQLException, HanaException {
        return this.stream().collect(Collectors.toList());
    }

    public Stream<T> stream() throws SQLException {
        final String sql = buildQuery();
        PreparedStatement statement = this.prepare(sql);
        ResultSet rs = statement.executeQuery();
        HanaIterator<T> iterator = new HanaIterator<>(statement, rs, klass);
        return StreamSupport.stream(
                Spliterators.spliteratorUnknownSize(iterator, 0), false);
    }

    private PreparedStatement prepare(String sql) throws SQLException {
        PreparedStatement statement = this.session.getConnection()
                .prepareStatement(sql);
        for (int i = 0; i < this.binds.size(); ++i) {
            statement.setString(i + 1, this.binds.get(i));
        }
        return statement;
    }

    private String buildQuery() {
        StringBuilder buf = new StringBuilder();
        buf.append("SELECT * FROM ");
        buf.append(quote(table));
        if (where.size() > 0) {
            buf.append(" WHERE ");
            for (int i = 0; i < where.size(); ++i) {
                buf.append("(");
                buf.append(where.get(i));
                buf.append(")");
                if (i != where.size() - 1) {
                    buf.append(" AND ");
                }
            }
        }
        if (orderBy != null) {
            buf.append(" ORDER BY ");
            buf.append(orderBy);
        }
        return new String(buf);
    }

    public Optional<T> first() throws HanaException, SQLException {
        List<T> list = this.getResultList();
        if (list.size() > 0) {
            return Optional.of(list.get(0));
        } else {
            return Optional.empty();
        }
    }

    public long count() throws SQLException, HanaException {
        final String sql = "SELECT COUNT(*) FROM (" + this.buildQuery() + ")";
        PreparedStatement statement = this.prepare(sql);
		session.logStatement(statement);
        ResultSet rs = statement.executeQuery();
        if (rs.next()) {
            return rs.getLong(1);
        } else {
            throw new HanaException("Cannot get count by : " + sql);
        }
    }

}
