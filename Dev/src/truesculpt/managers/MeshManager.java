package truesculpt.managers;

import java.io.IOException;

import javax.microedition.khronos.opengles.GL10;

import truesculpt.mesh.Mesh;
import truesculpt.renderer.PickHighlight;
import truesculpt.renderer.RayPickDebug;
import truesculpt.utils.MatrixUtils;
import android.content.Context;
import android.os.SystemClock;

//for mesh storage, computation and transformation application
public class MeshManager extends BaseManager
{
	private String Name = "";
	private boolean bInitOver = true;
	float[] intersectPt = new float[3];

	class MeshInitTask implements Runnable
	{
		public int nSubdivionLevel = 0;
		public String strLastUsedFile = "";

		@Override
		public void run()
		{
			getManagers().getUtilsManager();
			if (getManagers().getOptionsManager().getLoadLastUsedFileAtStartup() && strLastUsedFile != "" && UtilsManager.CheckSculptureExist(strLastUsedFile))
			{
				OpenMeshBlocking(strLastUsedFile);
			}
			else
			{
				NewMeshBlocking(nSubdivionLevel);
			}
		}
	}

	public void NewMeshBlocking(int nSubdivionLevel)
	{
		bInitOver = false;

		mMesh = new Mesh(getManagers(), nSubdivionLevel);

		getManagers().getUtilsManager();
		Name = UtilsManager.GetDefaultFileName();

		bInitOver = true;

		NotifyListeners();
	}

	public void OpenMeshBlocking(String name)
	{
		bInitOver = false;

		mMesh = new Mesh(getManagers(), -1);
		Name = name;

		try
		{
			mMesh.ImportFromOBJ(getManagers().getUtilsManager().GetObjectFileName());
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}

		bInitOver = true;

		NotifyListeners();
	}

	MeshInitTask mInitTask = new MeshInitTask();// TODO move in a panel to get a waiting spinner

	long mLastPickDurationMs = -1;

	private final PickHighlight mPickHighlight = new PickHighlight();
	private final RayPickDebug mRay = new RayPickDebug();

	float[] rayPt1 = new float[3];
	float[] rayPt2 = new float[3];

	// Main Mesh test
	Mesh mMesh = null;

	public MeshManager(Context baseContext)
	{
		super(baseContext);

		intersectPt[0] = 0f;
		intersectPt[1] = 0f;
		intersectPt[2] = 1f;
	}

	public void draw(GL10 gl)
	{
		if (IsInitOver())
		{
			mMesh.draw(gl);

			if (getManagers().getOptionsManager().getDisplayDebugInfos())
			{
				mMesh.drawNormals(gl);
				mMesh.drawOctree(gl);

				// pick debug
				// mRay.draw(gl);
				// mPickHighlight.draw(gl);
			}
		}
	}

	public int getFacesCount()
	{
		int nCount = -1;
		if (IsInitOver())
		{
			nCount = mMesh.getFaceCount();
		}
		return nCount;
	}

	public long getLastPickDurationMs()
	{
		return mLastPickDurationMs;
	}

	public int getVertexCount()
	{
		int nCount = -1;
		if (IsInitOver())
		{
			nCount = mMesh.getVertexCount();
		}
		return nCount;
	}

	@Override
	public void onCreate()
	{
		InitMeshThreaded(5, getManagers().getOptionsManager().getLastUsedFile());// TODO adapt init level to power of machine
	}

	public boolean InitMeshThreaded(int nSubdivionLevel, String lastUsedFile)
	{
		boolean bRes = false;
		mInitTask.nSubdivionLevel = nSubdivionLevel;
		mInitTask.strLastUsedFile = lastUsedFile;

		if (bInitOver)
		{
			Thread thr = new Thread(null, mInitTask, "Mesh_Init");
			thr.start();
			bRes = true;
		}

		return bRes;
	}

	@Override
	public void onDestroy()
	{

	}

	// TODO threaded to improve GUI reactivity
	// pick is not an action
	public int Pick(float screenX, float screenY, ToolsManager.ESymmetryMode symmetryMode)
	{
		int nIndex = -1;

		if (IsInitOver())
		{
			// normalized z between -1 and 1
			getManagers().getRendererManager().getMainRenderer().GetWorldCoords(rayPt2, screenX, screenY, 1.0f);
			getManagers().getRendererManager().getMainRenderer().GetWorldCoords(rayPt1, screenX, screenY, -1.0f);

			mRay.setRayPos(rayPt1, rayPt2);

			switch (symmetryMode)
			{
			case X:
				rayPt1[0] *= -1;
				rayPt2[0] *= -1;
				break;
			case Y:
				rayPt1[1] *= -1;
				rayPt2[1] *= -1;
				break;
			case Z:
				rayPt1[2] *= -1;
				rayPt2[2] *= -1;
				break;
			}

			nIndex = PickRay();
		}

		return nIndex;
	}

	private int PickRay()
	{
		long tPickStart = SystemClock.uptimeMillis();

		int nIndex = mMesh.Pick(rayPt1, rayPt2, intersectPt);

		mLastPickDurationMs = SystemClock.uptimeMillis() - tPickStart;

		if (nIndex >= 0)
		{
			mPickHighlight.setPickHighlightPosition(intersectPt);
		}
		else
		{
			float[] zero = { 0, 0, 0 };
			mPickHighlight.setPickHighlightPosition(zero);
		}

		return nIndex;
	}

	public void getPickRayVector(float[] res)
	{
		MatrixUtils.minus(rayPt1, rayPt2, res);
		MatrixUtils.normalize(res);
	}

	public void getLastPickingPoint(float[] point)
	{
		MatrixUtils.copy(intersectPt, point);
	}

	public void setName(String name)
	{
		Name = name;
	}

	public String getName()
	{
		return Name;
	}

	public boolean IsInitOver()
	{
		return (mMesh != null) && bInitOver;
	}

	public void ImportFromOBJ(String objfilename) throws IOException
	{
		if (IsInitOver())
		{
			mMesh.ImportFromOBJ(objfilename);
		}
	}

	public void ExportToOBJ(String strObjFileName)
	{
		if (IsInitOver())
		{
			mMesh.ExportToOBJ(strObjFileName);
		}
	}

	public Mesh getMesh()
	{
		return mMesh;
	}

}
