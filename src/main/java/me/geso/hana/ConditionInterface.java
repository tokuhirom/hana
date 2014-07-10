/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package me.geso.hana;

import java.util.List;
import me.geso.hana.Condition.AndCondition;
import me.geso.hana.Condition.OrCondition;

/**
 *
 * @author Tokuhiro Matsuno <tokuhirom@gmail.com>
 */
public interface ConditionInterface {

	public String getTerm(String identifierQuoteString);

	public List<Object> getParams();

	/**
	 * <code>
	 *	( this ) AND ( x )
	 * </code>
	 *
	 * @param x
	 * @return
	 */
	default public ConditionInterface and(ConditionInterface x) {
		return new AndCondition(this, x);
	}

	/**
	 * <code>
	 *	( this ) OR ( x )
	 * </code>
	 *
	 * @param x
	 * @return
	 */
	default public ConditionInterface or(ConditionInterface x) {
		return new OrCondition(this, x);
	}

}
