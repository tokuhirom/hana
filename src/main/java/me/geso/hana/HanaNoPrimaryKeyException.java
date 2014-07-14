/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package me.geso.hana;

/**
 *
 * @author Tokuhiro Matsuno <tokuhirom@gmail.com>
 */
public class HanaNoPrimaryKeyException extends HanaException {

	public HanaNoPrimaryKeyException(String string) {
		super(string);
	}

}
