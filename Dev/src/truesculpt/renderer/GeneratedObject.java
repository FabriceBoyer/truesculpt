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
import java.util.Random;
import java.util.Vector;

import javax.microedition.khronos.opengles.GL10;

import truesculpt.renderer.generator.RecursiveSphereGenerator;
import truesculpt.utils.MatrixUtils;
import truesculpt.utils.Utils;

/**
 * A vertex shaded cube.
 */
public class GeneratedObject
{
	private FloatBuffer mColorBuffer = null;
	private ShortBuffer mIndexBuffer = null;
	private FloatBuffer mVertexBuffer = null;
	private FloatBuffer mNormalBuffer = null;

	private ShortBuffer mDrawNormalIndexBuffer = null;
	private FloatBuffer mDrawNormalVertexBuffer = null;

	private int mFacesCount = 0;
	private int mVertexCount = 0;

	public GeneratedObject(int color, int recursionLevel)
	{
		RecursiveSphereGenerator mGenerator = new RecursiveSphereGenerator(recursionLevel);

		Vector<Float> vertices = mGenerator.getVertices();
		Vector<Integer> faces = mGenerator.getFaces();
		Vector<Float> normals = mGenerator.getNormals();

		mVertexCount = vertices.size() / 3;
		ByteBuffer vbb = ByteBuffer.allocateDirect(mVertexCount * 3 * 4);// float is 4 bytes, vertices contains x,y,z in seq
		vbb.order(ByteOrder.nativeOrder());
		mVertexBuffer = vbb.asFloatBuffer();
		putFloatVectorToBuffer(mVertexBuffer, vertices);
		mVertexBuffer.position(0);

		ByteBuffer cbb = ByteBuffer.allocateDirect(mVertexCount * 4 * 4); // 4 color elem (RGBA) in float (4 bytes)
		cbb.order(ByteOrder.nativeOrder());
		mColorBuffer = cbb.asFloatBuffer();
		if (color != -1)
		{
			putColorInFloatBuffer(mColorBuffer, mVertexCount, color);
		} else
		{
			putRandomColorsInFloatBuffer(mColorBuffer, mVertexCount);
		}
		mColorBuffer.position(0);

		mFacesCount = faces.size() / 3;
		ByteBuffer ibb = ByteBuffer.allocateDirect(mFacesCount * 3 * 2);// faces are 3 elements in short ( 2 bytes )
		ibb.order(ByteOrder.nativeOrder());
		mIndexBuffer = ibb.asShortBuffer();
		putIntVectorToShortBuffer(mIndexBuffer, faces);
		mIndexBuffer.position(0);

		ByteBuffer nbb = ByteBuffer.allocateDirect(mVertexCount * 3 * 4);// float is 4 bytes, normals contains x,y,z in seq
		nbb.order(ByteOrder.nativeOrder());
		mNormalBuffer = nbb.asFloatBuffer();
		putFloatVectorToBuffer(mNormalBuffer, normals);
		mNormalBuffer.position(0);

		ByteBuffer ndvbb = ByteBuffer.allocateDirect(2 * 3 * 4);// float is 4 bytes, normals contains x,y,z in seq
		ndvbb.order(ByteOrder.nativeOrder());
		mDrawNormalVertexBuffer = ndvbb.asFloatBuffer();
		mDrawNormalVertexBuffer.position(0);

		ByteBuffer ndibb = ByteBuffer.allocateDirect(2 * 2);// line are 3 elements in short ( 2 bytes )
		ndibb.order(ByteOrder.nativeOrder());
		mDrawNormalIndexBuffer = ndibb.asShortBuffer();
		mDrawNormalIndexBuffer.position(0);
		mDrawNormalIndexBuffer.put((short) 0);
		mDrawNormalIndexBuffer.put((short) 1);
		mDrawNormalIndexBuffer.position(0);

	}

	public void draw(GL10 gl)
	{
		gl.glEnableClientState(GL10.GL_COLOR_ARRAY);
		gl.glEnableClientState(GL10.GL_NORMAL_ARRAY);

		gl.glFrontFace(GL10.GL_CCW);// counter clock wise is specific to previous format
		gl.glVertexPointer(3, GL10.GL_FLOAT, 0, mVertexBuffer);
		gl.glColorPointer(4, GL10.GL_FLOAT, 0, mColorBuffer);
		gl.glNormalPointer(GL10.GL_FLOAT, 0, mNormalBuffer);
		gl.glDrawElements(GL10.GL_TRIANGLES, mFacesCount * 3, GL10.GL_UNSIGNED_SHORT, mIndexBuffer);

		gl.glDisableClientState(GL10.GL_NORMAL_ARRAY);
		gl.glDisableClientState(GL10.GL_COLOR_ARRAY);
	}

	public void drawNormals(GL10 gl)
	{
		gl.glFrontFace(GL10.GL_CCW);// counter clock wise is specific to previous format
		gl.glVertexPointer(3, GL10.GL_FLOAT, 0, mDrawNormalVertexBuffer);

		float[] V0 = new float[3];
		float[] V1 = new float[3];

		int nCount = mNormalBuffer.capacity();
		for (int i = 0; i < nCount; i = i + 3)
		{
			mVertexBuffer.position(i);
			mVertexBuffer.get(V0, 0, 3);

			mNormalBuffer.position(i);
			mNormalBuffer.get(V1, 0, 3);

			MatrixUtils.scalarMultiply(V1, 0.1f);
			MatrixUtils.plus(V0, V1, V1);

			mDrawNormalVertexBuffer.position(0);
			mDrawNormalVertexBuffer.put(V0);
			mDrawNormalVertexBuffer.position(3);
			mDrawNormalVertexBuffer.put(V1);
			mDrawNormalVertexBuffer.position(0);

			gl.glDrawElements(GL10.GL_LINES, 2, GL10.GL_UNSIGNED_SHORT, mDrawNormalIndexBuffer);
		}

		mVertexBuffer.position(0);
		mNormalBuffer.position(0);

	}

	public FloatBuffer getColorBuffer()
	{
		return mColorBuffer;
	}

	public int getFacesCount()
	{
		return mFacesCount;
	}

	public ShortBuffer getIndexBuffer()
	{
		return mIndexBuffer;
	}

	public FloatBuffer getNormalBuffer()
	{
		return mNormalBuffer;
	}

	public FloatBuffer getVertexBuffer()
	{
		return mVertexBuffer;
	}

	public int getVertexCount()
	{
		return mVertexCount;
	}

	private void putColorInFloatBuffer(FloatBuffer buff, int nCount, int color)
	{
		float[] VCol = new float[4];

		Utils.ColorIntToFloatVector(color, VCol);

		for (int j = 0; j < nCount; j++)
		{
			buff.put(VCol);
		}
	}

	private void putFloatVectorToBuffer(FloatBuffer buff, Vector<Float> vec)
	{
		int n = vec.size();
		for (int i = 0; i < n; i++)
		{
			Float val = vec.get(i);
			if (val != null)
			{
				buff.put(val);
			} else
			{
				assert false;
			}
		}
	}

	private void putIntVectorToShortBuffer(ShortBuffer buff, Vector<Integer> vec)
	{
		int n = vec.size();
		for (int i = 0; i < n; i++)
		{
			Integer val = vec.get(i);
			if (val != null)
			{
				buff.put((short) (int) val);
			} else
			{
				assert false;
			}
		}

	}

	private void putRandomColorsInFloatBuffer(FloatBuffer buff, int nCount)
	{
		Random rand = new Random();
		for (int j = 0; j < nCount; j++)
		{
			float r = rand.nextFloat();
			float g = rand.nextFloat();
			float b = rand.nextFloat();

			float[] col = { r, g, b, 1 };
			buff.put(col);
		}
	}

}
