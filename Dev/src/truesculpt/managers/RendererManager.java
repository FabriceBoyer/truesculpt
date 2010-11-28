package truesculpt.managers;

import truesculpt.renderer.MainRenderer;
import android.content.Context;

public class RendererManager extends BaseManager
{

	private MainRenderer mRenderer = null;

	public RendererManager(Context baseContext)
	{
		super(baseContext);

		mRenderer = new MainRenderer(getManagers());
		onPointOfViewChange();
	}

	/**
	 * @return the mRenderer
	 */
	public MainRenderer getmRenderer()
	{
		return mRenderer;
	}

	@Override
	public void onCreate()
	{
		// TODO Auto-generated method stub

	}

	@Override
	public void onDestroy()
	{
		// TODO Auto-generated method stub

	}

	public void onPointOfViewChange()
	{
		PointOfViewManager povManager = getManagers().getPointOfViewManager();
		mRenderer.onPointOfViewChange(povManager.getRotationAngle(), povManager.getZoomDistance(), povManager.getElevationAngle());
	}

}
