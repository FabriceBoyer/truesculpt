package truesculpt.tools.sculpting;

import truesculpt.main.Managers;
import truesculpt.main.R;
import truesculpt.tools.base.SculptingTool;

public class ClayTool extends SculptingTool
{

	public ClayTool(Managers managers)
	{
		super(managers);

	}

	@Override
	protected void Work()
	{
	}

	@Override
	public int GetIcon()
	{
		return R.drawable.clay;
	}

	@Override
	public String GetName()
	{
		return "Clay";
	}

}
