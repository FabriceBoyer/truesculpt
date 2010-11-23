package truesculpt.managers;

import java.util.Vector;
import javax.microedition.khronos.opengles.GL10;
import javax.microedition.khronos.opengles.GL11;

import truesculpt.managers.PointOfViewManager.OnPointOfViewChangeListener;
import truesculpt.renderer.GeneratedObject;
import truesculpt.renderer.MatrixGrabber;
import truesculpt.renderer.PickHighlight;
import truesculpt.renderer.RayPickDebug;
import android.content.Context;
import android.opengl.GLES11;
import android.opengl.GLU;
import android.opengl.Matrix;
import android.util.Log;
import truesculpt.renderer.*;

//for mesh storage, computation and transformation application
public class MeshManager extends BaseManager {

	
	private GeneratedObject mObject=null;
	private PickHighlight mPickHighlight= new PickHighlight();
	private RayPickDebug mRay= new RayPickDebug();

	public MeshManager(Context baseContext) {
		super(baseContext);	 			
	}

    private float [] mModelView = new float[16];
    private float [] mProjection = new float[16];
    private int[] mViewPort=new int[4];
    
    float[] rayPt1=new float[3];
	float[] rayPt2=new float[3];
    
	private boolean bInitOver=false;
	
	@Override
	public void onCreate() {
		 Thread thr = new Thread(null, mInitTask, "Mesh_Init");
		 thr.start();		
	}
	
	Runnable mInitTask = new Runnable()
	{		   
		@Override
		public void run() {
			
			mObject= new GeneratedObject();	
			
	        bInitOver=true;	
	        
	        NotifyListeners();
		}
	};

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		
	}
	
	private void NotifyListeners()
	{		
		for (OnMeshChangeListener listener : mListeners) 
		{
			listener.onMeshChange();		
		}	
	}
	
	public void registerPointOfViewChangeListener(OnMeshChangeListener listener)
	{
		mListeners.add(listener);	
	}
	
	public void unRegisterPointOfViewChangeListener(OnMeshChangeListener listener)
	{
		mListeners.remove(listener);	
	}

	public interface OnMeshChangeListener
	{
		void onMeshChange();
	}
	private Vector<OnMeshChangeListener> mListeners= new Vector<OnMeshChangeListener>();
	
	public void draw(GL10 gl)
	{
		if (mObject!=null && bInitOver)
		{
			mObject.draw(gl);
		}
		
		mRay.draw(gl);
		mPickHighlight.draw(gl);
	}

    public int getFacesCount() {
    	int nCount=-1;
    	if (mObject!=null)
		{
		 	nCount=mObject.getFacesCount();
		}
    	return nCount;
	}
    
    public int getVertexCount() {
    	int nCount=-1;
    	if (mObject!=null)
		{
		 	nCount=mObject.getVertexCount();
		}
    	return nCount;
	}
    
    public void Pick(float screenX,float screenY)
    {    	
    	
    	GetWorldCoords(rayPt1,screenX,screenY, 1.0f);//normalized z between -1 and 1    	
    	GetWorldCoords(rayPt2,screenX,screenY, -1.0f);
		
		mPickHighlight.setPickHighlightPosition(rayPt1);

		mRay.setRayPos(rayPt1,rayPt2);		

		String msg="Pt1 : x="+Float.toString(rayPt1[0])+"; y="+Float.toString(rayPt1[1])+"; z="+Float.toString(rayPt1[2])+"\n";
		msg+=	   "Pt2 : x="+Float.toString(rayPt2[0])+"; y="+Float.toString(rayPt2[1])+"; z="+Float.toString(rayPt2[2])+"\n";
		Log.i("Pick",msg);
		
		NotifyListeners();
    }
    
    void PrintMat(String logID,float[] mat)
    {
    	String msg="";	
    	int n=mat.length;
    	for (int i=0;i<n;i++)
    	{
    		msg+=Float.toString(mat[i])+ " ";
    		if (i%4==0)
    		{
    			msg+="\n";
    		}
    	}
    	Log.i(logID,msg);
    }
    /**
     * Calculates the transform from screen coordinate
     * system to world coordinate system coordinates
     * for a specific point, given a camera position.
     *
     * @return position in WCS.
     */
    public void GetWorldCoords( float[] worldPos, float touchX, float touchY, float z)
    {  
        
        // SCREEN height & width (ej: 320 x 480)
        float screenW = mViewPort[2];
        float screenH = mViewPort[3];
               
        // Auxiliary matrix and vectors
        // to deal with ogl.
        float[] invertedMatrix, transformMatrix,
            normalizedInPoint, outPoint;
        invertedMatrix = new float[16];
        transformMatrix = new float[16];
        normalizedInPoint = new float[4];
        outPoint = new float[4];
  
        // Invert y coordinate, as android uses
        // top-left, and ogl bottom-left.
        int oglTouchY = (int) (screenH - touchY);
        
        /* Transform the screen point to clip
        space in ogl (-1,1) */       
        normalizedInPoint[0] =
         (float) ((touchX) * 2.0f / screenW - 1.0);
        normalizedInPoint[1] =
         (float) ((oglTouchY) * 2.0f / screenH - 1.0);
        normalizedInPoint[2] = z;
        normalizedInPoint[3] = 1.0f;
  
        /* Obtain the transform matrix and
        then the inverse. */
        PrintMat("Proj", mProjection);
        PrintMat("Model", mModelView);
        Matrix.multiplyMM(
            transformMatrix, 0,
            mProjection, 0,
            mModelView, 0);
        Matrix.invertM(invertedMatrix, 0,
            transformMatrix, 0);       
  
        /* Apply the inverse to the point
        in clip space */
        Matrix.multiplyMV(
            outPoint, 0,
            invertedMatrix, 0,
            normalizedInPoint, 0);
        
        if (outPoint[3] == 0.0)
        {
            // Avoid /0 error.
            Log.e("World coords", "ERROR!");
           return;
        }
        
        // Divide by the 3rd component to find
        // out the real position.
        worldPos[0]= (outPoint[0] / outPoint[3]);
        worldPos[1]= (outPoint[1] / outPoint[3]);
        worldPos[2]= (outPoint[2] / outPoint[3]);     
    }


   
    //TODO test for GL11 instanceof to handle not GL11 devices
    //TODO use GL11ES calls indepedent of redraw with gl param
    public void getCurrentModelView(GL10 gl) {
    	GL11 gl2=(GL11)gl;
    	gl2.glGetFloatv(GL11.GL_MODELVIEW_MATRIX, mModelView,0);       
    }

  
    public void getCurrentProjection(GL10 gl) {
    	GL11 gl2=(GL11)gl;
    	gl2.glGetFloatv(GL11.GL_PROJECTION_MATRIX, mProjection,0);
    }
    
    
    public void getViewport(GL10 gl) {
    	GL11 gl2=(GL11)gl;
    	gl2.glGetIntegerv(GL11.GL_VIEWPORT, mViewPort,0);
    }
    
   
    
}
