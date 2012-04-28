package truesculpt.renderer;

import java.io.FileOutputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.ShortBuffer;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;
import javax.microedition.khronos.opengles.GL11;

import truesculpt.main.Managers;
import android.graphics.Bitmap;
import android.opengl.GLSurfaceView;
import android.opengl.GLU;
import android.opengl.Matrix;
import android.os.SystemClock;
import android.util.Log;

public class MainRenderer implements GLSurfaceView.Renderer
{
	float fShininess = 25.0f;
	float lightAmbient[] = new float[] { 0.1f, 0.1f, 0.1f, 1.0f };
	float lightDiffuse[] = new float[] { 0.9f, 0.9f, 0.9f, 1.0f };
	float[] lightPos = new float[] { 5, 5, 10, 1 };
	float lightSpecular[] = new float[] { 0.2f, 0.2f, 0.2f, 1.0f };
	float matAmbient[] = new float[] { 1, 1, 1, 1 };
	float matDiffuse[] = new float[] { 1, 1, 1, 1 };
	float matSpecular[] = new float[] { 1, 1, 1, 1 };

	private final ReferenceAxis mAxis = new ReferenceAxis();
	private final SymmetryPlane mSymmetryPlane = new SymmetryPlane();
	private final ToolOverlay mToolOverlay = new ToolOverlay();
	private final BackgroundPlane mBackgroundPlane = new BackgroundPlane();

	private long mLastFrameDurationMs = 0;
	private Managers mManagers = null;

	private float mDistance;
	private float mXPanOffset;
	private float mYPanOffset;

	private float mHead;
	private float mPitch;
	private float mRoll;

	private boolean mbTakeScreenshot = false;
	private String mStrSnapshotName = "";

	private float mScreenAspectRatio = -1;
	private final float mFovY_deg = 50f;
	private final float mZnear = 0.1f;
	private final float mZfar = 10f;

	private final float[] mModelView = new float[16];
	private final int[] mViewPort = new int[4];
	private final float[] mProjection = new float[16];

	public MainRenderer(Managers managers)
	{
		super();
		this.mManagers = managers;
	}

	public long getLastFrameDurationMs()
	{
		return mLastFrameDurationMs;
	}

	public Managers getManagers()
	{
		return mManagers;
	}

	@Override
	public void onDrawFrame(GL10 gl)
	{
		long tStart = SystemClock.uptimeMillis();

		gl.glClear(GL10.GL_COLOR_BUFFER_BIT | GL10.GL_DEPTH_BUFFER_BIT);

		gl.glMatrixMode(GL10.GL_MODELVIEW);
		gl.glLoadIdentity();
		
		gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);
		
		mBackgroundPlane.draw(gl);

		// eyeX, eyeY, eyeZ, centerX, centerY, centerZ, upX, upY, upZ
		// GLU.gluLookAt(gl, mDistance * (float) Math.cos(-mPitch) * (float) Math.cos(mHead), mDistance * (float) Math.sin(-mPitch), mDistance * (float) Math.cos(-mPitch) * (float) Math.sin(mHead), 0, 0, 0, 0, 0, 1);

		gl.glTranslatef(mXPanOffset, mYPanOffset, -mDistance);
		gl.glRotatef(mPitch, 1, 0, 0);
		gl.glRotatef(mHead, 0, 1, 0);
		gl.glRotatef(mRoll, 0, 0, 1);	
		
		// only if point of view changed
		setCurrentModelView(gl);

		// if (getManagers().getOptionsManager().getDisplayDebugInfos())// TODO use cache
		if (true)
		{
			mAxis.draw(gl);
		}

		// main draw call
		getManagers().getMeshManager().draw(gl);

		mToolOverlay.draw(gl, mManagers);

		mSymmetryPlane.draw(gl, mManagers);

		gl.glDisableClientState(GL10.GL_VERTEX_ARRAY);

		if (mbTakeScreenshot)
		{
			TakeGLScreenshot(gl);
			mbTakeScreenshot = false;
		}

		long tStop = SystemClock.uptimeMillis();
		mLastFrameDurationMs = tStop - tStart;
	}

	public void TakeGLScreenshot(GL10 gl)
	{
		if (mStrSnapshotName != "")
		{
			getManagers().getUsageStatisticsManager().TrackEvent("Screenshot", "Count", 0);

			int[] mViewPort = new int[4];
			GL11 gl2 = (GL11) gl;
			gl2.glGetIntegerv(GL11.GL_VIEWPORT, mViewPort, 0);

			int width = mViewPort[2];
			int height = mViewPort[3];

			int size = width * height;
			ByteBuffer buf = ByteBuffer.allocateDirect(size * 4);
			buf.order(ByteOrder.nativeOrder());
			gl.glReadPixels(0, 0, width, height, GL10.GL_RGBA, GL10.GL_UNSIGNED_BYTE, buf);
			int data[] = new int[size];
			buf.asIntBuffer().get(data);
			buf = null;
			Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);
			bitmap.setPixels(data, size - width, -width, 0, 0, width, height);
			data = null;

			short sdata[] = new short[size];
			ShortBuffer sbuf = ShortBuffer.wrap(sdata);
			bitmap.copyPixelsToBuffer(sbuf);
			for (int i = 0; i < size; ++i)
			{
				// BGR-565 to RGB-565
				short v = sdata[i];
				sdata[i] = (short) ((v & 0x1f) << 11 | v & 0x7e0 | (v & 0xf800) >> 11);
			}
			sbuf.rewind();
			bitmap.copyPixelsFromBuffer(sbuf);

			try
			{
				FileOutputStream fos = new FileOutputStream(mStrSnapshotName);
				bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos);
				fos.flush();
				fos.close();
			}
			catch (Exception e)
			{
				e.printStackTrace();
				return;
			}

			mStrSnapshotName = "";// reset
		}
	}

	public void onPointOfViewChange()
	{
		mHead = getManagers().getPointOfViewManager().getHeadAngle();
		mPitch = getManagers().getPointOfViewManager().getPitchAngle();
		mRoll = getManagers().getPointOfViewManager().getRollAngle();
		mDistance = getManagers().getPointOfViewManager().getZoomDistance();
		mXPanOffset = getManagers().getPointOfViewManager().getXPanOffset();
		mYPanOffset = getManagers().getPointOfViewManager().getYPanOffset();
	}

	@Override
	public void onSurfaceChanged(GL10 gl, int width, int height)
	{
		gl.glViewport(0, 0, width, height);

		// Set our projection matrix. This doesn't have to be done each time we draw, but usually a new projection needs to be set when the viewport is resized.

		mScreenAspectRatio = (float) width / height;

		gl.glMatrixMode(GL10.GL_PROJECTION);
		gl.glLoadIdentity();
		GLU.gluPerspective(gl, mFovY_deg, mScreenAspectRatio, mZnear, mZfar);

		// setPerspective(gl, mFovY_deg, mScreenAspectRatio, mZnear, mZfar);

		setCurrentProjection(gl);
		setViewport(gl);
	}

	// replacement for gluPerspective
	private void setPerspective(GL10 gl, float fovy_deg, float aspect, float zNear, float zFar)
	{
		float pi180 = (float) (Math.PI / 180);
		float top, bottom, left, right;
		top = (float) (zNear * Math.tan(pi180 * fovy_deg / 2));
		bottom = -top;
		right = aspect * top;
		left = -right;
		gl.glMatrixMode(GL10.GL_PROJECTION);
		gl.glLoadIdentity();
		gl.glFrustumf(left, right, bottom, top, zNear, zFar);
	}

	@Override
	public void onSurfaceCreated(GL10 gl, EGLConfig config)
	{
		gl.glHint(GL10.GL_PERSPECTIVE_CORRECTION_HINT, GL10.GL_FASTEST);

		gl.glClearColor(0, 0, 0, 0);
		
		gl.glEnable(GL10.GL_LIGHTING);
		gl.glEnable(GL10.GL_LIGHT0);

		gl.glMaterialfv(GL10.GL_FRONT_AND_BACK, GL10.GL_AMBIENT, matAmbient, 0);
		gl.glMaterialfv(GL10.GL_FRONT_AND_BACK, GL10.GL_DIFFUSE, matDiffuse, 0);
		gl.glMaterialfv(GL10.GL_FRONT_AND_BACK, GL10.GL_SPECULAR, matSpecular, 0);
		gl.glMaterialf(GL10.GL_FRONT_AND_BACK, GL10.GL_SHININESS, fShininess);

		// TODO use texture, not color at point
		gl.glEnable(GL10.GL_COLOR_MATERIAL);

		gl.glLightfv(GL10.GL_LIGHT0, GL10.GL_POSITION, lightPos, 0);

		gl.glLightfv(GL10.GL_LIGHT0, GL10.GL_AMBIENT, lightAmbient, 0);
		gl.glLightfv(GL10.GL_LIGHT0, GL10.GL_DIFFUSE, lightDiffuse, 0);
		gl.glLightfv(GL10.GL_LIGHT0, GL10.GL_SPECULAR, lightSpecular, 0);

		gl.glEnable(GL10.GL_DEPTH_TEST);
		gl.glDepthFunc(GL10.GL_LEQUAL);

		gl.glEnable(GL10.GL_CULL_FACE);
		gl.glShadeModel(GL10.GL_SMOOTH);

		// transparency
		gl.glEnable(GL10.GL_BLEND);
		gl.glBlendFunc(GL10.GL_SRC_ALPHA, GL10.GL_ONE_MINUS_SRC_ALPHA);
	}

	public void TakeGLScreenshotOfNextFrame(String strSnapshotName)
	{
		mStrSnapshotName = strSnapshotName;
		this.mbTakeScreenshot = true;
	}

	public void onToolChange()
	{
		mToolOverlay.updateTool(mManagers);
	}

	// TODO test for GL11 instance of to handle not GL11 devices
	// TODO use GL11ES calls independent of redraw with gl param
	public void setCurrentModelView(GL10 gl)
	{
		GL11 gl2 = (GL11) gl;
		gl2.glGetFloatv(GL11.GL_MODELVIEW_MATRIX, mModelView, 0);
	}

	public void setCurrentProjection(GL10 gl)
	{
		GL11 gl2 = (GL11) gl;
		gl2.glGetFloatv(GL11.GL_PROJECTION_MATRIX, mProjection, 0);
	}

	public void setViewport(GL10 gl)
	{
		GL11 gl2 = (GL11) gl;
		gl2.glGetIntegerv(GL11.GL_VIEWPORT, mViewPort, 0);
	}

	/**
	 * Calculates the transform from screen coordinate system to world coordinate system coordinates for a specific point, given a camera position.
	 * 
	 * @return position in WCS.
	 */
	public void GetWorldCoords_replacement(float[] worldPos, float touchX, float touchY, float z)
	{
		GLU.gluUnProject(touchX, touchY, z, mModelView, 0, mProjection, 0, mViewPort, 0, worldPos, 0);
	}

	// Auxiliary matrix and vectors
	// to deal with ogl.
	private static final float[] invertedMatrix = new float[16];
	private static final float[] transformMatrix = new float[16];
	private static final float[] normalizedInPoint = new float[4];
	private static final float[] outPoint = new float[4];

	public void GetWorldCoords(float[] worldPos, float touchX, float touchY, float z)
	{
		// SCREEN height & width (ej: 320 x 480)
		float screenW = mViewPort[2];
		float screenH = mViewPort[3];

		// Invert y coordinate, as android uses
		// top-left, and ogl bottom-left.
		int oglTouchY = (int) (screenH - touchY);

		/*
		 * Transform the screen point to clip space in ogl (-1,1)
		 */
		normalizedInPoint[0] = (float) (touchX * 2.0f / screenW - 1.0);
		normalizedInPoint[1] = (float) (oglTouchY * 2.0f / screenH - 1.0);
		normalizedInPoint[2] = z;
		normalizedInPoint[3] = 1.0f;

		/*
		 * Obtain the transform matrix and then the inverse.
		 */
		// MatrixUtils.PrintMat("Proj", mProjection);
		// MatrixUtils.PrintMat("Model", mModelView);
		Matrix.multiplyMM(transformMatrix, 0, mProjection, 0, mModelView, 0);
		Matrix.invertM(invertedMatrix, 0, transformMatrix, 0);

		/*
		 * Apply the inverse to the point in clip space
		 */
		Matrix.multiplyMV(outPoint, 0, invertedMatrix, 0, normalizedInPoint, 0);

		if (outPoint[3] == 0.0)
		{
			// Avoid /0 error.
			Log.e("World coords", "ERROR!");
			return;
		}

		// Divide by the 3rd component to find
		// out the real position.
		worldPos[0] = outPoint[0] / outPoint[3];
		worldPos[1] = outPoint[1] / outPoint[3];
		worldPos[2] = outPoint[2] / outPoint[3];
	}

	public float[] GetModelViewMatrix()
	{
		return mModelView;
	}

	public float[] GetProjectionMatrix()
	{
		return mProjection;
	}

}
