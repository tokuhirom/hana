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
public class StringColumn<T extends AbstractTypeSafeRow> extends Column {

    public StringColumn(String tableName, String name) {
        super(tableName, name);
    }

    public LikeClause<T> like(String pattern) {
        return new LikeClause<>(this, pattern);
    }

    public EqualsClause<T> is(String pattern) {
        return new EqualsClause<>(this, pattern);
    }
    
}
