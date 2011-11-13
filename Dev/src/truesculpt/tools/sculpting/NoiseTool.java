package truesculpt.tools.sculpting;

import truesculpt.main.Managers;
import truesculpt.main.R;
import truesculpt.tools.base.SculptingTool;

public class NoiseTool extends SculptingTool
{

	public NoiseTool(Managers managers)
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
		return "Noise";
	}

	@Override
	public int GetIcon()
	{
		return R.drawable.noise;
	}

}
