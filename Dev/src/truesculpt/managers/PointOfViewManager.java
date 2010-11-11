package truesculpt.managers;

import java.util.Vector;

import truesculpt.managers.SensorsManager.OnSensorChangeListener;
import truesculpt.utils.Utils;

import android.content.Context;

public class PointOfViewManager extends BaseManager implements OnSensorChangeListener {

	//camera pos
	private float mX=0.0f;
	
	//looked at point
	private float mXOrig=0.0f;
	private float mY=0.0f;
	private float mYOrig=0.0f;	
	private float mZ=10.0f;	
	private float mZOrig=0.0f;
	
	private float mR=10.0f;
	private float mTheta=0.0f;
	private float mPhi=0.0f;
	
	public PointOfViewManager(Context baseContext) {
		super(baseContext);
		
	}		

	@Override
	public void onCreate() {
		super.onCreate();
		getManagers().getmSensorsManager().registerOnSensorChangeListener(this);
		
	}
	
	@Override
	public void onDestroy() {		
		super.onDestroy();
	}

	public void setElevationAngle(float angle)
	{
		mPhi=angle;
		NotifyListeners();
	}
	
	public void setRotationAngle(float angle)
	{
		mTheta=angle;
		NotifyListeners();
	}	
	public void setZoomDistance(float dist)
	{
		mR=dist;
		NotifyListeners();
	}
	
	public float getElevationAngle()
	{		
		return mPhi;
	}
	
	public float getRotationAngle()
	{
		return mTheta;
	}
	
	public float getZoomDistance()
	{
		return mR;
	}
	
	private void NotifyListeners()
	{
		for (OnPointOfViewChangeListener listener : mListeners) 
		{
			listener.onPointOfViewChange();		
		}	
	}
	
	public void registerPointOfViewChangeListener(OnPointOfViewChangeListener listener)
	{
		mListeners.add(listener);	
	}

	public interface OnPointOfViewChangeListener
	{
		void onPointOfViewChange();
	}
	private Vector<OnPointOfViewChangeListener> mListeners= new Vector<OnPointOfViewChangeListener>();
	
	@Override
	public void onSensorChanged() {
		NotifyListeners();		
	}	


}
