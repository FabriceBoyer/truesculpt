package truesculpt.managers;

import truesculpt.utils.Global;
import android.app.Activity;
import android.util.Log;
import android.view.MotionEvent;
import android.view.*;

//To detect sculpture action, zoom and pan actions based on gesture
public class TouchManager extends BaseManager {

	public TouchManager(Activity mBaseActivity) {
		super(mBaseActivity);
		// TODO Auto-generated constructor stub
	}

	//ScaleGestureDetector mScaleGestureDetector = new ScaleGestureDetector();
	public void onTouchEvent(MotionEvent event)
	{
		//String msg="Pressure = " + Float.toString(event.getPressure());
		String msg="(x,y) = (" + Float.toString(event.getX()) +"," +Float.toString(event.getY()) + ")";

		Log.i(Global.TAG,msg);
		
	}
}
