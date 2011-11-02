package truesculpt.ui.dialogs;

import truesculpt.ui.views.ColorPickerView;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;

public class ColorPickerDialog extends Dialog
{

	public interface OnColorChangedListener
	{
		void colorChanged(int color);

		void colorChangeStart(int color);

		void colorChangeStop(int color);
	}

	private int mInitialColor;

	private OnColorChangedListener mListener;

	public ColorPickerDialog(Context context, OnColorChangedListener listener, int initialColor)
	{
		super(context);

		mListener = listener;
		mInitialColor = initialColor;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		OnColorChangedListener l = new OnColorChangedListener()
		{
			@Override
			public void colorChanged(int color)
			{
				mListener.colorChanged(color);
			}

			@Override
			public void colorChangeStart(int color)
			{
				mListener.colorChangeStart(color);
			}

			@Override
			public void colorChangeStop(int color)
			{
				mListener.colorChangeStop(color);
			}
		};

		ColorPickerView pickerView = new ColorPickerView(getContext(), null);
		pickerView.SetColor(mInitialColor);
		pickerView.SetColorChangeListener(l);
		setContentView(pickerView);
		setTitle("Pick a Color");
	}
}
