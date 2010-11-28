package truesculpt.renderer.generator;

import java.util.Vector;

public class BasicSphereGenerator {

	Vector<Integer> faces = new Vector<Integer>();
	Vector<Float> vertices = new Vector<Float>();

	public BasicSphereGenerator() {
		int nTheta = 50;
		int nPhi = 50;
		float deltaTheta = 360.0f / nTheta;
		float deltaPhi = 180.0f / nPhi;
		float R = 1.0f;

		for (float theta = 0; theta < 360.0f; theta += deltaTheta) {
			for (float phi = -90; phi < 90; phi += deltaPhi) {

			}
		}

	}

}
