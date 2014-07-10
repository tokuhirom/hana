/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package me.geso.hana;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import lombok.NonNull;

/**
 *
 * @author tokuhirom
 * @param <Value>
 */
public class Condition<Value> {

	public static ConditionInterface eq(String column, Object value) {
		return new EqualsCondition(column, value);
	}

	public static ConditionInterface ne(String column, Object value) {
		return new NotEqualsCondition(column, value);
	}

	/**
	 * Condition: `column > value`
	 *
	 * @param <Value>
	 * @param column
	 * @param value
	 * @return
	 */
	public static <Value> ConditionInterface gt(String column, @NonNull Value value) {
		return new GraterThanCondition(column, value);
	}

	/**
	 * Condition: `column < value`
	 *
	 * @param <Value>
	 * @param column
	 * @param value
	 * @return
	 */
	public static <Value> ConditionInterface lt(String column, @NonNull Value value) {
		return new LessThanCondition(column, value);
	}

	/**
	 * >=
	 *
	 * @param <Value>
	 * @param column
	 * @param value
	 * @return
	 */
	public static <Value> ConditionInterface ge(String column, @NonNull Value value) {
		return new GraterThanOrEqualsCondition(column, value);
	}

	/**
	 * <=
	 *
	 * @param <Value>
	 * @param column
	 * @param value
	 * @return
	 */
	public static <Value> ConditionInterface le(String column, @NonNull Value value) {
		return new LessThanOrEqualsCondition(column, value);
	}

	/**
	 * <code>
	 *	.where(in("a", Arrays.asList(1,2,3)));
	 * </code>
	 *
	 * @param <Value>
	 * @param column
	 * @param values
	 * @return
	 */
	public static <Value> ConditionInterface in(String column, @NonNull List<Object> values) {
		return new InCondition(column, values);
	}

	public static <Value> ConditionInterface not_in(String column, @NonNull List<Object> values) {
		return new NotInCondition(column, values);
	}

	public static ConditionInterface like(String column, String value) {
		return new LikeCondition(column, value);
	}

	abstract static class ListBinaryCondition implements ConditionInterface {

		protected String column;
		protected List<Object> values;

		public ListBinaryCondition(@NonNull String column, @NonNull List<Object> values) {
			this.column = column;
			this.values = values;
		}

		public List<Object> getParams() {
			return values;
		}
	}

	static class InCondition extends ListBinaryCondition {

		public InCondition(String column, List<Object> values) {
			super(column, values);
		}

		@Override
		public String getTerm(String identifierQuoteString) {
			if (values.isEmpty()) {
				return "1=0";
			} else {
				String placeholder = values
						.stream().map(e -> "?")
						.collect(Collectors.joining(","));
				return Identifier.quote(column, identifierQuoteString) + " IN (" + placeholder + ")";
			}

		}
	}

	static class NotInCondition extends ListBinaryCondition {

		public NotInCondition(String column, List<Object> values) {
			super(column, values);
		}

		@Override
		public String getTerm(String identifierQuoteString) {
			if (values.isEmpty()) {
				return "1=1";
			} else {
				String placeholder = values
						.stream().map(e -> "?")
						.collect(Collectors.joining(","));
				return Identifier.quote(column, identifierQuoteString) + " NOT IN (" + placeholder + ")";
			}

		}
	}

	abstract static class BinaryCondition implements ConditionInterface {

		protected String lhs;
		protected Object rhs;

		public BinaryCondition(@NonNull String lhs, Object rhs) {
			this.lhs = lhs;
			this.rhs = rhs;
		}

		abstract public String getTerm(String identifierQuoteString);

		public List<Object> getParams() {
			if (rhs == null) {
				return Arrays.asList();
			} else {
				return Arrays.asList(rhs);
			}
		}
	}

	static class EqualsCondition extends BinaryCondition implements ConditionInterface {

		public EqualsCondition(String lhs, Object rhs) {
			super(lhs, rhs);
		}

		@Override
		public String getTerm(String identifierQuoteString) {
			if (rhs == null) {
				return Identifier.quote(lhs, identifierQuoteString) + " IS NULL";
			} else {
				return Identifier.quote(lhs, identifierQuoteString) + "=?";
			}
		}
	}

	static class NotEqualsCondition extends BinaryCondition implements ConditionInterface {

		public NotEqualsCondition(String lhs, Object rhs) {
			super(lhs, rhs);
		}

		@Override
		public String getTerm(String identifierQuoteString) {
			if (rhs == null) {
				return Identifier.quote(lhs, identifierQuoteString) + " IS NOT NULL";
			} else {
				return Identifier.quote(lhs, identifierQuoteString) + "!=?";
			}
		}
	}

	static class GraterThanCondition extends BinaryCondition implements ConditionInterface {

		public GraterThanCondition(String lhs, @NonNull Object rhs) {
			super(lhs, rhs);
		}

		@Override
		public String getTerm(String identifierQuoteString) {
			return Identifier.quote(lhs, identifierQuoteString) + ">?";
		}
	}

	static class LessThanCondition extends BinaryCondition implements ConditionInterface {

		public LessThanCondition(String lhs, @NonNull Object rhs) {
			super(lhs, rhs);
		}

		@Override
		public String getTerm(String identifierQuoteString) {
			return Identifier.quote(lhs, identifierQuoteString) + "<?";
		}
	}

	static class GraterThanOrEqualsCondition extends BinaryCondition implements ConditionInterface {

		public GraterThanOrEqualsCondition(String lhs, @NonNull Object rhs) {
			super(lhs, rhs);
		}

		@Override
		public String getTerm(String identifierQuoteString) {
			return Identifier.quote(lhs, identifierQuoteString) + ">=?";
		}
	}

	static class LessThanOrEqualsCondition extends BinaryCondition implements ConditionInterface {

		public LessThanOrEqualsCondition(String lhs, @NonNull Object rhs) {
			super(lhs, rhs);
		}

		@Override
		public String getTerm(String identifierQuoteString) {
			return Identifier.quote(lhs, identifierQuoteString) + "<=?";
		}
	}

	static class LikeCondition extends BinaryCondition implements ConditionInterface {

		public LikeCondition(String lhs, @NonNull Object rhs) {
			super(lhs, rhs);
		}

		@Override
		public String getTerm(String identifierQuoteString) {
			return Identifier.quote(lhs, identifierQuoteString) + " LIKE ?";
		}
	}

	static class OrCondition implements ConditionInterface {

		private ConditionInterface a;
		private ConditionInterface b;

		public OrCondition(@NonNull ConditionInterface a, @NonNull ConditionInterface b) {
			this.a = a;
			this.b = b;
		}

		@Override
		public String getTerm(String identifierQuoteString) {
			return "( " + this.a.getTerm(identifierQuoteString) + " ) OR ( "
					+ this.b.getTerm(identifierQuoteString) + " )";
		}

		@Override
		public List<Object> getParams() {
			ArrayList<Object> l = new ArrayList();
			l.addAll(a.getParams());
			l.addAll(b.getParams());
			return l;
		}
	}

	static class AndCondition implements ConditionInterface {

		private ConditionInterface a;
		private ConditionInterface b;

		public AndCondition(@NonNull ConditionInterface a, @NonNull ConditionInterface b) {
			this.a = a;
			this.b = b;
		}

		@Override
		public String getTerm(String identifierQuoteString) {
			return "( " + this.a.getTerm(identifierQuoteString) + " ) AND ( "
					+ this.b.getTerm(identifierQuoteString) + " )";
		}

		@Override
		public List<Object> getParams() {
			ArrayList<Object> l = new ArrayList();
			l.addAll(a.getParams());
			l.addAll(b.getParams());
			return l;
		}
	}

}
