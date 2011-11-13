package truesculpt.ui.views;

import truesculpt.ui.dialogs.ColorPickerDialog.OnColorChangedListener;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

public class ColorShowView extends View
{
	public interface OnDoubleClickListener
	{
		void onDoubleClick(int color);
	}

	private final Paint mCenterPaint;
	float orig_x = 0;
	float orig_y = 0;
	private final int PixelAmplitude = 300;
	private final int mnPixelDeadZone = 100;
	private OnColorChangedListener mColorListener = null;
	private OnDoubleClickListener mDoubleClickListener = null;
	private long mLastTapTapTime = 0;
	private final long mTapTapTimeThresold = 500;// ms
	private int mColor = 0;
	private int mOldColor = 0;
	private int mStartColor = 0;

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
		float x = getWidth() / 2.0f;
		float y = getHeight() / 2.0f;
		float R = Math.min(getWidth(), getHeight()) / 2.5f;
		canvas.drawCircle(x, y, R, mCenterPaint);
	}

	public void setColor(int color)
	{
		mColor = color;
		mCenterPaint.setColor(mColor);
		invalidate();
	}

	@Override
	public boolean onTouchEvent(MotionEvent event)
	{
		boolean bRes = false;

		int action = event.getAction();
		int actionCode = action & MotionEvent.ACTION_MASK;

		float x = event.getX();
		float y = event.getY();

		float distX = x - orig_x;
		float distY = orig_y - y;
		float pixelDist = (float) Math.sqrt(Math.pow(distX, 2) + Math.pow(distY, 2));

		switch (actionCode)
		{
		case MotionEvent.ACTION_DOWN:
		{
			orig_x = x;
			orig_y = y;

			long curTapTapTime = System.currentTimeMillis();
			if ((curTapTapTime - mLastTapTapTime) < mTapTapTimeThresold)
			{
				if (mDoubleClickListener != null)
				{
					mDoubleClickListener.onDoubleClick(mOldColor);
				}
			}
			else
			{
				mOldColor = mColor;
				UpdateColorValue(0, State.START);
			}
			mLastTapTapTime = curTapTapTime;

			bRes = true;
			break;
		}
		case MotionEvent.ACTION_UP:
		{
			UpdateColorValue(pixelDist, State.STOP);
			bRes = true;
			break;
		}
		case MotionEvent.ACTION_MOVE:
		{
			UpdateColorValue(pixelDist, State.CHANGE);
			bRes = true;
			break;
		}
		}

		return bRes;
	}

	private enum State
	{
		START, CHANGE, STOP
	};

	private void UpdateColorValue(float pixelDist, State state)
	{
		if (state == State.START)
		{
			mStartColor = mColor;
		}
		int newColor = mStartColor;

		if (pixelDist >= mnPixelDeadZone)
		{
			float[] VCol = new float[3];
			Color.colorToHSV(newColor, VCol);
			float oldHue = VCol[0];
			float newHue = (oldHue + 360 * (pixelDist - mnPixelDeadZone) / PixelAmplitude) % 360;
			VCol[0] = newHue;
			newColor = Color.HSVToColor(VCol);
		}

		if (mColorListener != null)
		{
			switch (state)
			{
			case START:
				mColorListener.colorChangeStart(newColor);
				break;
			case CHANGE:
				mColorListener.colorChanged(newColor);
				break;
			case STOP:
				mColorListener.colorChangeStop(newColor);
				break;
			}
		}
	}

	public void SetColorChangeListener(OnColorChangedListener listener)
	{
		mColorListener = listener;
	}

	public void SetDoubleClickListener(OnDoubleClickListener listener)
	{
		mDoubleClickListener = listener;
	}

	public int getColor()
	{
		return mColor;
	}
}