package truesculpt.ui.views;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
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
		float x=getWidth()/2.0f;
		float y=getHeight()/2.0f;
		float R=Math.min(getWidth(), getHeight())/2.0f;
		canvas.drawCircle(x, y, R, mCenterPaint);
	}

	public void SetColor(int color)
	{
		mCenterPaint.setColor(color);
		invalidate();
	}

}