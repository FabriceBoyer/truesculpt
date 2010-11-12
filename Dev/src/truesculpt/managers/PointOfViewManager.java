package truesculpt.managers;

import java.util.Vector;

import truesculpt.managers.SensorsManager.OnSensorChangeListener;
import truesculpt.utils.Utils;

import android.content.Context;

public class PointOfViewManager extends BaseManager {


	//looked at point
	private float mX=0.0f;
	private float mXOrig=0.0f;
	private float mY=0.0f;
	private float mYOrig=0.0f;	
	private float mZ=10.0f;	
	private float mZOrig=0.0f;
	
	private float mR=6.0f;
	private float mTheta=0.0f;
	private float mPhi=0.0f;
	
	private float mRmax=10.0f;
	private float mRmin=1.0f;//to be recomputed to adapt to max size of object
	
	public PointOfViewManager(Context baseContext) {
		super(baseContext);
		
	}		

	@Override
	public void onCreate() {
		super.onCreate();	
		
	}
	
	@Override
	public void onDestroy() {		
		super.onDestroy();
		
	}

	public void setElevationAngle(float angle)
	{		 
		mPhi=angle%90;//+90 to -90
		NotifyListeners();
	}
	
	public void setRotationAngle(float angle)
	{
		mTheta=angle%180;//-180 to 180		
		NotifyListeners();
	}	
	public void setZoomDistance(float dist)
	{
		float RMax=getManagers().getmPointOfViewManager().getRmax();
		if (dist>RMax)
		{
			mR=RMax;
		}
		else
		{
			mR=dist;
		}
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
		getManagers().getmRendererManager().onPointOfViewChange();
		
		for (OnPointOfViewChangeListener listener : mListeners) 
		{
			listener.onPointOfViewChange();		
		}	
	}
	
	public void registerPointOfViewChangeListener(OnPointOfViewChangeListener listener)
	{
		mListeners.add(listener);	
	}
	
	public void unRegisterPointOfViewChangeListener(OnPointOfViewChangeListener listener)
	{
		mListeners.remove(listener);	
	}

	public interface OnPointOfViewChangeListener
	{
		void onPointOfViewChange();
	}
	private Vector<OnPointOfViewChangeListener> mListeners= new Vector<OnPointOfViewChangeListener>();
	
	
	public void onSensorChanged() {
		//NotifyListeners();		
	}

	public float getRmax() {
		return mRmax;
	}	

	public float getRmin() {
		return mRmin;
	}	

	public void ResetPOV()
	{
		mR=6.0f;
		mTheta=0.0f;
		mPhi=0.0f;
		
		NotifyListeners();
	}

}
