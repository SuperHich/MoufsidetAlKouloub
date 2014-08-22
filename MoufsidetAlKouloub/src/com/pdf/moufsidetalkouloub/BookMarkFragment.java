package com.pdf.moufsidetalkouloub;

import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ImageView;

import com.pdf.moufsidetalkouloub.adapters.BookMarkAdapter;
import com.pdf.moufsidetalkouloub.externals.BookMark;
import com.pdf.moufsidetalkouloub.externals.PDFDataBase;

/**
 * Moufsideet Al Kouloub
 * @author HICHEM LAROUSSI - RAMI TRABELSI
 * Copyright (c) 2014 Zad Group. All rights reserved.
 */

@SuppressLint("ValidFragment")
public class BookMarkFragment extends ListFragment{

	private Button btn_back;
	private ImageView img_title;
	private BookMarkAdapter adapter;
	private ArrayList<BookMark> bookMarks = new ArrayList<BookMark>();
	
	private PDFDataBase pdfDB;
	private int book_id;

	public BookMarkFragment(int book_id) {
		this.book_id = book_id;
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		
		pdfDB = ((PDFViewerActivity)getActivity()).pdfDB;
	}
	
	@Override
	public void onDetach() {
		super.onDetach();

	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_abwab, container, false);

		img_title = (ImageView) rootView.findViewById(R.id.img_title);
		img_title.setBackgroundResource(R.drawable.fawassel);
		
		btn_back = (Button) rootView.findViewById(R.id.btn_back);
		btn_back.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				getActivity().onBackPressed();
			}
		});
		
		adapter = new BookMarkAdapter(getActivity(), R.layout.bab_list_item, bookMarks);

		return rootView;
	}
	
	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		
		getListView().setAdapter(adapter);
		getListView().setCacheColorHint(Color.TRANSPARENT);
		
		bookMarks.clear();
		bookMarks.addAll(pdfDB.getBookMarksByID(book_id));
		adapter.notifyDataSetChanged();
		
		
		
		
		
		getListView().setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				
				((PDFViewerActivity) getActivity()).onPageItemClicked(bookMarks.get(position).getPageNb());
			}
		});
	}

}
