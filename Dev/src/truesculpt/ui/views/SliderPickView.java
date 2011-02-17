package truesculpt.ui.views;

import truesculpt.actions.ChangePOVAction;
import truesculpt.managers.ToolsManager.EPovToolSubMode;
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
	private String Text="Value : ";
	private String UnitText=" %";
	private float CurrentValue=50;
	private float MaxValue=100;
	private float MinValue=0;
	
	private float PixelToValueRatio=0.1f;
	private Paint mTextPaint;
	private float OrigValue=0;
	float orig_x=0;
	float orig_y=0;
	
	public SliderPickView(Context c, AttributeSet attrs)
	{
		super(c, attrs);
		
		mTextPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
		mTextPaint.setColor(Color.WHITE);
		mTextPaint.setStyle(Paint.Style.FILL);	
	
		invalidate();
	}

	@Override
	protected void onDraw(Canvas canvas)
	{
		float x=getWidth()/2.0f;
		float y1=1.0f/3.0f*getHeight();
		float y2=2.0f/3.0f*getHeight();
		String valuetext= Integer.toString((int)(CurrentValue*100.0f)/100)+ UnitText;
		float textWidth=mTextPaint.measureText(Text);
		float valueTextWidth=mTextPaint.measureText(valuetext);
		canvas.drawText(Text, x-textWidth/2.0f, y1, mTextPaint);
		canvas.drawText(valuetext, x-valueTextWidth/2.0f, y2, mTextPaint);
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
				OrigValue=CurrentValue;
				orig_x=x;
				orig_y=y;
				bRes=true;
				break;
			}
			case MotionEvent.ACTION_UP:
			{
				bRes=true;
				break;
			}
			case MotionEvent.ACTION_MOVE:
			{
				float distX=x-orig_x;
				float distY=orig_y-y;
				float dist=(float) Math.sqrt(Math.pow(distX,2)+Math.pow(distY,2));
				float valueAmplitude=MaxValue-MinValue;
				float sign=Math.signum(distX);
				float newValue=OrigValue+PixelToValueRatio*distY;
				setCurrentValue(newValue);
				bRes=true;
				break;
			}
		}
		
		return bRes;
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
}