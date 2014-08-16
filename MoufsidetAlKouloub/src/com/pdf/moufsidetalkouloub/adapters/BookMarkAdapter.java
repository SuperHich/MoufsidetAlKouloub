package com.pdf.moufsidetalkouloub.adapters;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.pdf.moufsidetalkouloub.R;
import com.pdf.moufsidetalkouloub.externals.BookMark;
import com.pdf.moufsidetalkouloub.utils.MySuperScaler;

public class BookMarkAdapter extends ArrayAdapter<BookMark> {

	Context mContext;
	int layoutResourceId;
	ArrayList<BookMark> data = null;
	LayoutInflater inflater;
	
	public BookMarkAdapter(Context mContext, int layoutResourceId, ArrayList<BookMark> data) {

		super(mContext, layoutResourceId, data);
		this.layoutResourceId = layoutResourceId;
		this.mContext = mContext;
		this.data = data;
		inflater = ((Activity) mContext).getLayoutInflater();
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		ViewHolder holder;
		if(convertView==null)
		{
			holder = new ViewHolder();
			convertView = inflater.inflate(layoutResourceId, parent, false);
			
//			MySuperScaler.scaleViewAndChildren(convertView, MySuperScaler.scale);
			
			// get the elements in the layout
			holder.txv_title = (TextView) convertView.findViewById(R.id.txv_babTitle); 
			holder.txv_pageNb = (TextView) convertView.findViewById(R.id.txv_pageNb); 
			
			convertView.setTag(holder);
		}
		else {
			holder = (ViewHolder)convertView.getTag();
		}

		/*
		 * Set the data for the list item. You can also set tags here if you
		 * want.
		 */
		
		int size = (int) MySuperScaler.screen_width / 24 ;
		holder.txv_pageNb.setTextSize(TypedValue.COMPLEX_UNIT_PX, size);
		
		int size2 = (int) MySuperScaler.screen_width / 24 ;
		holder.txv_title.setTextSize(TypedValue.COMPLEX_UNIT_PX, size2);
		
		
		BookMark bMark = data.get(position);

		holder.txv_title.setText(mContext.getString(R.string.page_num));
		
		
		
		holder.txv_pageNb.setText("" + bMark.getPageNb());

		return convertView;
	}

	class ViewHolder
	{
		TextView txv_title; 
		TextView txv_pageNb;
	}

}
