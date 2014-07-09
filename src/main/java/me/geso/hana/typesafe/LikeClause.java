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
 */
public class LikeClause<T extends AbstractTypeSafeRow> extends Condition<T> {
    @Getter
    final StringColumn column;
    @Getter
    final String pattern;

    public LikeClause(StringColumn column, String pattern) {
        this.column = column;
        this.pattern = pattern;
    }
    
}
