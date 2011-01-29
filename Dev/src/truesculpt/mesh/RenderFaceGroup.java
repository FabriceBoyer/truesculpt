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
		ByteBuffer vbb = ByteBuffer.allocateDirect(mVertexCount * 3 * 4);// float is 4 bytes, vertices contains x,y,z in seq
		vbb.order(ByteOrder.nativeOrder());
		mVertexBuffer = vbb.asFloatBuffer();
		for (Vertex vertex : mMesh.mVertexList)
		{
			mVertexBuffer.put(vertex.Coord[0]);
			mVertexBuffer.put(vertex.Coord[1]);
			mVertexBuffer.put(vertex.Coord[2]);
		}
		mVertexBuffer.position(0);

		ByteBuffer cbb = ByteBuffer.allocateDirect(mVertexCount * 4 * 4); // 4 color elem (RGBA) in float (4 bytes)
		cbb.order(ByteOrder.nativeOrder());
		mColorBuffer = cbb.asFloatBuffer();
		float[] VCol = new float[4];
		for (Vertex vertex : mMesh.mVertexList)
		{
			Utils.ColorIntToFloatVector(vertex.Color, VCol);
			mColorBuffer.put(VCol);
		}
		mColorBuffer.position(0);

		mFacesCount = mMesh.mFaceList.size();
		ByteBuffer ibb = ByteBuffer.allocateDirect(mFacesCount * 3 * 2);// faces are 3 elements (vertex index ) in short ( 2 bytes )
		ibb.order(ByteOrder.nativeOrder());
		mIndexBuffer = ibb.asShortBuffer();
		for (Face face : mMesh.mFaceList)
		{
			mIndexBuffer.put((short) mMesh.mVertexList.indexOf(face.getV0()));
			mIndexBuffer.put((short) mMesh.mVertexList.indexOf(face.getV1()));
			mIndexBuffer.put((short) mMesh.mVertexList.indexOf(face.getV2()));
		}
		mIndexBuffer.position(0);

		ByteBuffer nbb = ByteBuffer.allocateDirect(mVertexCount * 3 * 4);// float is 4 bytes, normals contains x,y,z in seq
		nbb.order(ByteOrder.nativeOrder());
		mNormalBuffer = nbb.asFloatBuffer();
		for (Vertex vertex : mMesh.mVertexList)
		{
			mNormalBuffer.put(vertex.Normal[0]);
			mNormalBuffer.put(vertex.Normal[1]);
			mNormalBuffer.put(vertex.Normal[2]);
		}
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

}
