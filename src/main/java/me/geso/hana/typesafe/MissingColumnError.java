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
class MissingColumnError extends RuntimeException {
    @Getter
    private final String key;

    public MissingColumnError(String key) {
        this.key = key;
    }
    
}
