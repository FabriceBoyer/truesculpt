package truesculpt.managers;

import truesculpt.renderer.MainRenderer;
import android.content.Context;
import android.opengl.GLSurfaceView.Renderer;

public class RendererManager extends BaseManager {

	private MainRenderer mRenderer = null;

	public RendererManager(Context baseContext) {
		super(baseContext);

		mRenderer = new MainRenderer(false);
		onPointOfViewChange();
	}

	/**
	 * @return the mRenderer
	 */
	public Renderer getmRenderer() {
		return mRenderer;
	}
	
	public void onPointOfViewChange()
	{
		mRenderer.onPointOfViewChange(
				getManagers().getmPointOfViewManager().getRotationAngle(),
				getManagers().getmPointOfViewManager().getZoomDistance(),
				getManagers().getmPointOfViewManager().getElevationAngle());
	}

}
