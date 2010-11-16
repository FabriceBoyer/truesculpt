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
	private float fRot=0.0f;
	private float fElev=0.0f;
	
	private float fDemultFactor=5.0f;
	
	//ScaleGestureDetector mScaleGestureDetector = new ScaleGestureDetector();
	public void onTouchEvent(MotionEvent event)
	{
		//String msg="Pressure = " + Float.toString(event.getPressure());
		String msg="(x,y) = (" + Float.toString(event.getX()) +"," +Float.toString(event.getY()) + ")";

		Log.i(Global.TAG,msg);
		
		switch(event.getAction())		
		{
			case MotionEvent.ACTION_DOWN:
			{
				mLastX=event.getX();
				mLastY=event.getY();
				fRot=getManagers().getmPointOfViewManager().getRotationAngle();
				fElev=getManagers().getmPointOfViewManager().getElevationAngle();
				break;
			}
			case MotionEvent.ACTION_MOVE:
			{				
				float x=event.getX();
				float angleRot =fRot + (x-mLastX)/fDemultFactor;				
								
				float y=event.getY();
				float angleElev= fElev + (y-mLastY)/fDemultFactor;
								
				float dist =getManagers().getmPointOfViewManager().getZoomDistance();
				
				getManagers().getmPointOfViewManager().SetAllAngles(angleRot,angleElev,dist);
				
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
