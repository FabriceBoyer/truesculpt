package truesculpt.tools.sculpting;

import truesculpt.main.Managers;
import truesculpt.tools.base.SculptingTool;
import android.graphics.drawable.Drawable;

public class SmoothTool extends SculptingTool
{
	public SmoothTool(Managers managers)
	{
		super(managers);
	}

	@Override
	protected void Work()
	{

	}

	@Override
	public Drawable GetIcon()
	{
		return null;
	}

	@Override
	public String GetName()
	{
		return "Smooth";
	}
}
