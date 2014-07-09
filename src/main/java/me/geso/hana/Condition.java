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

/**
 *
 * @author tokuhirom
 * @param <Value>
 */
public class Condition<Value> {

	@Getter
	private final String term;

	@Getter
	private final List<Value> params;

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
	// TODO test me
	public static <Value> Condition in(String column, List<Value> values) {
		if (values == null) {
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

	public static <Value> Condition like(String column, Value value) {
		if (value == null) {
			return new Condition(column + " IS NULL");
		} else {
			List<Value> values = new ArrayList<>();
			values.add(value);
			return new Condition(column + " LIKE ?", values);
		}
	}

}
