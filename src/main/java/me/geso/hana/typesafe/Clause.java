/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package me.geso.hana.typesafe;

import java.util.stream.Stream;
import me.geso.hana.HanaSession;

/**
 *
 * @author tokuhirom
 * @param <T>
 */
public class Clause<T extends AbstractTypeSafeRow> {

    public Stream<T> search(HanaSession session) {
        // TBI
        return null;
    }

    public Stream<T> count(HanaSession session) {
        // TBI
        return null;
    }
    
}
