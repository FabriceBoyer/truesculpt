package truesculpt.ui.views;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

//TODO rotate button
//TODO greater increment with greater speed
//TODO stay pushed= circular autoincrement, pb with onmove not sent, use timer thread, plus value hidden by finger
public class SliderPickView extends View
{	
	public interface OnSliderPickChangedListener
	{
		void sliderValueChanged(float value);
		void sliderChangeStart();
		void sliderChangeStop(float value);
	}
	
	public interface OnDoubleClickListener
	{
		 void onDoubleClick();	 
	}
	
	private String Text="Value : ";
	private String UnitText=" %";
	private float CurrentValue=0;
	private float MaxValue=100;
	private float MinValue=0;
	private int PixelAmplitude=300;	
	private Paint mTextPaint=null;
	private Paint mCenterPaint=null;
	float orig_x=0;
	float orig_y=0;
	private long mLastTapTapTime=0;
	private long mTapTapTimeThresold=500;//ms
	private float mOldValue=0;
	
	private OnSliderPickChangedListener mListener=null;
	private OnDoubleClickListener mDoubleClickListener=null;
	
	public SliderPickView(Context c, AttributeSet attrs)
	{
		super(c, attrs);
		
		mTextPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
		mTextPaint.setColor(Color.WHITE);
		mTextPaint.setTextSize(16);
			
		mCenterPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
		mCenterPaint.setColor(Color.DKGRAY);
		mCenterPaint.setStyle(Paint.Style.FILL);
		mCenterPaint.setStrokeWidth(1);
	
		invalidate();
	}

	@Override
	protected void onDraw(Canvas canvas)
	{
		float xc=getWidth()/2.0f;
		float yc=getHeight()/2.0f;
		
		float valueAmplitude=MaxValue-MinValue;
		float R=Math.min(getWidth(), getHeight())/2.0f;
		float radius=(CurrentValue-MinValue)/valueAmplitude*R;
		canvas.drawCircle(xc,yc,radius,mCenterPaint);	
		
		float y1=1.0f/3.0f*getHeight()+5;
		float y2=2.0f/3.0f*getHeight()+5;
		String valuetext= Integer.toString((int)(CurrentValue*100.0f)/100)+ UnitText;
		float textWidth=mTextPaint.measureText(Text);
		float valueTextWidth=mTextPaint.measureText(valuetext);
		canvas.drawText(Text, xc-textWidth/2.0f, y1, mTextPaint);
		canvas.drawText(valuetext, xc-valueTextWidth/2.0f, y2, mTextPaint);	
	}

	private enum State {START, CHANGE, STOP};
	
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
				
				long curTapTapTime = System.currentTimeMillis();
				if ((curTapTapTime - mLastTapTapTime) < mTapTapTimeThresold)
				{
					if (mDoubleClickListener!=null)
					{
						setCurrentValue(mOldValue);						
						mDoubleClickListener.onDoubleClick();
					}
				}
				else
				{
					mOldValue=CurrentValue;	
					UpdateSliderValue(x, y, State.START);
				}
				mLastTapTapTime = curTapTapTime;
				
				bRes=true;
				break;
			}
			case MotionEvent.ACTION_UP:		
			{
				UpdateSliderValue(x, y, State.STOP);
				bRes=true;
				break;
			}
			case MotionEvent.ACTION_MOVE:
			{
				UpdateSliderValue(x, y, State.CHANGE);
				bRes=true;
				break;
			}
		}
		
		return bRes;
	}

	private void UpdateSliderValue(float x, float y, State state)
	{
		float newValue=CurrentValue;
		if (state!=State.START)
		{
			float distX=x-orig_x;
			float distY=orig_y-y;
			float pixelDist=(float) Math.sqrt(Math.pow(distX,2)+Math.pow(distY,2));
			//pixelDist=pixelDist%PixelAmplitude;
			float valueAmplitude=MaxValue-MinValue;
			newValue=MinValue+pixelDist*(valueAmplitude/PixelAmplitude);
			setCurrentValue(newValue);
		}
		if (mListener!=null)
		{
			switch (state)
			{
			case START:
				mListener.sliderChangeStart();
				break;
			case CHANGE:
				mListener.sliderValueChanged(newValue);
				break;
			case STOP:
				mListener.sliderChangeStop(newValue);
				break;
			}			
		}
	}	

	public void setMaxValue(float maxValue)
	{
		MaxValue = maxValue;
	}

	public float getMaxValue()
	{
		return MaxValue;
	}


	public void setMinValue(float minValue)
	{
		MinValue = minValue;
	}

	public float getMinValue()
	{
		return MinValue;
	}

	public void setCurrentValue(float currentValue)
	{
		CurrentValue = currentValue;
		
		//Saturation
		if (currentValue<MinValue) CurrentValue=MinValue;
		if (currentValue>MaxValue) CurrentValue=MaxValue;	
		
		invalidate();
	}

	public float getCurrentValue()
	{
		return CurrentValue;
	}
	
	public String getText()
	{
		return Text;
	}

	public void setText(String text)
	{
		Text = text;
	}
	
	public void setSliderChangeListener(OnSliderPickChangedListener mListener)
	{
		this.mListener = mListener;
	}

	public int getPixelAmplitude()
	{
		return PixelAmplitude;
	}

	public void setPixelAmplitude(int pixelAmplitude)
	{
		PixelAmplitude = pixelAmplitude;
	}
	public void SetDoubleClickListener(OnDoubleClickListener listener)
	{
		mDoubleClickListener=listener;
	}
	public void SetCircleBackColor(int color)
	{
		mCenterPaint.setColor(color);
	}
	
}