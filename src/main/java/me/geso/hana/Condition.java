/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package me.geso.hana;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import lombok.Getter;
import lombok.NonNull;

/**
 *
 * @author tokuhirom
 * @param <Value>
 */
public class Condition<Value> {

	@Getter
	private final String term;

	@Getter
	final List<Value> params;

	public Condition(String term) {
		this.term = term;
		this.params = new ArrayList<>();
	}

	private Condition(String term, List<Value> params) {
		this.term = term;
		this.params = params;
	}

	public static <Value> Condition eq(String column, Value value) {
		if (value == null) {
			return new Condition(column + " IS NULL");
		} else {
			List<Value> values = new ArrayList<>();
			values.add(value);
			return new Condition(column + "=?", values);
		}
	}

	public static <Value> Condition ne(String column, Value value) {
		if (value == null) {
			return new Condition(column + " IS NOT NULL");
		} else {
			List<Value> values = new ArrayList<>();
			values.add(value);
			return new Condition(column + "!=?", values);
		}
	}

	/**
	 * Condition: `column > value`
	 *
	 * @param <Value>
	 * @param column
	 * @param value
	 * @return
	 */
	public static <Value> Condition gt(String column, @NonNull Value value) {
		List<Value> values = new ArrayList<>();
		values.add(value);
		return new Condition(column + ">?", values);
	}

	/**
	 * Condition: `column < value`
	 *
	 * @param <Value>
	 * @param column
	 * @param value
	 * @return
	 */
	public static <Value> Condition lt(String column, @NonNull Value value) {
		List<Value> values = new ArrayList<>();
		values.add(value);
		return new Condition(column + "<?", values);
	}

	/**
	 * >=
	 *
	 * @param <Value>
	 * @param column
	 * @param value
	 * @return
	 */
	public static <Value> Condition ge(String column, @NonNull Value value) {
		List<Value> values = new ArrayList<>();
		values.add(value);
		return new Condition(column + ">=?", values);
	}

	/**
	 * <=
	 *
	 * @param <Value>
	 * @param column
	 * @param value
	 * @return
	 */
	public static <Value> Condition le(String column, @NonNull Value value) {
		List<Value> values = new ArrayList<>();
		values.add(value);
		return new Condition(column + "<=?", values);
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
	public static <Value> Condition in(String column, @NonNull List<Value> values) {
		if (values.isEmpty()) {
			return new Condition("1=0");
		} else {
			String placeholder = values
					.stream().map(e -> "?")
					.collect(Collectors.joining(","));
			return new Condition(
					column + " IN (" + placeholder + ")",
					values);
		}
	}

	public static <Value> Condition not_in(String column, @NonNull List<Value> values) {
		if (values.isEmpty()) {
			return new Condition("1=1");
		} else {
			String placeholder = values
					.stream().map(e -> "?")
					.collect(Collectors.joining(","));
			return new Condition(
					column + " NOT IN (" + placeholder + ")",
					values);
		}
	}

	public static Condition like(String column, String value) {
		if (value == null) {
			return new Condition(column + " IS NULL");
		} else {
			List<String> values = new ArrayList<>();
			values.add(value);
			return new Condition(column + " LIKE ?", values);
		}
	}

	/**
	 * <code>
	 *	( this ) AND ( x )
	 * </code>
	 *
	 * @param x
	 * @return
	 */
	public Condition and(Condition<Value> x) {
		List<Value> values = new ArrayList<>();
		values.addAll(this.params);
		values.addAll(x.params);
		return new Condition(
				"( " + this.getTerm() + " ) AND ( " + x.getTerm() + " )",
				values);
	}

	/**
	 * <code>
	 *	( this ) OR ( x )
	 * </code>
	 *
	 * @param x
	 * @return
	 */
	public Condition or(Condition<Value> x) {
		List<Value> values = new ArrayList<>();
		values.addAll(this.params);
		values.addAll(x.params);
		return new Condition(
				"( " + this.getTerm() + " ) OR ( " + x.getTerm() + " )",
				values);
	}

}
