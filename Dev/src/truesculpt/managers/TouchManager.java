package truesculpt.managers;

import truesculpt.managers.ToolsManager.EPovToolSubMode;
import truesculpt.managers.ToolsManager.ESymmetryMode;
import truesculpt.managers.ToolsManager.EToolMode;
import truesculpt.utils.MatrixUtils;
import android.content.Context;
import android.util.Log;
import android.view.MotionEvent;

//To detect sculpture action, zoom and pan actions based on gesture
public class TouchManager extends BaseManager
{
	private final float fDemultRotateFactor = 2.5f;
	private final float fDemultZoomFactor = fDemultRotateFactor * 20.0f;
	private final float fDemultPanFactor = fDemultRotateFactor * 40.0f;
	private final float fDemultRollFactor = fDemultRotateFactor * (float) (180 / Math.PI);

	private final float fTapTapTimeThresold = 500.0f;// ms
	private long mLastTapTapTime = 0;

	private boolean mbInit = false;

	private float mLastX = 0.0f;
	private float mLastY = 0.0f;
	private float mHeadInit = 0.0f;
	private float mPitchInit = 0.0f;

	private float mLastFingerSpacing = 0.0f;
	private float mZoomInit = 0.0f;

	private float mLastXPan = 0.0f;
	private float mLastYPan = 0.0f;
	private float mXPanInit = 0.0f;
	private float mYPanInit = 0.0f;

	private float mLastRollingOrientation = 0.0f;
	private float mRollInit = 0.0f;

	private boolean m_bUseSensors = false;

	public TouchManager(Context baseContext)
	{
		super(baseContext);

		UpdateUseSensors();

		lastHPR[0] = 0;
		lastHPR[1] = 0;
		lastHPR[2] = 0;

		// hprUnitTest();
	}

	private static void hprUnitTest()
	{
		int nFailureCount = 0;
		for (int i = 0; i < 100; i++)
		{
			boolean bSuccess = HPRUnitTesting((float) Math.random() * 360 - 180, (float) Math.random() * 360 - 180, (float) Math.random() * 360 - 180);
			if (!bSuccess) nFailureCount++;
		}
		Log.d("TOUCH", "FailureCount=" + nFailureCount);
	}

	public void UpdateUseSensors()
	{
		m_bUseSensors = getManagers().getOptionsManager().getUseSensorsToChangePOV();
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
		mHeadInit = getManagers().getPointOfViewManager().getHeadAngle();
		mPitchInit = getManagers().getPointOfViewManager().getPitchAngle();
		mRollInit = getManagers().getPointOfViewManager().getRollAngle();

		mbInit = true;
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

			// Rolling
			mRollInit = getManagers().getPointOfViewManager().getRollAngle();
			mLastRollingOrientation = (float) Math.atan2(event.getY(1) - event.getY(0), event.getX(1) - event.getX(0));

			getManagers().getToolsManager().setToolMode(EToolMode.POV);// two fingers is always POV
			getManagers().getToolsManager().setPovSubMode(EPovToolSubMode.ZOOM_PAN_ROLL);
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
			mbInit = false;
			break;
		}
		case MotionEvent.ACTION_MOVE:
		{
			switch (getManagers().getToolsManager().getToolMode())
			{
			case POV:
			{
				if (!m_bUseSensors && mbInit)
				{
					if (getManagers().getToolsManager().getPovSubMode() == EPovToolSubMode.ROTATE)
					{
						float angleHeadIncr = +(x - mLastX) / fDemultRotateFactor;
						float anglePitchIncr = +(y - mLastY) / fDemultRotateFactor;

						AddAnglesInCurrentFrame(mHeadInit, mPitchInit, mRollInit, angleHeadIncr, anglePitchIncr, 0);
					}
					if (getManagers().getToolsManager().getPovSubMode() == EPovToolSubMode.ZOOM_PAN_ROLL)
					{
						// Zoom
						float currFingersSpacing = getDistanceBetweenFingers(event);
						float currZoomDistIncr = -(currFingersSpacing - mLastFingerSpacing) / fDemultZoomFactor;

						getManagers().getPointOfViewManager().setZoomDistance(mZoomInit + currZoomDistIncr);

						// Pan
						float xCenter = (event.getX(0) + event.getX(1)) / 2;
						float yCenter = (event.getY(0) + event.getY(1)) / 2;
						float currXPanIncr = (xCenter - mLastXPan) / fDemultPanFactor;
						float currYPanIncr = -(yCenter - mLastYPan) / fDemultPanFactor;
						getManagers().getPointOfViewManager().setPanOffset(mXPanInit + currXPanIncr, mYPanInit + currYPanIncr);
						// Log.d("PAN", "New Pan X=" + currXPan + ", Y=" + mLastYPan + ", XCenter=" + xCenter + ", YCenter=" + yCenter);

						// Roll
						float currRollingOrientation = (float) Math.atan2(event.getY(1) - event.getY(0), event.getX(1) - event.getX(0));
						float currRollIncr = (mLastRollingOrientation - currRollingOrientation) * fDemultRollFactor;
						AddAnglesInCurrentFrame(mHeadInit, mPitchInit, mRollInit, 0, 0, currRollIncr);
						// Log.d("ROLL", "currRoll=" + currRoll + " , mLastRollingOrientation=" + mLastRollingOrientation + " , currRollingOrientation=" + currRollingOrientation + ", mRollInit=" + mRollInit);
					}
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

	private static final float[][] mX = new float[4][4];
	private static final float[][] mY = new float[4][4];
	private static final float[][] mZ = new float[4][4];
	private static final float[][] temp = new float[4][4];
	private static final float[][] rot1 = new float[4][4];
	private static final float[][] rot2 = new float[4][4];
	private static final float[][] rot3 = new float[4][4];
	private static final float[][] rotFinal = new float[4][4];
	private static final float[] hpr = new float[3];
	private static final float[] lastHPR = new float[3];
	private static final float[] VIncr = new float[4];
	private static final float[] Vtemp = new float[4];

	private void AddAnglesInCurrentFrame(float headInit, float pitchInit, float rollInit, float headIncr, float pitchIncr, float rollIncr)
	{
		Log.d("TOUCH", "HeadInit=" + headInit + " , PitchInit=" + pitchInit + " , RollInit=" + rollInit);
		Log.d("TOUCH", "HeadIncr=" + headIncr + " , PitchIncr=" + pitchIncr + " , RollIncr=" + rollIncr);

		headInit *= Math.PI / 180;
		pitchInit *= Math.PI / 180;
		rollInit *= Math.PI / 180;
		headIncr *= Math.PI / 180;
		pitchIncr *= Math.PI / 180;
		rollIncr *= Math.PI / 180;

		MatrixUtils.rotateMatrixX(mX, pitchInit);
		MatrixUtils.rotateMatrixY(mY, headInit);
		MatrixUtils.rotateMatrixZ(mZ, rollInit);
		MatrixUtils.multiply(mZ, mY, temp);
		MatrixUtils.multiply(temp, mX, rot1);

		VIncr[0] = pitchIncr;
		VIncr[1] = headIncr;
		VIncr[2] = rollIncr;
		VIncr[3] = 0;
		MatrixUtils.multiply(rot1, VIncr, Vtemp);

		MatrixUtils.rotateMatrixX(mX, Vtemp[0]);
		MatrixUtils.rotateMatrixY(mY, Vtemp[1]);
		MatrixUtils.rotateMatrixZ(mZ, Vtemp[2]);
		MatrixUtils.multiply(mZ, mY, temp);
		MatrixUtils.multiply(temp, mX, rot2);

		MatrixUtils.multiply(rot2, rot1, rotFinal);

		// MatrixUtils.printMatrix("TOUCH", "mX", mX);
		// MatrixUtils.printMatrix("TOUCH", "mY", mY);
		// MatrixUtils.printMatrix("TOUCH", "mZ", mZ);
		// MatrixUtils.printMatrix("TOUCH", "rot2", rot2);
		// MatrixUtils.printMatrix("TOUCH", "rotFinal", rotFinal);

		float head = headInit + headIncr;
		float pitch = pitchInit + pitchIncr;
		float roll = rollInit + rollIncr;

		// MatrixUtils.rotationMatrixToHPR(rotFinal, hpr, lastHPR);
		// pitch = hpr[0];
		// head = hpr[1];
		// roll = hpr[2];
		//
		// MatrixUtils.copy(hpr, lastHPR);

		head *= 180 / (float) Math.PI;
		pitch *= 180 / (float) Math.PI;
		roll *= 180 / (float) Math.PI;

		Log.d("TOUCH", "Head=" + head + " , Pitch=" + pitch + " , Roll=" + roll);
		Log.d("TOUCH", "  ");

		getManagers().getPointOfViewManager().SetAllAngles(head, pitch, roll);
	}

	private static boolean HPRUnitTesting(float head, float pitch, float roll)
	{
		boolean bSuccess = false;

		float headInit = head;
		float pitchInit = pitch;
		float rollInit = roll;

		Log.d("TOUCH", "Init : Head_X=" + head + " , Pitch_Y=" + pitch + " , Roll_Z=" + roll);

		head *= Math.PI / 180;
		pitch *= Math.PI / 180;
		roll *= Math.PI / 180;

		MatrixUtils.rotateMatrixX(mX, head);
		MatrixUtils.rotateMatrixY(mY, pitch);
		MatrixUtils.rotateMatrixZ(mZ, roll);
		MatrixUtils.multiply(mZ, mY, temp);
		MatrixUtils.multiply(temp, mX, rotFinal);

		MatrixUtils.rotationMatrixToHPR(rotFinal, hpr, lastHPR);

		head = hpr[0];
		pitch = hpr[1];
		roll = hpr[2];

		head *= 180 / (float) Math.PI;
		pitch *= 180 / (float) Math.PI;
		roll *= 180 / (float) Math.PI;

		Log.d("TOUCH", "End : Head_X=" + head + " , Pitch_Y=" + pitch + " , Roll_Z=" + roll);

		if (Math.round(head) == Math.round(headInit) && Math.round(pitch) == Math.round(pitchInit) && Math.round(roll) == Math.round(rollInit))
		{
			bSuccess = true;
		}
		Log.d("TOUCH", bSuccess ? "SUCCESS" : "FAILURE");
		Log.d("TOUCH", "  ");

		return bSuccess;
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
