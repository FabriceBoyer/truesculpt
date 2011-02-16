package truesculpt.mesh;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;

import javax.microedition.khronos.opengles.GL10;

import truesculpt.utils.MatrixUtils;
import truesculpt.utils.Utils;

public class RenderFaceGroup
{
	private FloatBuffer mColorBuffer = null;
	private ShortBuffer mIndexBuffer = null;
	private FloatBuffer mVertexBuffer = null;
	private FloatBuffer mNormalBuffer = null;

	private ShortBuffer mDrawNormalIndexBuffer = null;
	private FloatBuffer mDrawNormalVertexBuffer = null;

	private int mFacesCount = 0;
	private int mVertexCount = 0;

	private Mesh mMesh = null;

	public RenderFaceGroup(Mesh mesh)
	{
		mMesh = mesh;

		mVertexCount = mMesh.mVertexList.size();
		ByteBuffer vbb = ByteBuffer.allocateDirect(mVertexCount * 3 * 4);// vertices contains 3 elem (x,y,z) in float(4 bytes)
		vbb.order(ByteOrder.nativeOrder());
		mVertexBuffer = vbb.asFloatBuffer();
		for (Vertex vertex : mMesh.mVertexList)
		{
			mVertexBuffer.put(vertex.Coord[0]);
			mVertexBuffer.put(vertex.Coord[1]);
			mVertexBuffer.put(vertex.Coord[2]);
		}

		ByteBuffer cbb = ByteBuffer.allocateDirect(mVertexCount * 4 * 4); // color is a 4 elem group (RGBA) in float (4 bytes)
		cbb.order(ByteOrder.nativeOrder());
		mColorBuffer = cbb.asFloatBuffer();
		float[] VCol = new float[4];
		for (Vertex vertex : mMesh.mVertexList)
		{
			Utils.ColorIntToFloatVector(vertex.Color, VCol);
			mColorBuffer.put(VCol);
		}

		mFacesCount = mMesh.mFaceList.size();
		ByteBuffer ibb = ByteBuffer.allocateDirect(mFacesCount * 3 * 2);// faces are 3 vertex indices (i,j,k) of the vertextable in short ( 2 bytes )
		ibb.order(ByteOrder.nativeOrder());
		mIndexBuffer = ibb.asShortBuffer();
		for (Face face : mMesh.mFaceList)
		{
			mIndexBuffer.put((short)face.V0);
			mIndexBuffer.put((short)face.V1);
			mIndexBuffer.put((short)face.V2);
		}

		ByteBuffer nbb = ByteBuffer.allocateDirect(mVertexCount * 3 * 4);// normals contains 3 elem (x,y,z) in float(4 bytes)
		nbb.order(ByteOrder.nativeOrder());
		mNormalBuffer = nbb.asFloatBuffer();
		for (Vertex vertex : mMesh.mVertexList)
		{
			mNormalBuffer.put(vertex.Normal[0]);
			mNormalBuffer.put(vertex.Normal[1]);
			mNormalBuffer.put(vertex.Normal[2]);
		}

		ByteBuffer ndvbb = ByteBuffer.allocateDirect(mVertexCount * 2 * 3 * 4);//  normals contains 3 elem (x,y,z) in float(4 bytes)
		ndvbb.order(ByteOrder.nativeOrder());
		mDrawNormalVertexBuffer = ndvbb.asFloatBuffer();
		for (Vertex vertex : mMesh.mVertexList)
		{
			mDrawNormalVertexBuffer.put(vertex.Coord[0]);
			mDrawNormalVertexBuffer.put(vertex.Coord[1]);
			mDrawNormalVertexBuffer.put(vertex.Coord[2]);
			
			float scaleFactor=0.1f;
			mDrawNormalVertexBuffer.put(vertex.Coord[0]+vertex.Normal[0]*scaleFactor);
			mDrawNormalVertexBuffer.put(vertex.Coord[1]+vertex.Normal[1]*scaleFactor);
			mDrawNormalVertexBuffer.put(vertex.Coord[2]+vertex.Normal[2]*scaleFactor);
		}

		ByteBuffer ndibb = ByteBuffer.allocateDirect(mVertexCount * 2 * 2);// line are 2 elements in short ( 2 bytes )
		ndibb.order(ByteOrder.nativeOrder());
		mDrawNormalIndexBuffer = ndibb.asShortBuffer();
		for (int i=0;i<mVertexCount;i=i+2)
		{
			mDrawNormalIndexBuffer.put((short) i);
			mDrawNormalIndexBuffer.put((short)(i+1));
		}
	}

	public void draw(GL10 gl)
	{
		//reinit position once here to avoid doing it in each update (but not great to do it here)
		mVertexBuffer.position(0);
		mColorBuffer.position(0);
		mIndexBuffer.position(0);
		mNormalBuffer.position(0);
		
		gl.glEnableClientState(GL10.GL_COLOR_ARRAY);
		gl.glEnableClientState(GL10.GL_NORMAL_ARRAY);

		gl.glFrontFace(GL10.GL_CCW);// counter clock wise is specific to previous format
		gl.glVertexPointer(3, GL10.GL_FLOAT, 0, mVertexBuffer);
		gl.glColorPointer(4, GL10.GL_FLOAT, 0, mColorBuffer);
		gl.glNormalPointer(GL10.GL_FLOAT, 0, mNormalBuffer);

		//Wireframe : use GL10.GL_LINES
		gl.glDrawElements(GL10.GL_TRIANGLES, mFacesCount * 3, GL10.GL_UNSIGNED_SHORT, mIndexBuffer);

		gl.glDisableClientState(GL10.GL_NORMAL_ARRAY);
		gl.glDisableClientState(GL10.GL_COLOR_ARRAY);
	}

	public void drawNormals(GL10 gl)
	{
		mDrawNormalIndexBuffer.position(0);
		mDrawNormalVertexBuffer.position(0);
		
		gl.glFrontFace(GL10.GL_CCW);// counter clock wise is specific to previous format
		gl.glVertexPointer(3, GL10.GL_FLOAT, 0, mDrawNormalVertexBuffer);

		int nVertexCount = mMesh.getVertexCount();			
		gl.glDrawElements(GL10.GL_LINES, nVertexCount, GL10.GL_UNSIGNED_SHORT, mDrawNormalIndexBuffer);		
	}

	public FloatBuffer getColorBuffer()
	{
		return mColorBuffer;
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
	
	public void UpdateVertexValue(int nVertexIndex, float[] val, float[] normal)
	{
		mVertexBuffer.position(nVertexIndex*3);
		mVertexBuffer.put(val, 0, 3);
		
		mNormalBuffer.position(nVertexIndex*3);
		mNormalBuffer.put(normal, 0, 3);		
	}
	
	public void UpdateVertexColor( int nVertexIndex, int color)
	{
		mColorBuffer.position(nVertexIndex*4);
		float[] VCol = new float[4];
		Utils.ColorIntToFloatVector(color, VCol);
		mColorBuffer.put(VCol,0,4);		
	}
}
