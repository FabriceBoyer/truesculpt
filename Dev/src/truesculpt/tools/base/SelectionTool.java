package truesculpt.tools.base;

import java.util.HashSet;

import truesculpt.actions.BaseAction;
import truesculpt.main.Managers;
import truesculpt.managers.ToolsManager.ESymmetryMode;
import truesculpt.mesh.Face;
import truesculpt.mesh.Vertex;
import android.os.SystemClock;

public abstract class SelectionTool extends BaseTool
{
	protected final float FWHM = (float) (2f * Math.sqrt(2 * Math.log(2f)));// full width at half maximum
	protected final static float oneoversqrttwopi = (float) (1f / Math.sqrt(2f * Math.PI));

	protected final float MIN_RADIUS = 0.01f;// meters
	protected final float MAX_RADIUS = 1f;// meters

	protected final HashSet<Vertex> mVerticesRes = new HashSet<Vertex>();
	protected final HashSet<Vertex> mCumulatedVerticesRes = new HashSet<Vertex>();
	protected Vertex mLastVertex = null;
	protected Vertex mLastVertexSymmetry = null;
	protected final Path mPath = new Path();
	protected int mTriangleIndex = -1;
	protected float mSquareMaxDistance = -1;
	protected float mMaxDistance = -1;
	protected BaseAction mAction = null;
	protected float mSigma = -1;
	protected float mMaxGaussian = -1;
	protected Vertex mOrigVertex = null;
	protected int nOrigVertex = -1;

	public SelectionTool(Managers managers)
	{
		super(managers);
	}

	abstract protected void Work();

	private void ResetData()
	{
		mVerticesRes.clear();
		mCumulatedVerticesRes.clear();
		mLastVertex = null;
		mLastVertexSymmetry = null;
		mPath.Clear();
		mAction = null;
	}

	@Override
	public void Start(float xScreen, float yScreen)
	{
		super.Start(xScreen, yScreen);

		ResetData();

		mSquareMaxDistance = (float) Math.pow((MAX_RADIUS - MIN_RADIUS) * getManagers().getToolsManager().getRadius() / 100f + MIN_RADIUS, 2);
		mMaxDistance = (float) Math.sqrt(mSquareMaxDistance);

		mSigma = (float) ((Math.sqrt(mSquareMaxDistance) / 1.5f) / FWHM);
		mMaxGaussian = Gaussian(mSigma, 0);
	}

	@Override
	public void Pick(float xScreen, float yScreen)
	{
		super.Pick(xScreen, yScreen);

		tSculptStart = SystemClock.uptimeMillis();

		// symmetry handling
		switch (getManagers().getToolsManager().getSymmetryMode())
		{
		case NONE:
			// nop
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
		case YZ:
		case XZ:
			// not handled at present time
			break;
		}

		// Regular pick always done
		PickInternal(xScreen, yScreen, ESymmetryMode.NONE);

		mLastSculptDurationMs = SystemClock.uptimeMillis() - tSculptStart;
	}

	private void PickInternal(float xScreen, float yScreen, ESymmetryMode mode)
	{
		mTriangleIndex = getManagers().getMeshManager().Pick(xScreen, yScreen, mode);

		if (mTriangleIndex >= 0 && mMesh != null)
		{
			Face face = mMesh.mFaceList.get(mTriangleIndex);
			nOrigVertex = face.E0.V0;// TODO choose closest point in triangle from pick point
			mOrigVertex = mMesh.mVertexList.get(nOrigVertex);

			mVerticesRes.clear();
			Vertex currLastVertex = mLastVertex;
			if (mode != ESymmetryMode.NONE)
			{
				currLastVertex = mLastVertexSymmetry;
			}

			mMesh.GetVerticesAtDistanceFromSegment(mOrigVertex, currLastVertex, mSquareMaxDistance, mVerticesRes);

			mCumulatedVerticesRes.addAll(mVerticesRes);// shared for symmetry and regular pick

			// Main tool call
			Work();

			if (mode != ESymmetryMode.NONE)
			{
				mLastVertexSymmetry = mOrigVertex;
			}
			else
			{
				mLastVertex = mOrigVertex;
			}

			getManagers().getMeshManager().NotifyListeners();
		}
	}

	@Override
	public void Stop(float xScreen, float yScreen)
	{
		super.Stop(xScreen, yScreen);

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

	protected static float Gaussian(float sigma, float sqDist)
	{
		return (float) (oneoversqrttwopi / sigma * Math.exp(-sqDist / (2 * sigma * sigma)));
	}
}
