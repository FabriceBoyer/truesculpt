package truesculpt.managers;

import truesculpt.renderer.CubeRenderer;
import android.content.Context;
import android.opengl.GLSurfaceView.Renderer;

public class RendererManager extends BaseManager {

	private CubeRenderer mRenderer = null;

	public RendererManager(Context baseContext) {
		super(baseContext);

		mRenderer = new CubeRenderer(false);
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
