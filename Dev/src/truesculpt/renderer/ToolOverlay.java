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

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;

import javax.microedition.khronos.opengles.GL10;

import truesculpt.main.Managers;
import truesculpt.utils.Utils;

/**
 * A vertex shaded cube.
 */
public class ToolOverlay
{
	private FloatBuffer mColorBuffer;
	private ShortBuffer mIndexBuffer;
	private FloatBuffer mVertexBuffer;

	int nVertices=100;	
	float offset[]=new float[3];
	float scale[]=new float[3];
	
	public ToolOverlay()
	{
		float zero = 0.0f;
		float one = 1.0f;		
		float half = 0.5f;

		ByteBuffer vbb = ByteBuffer.allocateDirect(nVertices * 3 * 4);
		vbb.order(ByteOrder.nativeOrder());
		mVertexBuffer = vbb.asFloatBuffer();
		for (int i=0;i<nVertices;i++)
		{
			mVertexBuffer.put(0.0f);
			mVertexBuffer.put(0.0f);
			mVertexBuffer.put(0.0f);
		}

		ByteBuffer cbb = ByteBuffer.allocateDirect(nVertices * 4 * 4);
		cbb.order(ByteOrder.nativeOrder());
		mColorBuffer = cbb.asFloatBuffer();
		for (int i=0;i<nVertices;i++)
		{
			mColorBuffer.put(1.0f);
			mColorBuffer.put(1.0f);
			mColorBuffer.put(1.0f);
			mColorBuffer.put(1.0f);
		}		

		ByteBuffer ibb = ByteBuffer.allocateDirect(nVertices * 2);
		ibb.order(ByteOrder.nativeOrder());
		mIndexBuffer = ibb.asShortBuffer();
		for (int i=0;i<nVertices;i++)
		{
			mIndexBuffer.put((short)i);
		}
		
		offset[0]=0f;
		offset[1]=0f;
		offset[2]=0f;
		
		scale[0]=1f;
		scale[1]=1f;
		scale[2]=1f;		
		
		updateGeometry(0.5f,1f,0.1f);
	}

	public void draw(GL10 gl, Managers managers)
	{				
		managers.getMeshManager().getLastPickingPoint(offset);
		gl.glPushMatrix();
		gl.glScalef(scale[0], scale[1], scale[2]);
		gl.glTranslatef(offset[0], offset[1], offset[2]);
		//TODO rotate based on normal to center
		draw(gl);
		gl.glPopMatrix();	
	}
	
	public void draw(GL10 gl)
	{
		mVertexBuffer.position(0);
		mIndexBuffer.position(0);	
		mColorBuffer.position(0);
		
		gl.glEnableClientState(GL10.GL_COLOR_ARRAY);
		gl.glEnable(GL10.GL_NORMALIZE);
		
		gl.glVertexPointer(3, GL10.GL_FLOAT, 0, mVertexBuffer);
		gl.glColorPointer(4, GL10.GL_FLOAT, 0, mColorBuffer);
		
		//two sided plane
		gl.glFrontFace(GL10.GL_CCW);
		gl.glDrawElements(GL10.GL_TRIANGLE_STRIP, nVertices, GL10.GL_UNSIGNED_SHORT, mIndexBuffer);		
		gl.glFrontFace(GL10.GL_CW);
		gl.glDrawElements(GL10.GL_TRIANGLE_STRIP, nVertices, GL10.GL_UNSIGNED_SHORT, mIndexBuffer);

		gl.glDisable(GL10.GL_NORMALIZE);
		gl.glDisableClientState(GL10.GL_COLOR_ARRAY);
	}	
	
	public void updateColor(int color)
	{
		float[] VCol = new float[4];
		Utils.ColorIntToFloatVector(color, VCol);
		
		mColorBuffer.position(0);
		for (int i=0;i<nVertices;i++)
		{
			mColorBuffer.put(VCol,0,4);
		}			
	}

	public void updateTool(Managers mManagers)
	{
				
	}	
	
	//geometry is a cut cone along z with base in x,y plane
	private void updateGeometry(float fSmallRadius, float fBigRadius, float fHeight )
	{
		float angle=0;
		float incr=360/nVertices;
		boolean bIsTop=false;
		mVertexBuffer.position(0);
		for (int i=0;i<nVertices;i++)
		{			
			float x=(float) Math.cos(Math.toRadians(angle));
			float y=(float) Math.sin(Math.toRadians(angle));
			float z=0f;
			if (bIsTop) z=fHeight;
				
			mVertexBuffer.put(x);
			mVertexBuffer.put(y);
			mVertexBuffer.put(z);
			
			angle+=incr;
			bIsTop=!bIsTop;				
		}				
	}
	
}
