/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package me.geso.hana;

/**
 *
 * @author tokuhirom
 */
public class Identifier {

	/**
	 * Quote SQL identifier. You should get identifierQuoteString from DatabaseMetadata.
	 *
	 * @param identifier
	 * @param identifierQuoteString
	 * @return Escaped identifier.
	 */
	public static String quote(String identifier, String identifierQuoteString) {
		return identifierQuoteString
				+ identifier.replace(identifierQuoteString, identifierQuoteString + identifierQuoteString)
				+ identifierQuoteString;
	}
}
