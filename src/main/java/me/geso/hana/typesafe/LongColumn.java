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
public class LongColumn<T extends AbstractTypeSafeRow> extends Column {

    public LongColumn(String tableName, String name) {
        super(tableName, name);
    }

    public LongEqualsClause<T> is(long value) {
	return new LongEqualsClause<>(this, value);
    }
    
}
