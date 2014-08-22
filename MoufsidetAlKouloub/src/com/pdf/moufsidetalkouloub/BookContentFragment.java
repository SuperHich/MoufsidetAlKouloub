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

import com.pdf.moufsidetalkouloub.adapters.BookContentAdapter;
import com.pdf.moufsidetalkouloub.externals.BookPart;
import com.pdf.moufsidetalkouloub.externals.PDFDataBase;

/**
 * Moufsideet Al Kouloub
 * @author HICHEM LAROUSSI - RAMI TRABELSI
 * Copyright (c) 2014 Zad Group. All rights reserved.
 */

@SuppressLint("ValidFragment")
public class BookContentFragment extends ListFragment{

	public static final String ARG_AHADITH = "ahadith_type";
	
	private Button btn_back;
	private ImageView img_title;
	private BookContentAdapter adapter;
	private ArrayList<BookPart> parts = new ArrayList<BookPart>();
	
	private PDFDataBase pdfDB;
	private int book_id;

	public BookContentFragment(int book_id) {
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
		img_title.setBackgroundResource(R.drawable.mo7tawayat);
		btn_back = (Button) rootView.findViewById(R.id.btn_back);
		btn_back.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				getActivity().onBackPressed();
			}
		});
		
		adapter = new BookContentAdapter(getActivity(), R.layout.summary_list_item, parts);

		return rootView;
	}
	
	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		
		getListView().setAdapter(adapter);
		getListView().setCacheColorHint(Color.TRANSPARENT);
		
		parts.clear();
		parts.addAll(pdfDB.getBookPartsByID(book_id));
		adapter.notifyDataSetChanged();
		
		getListView().setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				
				((PDFViewerActivity) getActivity()).onPageItemClicked(parts.get(position).getPageNb());
			}
		});
	}

}
