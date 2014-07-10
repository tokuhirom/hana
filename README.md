hana
====

Lightweight O/R Mapper for Java 8.

UNDER DEVELOPMENT.

## FAQ

### How do I log query with hana?

You can use log4jdbc.

You can get the jar from maven central repository:
http://search.maven.org/#artifactdetails%7Ccom.googlecode.log4jdbc%7Clog4jdbc%7C1.2%7Cjar

In your code, you can wrap the connection by ConectionSpy class. It prints the queries by log4jdbc.

    conn = new net.sf.log4jdbc.ConnectionSpy(conn);

Or just put `:log4jdbc:` in JDBC dsn:

    "jdbc:log4jdbc:h2:mem:test;DATABASE_TO_UPPER=FALSE"


