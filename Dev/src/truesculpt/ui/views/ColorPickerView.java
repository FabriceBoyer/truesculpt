package truesculpt.ui.views;

import truesculpt.ui.dialogs.ColorPickerDialog.OnColorChangedListener;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.SweepGradient;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

public class ColorPickerView extends View
{
	private static final int CENTER_RADIUS = 32;
	private static final int CENTER_X = 100;
	private static final int CENTER_Y = 100;
	private static final float PI = 3.1415926f;
	private Paint mCenterPaint=null;
	private final int[] mColors;
	private OnColorChangedListener mListener=null;
	private Paint mPaint;

	public ColorPickerView(Context c, AttributeSet attrs)
	{
		super(c, attrs);

		mColors = new int[] { 0xFFFF0000, 0xFFFF00FF, 0xFF0000FF, 0xFF00FFFF, 0xFF00FF00, 0xFFFFFF00, 0xFFFF0000 };
		Shader s = new SweepGradient(0, 0, mColors, null);

		mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
		mPaint.setShader(s);
		mPaint.setStyle(Paint.Style.STROKE);
		mPaint.setStrokeWidth(32);

		mCenterPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
		mCenterPaint.setColor(0);
		mCenterPaint.setStrokeWidth(5);
	}

	private int ave(int s, int d, float p)
	{
		return s + java.lang.Math.round(p * (d - s));
	}

	private int interpColor(int colors[], float unit)
	{
		if (unit <= 0)
		{
			return colors[0];
		}
		if (unit >= 1)
		{
			return colors[colors.length - 1];
		}

		float p = unit * (colors.length - 1);
		int i = (int) p;
		p -= i;

		// now p is just the fractional part [0...1) and i is the index
		int c0 = colors[i];
		int c1 = colors[i + 1];
		int a = ave(Color.alpha(c0), Color.alpha(c1), p);
		int r = ave(Color.red(c0), Color.red(c1), p);
		int g = ave(Color.green(c0), Color.green(c1), p);
		int b = ave(Color.blue(c0), Color.blue(c1), p);

		return Color.argb(a, r, g, b);
	}

	@Override
	protected void onDraw(Canvas canvas)
	{
		float r = CENTER_X - mPaint.getStrokeWidth() * 0.5f;

		canvas.translate(CENTER_X, CENTER_X);

		canvas.drawOval(new RectF(-r, -r, r, r), mPaint);
		canvas.drawCircle(0, 0, CENTER_RADIUS, mCenterPaint);

		int c = mCenterPaint.getColor();
		mCenterPaint.setStyle(Paint.Style.STROKE);

		mCenterPaint.setAlpha(0xFF);

		canvas.drawCircle(0, 0, CENTER_RADIUS + mCenterPaint.getStrokeWidth(), mCenterPaint);

		mCenterPaint.setStyle(Paint.Style.FILL);
		mCenterPaint.setColor(c);
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec)
	{
		setMeasuredDimension(CENTER_X * 2, CENTER_Y * 2);
	}

	private enum State {START, CHANGE, STOP};
	
	@Override
	public boolean onTouchEvent(MotionEvent event)
	{
		boolean bRes=false;
		float x = event.getX() - CENTER_X;
		float y = event.getY() - CENTER_Y;
		float R=(float) java.lang.Math.sqrt(x * x + y * y);
		boolean inCenter = R <= CENTER_RADIUS;

		if (!inCenter)
		{			
			float angle = (float) java.lang.Math.atan2(y, x);
			// need to turn angle [-PI ... PI] into unit [0....1]
			float unit = angle / (2 * PI);
			if (unit < 0)
			{
				unit += 1;
			}
			int col=interpColor(mColors, unit);
			
			int action = event.getAction();
			int actionCode = action & MotionEvent.ACTION_MASK;
			switch (actionCode)
			{
				case MotionEvent.ACTION_DOWN:
				{			
					UpdateColor(col, State.START);
					break;
				}
				case MotionEvent.ACTION_UP:		
				{
					UpdateColor(col, State.STOP);
					bRes=true;
					break;
				}
				case MotionEvent.ACTION_MOVE:
				{
					UpdateColor(col, State.CHANGE);
					bRes=true;
					break;
				}
			}			
			
			bRes=true;
		}
		else
		{
			bRes=super.onTouchEvent(event);
		}
		 
		return bRes;	
	}
	
	private void UpdateColor(int color, State state)
	{
		if (mListener!=null)
		{
			switch (state)
			{
			case START:
				mListener.colorChangeStart();
				break;
			case CHANGE:
				mListener.colorChanged(color);
				break;
			case STOP:
				mListener.colorChangeStop(color);
				break;
			}					
		}
		SetColor(color);
	}

	public void SetColor(int color)
	{
		mCenterPaint.setColor(color);
		invalidate();
	}

	public void SetColorChangeListener(OnColorChangedListener listener)
	{
		mListener = listener;
	}
}