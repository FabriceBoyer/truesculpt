package truesculpt.tools.painting;

import truesculpt.main.Managers;
import truesculpt.tools.base.PaintingTool;
import android.graphics.drawable.Drawable;

public class TexturePaintTool extends PaintingTool
{
	public TexturePaintTool(Managers managers)
	{
		super(managers);
	}

	@Override
	protected void Work()
	{
	}

	@Override
	public String GetName()
	{
		return "Paint";
	}

	@Override
	public Drawable GetIcon()
	{
		return null;
	}
}
