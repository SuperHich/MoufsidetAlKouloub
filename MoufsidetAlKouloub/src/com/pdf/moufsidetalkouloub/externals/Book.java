package com.pdf.moufsidetalkouloub.externals;

/**
 * Moufsideet Al Kouloub
 * @author HICHEM LAROUSSI - RAMI TRABELSI
 * Copyright (c) 2014 Zad Group. All rights reserved.
 */

public class Book {
	
	private int id;
	private String cover;
	private String pdfFile;
	private int pagesNumber;
	
	public Book() {
		// TODO Auto-generated constructor stub
	}
	
	public Book(int id, String cover, String pdfFile, int pagesNumber){
		this.id = id;
		this.cover = cover;
		this.pdfFile = pdfFile;
		this.pagesNumber = pagesNumber;
	}
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getCover() {
		return cover;
	}
	public void setCover(String cover) {
		this.cover = cover;
	}
	public String getPdfFile() {
		return pdfFile;
	}
	public void setPdfFile(String pdfFile) {
		this.pdfFile = pdfFile;
	}

	public int getPagesNumber() {
		return pagesNumber;
	}

	public void setPagesNumber(int pagesNumber) {
		this.pagesNumber = pagesNumber;
	}
	
	

}
