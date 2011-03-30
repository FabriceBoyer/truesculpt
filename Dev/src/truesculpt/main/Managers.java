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

	/**
	 * @return the mActionsManager
	 */
	public ActionsManager getActionsManager()
	{
		return mActionsManager;
	}

	/**
	 * @return the mFileManager
	 */
	public FileManager getFileManager()
	{
		return mFileManager;
	}

	/**
	 * @return the mMemoryManager
	 */
	public MemoryManager getMemoryManager()
	{
		return mMemoryManager;
	}

	/**
	 * @return the mMeshManager
	 */
	public MeshManager getMeshManager()
	{
		return mMeshManager;
	}

	/**
	 * @return the mOptionsManager
	 */
	public OptionsManager getOptionsManager()
	{
		return mOptionsManager;
	}

	/**
	 * @return the mPointOfViewManager
	 */
	public PointOfViewManager getPointOfViewManager()
	{
		return mPointOfViewManager;
	}

	/**
	 * @return the mRendererManager
	 */
	public RendererManager getRendererManager()
	{
		return mRendererManager;
	}

	/**
	 * @return the mSensorsManager
	 */
	public SensorsManager getSensorsManager()
	{
		return mSensorsManager;
	}

	public SleepPowerManager getSleepPowerManager()
	{
		return mPowerManager;
	}

	/**
	 * @return the mToolsManager
	 */
	public ToolsManager getToolsManager()
	{
		return mToolsManager;
	}

	/**
	 * @return the mTouchManager
	 */
	public TouchManager getTouchManager()
	{
		return mTouchManager;
	}

	/**
	 * @return the mUpdateManager
	 */
	public UpdateManager getUpdateManager()
	{
		return mUpdateManager;
	}

	/**
	 * @return the mUsageStatisticsManager
	 */
	public UsageStatisticsManager getUsageStatisticsManager()
	{
		return mUsageStatisticsManager;
	}

	public UtilsManager getUtilsManager()
	{
		return mUtilsManager;
	}

	public void Init(Context baseContext)
	{

		// init
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

	}
}
