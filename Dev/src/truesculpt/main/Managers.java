package truesculpt.main;

import java.util.ArrayList;

import truesculpt.managers.ActionsManager;
import truesculpt.managers.BaseManager;
import truesculpt.managers.FileManager;
import truesculpt.managers.MemoryManager;
import truesculpt.managers.MeshManager;
import truesculpt.managers.OptionsManager;
import truesculpt.managers.PointOfViewManager;
import truesculpt.managers.RendererManager;
import truesculpt.managers.SensorsManager;
import truesculpt.managers.SleepPowerManager;
import truesculpt.managers.ToolsManager;
import truesculpt.managers.TouchManager;
import truesculpt.managers.UpdateManager;
import truesculpt.managers.UsageStatisticsManager;
import truesculpt.managers.UtilsManager;
import truesculpt.managers.WebManager;
import android.content.Context;

public class Managers
{
	private ArrayList<BaseManager> mManagersList = new ArrayList<BaseManager>();

	private ActionsManager mActionsManager = null;
	private FileManager mFileManager = null;
	private MemoryManager mMemoryManager = null;
	private MeshManager mMeshManager = null;
	private OptionsManager mOptionsManager = null;
	private PointOfViewManager mPointOfViewManager = null;
	private RendererManager mRendererManager = null;
	private SensorsManager mSensorsManager = null;
	private ToolsManager mToolsManager = null;
	private TouchManager mTouchManager = null;
	private UpdateManager mUpdateManager = null;
	private UsageStatisticsManager mUsageStatisticsManager = null;
	private SleepPowerManager mPowerManager = null;
	private UtilsManager mUtilsManager = null;
	private WebManager mWebManager = null;

	public Managers()
	{

	}

	public void Create()
	{
		for (BaseManager manager : mManagersList)
		{
			manager.onCreate();
		}
	}

	public void Destroy()
	{
		for (BaseManager manager : mManagersList)
		{
			manager.onDestroy();
		}
		mManagersList.clear();
	}

	public ActionsManager getActionsManager()
	{
		return mActionsManager;
	}

	public FileManager getFileManager()
	{
		return mFileManager;
	}

	public MemoryManager getMemoryManager()
	{
		return mMemoryManager;
	}

	public MeshManager getMeshManager()
	{
		return mMeshManager;
	}

	public OptionsManager getOptionsManager()
	{
		return mOptionsManager;
	}

	public PointOfViewManager getPointOfViewManager()
	{
		return mPointOfViewManager;
	}

	public RendererManager getRendererManager()
	{
		return mRendererManager;
	}

	public SensorsManager getSensorsManager()
	{
		return mSensorsManager;
	}

	public SleepPowerManager getSleepPowerManager()
	{
		return mPowerManager;
	}

	public ToolsManager getToolsManager()
	{
		return mToolsManager;
	}

	public TouchManager getTouchManager()
	{
		return mTouchManager;
	}

	public UpdateManager getUpdateManager()
	{
		return mUpdateManager;
	}

	public UsageStatisticsManager getUsageStatisticsManager()
	{
		return mUsageStatisticsManager;
	}

	public UtilsManager getUtilsManager()
	{
		return mUtilsManager;
	}
	
	public WebManager getWebManager()
	{
		return mWebManager;
	}	

	public void Init(Context baseContext)
	{
		mActionsManager = new ActionsManager(baseContext);
		mMemoryManager = new MemoryManager(baseContext);
		mMeshManager = new MeshManager(baseContext);
		mOptionsManager = new OptionsManager(baseContext);
		mPointOfViewManager = new PointOfViewManager(baseContext);
		mRendererManager = new RendererManager(baseContext);
		mSensorsManager = new SensorsManager(baseContext);
		mToolsManager = new ToolsManager(baseContext);
		mTouchManager = new TouchManager(baseContext);
		mUpdateManager = new UpdateManager(baseContext);
		mUsageStatisticsManager = new UsageStatisticsManager(baseContext);
		mFileManager = new FileManager(baseContext);
		mPowerManager = new SleepPowerManager(baseContext);
		mUtilsManager = new UtilsManager(baseContext);
		mWebManager = new WebManager(baseContext);

		mManagersList.add(mActionsManager);
		mManagersList.add(mFileManager);
		mManagersList.add(mMemoryManager);
		mManagersList.add(mMeshManager);
		mManagersList.add(mOptionsManager);
		mManagersList.add(mPointOfViewManager);
		mManagersList.add(mRendererManager);
		mManagersList.add(mSensorsManager);
		mManagersList.add(mToolsManager);
		mManagersList.add(mTouchManager);
		mManagersList.add(mUpdateManager);
		mManagersList.add(mUsageStatisticsManager);
		mManagersList.add(mPowerManager);
		mManagersList.add(mUtilsManager);
		mManagersList.add(mWebManager);

	}
}
