package truesculpt.tools.base;

import java.util.HashSet;

import truesculpt.main.Managers;
import truesculpt.mesh.Vertex;
import android.os.SystemClock;

public class ToolsBase implements ITools
{
	protected final float FWHM = (float) (2f * Math.sqrt(2 * Math.log(2f)));// full width at half maximum
	protected final static float oneoversqrttwopi = (float) (1f / Math.sqrt(2f * Math.PI));

	protected final float MAX_DEFORMATION = 0.2f;
	protected final float MIN_RADIUS = 0.01f;// meters
	protected final float MAX_RADIUS = 1f;// meters

	protected final HashSet<Vertex> verticesRes = new HashSet<Vertex>();
	protected final HashSet<Vertex> cumulatedVerticesRes = new HashSet<Vertex>();
	protected Vertex mLastVertex = null;
	protected final Path mPath = new Path();
	protected long mLastSculptDurationMs = -1;
	protected long tSculptStart = -1;

	private Managers mManagers = null;

	public ToolsBase(Managers managers)
	{
		mManagers = managers;
	}

	@Override
	public void Start(float xScreen, float yScreen)
	{
		cumulatedVerticesRes.clear();
		mLastVertex = null;
		mPath.Clear();
	}

	@Override
	public void Pick(float xScreen, float yScreen)
	{
		tSculptStart = SystemClock.uptimeMillis();
	}

	public void EndPick()
	{
		mLastSculptDurationMs = SystemClock.uptimeMillis() - tSculptStart;
	}

	@Override
	public void Stop(float xScreen, float yScreen)
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

	protected static float Gaussian(float sigma, float sqDist)
	{
		return (float) (oneoversqrttwopi / sigma * Math.exp(-sqDist / (2 * sigma * sigma)));
	}
}
