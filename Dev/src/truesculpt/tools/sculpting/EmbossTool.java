package truesculpt.tools.sculpting;

import truesculpt.main.Managers;
import truesculpt.main.R;
import truesculpt.tools.base.SculptingTool;

public class EmbossTool extends SculptingTool
{

	public EmbossTool(Managers managers)
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
		return "Emboss";
	}

	@Override
	public int GetIcon()
	{
		return R.drawable.flash;
	}

}
