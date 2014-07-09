package me.geso.hana.generator;

import java.sql.SQLException;
import me.geso.dbinspector.Table;

public abstract class Renderer {

    private final StringBuilder buf = new StringBuilder();

    @Override
    public String toString() {
	return buf.toString();
    }

    public void appendf(String format, Object... args) {
	buf.append(String.format(format, args));
    }

    public void appendfln(String format, Object... args) {
	buf.append(String.format(format, args)).append("\n");
    }

    public void append(String str) {
	buf.append(str);
    }

    public abstract String render() throws SQLException;
}
