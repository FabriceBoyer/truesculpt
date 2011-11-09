package truesculpt.tools.sculpting;

import truesculpt.main.Managers;
import truesculpt.tools.base.SculptingTool;
import android.graphics.drawable.Drawable;

public class FlattenTool extends SculptingTool
{

	public FlattenTool(Managers managers)
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
		return "Flatten";
	}

	@Override
	public Drawable GetIcon()
	{
		return null;
	}

}
