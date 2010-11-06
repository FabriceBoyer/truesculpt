package truesculpt.managers;

import android.app.Activity;

public class ManagersManager extends BaseManager {

	public ManagersManager(Activity baseActivity) {
		super(baseActivity);
		// TODO Auto-generated constructor stub
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

	private ActionsManager mActionsManager=new ActionsManager(getBaseActivity());
	private MemoryManager mMemoryManager=new MemoryManager(getBaseActivity());
	private MeshManager mMeshManager=new MeshManager(getBaseActivity());
	private OptionsManager mOptionsManager=new OptionsManager(getBaseActivity());
	private PointOfViewManager mPointOfViewManager=new PointOfViewManager(getBaseActivity());
	private RendererManager mRendererManager=new RendererManager(getBaseActivity());
	private SensorsManager mSensorsManager=new SensorsManager(getBaseActivity());
	private ToolsManager mToolsManager=new ToolsManager(getBaseActivity());
	private TouchManager mTouchManager=new TouchManager(getBaseActivity());
	private UpdateManager mUpdateManager=new UpdateManager(getBaseActivity());
	private WebManager mWebManager=new WebManager(getBaseActivity());
	
}
