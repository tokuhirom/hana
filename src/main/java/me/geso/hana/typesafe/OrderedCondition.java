/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package me.geso.hana.typesafe;

import java.util.ArrayList;
import java.util.List;
import lombok.Getter;

/**
 *
 * @author tokuhirom
 * @param <T>
 */
public class OrderedCondition<T extends AbstractTypeSafeRow> extends Clause<T> {
    @Getter
    final List<Order> orderBy = new ArrayList<>();
    @Getter
    final Condition condition;

    public OrderedCondition(Condition condition, Column column, OrderType type) {
        this.condition = condition;
        this.orderBy.add(new Order(column, type));
    }

    public class Order {

        private final Column column;
        private final OrderType type;

        private Order(Column column, OrderType type) {
            this.column = column;
            this.type = type;
        }
    }
    
}
