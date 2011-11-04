package truesculpt.tools;

import java.util.HashSet;

import truesculpt.main.Managers;
import truesculpt.mesh.Vertex;

public class ToolsBase implements ITools
{
	protected final float FWHM = (float) (2f * Math.sqrt(2 * Math.log(2f)));// full width at half maximum
	protected final float oneoversqrttwopi = (float) (1f / Math.sqrt(2f * Math.PI));

	protected final float MAX_DEFORMATION = 0.2f;
	protected final float MIN_RADIUS = 0.01f;// meters
	protected final float MAX_RADIUS = 1f;// meters

	protected HashSet<Vertex> verticesRes = new HashSet<Vertex>();

	protected long mLastSculptDurationMs = -1;

	private Managers mManagers = null;

	public ToolsBase(Managers managers)
	{
		mManagers = managers;
	}

	@Override
	public void Start(float xScreen, float yScreen)
	{

	}

	@Override
	public void Pick(float xScreen, float yScreen)
	{

	}

	@Override
	public void Stop()
	{

	}

	public Managers getManagers()
	{
		return mManagers;
	}

	public long getLastSculptDurationMs()
	{
		return mLastSculptDurationMs;
	}

}
