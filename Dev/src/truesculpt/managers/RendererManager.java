package truesculpt.managers;

import truesculpt.renderer.SphereRenderer;
import android.app.Activity;

public class RendererManager extends BaseManager {

	private SphereRenderer mRenderer = null;
	
	public RendererManager(Activity baseActivity) {
		super(baseActivity);

		mRenderer = new SphereRenderer(false);
	}

	/**
	 * @return the mRenderer
	 */
	public SphereRenderer getmRenderer() {
		return mRenderer;
	}

}
