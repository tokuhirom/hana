/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package me.geso.hana;

import java.util.List;

/**
 *
 * @author Tokuhiro Matsuno <tokuhirom@gmail.com>
 */
public class Page<T> {

	private final long entriesPerPage;
	private final long currentPage;
	private final boolean hasNext;
	private final List<T> rows;

	/**
	 *
	 * @param entriesPerPage
	 * @param currentPage
	 * @param hasNext
	 * @param rows
	 */
	public Page(long entriesPerPage, long currentPage, boolean hasNext, List<T> rows) {
		this.entriesPerPage = entriesPerPage;
		this.currentPage = currentPage;
		this.hasNext = hasNext;
		this.rows = rows;
	}

	public long getEntriesPerPage() {
		return entriesPerPage;
	}

	public long getCurrentPage() {
		return currentPage;
	}

	public boolean hasNext() {
		return hasNext;
	}

	public List<T> getRows() {
		return rows;
	}

}
