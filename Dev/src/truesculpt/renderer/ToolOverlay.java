package truesculpt.renderer;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;

import javax.microedition.khronos.opengles.GL10;

import truesculpt.main.Managers;
import truesculpt.managers.ToolsManager.EToolMode;
import truesculpt.utils.MatrixUtils;
import truesculpt.utils.Utils;
import android.graphics.Color;

/**
 * A vertex shaded cube.
 */
public class ToolOverlay
{
	private final FloatBuffer mColorBuffer;
	private final ShortBuffer mIndexBuffer;
	private final FloatBuffer mVertexBuffer;

	int mnVertices = 100;
	float mDefaultTransparency = 0.2f;
	float mOffset[] = new float[3];
	float mScale[] = new float[3];
	boolean mbShowOverlay = false;

	public ToolOverlay()
	{
		ByteBuffer vbb = ByteBuffer.allocateDirect(mnVertices * 3 * 4);
		vbb.order(ByteOrder.nativeOrder());
		mVertexBuffer = vbb.asFloatBuffer();
		for (int i = 0; i < mnVertices; i++)
		{
			mVertexBuffer.put(0.0f);
			mVertexBuffer.put(0.0f);
			mVertexBuffer.put(0.0f);
		}

		ByteBuffer cbb = ByteBuffer.allocateDirect(mnVertices * 4 * 4);
		cbb.order(ByteOrder.nativeOrder());
		mColorBuffer = cbb.asFloatBuffer();
		updateColor(Color.BLUE, mDefaultTransparency);

		ByteBuffer ibb = ByteBuffer.allocateDirect((mnVertices + 2) * 2);
		ibb.order(ByteOrder.nativeOrder());
		mIndexBuffer = ibb.asShortBuffer();
		for (int i = 0; i < mnVertices; i++)
		{
			mIndexBuffer.put((short) i);
		}
		mIndexBuffer.put((short) 0);
		mIndexBuffer.put((short) 1);

		mOffset[0] = 0f;
		mOffset[1] = 0f;
		mOffset[2] = 0f;

		mScale[0] = 1f;
		mScale[1] = 1f;
		mScale[2] = 1f;

		updateGeometry(0.9f, 1f, 0.1f);
	}

	public void draw(GL10 gl, Managers managers)
	{
		if (mbShowOverlay)
		{
			managers.getMeshManager().getLastPickingPoint(mOffset);

			gl.glPushMatrix();
			gl.glScalef(mScale[0], mScale[1], mScale[2]);

			float length = MatrixUtils.magnitude(mOffset);
			gl.glTranslatef(mOffset[0], mOffset[1], mOffset[2]);
			float rot = (float) Math.toDegrees((float) Math.atan2(mOffset[0], mOffset[2]));
			float elev = (float) Math.toDegrees((float) Math.asin(mOffset[1] / length));
			gl.glRotatef(rot, 0, 1, 0);
			gl.glRotatef(-elev, 1, 0, 0);

			draw(gl);
			gl.glPopMatrix();
		}
	}

	private void draw(GL10 gl)
	{
		synchronized (this)
		{
			mVertexBuffer.position(0);
			mIndexBuffer.position(0);
			mColorBuffer.position(0);

			gl.glEnableClientState(GL10.GL_COLOR_ARRAY);

			gl.glVertexPointer(3, GL10.GL_FLOAT, 0, mVertexBuffer);
			gl.glColorPointer(4, GL10.GL_FLOAT, 0, mColorBuffer);

			// two sided plane
			gl.glFrontFace(GL10.GL_CCW);
			gl.glDrawElements(GL10.GL_TRIANGLE_STRIP, mnVertices + 2, GL10.GL_UNSIGNED_SHORT, mIndexBuffer);
			gl.glFrontFace(GL10.GL_CW);
			gl.glDrawElements(GL10.GL_TRIANGLE_STRIP, mnVertices + 2, GL10.GL_UNSIGNED_SHORT, mIndexBuffer);

			gl.glDisableClientState(GL10.GL_COLOR_ARRAY);
		}
	}

	public void updateColor(int color, float transparency)
	{
		float[] VCol = new float[4];
		Utils.ColorIntToFloatVector(color, VCol);
		VCol[3] = transparency;

		mColorBuffer.position(0);
		for (int i = 0; i < mnVertices; i++)
		{
			mColorBuffer.put(VCol, 0, 4);
		}
	}

	public void updateTool(Managers mManagers)
	{
		float strength = mManagers.getToolsManager().getStrength();// -100 to 100
		float radius = mManagers.getToolsManager().getRadius();// 0 to 100;

		float signedNormalizedStrength = strength / 100f;
		float bigRadius = radius / 100f + 0.1f;
		float smallRadius = bigRadius * 0.8f;// bigRadius*(1f-Math.abs(signedNormalizedStrength));
		float height = 0f;
		int color = Color.BLUE;
		if (signedNormalizedStrength < 0) color = Color.RED;
		float[] VCol = new float[3];
		Color.colorToHSV(color, VCol);
		VCol[2] = Math.abs(signedNormalizedStrength) * VCol[2];
		color = Color.HSVToColor(VCol);

		mbShowOverlay = mManagers.getToolsManager().getCurrentTool().RequiresToolOverlay();

		if (mManagers.getToolsManager().getToolMode() == EToolMode.POV)
		{
			// bShowOverlay=false;
		}

		if (mbShowOverlay)
		{
			synchronized (this)
			{
				updateGeometry(smallRadius, bigRadius, height);
				updateColor(color, mDefaultTransparency);
			}
		}
	}

	// geometry is a cut cone along z with base in x,y plane
	private void updateGeometry(float fSmallRadius, float fBigRadius, float fHeight)
	{
		float angle = 0;
		float incr = 360f / mnVertices;
		boolean bIsTop = false;
		mVertexBuffer.position(0);
		for (int i = 0; i < mnVertices; i++)
		{
			float x = (float) Math.cos(Math.toRadians(angle));
			float y = (float) Math.sin(Math.toRadians(angle));
			float z = 0f;
			if (bIsTop)
			{
				x *= fSmallRadius;
				y *= fSmallRadius;
				z = fHeight;
			}
			else
			{
				x *= fBigRadius;
				y *= fBigRadius;
				z = 0f;
			}

			mVertexBuffer.put(x);
			mVertexBuffer.put(y);
			mVertexBuffer.put(z);

			angle += incr;
			bIsTop = !bIsTop;
		}
	}

}
