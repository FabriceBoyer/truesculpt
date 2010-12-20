/*
 * Copyright (C) 2007 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package truesculpt.renderer;

import java.io.FileOutputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.ShortBuffer;
import java.util.Date;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;
import javax.microedition.khronos.opengles.GL11;

import truesculpt.main.Managers;
import android.graphics.Bitmap;
import android.opengl.GLSurfaceView;
import android.os.SystemClock;

/**
 * Render a pair of tumbling cubes.
 */

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

	private ReferenceAxis mAxis = new ReferenceAxis();

	private float mDistance;
	private float mElevation;
	private long mLastFrameDurationMs = 0;
	private Managers mManagers = null;
	private float mRot;

	public MainRenderer(Managers managers)
	{
		super();
		this.mManagers = managers;
	}

	public long getLastFrameDurationMs()
	{
		return mLastFrameDurationMs;
	}

	private boolean mbTakeScreenshot=false;
	
	@Override
	public void onDrawFrame(GL10 gl)
	{
		long tStart = SystemClock.uptimeMillis();
		/*
		 * Usually, the first thing one might want to do is to clear the screen. The most efficient way of doing this is to use glClear().
		 */

		gl.glClear(GL10.GL_COLOR_BUFFER_BIT | GL10.GL_DEPTH_BUFFER_BIT);

		/*
		 * Now we're ready to draw some 3D objects
		 */

		gl.glMatrixMode(GL10.GL_MODELVIEW);
		gl.glLoadIdentity();

		gl.glTranslatef(0, 0, -mDistance);

		gl.glRotatef(mElevation, 1, 0, 0);
		gl.glRotatef(mRot, 0, 1, 0);

		// common part (normals optionnal)
		gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);

		// only if point of view changed
		mManagers.getMeshManager().setCurrentModelView(gl);

		if (mManagers.getOptionsManager().getDisplayDebugInfos())
		{
			mAxis.draw(gl);
		}

		// main draw call
		mManagers.getMeshManager().draw(gl);

		gl.glDisableClientState(GL10.GL_VERTEX_ARRAY);

		if (mbTakeScreenshot)
		{
			TakeGLScreenshot(gl);
		}
		
		long tStop = SystemClock.uptimeMillis();
		mLastFrameDurationMs = tStop - tStart;
	}

	public void onPointOfViewChange(float fRot, float fDistance, float fElevation)
	{
		mRot = fRot;
		mDistance = fDistance;
		mElevation = fElevation;
	}

	@Override
	public void onSurfaceChanged(GL10 gl, int width, int height)
	{
		gl.glViewport(0, 0, width, height);

		/*
		 * Set our projection matrix. This doesn't have to be done each time we draw, but usually a new projection needs to be set when the viewport is resized.
		 */

		float ratio = (float) width / height;
		gl.glMatrixMode(GL10.GL_PROJECTION);
		gl.glLoadIdentity();
		gl.glFrustumf(-ratio, ratio, -1, 1, 1.0f, 10);

		mManagers.getMeshManager().setCurrentProjection(gl);
		mManagers.getMeshManager().setViewport(gl);
	}

	@Override
	public void onSurfaceCreated(GL10 gl, EGLConfig config)
	{
		gl.glHint(GL10.GL_PERSPECTIVE_CORRECTION_HINT, GL10.GL_FASTEST);

		// TODO back screen color configuration in options
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
	}
	
	private void TakeGLScreenshot(GL10 gl)
	{
		mbTakeScreenshot=false;
		
		int[] mViewPort = new int[4];
		GL11 gl2 = (GL11) gl;
		gl2.glGetIntegerv(GL11.GL_VIEWPORT, mViewPort, 0);
		
		int width = mViewPort[2];
		int  height = mViewPort[3];

		int size = width * height;
		ByteBuffer buf = ByteBuffer.allocateDirect(size * 4);
		buf.order(ByteOrder.nativeOrder());
		gl.glReadPixels(0, 0, width, height, GL10.GL_RGBA, GL10.GL_UNSIGNED_BYTE, buf);
		int data[] = new int[size];
		buf.asIntBuffer().get(data);
		buf = null;
		Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);
		bitmap.setPixels(data, size-width, -width, 0, 0, width, height);
		data = null;

		short sdata[] = new short[size];
		ShortBuffer sbuf = ShortBuffer.wrap(sdata);
		bitmap.copyPixelsToBuffer(sbuf);
		for (int i = 0; i < size; ++i) {
		    //BGR-565 to RGB-565
		    short v = sdata[i];
		    sdata[i] = (short) (((v&0x1f) << 11) | (v&0x7e0) | ((v&0xf800) >> 11));
		}
		sbuf.rewind();
		bitmap.copyPixelsFromBuffer(sbuf);
		
		try {
			String strSnapshotFileName=CreateSnapshotFileName();
		    FileOutputStream fos = new FileOutputStream(strSnapshotFileName);
		    bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos);
		    fos.flush();
		    fos.close();
		} catch (Exception e) {
		    assert(false);
		}
	}
	
	public String CreateSnapshotFileName()
	{
		//TODO add sculpture name in filename
		Date date= new Date();
		String strFileName="/sdcard/Truesculpt/Screenshot_"+date.toGMTString()+".png";
		strFileName=strFileName.replaceAll(":", "_");
		strFileName=strFileName.replaceAll(" ", "_");
		return strFileName;
	}

	public void TakeGLScreenshotOfNextFrame()
	{
		this.mbTakeScreenshot = true;
	}
}
