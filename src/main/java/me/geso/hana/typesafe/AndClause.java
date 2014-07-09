/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package me.geso.hana.typesafe;

/**
 *
 * @author tokuhirom
 * @param <T>
 */
public class AndClause<T extends AbstractTypeSafeRow> extends Condition<T> {
    final Condition cond1;
    final Condition cond2;

    public AndClause(Condition cond1, Condition cond2) {
        this.cond1 = cond1;
        this.cond2 = cond2;
    }
    
}
