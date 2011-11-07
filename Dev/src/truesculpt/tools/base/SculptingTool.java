package truesculpt.tools.base;

import truesculpt.main.Managers;

public abstract class SculptingTool extends ToolsBase
{
	protected final float[] VOffset = new float[3];
	protected final float[] VNormal = new float[3];

	public SculptingTool(Managers managers)
	{
		super(managers);
	}
}
