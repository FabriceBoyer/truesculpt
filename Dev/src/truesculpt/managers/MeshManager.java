package truesculpt.managers;

import java.nio.FloatBuffer;
import java.nio.ShortBuffer;
import java.util.Vector;
import javax.microedition.khronos.opengles.GL10;
import javax.microedition.khronos.opengles.GL11;

import truesculpt.renderer.GeneratedObject;
import truesculpt.renderer.PickHighlight;
import truesculpt.renderer.RayPickDebug;
import android.content.Context;
import android.opengl.Matrix;
import android.util.Log;
import truesculpt.utils.MatrixUtils;

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
	float[] intersectPt=new float[3];
    
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

		mRay.setRayPos(rayPt1,rayPt2);		

		String msg="Pt1 : x="+Float.toString(rayPt1[0])+"; y="+Float.toString(rayPt1[1])+"; z="+Float.toString(rayPt1[2])+"\n";
		msg+=	   "Pt2 : x="+Float.toString(rayPt2[0])+"; y="+Float.toString(rayPt2[1])+"; z="+Float.toString(rayPt2[2])+"\n";
		Log.i("Pick",msg);
		
		if (bInitOver)
		{
			try {
				int nIndex = GetPickedTriangleIndex();
				if (nIndex >= 0) {
					mPickHighlight.setPickHighlightPosition(intersectPt);

					msg = "Index =" + Integer.toString(nIndex) + "\n";
					msg += "intersectPt : x=" + Float.toString(intersectPt[0]) + "; y=" + Float.toString(intersectPt[1]) + "; z=" + Float.toString(intersectPt[2]) + "\n";
					Log.i("PickedTriangle", msg);
				}
			} catch (Exception e) {
				assert(false);
			}
		}
		
		NotifyListeners();
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
        MatrixUtils.PrintMat("Proj", mProjection);
        MatrixUtils.PrintMat("Model", mModelView);
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

    int GetPickedTriangleIndex()
    {
    	int nRes=-1;
    	
    	FloatBuffer mVertexBuffer = mObject.getVertexBuffer();
    	ShortBuffer mIndexBuffer= mObject.getIndexBuffer();
    	
    	float[] R0=new float[3];
    	float[] R1=new float[3];
    	float[] V0=new float[3];
    	float[] V1=new float[3];
    	float[] V2=new float[3];
    	float[] Ires=new float[3];
    	
    	int n=mIndexBuffer.capacity();
    	int nVertex=mVertexBuffer.capacity();    	
    	for (int i=0;i<n;i=i+3)
    	{
    		MatrixUtils.copy(rayPt1, R0);
    		MatrixUtils.copy(rayPt2, R1);
    		
    		int nIndex=3*mIndexBuffer.get(i); assert(nIndex<nVertex);
    		mVertexBuffer.position(nIndex);
    		mVertexBuffer.get(V0,0,3);
    		
    		nIndex=3*mIndexBuffer.get(i+1);assert(nIndex<nVertex);
    		mVertexBuffer.position(nIndex);
    		mVertexBuffer.get(V1,0,3);
    		
    		nIndex=3*mIndexBuffer.get(i+2);assert(nIndex<nVertex);
    		mVertexBuffer.position(nIndex);
    		mVertexBuffer.get(V2,0,3);    		
    		
    		int nCollide=intersect_RayTriangle( R0,  R1, V0,  V1,  V2, Ires );
    		
    		if (nCollide==1)
    		{
    			MatrixUtils.copy(Ires,intersectPt);
    			nRes=i;
    			break;
    		}
    	}
    	
    	mIndexBuffer.position(0);
    	mVertexBuffer.position(0);
    	
    	return nRes;    	
    }
    
	
	static float SMALL_NUM=  0.00000001f; // anything that avoids division overflow 
	// triangle vectors
	static float[] u=new float[3];
	static float[] v=new float[3];
    static float[] n=new float[3];    
    // ray vectors
    static float[] dir=new float[3];
    static float[] w0=new float[3];
    static float[] w=new float[3];          
    static float[] zero={0,0,0};
	
	 // intersect_RayTriangle(): intersect a ray with a 3D triangle
	//     Input:  a ray R (R0 and R1), and a triangle T (V0,V1)
	//     Output: *I = intersection point (when it exists)
	//     Return: -1 = triangle is degenerate (a segment or point)
	//              0 = disjoint (no intersect)
	//              1 = intersect in unique point I1
	//              2 = are in the same plane
	 static int intersect_RayTriangle( float[] R0, float[] R1, float[] V0, float[] V1, float[] V2, float[] Ires )
	 {
	     float     r, a, b;             // params to calc ray-plane intersect
	
	     // get triangle edge vectors and plane normal
	     MatrixUtils.minus(V1,V0,u);
	     MatrixUtils.minus(V2,V0,v);
	     
	     MatrixUtils.cross(u, v, n);             // cross product
	     if (n == zero)            // triangle is degenerate
	         return -1;                 // do not deal with this case
	  
	     MatrixUtils.minus(R1,R0,dir);             // ray direction vector
	     MatrixUtils.minus(R0,V0,w0);
	     a = -MatrixUtils.dot(n,w0);
	     b = MatrixUtils.dot(n,dir);
	     if (Math.abs(b) < SMALL_NUM) {     // ray is parallel to triangle plane
	         if (a == 0)                // ray lies in triangle plane
	             return 2;
	         else return 0;             // ray disjoint from plane
	     }
	
	     // get intersect point of ray with triangle plane
	     r = a / b;
	     if (r < 0.0)                   // ray goes away from triangle
	         return 0;                  // => no intersect
	     // for a segment, also test if (r > 1.0) => no intersect
		      
	     MatrixUtils.scalarMultiply(dir,r);
	     MatrixUtils.plus(R0, dir, Ires);	     
	
	     // is I inside T?
	     float    uu, uv, vv, wu, wv, D;
	     uu = MatrixUtils.dot(u,u);
	     uv = MatrixUtils.dot(u,v);
	     vv = MatrixUtils.dot(v,v);
	     MatrixUtils.minus(Ires, V0, w);	     
	     wu = MatrixUtils.dot(w,u);
	     wv = MatrixUtils.dot(w,v);
	     D = uv * uv - uu * vv;
	
	     // get and test parametric coords
	     float s, t;
	     s = (uv * wv - vv * wu) / D;
	     if (s < 0.0 || s > 1.0)        // I is outside T
	         return 0;
	     t = (uv * wu - uu * wv) / D;
	     if (t < 0.0 || (s + t) > 1.0)  // I is outside T
	         return 0;
	
	     return 1;                      // I is in T
	 }
    
}
