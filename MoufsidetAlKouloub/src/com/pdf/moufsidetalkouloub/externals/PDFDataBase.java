package com.pdf.moufsidetalkouloub.externals;

import java.util.ArrayList;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.util.Log;

import com.readystatesoftware.sqliteasset.SQLiteAssetHelper;

/**
 * AlMoufasserAlSaghir
 * @author HICHEM LAROUSSI - RAMI TRABELSI
 * Copyright (c) 2014 Zad Group. All rights reserved.
 */

public class PDFDataBase extends SQLiteAssetHelper {

    private static final String DATABASE_NAME = "HeartsWorks_v_1.0.sqlite";
    private static final int DATABASE_VERSION = 1;
    
    private static final String TABLE_BOOKMARK = "BookMark";
    private static final String COL_ID = "_id";
    private static final String COL_BOOK_ID = "BookID";
    private static final String COL_PAGE_NUMBER = "PageNumber";
    
    private static final String CREATE_TABLE_BOOKMARK = "CREATE TABLE IF NOT EXISTS " + TABLE_BOOKMARK
			+ " (" + COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " 
			+ COL_BOOK_ID 	+ " INTEGER, "
			+ COL_PAGE_NUMBER + " INTEGER);";
    
    public PDFDataBase(Context context) {
    	super(context.getApplicationContext(), DATABASE_NAME, context.getExternalFilesDir(null).getAbsolutePath(), null, DATABASE_VERSION);
        
    	createTableBookMark();
    }
    
    public void createTableBookMark(){
    	SQLiteDatabase db = getWritableDatabase();
    	db.execSQL(CREATE_TABLE_BOOKMARK);
    }
    
    public ArrayList<BookPart> getBookPartsByID(int bookId) {

		SQLiteDatabase db = getReadableDatabase();
		SQLiteQueryBuilder qb = new SQLiteQueryBuilder();

		String sqlTable = "BooksContents_content";
		String whereClause = "c0BookID = ?";
		
		String[] whereArgs = {String.valueOf(bookId)};
		
		qb.setTables(sqlTable);
		Cursor c = qb.query(db, null, whereClause, whereArgs, null, null, null);

		ArrayList<BookPart> parts = new ArrayList<BookPart>();
		if(c.moveToFirst())
			do{
				BookPart part = new BookPart();
				part.setId(c.getInt(0));
				part.setBookId(c.getInt(1));
				part.setTitle(c.getString(2));
				part.setPageNb(c.getInt(3));
				
				Log.i("", part.toString());

				parts.add(part);
			}while (c.moveToNext());
		
		c.close();
		return parts;

	}
    
    public ArrayList<BookMark> getBookMarksByID(int bookId) {

		SQLiteDatabase db = getReadableDatabase();
		SQLiteQueryBuilder qb = new SQLiteQueryBuilder();

		String sqlTable = TABLE_BOOKMARK;
		String whereClause = COL_BOOK_ID +  " = ?";
		String orderBy = COL_PAGE_NUMBER;
		
		String[] whereArgs = {String.valueOf(bookId)};
		
		qb.setTables(sqlTable);
		Cursor c = qb.query(db, null, whereClause, whereArgs, null, null, orderBy);

		ArrayList<BookMark> bookMarks = new ArrayList<BookMark>();
		if(c.moveToFirst())
			do{
				BookMark bMark = new BookMark();
				bMark.setId(c.getInt(0));
				bMark.setBookId(c.getInt(1));
				bMark.setPageNb(c.getInt(2));
				
				Log.i("", bMark.toString());

				bookMarks.add(bMark);
			}while (c.moveToNext());
		
		c.close();
		return bookMarks;
	}
    
    public boolean addToBookMarks(int bookId, int pageNb){    	
    	SQLiteDatabase db = getWritableDatabase();

		ContentValues values = new ContentValues();
		values.put(COL_BOOK_ID, bookId);
		values.put(COL_PAGE_NUMBER, pageNb);
		
		long insertedId = db.insertWithOnConflict(TABLE_BOOKMARK, null, values, SQLiteDatabase.CONFLICT_REPLACE);
		
		return insertedId != -1;
    }

    public boolean removeFromBookMarks(int bookId, int pageNb){    	
    	SQLiteDatabase db = getWritableDatabase();

		String whereClause = COL_BOOK_ID + " = ? AND " + COL_PAGE_NUMBER + " = ?";
		String[] whereArgs = {String.valueOf(bookId), String.valueOf(pageNb)};
		
		long insertedId = db.delete(TABLE_BOOKMARK, whereClause, whereArgs);
		
		return insertedId > 0;
    }
    
    public boolean isBookMarked(int bookId, int pageNb){    	
    	SQLiteDatabase db = getWritableDatabase();
		SQLiteQueryBuilder qb = new SQLiteQueryBuilder();

		String whereClause = COL_BOOK_ID + " = ? AND " + COL_PAGE_NUMBER + " = ?";
		String[] whereArgs = {String.valueOf(bookId), String.valueOf(pageNb)};
		
		qb.setTables(TABLE_BOOKMARK);
		Cursor c = qb.query(db, null, whereClause, whereArgs, null, null, null);
		
		if(c.moveToFirst()){
			c.close();
			return true;
		}
		
		c.close();
		return false;
    }
}