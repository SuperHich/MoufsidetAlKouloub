/**
 * 
 */
package com.pdf.moufsidetalkouloub.utils;


import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

public class LineView extends View {
	int x;
	int y;
	int x1;
	int y1;
	Paint p;
	Context context;
	ImageView img;

	public LineView(Context context, ImageView image, int x, int y, int x1,
			int y1) {
		super(context);
		this.context = context;
		this.x = x;
		this.y = y;
		this.x1 = x1;
		this.y1 = y1;
		this.img = image;

		this.p = new Paint();
		this.p.setColor(Color.BLACK);
		this.p.setStrokeWidth(3.0F);
	}

	public void setColor(int color) {
		this.p.setColor(color);
	}

	public void setLineSize(int size) {
		this.p.setStrokeWidth(size);
	}

	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);

		Log.e("User", "Line " + x + " , " + y + " : " + x1 + " , " + y1);
		canvas.drawLine(this.x + this.img.getWidth() / 2,
				this.y + this.img.getHeight() / 2,
				this.x1 + this.img.getWidth() / 2,
				this.y1 + this.img.getHeight() / 2, this.p);
	}
}
