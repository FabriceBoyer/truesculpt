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

	//ScaleGestureDetector mScaleGestureDetector = new ScaleGestureDetector();
	public void onTouchEvent(MotionEvent event)
	{
		//String msg="Pressure = " + Float.toString(event.getPressure());
		String msg="(x,y) = (" + Float.toString(event.getX()) +"," +Float.toString(event.getY()) + ")";

		Log.i(Global.TAG,msg);
		
		if (event.getAction()==MotionEvent.ACTION_MOVE)
		{
			float fRot=getManagers().getmPointOfViewManager().getRotationAngle();
			fRot+=(event.getX()-event.getHistoricalX(0));
			getManagers().getmPointOfViewManager().setRotationAngle(fRot);
			
			float fElev=getManagers().getmPointOfViewManager().getElevationAngle();
			fElev+=(event.getY()-event.getHistoricalY(0));
			getManagers().getmPointOfViewManager().setElevationAngle(fElev);
		}
		
	}
}
