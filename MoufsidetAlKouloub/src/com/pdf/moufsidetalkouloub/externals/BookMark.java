package com.pdf.moufsidetalkouloub.externals;

public class BookMark {
	
	private int id;
	private int bookId;
	private int pageNb;
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getBookId() {
		return bookId;
	}
	public void setBookId(int bookId) {
		this.bookId = bookId;
	}
	public int getPageNb() {
		return pageNb;
	}
	public void setPageNb(int pageNb) {
		this.pageNb = pageNb;
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("ID " + getId() + "\n");
		sb.append("BookID " + getBookId() + "\n");
		sb.append("PageNumber " + getPageNb());
		return sb.toString();
	}
}
