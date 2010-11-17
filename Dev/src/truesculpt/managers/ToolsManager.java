package truesculpt.managers;

import android.content.Context;

public class ToolsManager extends BaseManager {	
	
	public enum EToolMode { POV, SCULPT};
	
	private EToolMode mMode=EToolMode.POV;
	private int mColor=0;
	
	public EToolMode getToolMode() {
		return mMode;
	}
	public void setToolMode(EToolMode mMode) {
		this.mMode = mMode;
	}

	public ToolsManager(Context baseContext) {
		super(baseContext);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		
	}

	public int getColor() {
		return mColor;
	}

	public void setColor(int mColor) {
		this.mColor = mColor;
	}

	

}
