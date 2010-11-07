package truesculpt.managers;

import android.app.Activity;

public class PointOfViewManager extends BaseManager {

	//camera pos
	private float mX=0.0f;
	private float mY=0.0f;
	private float mZ=10.0f;
	
	//looked at point
	private float mXOrig=0.0f;
	private float mYOrig=0.0f;
	private float mZOrig=0.0f;
	
	public PointOfViewManager(Activity mBaseActivity) {
		super(mBaseActivity);
		// TODO Auto-generated constructor stub
	}
	
	public void addZoomDistance(float dist)
	{
		
	}
	
	public void addRotationAngle(float angle)
	{
		
	}

}
