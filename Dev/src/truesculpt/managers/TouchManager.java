package truesculpt.managers;

import truesculpt.managers.ToolsManager.EPovToolSubMode;
import truesculpt.managers.ToolsManager.ESymmetryMode;
import truesculpt.managers.ToolsManager.EToolMode;
import android.content.Context;
import android.util.Log;
import android.view.MotionEvent;

//To detect sculpture action, zoom and pan actions based on gesture
public class TouchManager extends BaseManager
{

	private final float fDemultRotateFactor = 2.5f;

	private final float fDemultZoomFactor = fDemultRotateFactor * 20.0f;
	private final float fDemultPanFactor = fDemultRotateFactor * 40.0f;
	private final float fTapTapTimeThresold = 500.0f;// ms
	private float mElevInit = 0.0f;
	private float mLastFingerSpacing = 0.0f;
	private long mLastTapTapTime = 0;
	private float mLastX = 0.0f;
	private float mLastY = 0.0f;
	private float mLastXPan = 0.0f;
	private float mLastYPan = 0.0f;

	private float mRotInit = 0.0f;
	private float mZoomInit = 0.0f;
	private float mXPanInit = 0.0f;
	private float mYPanInit = 0.0f;

	public TouchManager(Context baseContext)
	{
		super(baseContext);
	}

	/** Show an event in the LogCat view, for debugging */
	private static void dumpEvent(MotionEvent event)
	{
		String names[] = { "DOWN", "UP", "MOVE", "CANCEL", "OUTSIDE", "POINTER_DOWN", "POINTER_UP", "7?", "8?", "9?" };
		StringBuilder sb = new StringBuilder();
		int action = event.getAction();
		int actionCode = action & MotionEvent.ACTION_MASK;
		sb.append("event ACTION_").append(names[actionCode]);
		if (actionCode == MotionEvent.ACTION_POINTER_DOWN || actionCode == MotionEvent.ACTION_POINTER_UP)
		{
			sb.append("(pid ").append(action >> MotionEvent.ACTION_POINTER_ID_SHIFT);
			sb.append(")");
		}
		sb.append("[");
		for (int i = 0; i < event.getPointerCount(); i++)
		{
			sb.append("#").append(i);
			sb.append("(pid ").append(event.getPointerId(i));
			sb.append(")=").append((int) event.getX(i));
			sb.append(",").append((int) event.getY(i));
			if (i + 1 < event.getPointerCount())
			{
				sb.append(";");
			}
		}
		sb.append("]");
		Log.d("POINTER", sb.toString());
	}

	private static float getDistanceBetweenFingers(MotionEvent event)
	{
		float x = event.getX(0) - event.getX(1);
		float y = event.getY(0) - event.getY(1);
		return (float) Math.sqrt(x * x + y * y);
	}

	private void initPOVValues(MotionEvent event, boolean bZoomGestureEnd)
	{
		int nIndex = 0;

		int nCount = event.getPointerCount();

		if (bZoomGestureEnd)
		{
			int action = event.getAction();
			nIndex = action >> MotionEvent.ACTION_POINTER_ID_SHIFT;
			nIndex++;// take another index (non risen up)
		}

		if (nIndex < 0)
		{
			nIndex = nCount - 1;
		}
		if (nIndex >= nCount)
		{
			nIndex = 0;
		}

		// Log.i("POINTER", "Init POV values with finger index=" + Integer.toString(nIndex));
		float x = event.getX(nIndex);
		float y = event.getY(nIndex);

		mLastX = x;
		mLastY = y;
		mRotInit = getManagers().getPointOfViewManager().getRotationAngle();
		mElevInit = getManagers().getPointOfViewManager().getElevationAngle();
	}

	@Override
	public void onCreate()
	{

	}

	@Override
	public void onDestroy()
	{

	}

	// ScaleGestureDetector mScaleGestureDetector = new ScaleGestureDetector();
	public int onTouchEvent(MotionEvent event)
	{
		int nReturn = 0;

		// String msg="Pressure = " + Float.toString(event.getPressure());
		// String msg="(x,y) = (" + Float.toString(event.getX()) +","
		// +Float.toString(event.getY()) + ")";
		// Log.i(Global.TAG,msg);

		// dumpEvent(event);

		float x = event.getX();
		float y = event.getY();

		int action = event.getAction();
		int actionCode = action & MotionEvent.ACTION_MASK;

		switch (actionCode)
		{
		case MotionEvent.ACTION_DOWN:
		{
			long curTapTapTime = System.currentTimeMillis();
			if (curTapTapTime - mLastTapTapTime < fTapTapTimeThresold)
			{
				StartTapTapAction();
				nReturn = 1;
			}
			mLastTapTapTime = curTapTapTime;

			initPOVValues(event, false);
			getManagers().getToolsManager().setPovSubMode(EPovToolSubMode.ROTATE);

			int nRes = getManagers().getMeshManager().Pick(x, y, ESymmetryMode.NONE);// not tool pick, but mesh pick
			if (nRes < 0)
			{
				getManagers().getToolsManager().setToolMode(EToolMode.POV);
			}
			else
			{
				getManagers().getToolsManager().setToolMode(EToolMode.SCULPT);
				getManagers().getToolsManager().getCurrentTool().Start(x, y);
				getManagers().getToolsManager().getCurrentTool().Pick(x, y);
			}

			break;
		}

		case MotionEvent.ACTION_POINTER_DOWN:
		{
			// Zoom
			mZoomInit = getManagers().getPointOfViewManager().getZoomDistance();
			mLastFingerSpacing = getDistanceBetweenFingers(event);

			// Pan
			mXPanInit = getManagers().getPointOfViewManager().getXPanOffset();
			mYPanInit = getManagers().getPointOfViewManager().getYPanOffset();
			mLastXPan = (event.getX(0) + event.getX(1)) / 2;
			mLastYPan = (event.getY(0) + event.getY(1)) / 2;
			// Log.d("PAN", "Pan init XInit=" + mXPanInit + ", YInit=" + mXPanInit + ", XLast=" + mLastXPan + ", YLast=" + mLastYPan);

			getManagers().getToolsManager().setPovSubMode(EPovToolSubMode.ZOOM_AND_PAN);
			break;
		}
		case MotionEvent.ACTION_POINTER_UP:
		{
			initPOVValues(event, true);// reinit rotate values

			getManagers().getToolsManager().setPovSubMode(EPovToolSubMode.ROTATE);
			break;
		}
		case MotionEvent.ACTION_UP:
		{
			getManagers().getToolsManager().setPovSubMode(EPovToolSubMode.ROTATE);
			getManagers().getToolsManager().getCurrentTool().Pick(x, y);
			getManagers().getToolsManager().getCurrentTool().Stop(x, y);
			break;
		}
		case MotionEvent.ACTION_MOVE:
		{
			switch (getManagers().getToolsManager().getToolMode())
			{
			case POV:
			{
				if (getManagers().getToolsManager().getPovSubMode() == EPovToolSubMode.ROTATE)
				{
					float angleRot = mRotInit + (x - mLastX) / fDemultRotateFactor;
					float angleElev = mElevInit + (y - mLastY) / fDemultRotateFactor;
					float dist = getManagers().getPointOfViewManager().getZoomDistance();

					getManagers().getPointOfViewManager().SetAllAngles(angleRot, angleElev, dist);
				}
				if (getManagers().getToolsManager().getPovSubMode() == EPovToolSubMode.ZOOM_AND_PAN)
				{
					// Zoom
					float currFingersSpacing = getDistanceBetweenFingers(event);
					float newZoomDist = mZoomInit - (currFingersSpacing - mLastFingerSpacing) / fDemultZoomFactor;

					getManagers().getPointOfViewManager().setZoomDistance(newZoomDist);

					// Pan
					float xCenter = (event.getX(0) + event.getX(1)) / 2;
					float yCenter = (event.getY(0) + event.getY(1)) / 2;
					float currXPan = mXPanInit + (xCenter - mLastXPan) / fDemultPanFactor;
					float currYPan = mYPanInit - (yCenter - mLastYPan) / fDemultPanFactor;
					getManagers().getPointOfViewManager().setPanOffset(currXPan, currYPan);
					// Log.d("PAN", "New Pan X=" + currXPan + ", Y=" + mLastYPan + ", XCenter=" + xCenter + ", YCenter=" + yCenter);
				}

				break;
			}

			case SCULPT:
			{
				getManagers().getToolsManager().getCurrentTool().Pick(x, y);
				break;
			}
			}

			break;
		}
		}

		return nReturn;
	}

	private void StartTapTapAction()
	{
		switch (getManagers().getToolsManager().getToolMode())
		{
		case POV:
		{
			break;
		}
		case SCULPT:
		{
			break;
		}
		}
	}
}
