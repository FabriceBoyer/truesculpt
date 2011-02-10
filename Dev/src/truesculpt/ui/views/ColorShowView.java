package truesculpt.ui.views;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

public class ColorShowView extends View
{
	private Paint mCenterPaint;

	public ColorShowView(Context c, AttributeSet attrs)
	{
		super(c, attrs);

		mCenterPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
		mCenterPaint.setColor(0);
		mCenterPaint.setStyle(Paint.Style.FILL);
	}

	@Override
	protected void onDraw(Canvas canvas)
	{
		canvas.drawRect(0, 0, canvas.getWidth(), canvas.getHeight(), mCenterPaint);
	}

	public void SetColor(int color)
	{
		mCenterPaint.setColor(color);
		invalidate();
	}

}