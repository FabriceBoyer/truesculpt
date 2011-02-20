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
import truesculpt.managers.ToolsManager.ESymmetryMode;
import truesculpt.utils.Utils;

/**
 * A vertex shaded cube.
 */
public class SymmetryPlane
{
	private FloatBuffer mColorBuffer;
	private ShortBuffer mIndexBuffer;
	private FloatBuffer mVertexBuffer;
	private float mTransp=0.3f;
	
	public SymmetryPlane()
	{
		float zero = 0.0f;
		float one = 1.0f;
		float large=3.0f;
		float vertices[] = { large, large, zero,
							-large, large, zero,
							-large, -large, zero,
							large, -large, zero };
		float colors[] = { zero, one, zero, mTransp,
							zero, one, zero, mTransp,
							zero, one, zero, mTransp,
							zero, one, zero, mTransp};
		short indices[] = { 0, 1, 3,
							1, 2, 3};

		// Buffers to be passed to gl*Pointer() functions
		// must be direct, i.e., they must be placed on the
		// native heap where the garbage collector cannot
		// move them.
		//
		// Buffers with multi-byte datatypes (e.g., short, int, float)
		// must have their byte order set to native order

		ByteBuffer vbb = ByteBuffer.allocateDirect(4 * 3 * 4);
		vbb.order(ByteOrder.nativeOrder());
		mVertexBuffer = vbb.asFloatBuffer();
		mVertexBuffer.put(vertices);
		mVertexBuffer.position(0);

		ByteBuffer cbb = ByteBuffer.allocateDirect(4 * 4 * 4);
		cbb.order(ByteOrder.nativeOrder());
		mColorBuffer = cbb.asFloatBuffer();
		mColorBuffer.put(colors);
		mColorBuffer.position(0);

		ByteBuffer ibb = ByteBuffer.allocateDirect(6 * 2);
		ibb.order(ByteOrder.nativeOrder());
		mIndexBuffer = ibb.asShortBuffer();
		mIndexBuffer.put(indices);
		mIndexBuffer.position(0);
	}
	
	public void draw(GL10 gl,Managers managers)
	{
		gl.glPushMatrix();
		if (managers.getToolsManager().getSymmetryMode()!=ESymmetryMode.NONE)
		{			
			float quarter=90;//degrees
			switch (managers.getToolsManager().getSymmetryMode())
			{
			case X:
				gl.glRotatef(quarter, 0, 1, 0);
				break;
			case Y:
				gl.glRotatef(quarter, 1, 0, 0);
				break;
			case Z:				
				break;				
			}		
			draw(gl);
		}		
		gl.glPopMatrix();
	}

	public void draw(GL10 gl)
	{
		gl.glEnableClientState(GL10.GL_COLOR_ARRAY);
		
		gl.glVertexPointer(3, GL10.GL_FLOAT, 0, mVertexBuffer);
		gl.glColorPointer(4, GL10.GL_FLOAT, 0, mColorBuffer);
		
		//two sided plane
		gl.glFrontFace(GL10.GL_CCW);
		gl.glDrawElements(GL10.GL_TRIANGLES, 6, GL10.GL_UNSIGNED_SHORT, mIndexBuffer);		
		gl.glFrontFace(GL10.GL_CW);
		gl.glDrawElements(GL10.GL_TRIANGLES, 6, GL10.GL_UNSIGNED_SHORT, mIndexBuffer);

		gl.glDisableClientState(GL10.GL_COLOR_ARRAY);
	}	
	
	public void setColor(int color)
	{
		float[] VCol = new float[4];
		Utils.ColorIntToFloatVector(color, VCol);
		VCol[3]=mTransp;
		mColorBuffer.position(0);
		mColorBuffer.put(VCol,0,4);
		mColorBuffer.put(VCol,0,4);	
		mColorBuffer.put(VCol,0,4);	
		mColorBuffer.put(VCol,0,4);			
	}
	
	public void scalePlaneSize(float factor)
	{
		
	}
	
}
