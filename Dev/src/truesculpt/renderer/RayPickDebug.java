package truesculpt.renderer;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;

import javax.microedition.khronos.opengles.GL10;

/**
 * A vertex shaded cube.
 */
public class RayPickDebug
{
	private FloatBuffer mColorBuffer;
	private ShortBuffer mIndexBuffer;
	private FloatBuffer mVertexBuffer;

	public RayPickDebug()
	{
		float zero = 0.0f;
		float one = 1.0f;
		float vertices[] = { zero, zero, zero, zero, zero, zero };

		float colors[] = { one, one, one, one, one, one, one, one };

		short indices[] = { 0, 1 };

		// Buffers to be passed to gl*Pointer() functions
		// must be direct, i.e., they must be placed on the
		// native heap where the garbage collector cannot
		// move them.
		//
		// Buffers with multi-byte datatypes (e.g., short, int, float)
		// must have their byte order set to native order

		ByteBuffer vbb = ByteBuffer.allocateDirect(2 * 3 * 4);
		vbb.order(ByteOrder.nativeOrder());
		mVertexBuffer = vbb.asFloatBuffer();
		mVertexBuffer.put(vertices);

		ByteBuffer cbb = ByteBuffer.allocateDirect(2 * 4 * 4);
		cbb.order(ByteOrder.nativeOrder());
		mColorBuffer = cbb.asFloatBuffer();
		mColorBuffer.put(colors);

		ByteBuffer ibb = ByteBuffer.allocateDirect(2 * 2);
		ibb.order(ByteOrder.nativeOrder());
		mIndexBuffer = ibb.asShortBuffer();
		mIndexBuffer.put(indices);
	}

	public void draw(GL10 gl)
	{
		mVertexBuffer.position(0);
		mIndexBuffer.position(0);	
		mColorBuffer.position(0);
		
		gl.glEnableClientState(GL10.GL_COLOR_ARRAY);

		gl.glFrontFace(GL10.GL_CW);
		gl.glVertexPointer(3, GL10.GL_FLOAT, 0, mVertexBuffer);
		gl.glColorPointer(4, GL10.GL_FLOAT, 0, mColorBuffer);
		gl.glDrawElements(GL10.GL_LINE_STRIP, 2, GL10.GL_UNSIGNED_SHORT, mIndexBuffer);

		gl.glDisableClientState(GL10.GL_COLOR_ARRAY);
	}

	public void setRayPos(float[] pt1, float[] pt2)
	{
		mVertexBuffer.position(0);
		mVertexBuffer.put(pt1);
		mVertexBuffer.put(pt2);
	}
}
