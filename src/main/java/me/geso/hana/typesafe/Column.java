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
public class Column {
    @Getter
    private final String tableName;
    @Getter
    private final String name;

    public Column(String tableName, String name) {
        this.tableName = tableName;
        this.name = name;
    }
    
}
