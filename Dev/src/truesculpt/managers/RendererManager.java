package truesculpt.managers;

import truesculpt.renderer.MainRenderer;
import android.content.Context;

//TODO : remove cause useless
public class RendererManager extends BaseManager
{
	private MainRenderer mRenderer = null;

	public RendererManager(Context baseContext)
	{
		super(baseContext);

		mRenderer = new MainRenderer(getManagers());
	}

	/**
	 * @return the mRenderer
	 */
	public MainRenderer getMainRenderer()
	{
		return mRenderer;
	}

	@Override
	public void onCreate()
	{

	}

	@Override
	public void onDestroy()
	{

	}
}
