package me.geso.hana.typesafe.db.abstractrow;
import java.util.List;
import java.util.Arrays;
import me.geso.hana.typesafe.StringColumn;
import me.geso.hana.typesafe.LongColumn;
import me.geso.hana.typesafe.BlobColumn;
import me.geso.hana.typesafe.AbstractTypeSafeRow;
import me.geso.hana.typesafe.db.row.Comment;
public class AbstractComment extends AbstractTypeSafeRow {
	private static final String tableName = "comment";
	@Override
	public String getTableName() {
		return tableName;
	}

	private static final List<String> primaryKeys = Arrays.asList("id");
	@Override
	public List<String> getPrimaryKeys() {
		return primaryKeys;
	}

	public LongColumn id=new LongColumn("comment", "id");
	public LongColumn entry_id=new LongColumn("comment", "entry_id");
	public StringColumn body=new StringColumn("comment", "body");
	public BlobColumn data=new BlobColumn("comment", "data");
	public LongColumn created_on=new LongColumn("comment", "created_on");
	public String id() {
		return this.getColumn("id");
	}
	public String entry_id() {
		return this.getColumn("entry_id");
	}
	public String body() {
		return this.getColumn("body");
	}
	public String data() {
		return this.getColumn("data");
	}
	public String created_on() {
		return this.getColumn("created_on");
	}
	public Comment setId(String value) {
		this.setColumn("id", value);
		return (Comment)this;
	}
	public Comment setEntryId(String value) {
		this.setColumn("entry_id", value);
		return (Comment)this;
	}
	public Comment setBody(String value) {
		this.setColumn("body", value);
		return (Comment)this;
	}
	public Comment setData(String value) {
		this.setColumn("data", value);
		return (Comment)this;
	}
	public Comment setCreatedOn(String value) {
		this.setColumn("created_on", value);
		return (Comment)this;
	}
}
