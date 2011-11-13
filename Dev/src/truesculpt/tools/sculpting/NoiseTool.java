package truesculpt.tools.sculpting;

import java.util.Random;

import truesculpt.actions.SculptAction;
import truesculpt.main.Managers;
import truesculpt.main.R;
import truesculpt.mesh.RenderFaceGroup;
import truesculpt.mesh.Vertex;
import truesculpt.tools.base.SculptingTool;
import truesculpt.utils.MatrixUtils;

public class NoiseTool extends SculptingTool
{

	public NoiseTool(Managers managers)
	{
		super(managers);

	}

	private final Random mGenerator = new Random();

	@Override
	protected void Work()
	{
		for (Vertex vertex : mVerticesRes)
		{
			MatrixUtils.copy(vertex.Normal, VOffset);

			float newOffsetFactor = mGenerator.nextFloat() * 2 - 1;// -1 to 1

			MatrixUtils.scalarMultiply(VOffset, newOffsetFactor * mMaxDeformation);

			MatrixUtils.plus(VOffset, vertex.Coord, VOffset);
			((SculptAction) mAction).AddNewVertexValue(VOffset, vertex);

			// preview
			MatrixUtils.copy(vertex.Normal, VNormal);
			MatrixUtils.scalarMultiply(VNormal, 1 - newOffsetFactor);
			for (RenderFaceGroup renderGroup : mMesh.mRenderGroupList)
			{
				renderGroup.UpdateVertexValue(vertex.Index, VOffset, VNormal);
			}
		}
	}

	@Override
	public String GetName()
	{
		return "Noise";
	}

	@Override
	public int GetIcon()
	{
		return R.drawable.noise;
	}

}
