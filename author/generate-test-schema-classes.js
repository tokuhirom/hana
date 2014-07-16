var Inspector               = Java.type('me.geso.dbinspector.Inspector'),
    RowClassSchemaGenerator = Java.type('me.geso.hana.generator.basic.RowClassSchemaGenerator'),
    Configuration           = Java.type('me.geso.hana.generator.Configuration'),
    DriverManager           = Java.type('java.sql.DriverManager'),
    Class                   = Java.type('java.lang.Class');

// mvn exec:java -Dexec.mainClass="jdk.nashorn.tools.Shell" -Dexec.args="author/generate-test-schema-classes.js"  -Dexec.classpathScope="test"

Class.forName("org.h2.Driver");

var conn = DriverManager
        .getConnection("jdbc:h2:mem:test;DATABASE_TO_UPPER=FALSE;TRACE_LEVEL_SYSTEM_OUT=1");
// TODO: Use file resource.
conn.prepareStatement("DROP TABLE IF EXISTS member").executeUpdate();
conn.prepareStatement("DROP TABLE IF EXISTS follow").executeUpdate();
conn.prepareStatement("DROP TABLE IF EXISTS multi_pk_sample").executeUpdate();
conn.prepareStatement(
        "CREATE TABLE member (id BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY, email VARCHAR(255), created_on INTEGER UNSIGNED, updated_on INTEGER UNSIGNED)")
        .execute();
conn.prepareStatement(
        "CREATE TABLE IF NOT EXISTS blog (id INTEGER AUTO_INCREMENT PRIMARY KEY, title VARCHAR(255), url VARCHAR(65535), member_id INTEGER UNSIGNED, created_on INTEGER UNSIGNED, updated_on INTEGER UNSIGNED, FOREIGN KEY(member_id) REFERENCES member(id) ON DELETE CASCADE)")
        .execute();
conn.prepareStatement(
        "CREATE TABLE IF NOT EXISTS entry (id INTEGER AUTO_INCREMENT PRIMARY KEY, blog_id INT UNSIGNED NOT NULL, title VARCHAR(255), created_on INTEGER UNSIGNED NOT NULL, updated_on INTEGER UNSIGNED, FOREIGN KEY(blog_id) REFERENCES blog(id) ON DELETE CASCADE)")
        .execute();
conn.prepareStatement(
        "CREATE TABLE IF NOT EXISTS comment (id INTEGER AUTO_INCREMENT PRIMARY KEY, entry_id INT UNSIGNED NOT NULL, body VARCHAR(255) DEFAULT NULL, data LONGBLOB, created_on INTEGER UNSIGNED, FOREIGN KEY(entry_id) REFERENCES entry(id) ON DELETE CASCADE)")
        .execute();
conn.prepareStatement(
        "CREATE TABLE IF NOT EXISTS multi_pk_sample ("
        + "id1 INTEGER UNSIGNED NOT NULL,"
        + "id2 INTEGER UNSIGNED NOT NULL,"
        + "title VARCHAR(255) NOT NULL,"
        + "email VARCHAR(255) NOT NULL,"
        + " PRIMARY KEY (id1, id2)"
        + ")")
        .execute();
conn.prepareStatement(
        "CREATE TABLE IF NOT EXISTS follow ("
        + "from_member_id INTEGER UNSIGNED NOT NULL,"
        + " to_member_id INTEGER UNSIGNED NOT NULL,"
        + " created_on INTEGER UNSIGNED,"
        + " FOREIGN KEY(from_member_id) REFERENCES member(id) ON DELETE CASCADE,"
        + "  FOREIGN KEY(to_member_id) REFERENCES member(id) ON DELETE CASCADE"
        // + " UNIQUE (from_member_id, to_member_id)"
        // + " INDEX (to_member_id)"
        + ")")
        .execute();
conn.prepareStatement(
        "CREATE UNIQUE INDEX ON follow (from_member_id, to_member_id)")
        .execute();
conn.prepareStatement("DELETE FROM member").executeUpdate();

var inspector = new Inspector(conn);
var configuration = new Configuration("me.geso.hana", "src/test/java");
var generator = new RowClassSchemaGenerator(inspector,
        configuration);
generator.generateAll();
