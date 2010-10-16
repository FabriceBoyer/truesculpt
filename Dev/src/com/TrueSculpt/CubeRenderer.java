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

import javax.microedition.khronos.egl.EGL10;
import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;


import android.opengl.GLSurfaceView;

/**
 * Render a pair of tumbling cubes.
 */

class CubeRenderer implements GLSurfaceView.Renderer {
    public CubeRenderer(boolean useTranslucentBackground) {
        mTranslucentBackground = useTranslucentBackground;
        mWorld = new GLWorld();      

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
        Cube cube  = new Cube(mWorld, -1,-1,-1,1,1,1);

        for (int j = 0; j < 6; j++)
        {
           cube.setFaceColor(j, blue);
        }
        
        mWorld.addShape(cube);
        
        mWorld.generate();
    }

    
    public void SetColor(int color) 
    {
    	//mCube.SetColor(color);
    }
    
    public void SetOrientation(float angle, float vx, float vy, float vz)
    {
    	this.mAngle=angle;
    	this.vx=vx;
    	this.vy=vy;
    	this.vz=vz;    	
    }
    
    public void SetCameraPosition(float x, float y, float z, float head, float pitch, float roll)
    {
    	
    }
    
    public void GetPickedTriangle (float x_screen, float y_screen)
    {
    	
    }
    
    public void onDrawFrame(GL10 gl) {
    	

        /*
         * Usually, the first thing one might want to do is to clear
         * the screen. The most efficient way of doing this is to use
         * glClear(). However we must make sure to set the scissor
         * correctly first. The scissor is always specified in window
         * coordinates:
         */

        gl.glClearColor(0.5f,0.5f,0.5f,1);
        gl.glClear(GL10.GL_COLOR_BUFFER_BIT | GL10.GL_DEPTH_BUFFER_BIT);

        /*
         * Now we're ready to draw some 3D object
         */

        gl.glMatrixMode(GL10.GL_MODELVIEW);
        gl.glLoadIdentity();
        gl.glTranslatef(0, 0, -3.0f);
        gl.glScalef(0.5f, 0.5f, 0.5f);
        gl.glRotatef(mAngle,        0, 1, 0);
        gl.glRotatef(mAngle*0.25f,  1, 0, 0);

        gl.glColor4f(0.7f, 0.7f, 0.7f, 1.0f);
        gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);
        gl.glEnableClientState(GL10.GL_COLOR_ARRAY);
        gl.glEnable(GL10.GL_CULL_FACE);
        gl.glShadeModel(GL10.GL_SMOOTH);
        gl.glEnable(GL10.GL_DEPTH_TEST);

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
       //  gl.glHint(GL10.GL_PERSPECTIVE_CORRECTION_HINT,
       //          GL10.GL_FASTEST);

    }
    
    private boolean mTranslucentBackground;
    private  GLWorld mWorld;
    private float mAngle=0.0f;
    private float vx=0.0f;
    private float vy=0.0f;
    private float vz=0.0f;
}
