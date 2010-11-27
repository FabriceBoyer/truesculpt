package truesculpt.managers;

import java.util.Vector;
import android.content.Context;
import android.graphics.Color;

public class ToolsManager extends BaseManager {	
	
	public enum EToolMode { POV, SCULPT, PAINT};
	public enum EPovToolSubMode { ROTATE, ZOOM, PAN };
	public enum ESculptToolSubMode { RISE, SUNK, REFINE };
	
	private EToolMode mMode=EToolMode.POV;
	private EPovToolSubMode mPovSubMode=EPovToolSubMode.ROTATE;
	private int mColor=Color.rgb(0, 0, 200);
	private float mStrength=50.0f;//pct
	private float mRadius=50.0f;//pct

	private EToolMode mLastMode=EToolMode.SCULPT;
	
	public ToolsManager(Context baseContext) {
		super(baseContext);
		// TODO Auto-generated constructor stub
	}

	private void NotifyListeners()
	{		
		for (OnToolChangeListener listener : mListeners) 
		{
			listener.onToolChange();		
		}	
	}
	
	public void registerToolChangeListener(OnToolChangeListener listener)
	{
		mListeners.add(listener);	
	}
	
	public void unRegisterToolChangeListener(OnToolChangeListener listener)
	{
		mListeners.remove(listener);	
	}

	public interface OnToolChangeListener
	{
		void onToolChange();
	}
	private Vector<OnToolChangeListener> mListeners= new Vector<OnToolChangeListener>();

	
	
	public EToolMode getToolMode() {
		return mMode;
	}
	public void setToolMode(EToolMode mode) {
		if (mMode!=mode)
		{			
			mLastMode=mMode;
			mMode = mode;				
			NotifyListeners();						
		}
	}
	
	public void setLastToolMode()
	{
		if (mMode!=mLastMode)
		{
			mMode=mLastMode;
			NotifyListeners();
			//don't update mLastMode otherwise oscillate
		}
	}
	
	private boolean mForcedMode=false;

	public float getStrength() {
		return mStrength;
	}
	public void setStrength(float strength) {		
		this.mStrength = strength;
		
		if (mStrength>100) mStrength=100;
		if (mStrength<-100) mStrength=-100;
		
		NotifyListeners();
	}
	
	public float getRadius() {
		return mRadius;
	}
	public void setRadius(float radius) {
		this.mRadius = radius;
		if (mRadius>100) mRadius=100;
		if (mRadius<0) mRadius=0;
		NotifyListeners();
	}
	
	public int getColor() {
		return mColor;
	}
	public void setColor(int color) {
		this.mColor = color;
		NotifyListeners();
	}

	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		
	}

	public boolean getForcedMode() {
		return mForcedMode;
	}

	public void setForcedMode(boolean mForcedMode) {
		this.mForcedMode = mForcedMode;
	}

	public EToolMode getLastMode() {
		return mLastMode;
	}

	public EPovToolSubMode getPovSubMode() {
		return mPovSubMode;
	}

	public void setPovSubMode(EPovToolSubMode mPovSubMode) {
		this.mPovSubMode = mPovSubMode;
	}	

}
