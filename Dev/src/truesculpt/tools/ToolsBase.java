package truesculpt.tools;

import truesculpt.mesh.Mesh;

public interface ToolsBase {

	public void Start(Mesh mesh);
	public void Pick(float x, float y);
	public void Stop();
	
}
