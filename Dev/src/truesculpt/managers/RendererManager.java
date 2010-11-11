package truesculpt.managers;

import truesculpt.renderer.CubeRenderer;
import android.content.Context;
import android.opengl.GLSurfaceView.Renderer;

public class RendererManager extends BaseManager {

	private Renderer mRenderer = null;

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

}
