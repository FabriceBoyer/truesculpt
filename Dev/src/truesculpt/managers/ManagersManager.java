package truesculpt.managers;

import android.app.Activity;

public class ManagersManager {

	public ManagersManager() {

	}

	/**
	 * @return the mActionsManager
	 */
	public ActionsManager getmActionsManager() {
		return mActionsManager;
	}

	/**
	 * @return the mMemoryManager
	 */
	public MemoryManager getmMemoryManager() {
		return mMemoryManager;
	}

	/**
	 * @return the mMeshManager
	 */
	public MeshManager getmMeshManager() {
		return mMeshManager;
	}

	/**
	 * @return the mOptionsManager
	 */
	public OptionsManager getmOptionsManager() {
		return mOptionsManager;
	}

	/**
	 * @return the mPointOfViewManager
	 */
	public PointOfViewManager getmPointOfViewManager() {
		return mPointOfViewManager;
	}

	/**
	 * @return the mRendererManager
	 */
	public RendererManager getmRendererManager() {
		return mRendererManager;
	}

	/**
	 * @return the mSensorsManager
	 */
	public SensorsManager getmSensorsManager() {
		return mSensorsManager;
	}

	/**
	 * @return the mToolsManager
	 */
	public ToolsManager getmToolsManager() {
		return mToolsManager;
	}

	/**
	 * @return the mTouchManager
	 */
	public TouchManager getmTouchManager() {
		return mTouchManager;
	}

	/**
	 * @return the mUpdateManager
	 */
	public UpdateManager getmUpdateManager() {
		return mUpdateManager;
	}

	/**
	 * @return the mWebManager
	 */
	public WebManager getmWebManager() {
		return mWebManager;
	}

	/**
	 * @return the mUsageStatisticsManager
	 */
	public UsageStatisticsManager getmUsageStatisticsManager() {
		return mUsageStatisticsManager;
	}

	
	private ActionsManager mActionsManager = null;
	private MemoryManager mMemoryManager = null;
	private MeshManager mMeshManager = null;
	private OptionsManager mOptionsManager = null;
	private PointOfViewManager mPointOfViewManager = null;
	private RendererManager mRendererManager = null;
	private SensorsManager mSensorsManager = null;
	private ToolsManager mToolsManager = null;
	private TouchManager mTouchManager = null;
	private UpdateManager mUpdateManager = null;
	private WebManager mWebManager = null;
	private UsageStatisticsManager mUsageStatisticsManager = null;

	
	public void Init(Activity baseActivity) {
		mActionsManager = new ActionsManager(baseActivity);
		mMemoryManager = new MemoryManager(baseActivity);
		mMeshManager = new MeshManager(baseActivity);
		mOptionsManager = new OptionsManager(baseActivity);
		mPointOfViewManager = new PointOfViewManager(baseActivity);
		mRendererManager = new RendererManager(baseActivity);
		mSensorsManager = new SensorsManager(baseActivity);
		mToolsManager = new ToolsManager(baseActivity);
		mTouchManager = new TouchManager(baseActivity);
		mUpdateManager = new UpdateManager(baseActivity);
		mWebManager = new WebManager(baseActivity);
		mUsageStatisticsManager= new UsageStatisticsManager(baseActivity);
	}

}
