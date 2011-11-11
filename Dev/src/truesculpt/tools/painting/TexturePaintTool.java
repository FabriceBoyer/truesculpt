package truesculpt.tools.painting;

import truesculpt.main.Managers;
import truesculpt.main.R;
import truesculpt.tools.base.PaintingTool;

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
	public int GetIcon()
	{
		return R.drawable.brush;
	}
}
