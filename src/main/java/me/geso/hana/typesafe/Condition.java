/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package me.geso.hana.typesafe;

/**
 *
 * @author tokuhirom
 */
public class Condition<T extends AbstractTypeSafeRow> extends Clause<T> {

    public OrderedCondition<T> order_by(Column column, OrderType type) {
        return new OrderedCondition<>(this, column, type);
    }

    public OrderedCondition<T> order_by(Column column) {
        return this.order_by(column, OrderType.ASC);
    }

    public AndClause<T> and(Condition b) {
        return new AndClause<>(this, b);
    }
    
}
