package truesculpt.ui.views;

import truesculpt.ui.dialogs.ColorPickerDialog.OnColorChangedListener;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;


public class ColorShowView extends View
{
	private Paint mCenterPaint;
	float orig_x=0;
	float orig_y=0;
	private int PixelAmplitude=200;	
	private OnColorChangedListener mListener=null;
	int mColor=0;

	public ColorShowView(Context c, AttributeSet attrs)
	{
		super(c, attrs);

		mCenterPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
		mCenterPaint.setColor(mColor);
		mCenterPaint.setStyle(Paint.Style.FILL);
		
		invalidate();
	}

	@Override
	protected void onDraw(Canvas canvas)
	{
		float x=getWidth()/2.0f;
		float y=getHeight()/2.0f;
		float R=Math.min(getWidth(), getHeight())/2.0f;
		canvas.drawCircle(x, y, R, mCenterPaint);
	}

	public void setColor(int color)
	{
		mColor=color;
		mCenterPaint.setColor(mColor);
		invalidate();
	}	
	
	@Override
	public boolean onTouchEvent(MotionEvent event)
	{
		boolean bRes=false;
		
		int action = event.getAction();
		int actionCode = action & MotionEvent.ACTION_MASK;

		float x = event.getX();
		float y = event.getY();
		
		switch (actionCode)
		{
			case MotionEvent.ACTION_DOWN:
			{			
				orig_x=x;
				orig_y=y;
				UpdateValue(x, y);
				bRes=true;
				break;
			}
			case MotionEvent.ACTION_UP:			
			case MotionEvent.ACTION_MOVE:
			{
				UpdateValue(x, y);
				bRes=true;
				break;
			}
		}
		
		bRes=super.onTouchEvent(event);
		
		return bRes;
	}	
	
	private void UpdateValue(float x, float y)
	{
		float distX=x-orig_x;
		float distY=orig_y-y;
		float pixelDist=(float) Math.sqrt(Math.pow(distX,2)+Math.pow(distY,2));
		float newHue=360*pixelDist/PixelAmplitude;
		if (newHue<0) newHue=0;
		if (newHue>360) newHue=360;
		float [] VCol= new float[3];		
		Color.colorToHSV(mColor,VCol);
		VCol[0]=newHue;
		int newColor=Color.HSVToColor(VCol);
		setColor(newColor);
		if (mListener!=null) 
		{
			mListener.colorChanged(newColor);
		}
	}
	
	public void SetColorChangeListener(OnColorChangedListener listener)
	{
		mListener = listener;
	}

	public int getColor()
	{
		return mColor;
	}

}