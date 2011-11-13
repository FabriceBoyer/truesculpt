package truesculpt.tools.base;

import java.util.HashSet;

import truesculpt.actions.SelectAction;
import truesculpt.main.Managers;
import truesculpt.managers.ToolsManager.ESymmetryMode;
import truesculpt.mesh.Face;
import truesculpt.mesh.Vertex;

public abstract class SelectionTool extends BaseTool
{
	protected final float FWHM = (float) (2f * Math.sqrt(2 * Math.log(2f)));// full width at half maximum
	protected final static float oneoversqrttwopi = (float) (1f / Math.sqrt(2f * Math.PI));

	protected final HashSet<Vertex> mVerticesRes = new HashSet<Vertex>();
	protected final HashSet<Vertex> mCumulatedVerticesRes = new HashSet<Vertex>();
	protected Vertex mLastVertex = null;
	protected Vertex mLastVertexSymmetry = null;
	protected final Path mPath = new Path();
	protected int mTriangleIndex = -1;

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
	}

	@Override
	public void Start(float xScreen, float yScreen)
	{
		super.Start(xScreen, yScreen);

		ResetData();

		mSigma = (float) ((Math.sqrt(mSquareMaxDistance) / 1.5f) / FWHM);
		mMaxGaussian = Gaussian(mSigma, 0);

		if (mAction == null)
		{
			mAction = new SelectAction();// Default action
		}
	}

	@Override
	public void Pick(float xScreen, float yScreen)
	{
		super.Pick(xScreen, yScreen);
	}

	@Override
	protected void PickInternal(float xScreen, float yScreen, ESymmetryMode mode)
	{
		mTriangleIndex = getManagers().getMeshManager().Pick(xScreen, yScreen, mode);

		if (mTriangleIndex >= 0 && mMesh != null && mAction != null)
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
