/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package me.geso.hana.typesafe;

import java.sql.PreparedStatement;
import lombok.Getter;
import lombok.ToString;

/**
 *
 * @author tokuhirom
 */
@ToString
class CannotUpdateException extends Exception {
    @Getter
    private final PreparedStatement stmt;

    public CannotUpdateException(PreparedStatement stmt) {
        this.stmt = stmt;
    }
    
}
