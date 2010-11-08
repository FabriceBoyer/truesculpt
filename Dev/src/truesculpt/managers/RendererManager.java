package truesculpt.managers;

import truesculpt.renderer.SphereRenderer;
import android.app.Activity;
import android.content.Context;

public class RendererManager extends BaseManager {

	private SphereRenderer mRenderer = null;

	public RendererManager(Context baseContext) {
		super(baseContext);

		mRenderer = new SphereRenderer(false);
	}

	/**
	 * @return the mRenderer
	 */
	public SphereRenderer getmRenderer() {
		return mRenderer;
	}

}
