package me.geso.hana.typesafe.db.abstractrow;
import java.util.List;
import java.util.Arrays;
import me.geso.hana.typesafe.StringColumn;
import me.geso.hana.typesafe.LongColumn;
import me.geso.hana.typesafe.BlobColumn;
import me.geso.hana.typesafe.AbstractTypeSafeRow;
import me.geso.hana.typesafe.db.row.Member;
public class AbstractMember extends AbstractTypeSafeRow {
	private static final String tableName = "member";
	@Override
	public String getTableName() {
		return tableName;
	}

	private static final List<String> primaryKeys = Arrays.asList("id");
	@Override
	public List<String> getPrimaryKeys() {
		return primaryKeys;
	}

	public LongColumn id=new LongColumn("member", "id");
	public StringColumn email=new StringColumn("member", "email");
	public LongColumn created_on=new LongColumn("member", "created_on");
	public LongColumn updated_on=new LongColumn("member", "updated_on");
	public String id() {
		return this.getColumn("id");
	}
	public String email() {
		return this.getColumn("email");
	}
	public String created_on() {
		return this.getColumn("created_on");
	}
	public String updated_on() {
		return this.getColumn("updated_on");
	}
	public Member setId(String value) {
		this.setColumn("id", value);
		return (Member)this;
	}
	public Member setEmail(String value) {
		this.setColumn("email", value);
		return (Member)this;
	}
	public Member setCreatedOn(String value) {
		this.setColumn("created_on", value);
		return (Member)this;
	}
	public Member setUpdatedOn(String value) {
		this.setColumn("updated_on", value);
		return (Member)this;
	}
}
