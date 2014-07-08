package me.geso.hana;

import java.io.Closeable;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Iterator;

public class HanaIterator<T extends AbstractRow> implements Iterator<T>, Closeable {

	private final ResultSet rs;
	private final PreparedStatement ps;
	private final Class<? extends T> entityClass;

	public HanaIterator(PreparedStatement ps, ResultSet rs,
			Class<? extends T> entityClass) {
		this.rs = rs;
		this.ps = ps;
		this.entityClass = entityClass;
	}

	@Override
	public boolean hasNext() {
		try {
			boolean hasMore = rs.next();
			if (!hasMore) {
				close();
			}
			return hasMore;
		} catch (SQLException e) {
			this.close();
			throw new RuntimeException(e);
		}
	}

	@Override
	public T next() {
		try {
			T e = entityClass.newInstance();
			e.initialize(rs);
			return e;
		} catch (InstantiationException | IllegalAccessException | SecurityException | IllegalArgumentException | SQLException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public void close() {
		try {
			rs.close();
			try {
				ps.close();
			} catch (SQLException e) {
				// nothing we can do here
			}
		} catch (SQLException e) {
			// nothing we can do here
		}
	}

}
