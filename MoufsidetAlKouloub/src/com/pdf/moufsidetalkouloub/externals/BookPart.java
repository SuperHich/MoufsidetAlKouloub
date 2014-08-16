package com.pdf.moufsidetalkouloub.externals;

public class BookPart {
	
	private int id;
	private int bookId;
	private String title;
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
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
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
		sb.append("Title " + getTitle() + "\n");
		sb.append("PageNumber " + getPageNb());
		return sb.toString();
	}
}
