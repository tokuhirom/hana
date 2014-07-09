package me.geso.hana.typesafe.db.abstractrow;
import java.util.List;
import java.util.Arrays;
import me.geso.hana.typesafe.StringColumn;
import me.geso.hana.typesafe.LongColumn;
import me.geso.hana.typesafe.BlobColumn;
import me.geso.hana.typesafe.AbstractTypeSafeRow;
import me.geso.hana.typesafe.db.row.Blog;
public class AbstractBlog extends AbstractTypeSafeRow {
	private static final String tableName = "blog";
	@Override
	public String getTableName() {
		return tableName;
	}

	private static final List<String> primaryKeys = Arrays.asList("id");
	@Override
	public List<String> getPrimaryKeys() {
		return primaryKeys;
	}

	public LongColumn id=new LongColumn("blog", "id");
	public StringColumn title=new StringColumn("blog", "title");
	public StringColumn url=new StringColumn("blog", "url");
	public LongColumn member_id=new LongColumn("blog", "member_id");
	public LongColumn created_on=new LongColumn("blog", "created_on");
	public LongColumn updated_on=new LongColumn("blog", "updated_on");
	public String id() {
		return this.getColumn("id");
	}
	public String title() {
		return this.getColumn("title");
	}
	public String url() {
		return this.getColumn("url");
	}
	public String member_id() {
		return this.getColumn("member_id");
	}
	public String created_on() {
		return this.getColumn("created_on");
	}
	public String updated_on() {
		return this.getColumn("updated_on");
	}
	public Blog setId(String value) {
		this.setColumn("id", value);
		return (Blog)this;
	}
	public Blog setTitle(String value) {
		this.setColumn("title", value);
		return (Blog)this;
	}
	public Blog setUrl(String value) {
		this.setColumn("url", value);
		return (Blog)this;
	}
	public Blog setMemberId(String value) {
		this.setColumn("member_id", value);
		return (Blog)this;
	}
	public Blog setCreatedOn(String value) {
		this.setColumn("created_on", value);
		return (Blog)this;
	}
	public Blog setUpdatedOn(String value) {
		this.setColumn("updated_on", value);
		return (Blog)this;
	}
}
