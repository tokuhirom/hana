package me.geso.hana.typesafe.db.abstractrow;
import java.util.List;
import java.util.Arrays;
import me.geso.hana.typesafe.StringColumn;
import me.geso.hana.typesafe.LongColumn;
import me.geso.hana.typesafe.BlobColumn;
import me.geso.hana.typesafe.AbstractTypeSafeRow;
import me.geso.hana.typesafe.db.row.Entry;
public class AbstractEntry extends AbstractTypeSafeRow {
	private static final String tableName = "entry";
	@Override
	public String getTableName() {
		return tableName;
	}

	private static final List<String> primaryKeys = Arrays.asList("id");
	@Override
	public List<String> getPrimaryKeys() {
		return primaryKeys;
	}

	public LongColumn id=new LongColumn("entry", "id");
	public LongColumn blog_id=new LongColumn("entry", "blog_id");
	public StringColumn title=new StringColumn("entry", "title");
	public LongColumn created_on=new LongColumn("entry", "created_on");
	public LongColumn updated_on=new LongColumn("entry", "updated_on");
	public String id() {
		return this.getColumn("id");
	}
	public String blog_id() {
		return this.getColumn("blog_id");
	}
	public String title() {
		return this.getColumn("title");
	}
	public String created_on() {
		return this.getColumn("created_on");
	}
	public String updated_on() {
		return this.getColumn("updated_on");
	}
	public Entry setId(String value) {
		this.setColumn("id", value);
		return (Entry)this;
	}
	public Entry setBlogId(String value) {
		this.setColumn("blog_id", value);
		return (Entry)this;
	}
	public Entry setTitle(String value) {
		this.setColumn("title", value);
		return (Entry)this;
	}
	public Entry setCreatedOn(String value) {
		this.setColumn("created_on", value);
		return (Entry)this;
	}
	public Entry setUpdatedOn(String value) {
		this.setColumn("updated_on", value);
		return (Entry)this;
	}
}
