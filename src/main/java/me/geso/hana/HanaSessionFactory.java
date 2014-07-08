package me.geso.hana;

import java.sql.SQLException;

import lombok.Getter;

public class HanaSessionFactory {

    @Getter
    private final String url;
    @Getter
    private final String user;
    @Getter
    private final String password;

    private final ThreadLocal<HanaSession> currentSession = new ThreadLocal<>();

    public HanaSessionFactory(String url, String user, String password) {
        this.url = url;
        this.user = user;
        this.password = password;
    }

    public HanaSession createSession() throws SQLException {
        HanaSession session = new HanaSession(this);
        currentSession.set(session);
        return session;
    }

    public HanaSession findOrCreateSession() throws SQLException {
        synchronized (currentSession) {
            if (currentSession.get() == null) {
                this.createSession();
            }
            return currentSession.get();
        }
    }

    public HanaSession getCurrentSession() {
        return currentSession.get();
    }
}
