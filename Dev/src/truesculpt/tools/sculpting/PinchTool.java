package truesculpt.tools.sculpting;

import truesculpt.main.Managers;
import truesculpt.main.R;
import truesculpt.tools.base.SculptingTool;

public class PinchTool extends SculptingTool
{

	public PinchTool(Managers managers)
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
		return R.drawable.pinch;
	}

	@Override
	public String GetName()
	{
		return "Pinch";
	}

}
