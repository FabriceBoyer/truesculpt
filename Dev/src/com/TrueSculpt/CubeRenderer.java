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

package com.TrueSculpt;

import java.util.Currency;
import java.util.Date;

import javax.microedition.khronos.egl.EGL10;
import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;


import android.graphics.Color;
import android.opengl.GLSurfaceView;

/**
 * Render a pair of tumbling cubes.
 */

class CubeRenderer implements GLSurfaceView.Renderer {
    public CubeRenderer(boolean useTranslucentBackground) {
        mTranslucentBackground = useTranslucentBackground;
        mWorld = new GLWorld();      

        PopulateWorld();
    }
    
	private void PopulateWorld() {
		int one = 0x10000;
        int half = 0x08000;
        GLColor red = new GLColor(one, 0, 0);
        GLColor green = new GLColor(0, one, 0);
        GLColor blue = new GLColor(0, 0, one);
        GLColor yellow = new GLColor(one, one, 0);
        GLColor orange = new GLColor(one, half, 0);
        GLColor white = new GLColor(one, one, one);
        GLColor black = new GLColor(0, 0, 0);

        // top back, left to right
        mCube  = new Cube(mWorld, -1,-1,-1,1,1,1);

        for (int j = 0; j < 6; j=j+2)
        {
        	mCube.setFaceColor(j, blue);
        	mCube.setFaceColor(j+1, orange);
        }
        
        mWorld.addShape(mCube);
        
        mWorld.generate();
	}
    
    public void SetColor(int color) 
    {
    	float r= Color.red(color)/255*65536;
    	float g= Color.green(color)/255*65536;
    	float b= Color.blue(color)/255*65536;
    	GLColor col = new GLColor(
    			(int)(r),
    			(int)(g),
    			(int)(b));
        for (int j = 0; j < 6; j++)
        {
        	mCube.setFaceColor(j, col);        	
        }   
        
        mWorld.generate();
    }
    
    //set target orientation
    public void SetOrientation(float angleX, float angleY, float angleZ, float vx, float vy, float vz)
    {
    	this.mAngleX=angleX;
    	this.mAngleY=angleY;
    	this.mAngleZ=angleZ;
    	this.vx=vx;
    	this.vy=vy;
    	this.vz=vz;    	
    	
    	tLast=System.currentTimeMillis();
    }
    
    private float tTau=500.0f;
    private long tLast=0;
        
    private void UpdateAngle()
    {   	
    	mAngleCurrX=FirstOrderAngle(mAngleCurrX,mAngleX);
    	mAngleCurrY=FirstOrderAngle(mAngleCurrY,mAngleY);
    	mAngleCurrZ=FirstOrderAngle(mAngleCurrZ,mAngleZ);    		 
    }
    
    private float FirstOrderAngle(float angleLast,float angleObj)
    {
    	long tCurr=System.currentTimeMillis();
    	float tDelta=tCurr-tLast;
    	float angleDelta=angleObj-angleLast;
    	float slope=angleDelta/tTau;
    	float angleCurr=0;
    	if (tDelta<tTau)
    	{
    		angleCurr=angleLast+slope*tDelta;	
    	}
    	else
    	{
    		angleCurr=angleObj;
    	}
    	return angleCurr;    	
    }
    
    
    public void onDrawFrame(GL10 gl)
    {    
    	UpdateAngle();
    	 
        gl.glClear(GL10.GL_COLOR_BUFFER_BIT | GL10.GL_DEPTH_BUFFER_BIT);

        gl.glMatrixMode(GL10.GL_MODELVIEW);
        gl.glLoadIdentity();
        gl.glTranslatef(0, 0, -7.0f);        
        gl.glRotatef(mAngleCurrX,        1, 0, 0);
        gl.glRotatef(mAngleCurrY,        0, 1, 0);
        gl.glRotatef(mAngleCurrZ,        0, 0, 1);
        
        gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);
        gl.glEnableClientState(GL10.GL_COLOR_ARRAY);

        mWorld.draw(gl);           
    }

    public void onSurfaceChanged(GL10 gl, int width, int height) {
         gl.glViewport(0, 0, width, height);

         /*
          * Set our projection matrix. This doesn't have to be done
          * each time we draw, but usually a new projection needs to be set
          * when the viewport is resized.
          */

         float ratio = (float)width / height;
         gl.glMatrixMode(GL10.GL_PROJECTION);
         gl.glLoadIdentity();
         gl.glFrustumf(-ratio, ratio, -1, 1, 2, 12);

         /*
          * By default, OpenGL enables features that improve quality
          * but reduce performance. One might want to tweak that
          * especially on software renderer.
          */
         gl.glDisable(GL10.GL_DITHER);
         gl.glActiveTexture(GL10.GL_TEXTURE0);
    }

    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
  	
        /*
         * Some one-time OpenGL initialization can be made here
         * probably based on features of this particular context
         */
         gl.glHint(GL10.GL_PERSPECTIVE_CORRECTION_HINT,
                 GL10.GL_FASTEST);

         if (mTranslucentBackground) {
             gl.glClearColor(0,0,0,0);
         } else {
             gl.glClearColor(1,1,1,1);
         }
         
         gl.glEnable(GL10.GL_CULL_FACE);
         gl.glShadeModel(GL10.GL_SMOOTH);
         gl.glEnable(GL10.GL_DEPTH_TEST);

    }
    
    private boolean mTranslucentBackground;
    private  GLWorld mWorld;
    private Cube mCube; 
    private float mAngleX=0.0f;
    private float mAngleY=0.0f;
    private float mAngleZ=0.0f;
    private float mAngleCurrX=0.0f;
    private float mAngleCurrY=0.0f;
    private float mAngleCurrZ=0.0f;
    private float vx=0.0f;
    private float vy=0.0f;
    private float vz=0.0f;
}
