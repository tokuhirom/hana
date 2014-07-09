/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package me.geso.hana.typesafe;

import lombok.Getter;

/**
 *
 * @author tokuhirom
 * @param <T>
 */
public class LongEqualsClause<T extends AbstractTypeSafeRow> extends Condition<T> {
    @Getter
    final LongColumn column;
    @Getter
    final long value;

    public LongEqualsClause(LongColumn column, long value) {
        this.column = column;
        this.value = value;
    }
    
}
