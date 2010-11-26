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

/**
 * A vertex shaded cube.
 */
public class GeneratedObject
{
    private FloatBuffer   mColorBuffer=null;
    private ShortBuffer  mIndexBuffer=null;
    private FloatBuffer   mVertexBuffer=null;
        
    private int mFacesCount=0;
	private int mVertexCount=0;
    
    public int getFacesCount() {
		return mFacesCount;
	}
    
    public int getVertexCount() {
		return mVertexCount;
	}

	public GeneratedObject()
    {    	
		RecursiveSphereGenerator mGenerator=new RecursiveSphereGenerator(4);
    	
    	Vector<Float> vertices= mGenerator.getVertices();
    	Vector<Integer> faces=mGenerator.getFaces();
    	
    	mVertexCount=vertices.size()/3;
        ByteBuffer vbb = ByteBuffer.allocateDirect(mVertexCount*3*4);//float is 4 bytes, vertices contains x,y,z in seq
        vbb.order(ByteOrder.nativeOrder());
        mVertexBuffer = vbb.asFloatBuffer();
        putFloatVectorToBuffer(mVertexBuffer,vertices);
        mVertexBuffer.position(0);

       
        ByteBuffer cbb = ByteBuffer.allocateDirect(mVertexCount*4*4); //4 color elem (RGBA) in float (4 bytes)
        cbb.order(ByteOrder.nativeOrder());
        mColorBuffer = cbb.asFloatBuffer();
        putRandomColorsInFloatBuffer(mColorBuffer,mVertexCount);
        mColorBuffer.position(0);

        mFacesCount=faces.size()/3;
        ByteBuffer ibb = ByteBuffer.allocateDirect(mFacesCount*3*2);//faces are 3 elements in short ( 2 bytes )
        ibb.order(ByteOrder.nativeOrder());
        mIndexBuffer = ibb.asShortBuffer();
        putIntVectorToShortBuffer( mIndexBuffer,faces);
        mIndexBuffer.position(0);		
    } 
    
    private void  putRandomColorsInFloatBuffer(FloatBuffer buff, int nCount)
    {
    	Random rand = new Random();
		for (int j = 0; j < nCount; j++) 
		{
			float r = rand.nextFloat();
			float g = rand.nextFloat();
			float b = rand.nextFloat();
			
			float[] col={r,g,b,1};
			buff.put(col);			
		}    	
    }
    private void putFloatVectorToBuffer(FloatBuffer buff, Vector<Float> vec)
    {
    	int n=vec.size();
    	for (int i=0;i<n;i++)
    	{
    		Float val=vec.get(i);
    		if (val!=null)
    		{
    			buff.put((float)val);
    		}
    		else
    		{
    			assert(false);
    		}
    	}    	
    }
    
    private void putIntVectorToShortBuffer(ShortBuffer buff, Vector<Integer> vec)
    {
    	int n=vec.size();
    	for (int i=0;i<n;i++)
    	{
    		Integer val=vec.get(i);
    		if (val!=null)
    		{
    			buff.put((short)(int)val);
    		}
    		else
    		{
    			assert(false);
    		}
    	}
    	
    }
    public void draw(GL10 gl)
    {    	
        gl.glFrontFace(GL10.GL_CCW);//counter clock wise is specific to previous format
        gl.glVertexPointer(3, GL10.GL_FLOAT, 0, mVertexBuffer);
        gl.glColorPointer(4, GL10.GL_FLOAT, 0, mColorBuffer);
        gl.glDrawElements(GL10.GL_TRIANGLES, mFacesCount*3, GL10.GL_UNSIGNED_SHORT, mIndexBuffer);    
    }

	public FloatBuffer getVertexBuffer() {
		return mVertexBuffer;
	}

	public ShortBuffer getIndexBuffer() {
		return mIndexBuffer;
	}

	public FloatBuffer getColorBuffer() {
		return mColorBuffer;
	}
}
