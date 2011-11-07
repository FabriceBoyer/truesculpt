package truesculpt.tools.base;

import java.util.HashSet;

import truesculpt.actions.BaseAction;
import truesculpt.main.Managers;
import truesculpt.managers.ToolsManager.ESymmetryMode;
import truesculpt.mesh.Face;
import truesculpt.mesh.Mesh;
import truesculpt.mesh.Vertex;
import android.os.SystemClock;

public abstract class ToolsBase implements ITools
{
	protected final float FWHM = (float) (2f * Math.sqrt(2 * Math.log(2f)));// full width at half maximum
	protected final static float oneoversqrttwopi = (float) (1f / Math.sqrt(2f * Math.PI));

	protected final float MAX_DEFORMATION = 0.2f;
	protected final float MIN_RADIUS = 0.01f;// meters
	protected final float MAX_RADIUS = 1f;// meters

	protected final HashSet<Vertex> mVerticesRes = new HashSet<Vertex>();
	protected final HashSet<Vertex> mCumulatedVerticesRes = new HashSet<Vertex>();
	protected Vertex mLastVertex = null;
	protected final Path mPath = new Path();
	protected long mLastSculptDurationMs = -1;

	protected BaseAction mAction = null;
	protected Mesh mMesh = null;

	protected int nTriangleIndex = -1;
	protected float fMaxDeformation = -1;
	protected float sqMaxDist = -1;
	protected float MaxDist = -1;

	protected float sigma = -1;
	protected float maxGaussian = -1;

	private long tSculptStart = -1;
	private Managers mManagers = null;

	public ToolsBase(Managers managers)
	{
		mManagers = managers;
	}

	abstract protected void Work();

	private void ResetData()
	{
		mVerticesRes.clear();
		mCumulatedVerticesRes.clear();
		mLastVertex = null;
		mPath.Clear();
		mAction = null;
	}

	@Override
	public void Start(float xScreen, float yScreen)
	{
		ResetData();

		mMesh = getManagers().getMeshManager().getMesh();

		sqMaxDist = (float) Math.pow((MAX_RADIUS - MIN_RADIUS) * getManagers().getToolsManager().getRadius() / 100f + MIN_RADIUS, 2);
		MaxDist = (float) Math.sqrt(sqMaxDist);
		fMaxDeformation = getManagers().getToolsManager().getStrength() / 100.0f * MAX_DEFORMATION;// strength is -100 to 100
		sigma = (float) ((Math.sqrt(sqMaxDist) / 1.5f) / FWHM);
		maxGaussian = Gaussian(sigma, 0);
	}

	@Override
	public void Pick(float xScreen, float yScreen)
	{
		tSculptStart = SystemClock.uptimeMillis();

		PickInternal(xScreen, yScreen, ESymmetryMode.NONE);

		// symmetry handling
		switch (getManagers().getToolsManager().getSymmetryMode())
		{
		case NONE:
			break;
		case X:
			PickInternal(xScreen, yScreen, ESymmetryMode.X);
			break;
		case Y:
			PickInternal(xScreen, yScreen, ESymmetryMode.Y);
			break;
		case Z:
			PickInternal(xScreen, yScreen, ESymmetryMode.Z);
			break;
		case XY:
			PickInternal(xScreen, yScreen, ESymmetryMode.X);
			PickInternal(xScreen, yScreen, ESymmetryMode.Y);
			break;
		case YZ:
			PickInternal(xScreen, yScreen, ESymmetryMode.Y);
			PickInternal(xScreen, yScreen, ESymmetryMode.Z);
			break;
		case XZ:
			PickInternal(xScreen, yScreen, ESymmetryMode.X);
			PickInternal(xScreen, yScreen, ESymmetryMode.Z);
			break;
		}

		mLastSculptDurationMs = SystemClock.uptimeMillis() - tSculptStart;
	}

	private void PickInternal(float xScreen, float yScreen, ESymmetryMode mode)
	{
		nTriangleIndex = getManagers().getMeshManager().Pick(xScreen, yScreen, mode);

		if (nTriangleIndex >= 0 && mMesh != null)
		{
			Face face = mMesh.mFaceList.get(nTriangleIndex);
			int nOrigVertex = face.E0.V0;// TODO choose closest point in triangle from pick point
			Vertex origVertex = mMesh.mVertexList.get(nOrigVertex);

			mVerticesRes.clear();
			mMesh.GetVerticesAtDistanceFromSegment(origVertex, mLastVertex, sqMaxDist, mVerticesRes);
			mCumulatedVerticesRes.addAll(mVerticesRes);

			// Main tool call
			Work();

			mLastVertex = origVertex;

			getManagers().getMeshManager().NotifyListeners();
		}
	}

	@Override
	public void Stop(float xScreen, float yScreen)
	{
		if (mAction != null)
		{
			getManagers().getActionsManager().AddUndoAction(mAction);
			mAction.DoAction();
		}

		// last distance reset
		for (Vertex vertex : mCumulatedVerticesRes)
		{
			vertex.mLastTempSqDistance = -1.f;
		}

		ResetData();

		getManagers().getMeshManager().NotifyListeners();
	}

	protected Managers getManagers()
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
