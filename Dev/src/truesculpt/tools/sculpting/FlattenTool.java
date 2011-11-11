package truesculpt.tools.sculpting;

import truesculpt.main.Managers;
import truesculpt.main.R;
import truesculpt.tools.base.SculptingTool;

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
	public int GetIcon()
	{
		return R.drawable.flatten;
	}

}
