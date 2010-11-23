package truesculpt.managers;

import truesculpt.utils.Global;
import android.content.Context;
import android.util.Log;
import android.view.MotionEvent;

//To detect sculpture action, zoom and pan actions based on gesture
public class TouchManager extends BaseManager {

	public TouchManager(Context baseContext) {
		super(baseContext);
		// TODO Auto-generated constructor stub
	}

	private float mLastX=0.0f;
	private float mLastY=0.0f;
	private float mRotInit=0.0f;
	private float mElevInit=0.0f;
	
	private float fDemultFactor=5.0f;
	
	//ScaleGestureDetector mScaleGestureDetector = new ScaleGestureDetector();
	public void onTouchEvent(MotionEvent event)
	{
		//String msg="Pressure = " + Float.toString(event.getPressure());
		String msg="(x,y) = (" + Float.toString(event.getX()) +"," +Float.toString(event.getY()) + ")";

		Log.i(Global.TAG,msg);
		
		float x=event.getX();
		float y=event.getY();
		
		switch (getManagers().getToolsManager().getToolMode())
		{
			case POV:
			{
				switch(event.getAction())		
				{
					case MotionEvent.ACTION_DOWN:
					{
						mLastX=x;
						mLastY=y;
						mRotInit=getManagers().getPointOfViewManager().getRotationAngle();
						mElevInit=getManagers().getPointOfViewManager().getElevationAngle();
						break;
					}
					case MotionEvent.ACTION_MOVE:
					{						
						float angleRot =mRotInit + (x-mLastX)/fDemultFactor;
						float angleElev= mElevInit + (y-mLastY)/fDemultFactor;										
						float dist =getManagers().getPointOfViewManager().getZoomDistance();
						
						getManagers().getPointOfViewManager().SetAllAngles(angleRot,angleElev,dist);
						
						break;
					}
				}
				break;
			}
			case SCULPT:
			{
				switch(event.getAction())		
				{
					case MotionEvent.ACTION_DOWN:
					{
						getManagers().getMeshManager().Pick(x, y);
					}					
				}
				break;
			}
		}
		
	}

	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		
	}
}
