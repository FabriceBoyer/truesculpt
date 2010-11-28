package truesculpt.utils;

import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;

/**
 * LayoutUtils is a helper class that makes it a lot easier to perform Android layouts in Java code, without using XML.
 * 
 * @author Nazmul Idris
 * @version 1.0
 * @since Jul 3, 2008, 11:59:37 AM
 */
public class LayoutUtils
{

	public enum Layout {
		WidthFill_HeightFill, WidthFill_HeightWrap, WidthWrap_HeightFill, WidthWrap_HeightWrap;

		public void applyLinearLayoutParams(View linearlayout)
		{
			applyLinearLayoutParamsTo(this, linearlayout);
		}

		public void applyTableLayoutParams(View row)
		{
			applyTableLayoutParamsTo(this, row);
		}

		public void applyTableRowParams(View cell)
		{
			applyTableRowLayoutParamsTo(this, cell);
		}

		public void applyViewGroupParams(View component)
		{
			applyViewGroupLayoutParamsTo(this, component);
		}

	}

	private static void applyLinearLayoutParamsTo(Layout layout, View view)
	{

		switch (layout)
		{
		case WidthFill_HeightFill:
			view.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT));
			break;
		case WidthFill_HeightWrap:
			view.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));
			break;
		case WidthWrap_HeightFill:
			view.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.FILL_PARENT));
			break;
		case WidthWrap_HeightWrap:
			view.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
			break;
		}

	}

	private static void applyTableLayoutParamsTo(Layout layout, View view)
	{

		switch (layout)
		{
		case WidthFill_HeightFill:
			view.setLayoutParams(new TableLayout.LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT));
			break;
		case WidthFill_HeightWrap:
			view.setLayoutParams(new TableLayout.LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));
			break;
		case WidthWrap_HeightFill:
			view.setLayoutParams(new TableLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.FILL_PARENT));
			break;
		case WidthWrap_HeightWrap:
			view.setLayoutParams(new TableLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
			break;
		}

	}

	private static void applyTableRowLayoutParamsTo(Layout layout, View view)
	{

		switch (layout)
		{
		case WidthFill_HeightFill:
			view.setLayoutParams(new TableRow.LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT));
			break;
		case WidthFill_HeightWrap:
			view.setLayoutParams(new TableRow.LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));
			break;
		case WidthWrap_HeightFill:
			view.setLayoutParams(new TableRow.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.FILL_PARENT));
			break;
		case WidthWrap_HeightWrap:
			view.setLayoutParams(new TableRow.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
			break;
		}

	}

	private static void applyViewGroupLayoutParamsTo(Layout layout, View view)
	{

		switch (layout)
		{
		case WidthFill_HeightFill:
			view.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.FILL_PARENT));
			break;
		case WidthFill_HeightWrap:
			view.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
			break;
		case WidthWrap_HeightFill:
			view.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.FILL_PARENT));
			break;
		case WidthWrap_HeightWrap:
			view.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
			break;
		}

	}

}// end class LayoutUtils
