package truesculpt.renderer.generator;

import java.util.Arrays;
import java.util.Vector;

import truesculpt.utils.MatrixUtils;

public class RecursiveSphereGenerator {
	int edge_walk = 0;
	Vector<Integer> end = new Vector<Integer>();
	Vector<Integer> faces = new Vector<Integer>();

	Vector<Integer> midpoint = new Vector<Integer>();
	int n_edges = 0;
	int n_faces = 0;

	int n_vertices = 0;
	Vector<Float> normals = new Vector<Float>();
	Vector<Integer> start = new Vector<Integer>();
	Vector<Float> vertices = new Vector<Float>();

	public RecursiveSphereGenerator(int subdivideCount) {
		init_icosahedron();
		// init_octahedron();

		int n_subdivisions = subdivideCount;
		for (int i = 0; i < n_subdivisions; i++) {
			subdivide();
		}

		generateSphereNormals();
	}

	private void generateSphereNormals() {
		float[] V0 = new float[3];

		int n = vertices.size();
		for (int i = 0; i < n; i = i + 3) {
			Float x = vertices.get(i + 0);
			Float y = vertices.get(i + 1);
			Float z = vertices.get(i + 2);

			if (x != null && y != null && z != null) {
				V0[0] = x;
				V0[1] = y;
				V0[2] = z;

				MatrixUtils.normalize(V0);

				normals.add(V0[0]);
				normals.add(V0[1]);
				normals.add(V0[2]);
			}
		}
	}

	public Vector<Integer> getFaces() {
		return faces;
	}

	public Vector<Float> getNormals() {
		return normals;
	}

	public Vector<Float> getVertices() {
		return vertices;
	}

	void init_icosahedron() {
		float t = (float) ((1 + Math.sqrt(5)) / 2);
		float tau = (float) (t / Math.sqrt(1 + t * t));
		float one = (float) (1 / Math.sqrt(1 + t * t));

		Float icosahedron_vertices[] = { tau, one, 0.0f, -tau, one, 0.0f, -tau, -one, 0.0f, tau, -one, 0.0f, one, 0.0f, tau, one, 0.0f, -tau, -one, 0.0f, -tau, -one, 0.0f, tau, 0.0f, tau, one, 0.0f, -tau, one, 0.0f, -tau, -one, 0.0f, tau, -one };
		Integer icosahedron_faces[] = { 4, 8, 7, 4, 7, 9, 5, 6, 11, 5, 10, 6, 0, 4, 3, 0, 3, 5, 2, 7, 1, 2, 1, 6, 8, 0, 11, 8, 11, 1, 9, 10, 3, 9, 2, 10, 8, 4, 0, 11, 0, 5, 4, 9, 3, 5, 3, 10, 7, 8, 1, 6, 1, 11, 7, 2, 9, 6, 10, 2 };

		n_vertices = 12;
		n_faces = 20;
		n_edges = 30;
		vertices.addAll(Arrays.asList(icosahedron_vertices));
		faces.addAll(Arrays.asList(icosahedron_faces));
	}

	void init_octahedron() {
		Float octahedron_vertices[] = { 0.0f, 0.0f, -1.0f, 1.0f, 0.0f, 0.0f, 0.0f, -1.0f, 0.0f, -1.0f, 0.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f, 0.0f, 1.0f };
		Integer octahedron_faces[] = { 0, 1, 2, 0, 2, 3, 0, 3, 4, 0, 4, 1, 5, 2, 1, 5, 3, 2, 5, 4, 3, 5, 1, 4 };

		n_vertices = 6;
		n_faces = 8;
		n_edges = 12;
		vertices.addAll(Arrays.asList(octahedron_vertices));
		faces.addAll(Arrays.asList(octahedron_faces));
	}

	void init_tetrahedron() {
		float sqrt3 = (float) (1 / Math.sqrt(3.0));
		Float tetrahedron_vertices[] = { sqrt3, sqrt3, sqrt3, -sqrt3, -sqrt3, sqrt3, -sqrt3, sqrt3, -sqrt3, sqrt3, -sqrt3, -sqrt3 };
		Integer tetrahedron_faces[] = { 0, 2, 1, 0, 1, 3, 2, 3, 1, 3, 2, 0 };

		n_vertices = 4;
		n_faces = 4;
		n_edges = 6;
		vertices.addAll(Arrays.asList(tetrahedron_vertices));
		faces.addAll(Arrays.asList(tetrahedron_faces));
	}

	int search_midpoint(int index_start, int index_end) {
		int i;
		for (i = 0; i < edge_walk; i++) {
			if (start.get(i) == index_start && end.get(i) == index_end || start.get(i) == index_end && end.get(i) == index_start) {
				int res = midpoint.get(i);

				/* update the arrays */
				start.set(i, start.get(edge_walk - 1));
				end.set(i, end.get(edge_walk - 1));
				midpoint.set(i, midpoint.get(edge_walk - 1));
				edge_walk--;

				return res;
			}
		}

		/* vertex not in the list, so we add it */
		start.set(edge_walk, index_start);
		end.set(edge_walk, index_end);
		midpoint.set(edge_walk, n_vertices);

		/* create new vertex */
		vertices.set(3 * n_vertices, (vertices.get(3 * index_start) + vertices.get(3 * index_end)) / 2.0f);
		vertices.set(3 * n_vertices + 1, (vertices.get(3 * index_start + 1) + vertices.get(3 * index_end + 1)) / 2.0f);
		vertices.set(3 * n_vertices + 2, (vertices.get(3 * index_start + 2) + vertices.get(3 * index_end + 2)) / 2.0f);

		/* normalize the new vertex */
		float length = (float) Math.sqrt(vertices.get(3 * n_vertices) * vertices.get(3 * n_vertices) + vertices.get(3 * n_vertices + 1) * vertices.get(3 * n_vertices + 1) + vertices.get(3 * n_vertices + 2) * vertices.get(3 * n_vertices + 2));
		length = 1.0f / length;
		vertices.set(3 * n_vertices, vertices.get(3 * n_vertices) * length);
		vertices.set(3 * n_vertices + 1, vertices.get(3 * n_vertices + 1) * length);
		vertices.set(3 * n_vertices + 2, vertices.get(3 * n_vertices + 2) * length);

		n_vertices++;
		edge_walk++;
		return midpoint.get(edge_walk - 1);
	}

	void subdivide() {
		int n_vertices_new = n_vertices + 2 * n_edges;
		int n_faces_new = 4 * n_faces;
		int i = 0;

		edge_walk = 0;
		n_edges = 2 * n_vertices + 3 * n_faces;

		start.removeAllElements();
		start.setSize(n_edges);
		end.removeAllElements();
		end.setSize(n_edges);
		midpoint.removeAllElements();
		midpoint.setSize(n_edges);

		Vector<Integer> faces_old = new Vector<Integer>();
		faces_old.addAll(faces);

		vertices.setSize(3 * n_vertices_new);
		faces.setSize(3 * n_faces_new);
		n_faces_new = 0;

		for (i = 0; i < n_faces; i++) {
			int a = faces_old.get(3 * i);
			int b = faces_old.get(3 * i + 1);
			int c = faces_old.get(3 * i + 2);

			int ab_midpoint = search_midpoint(b, a);
			int bc_midpoint = search_midpoint(c, b);
			int ca_midpoint = search_midpoint(a, c);

			faces.set(3 * n_faces_new, a);
			faces.set(3 * n_faces_new + 1, ab_midpoint);
			faces.set(3 * n_faces_new + 2, ca_midpoint);
			n_faces_new++;
			faces.set(3 * n_faces_new, ca_midpoint);
			faces.set(3 * n_faces_new + 1, ab_midpoint);
			faces.set(3 * n_faces_new + 2, bc_midpoint);
			n_faces_new++;
			faces.set(3 * n_faces_new, ca_midpoint);
			faces.set(3 * n_faces_new + 1, bc_midpoint);
			faces.set(3 * n_faces_new + 2, c);
			n_faces_new++;
			faces.set(3 * n_faces_new, ab_midpoint);
			faces.set(3 * n_faces_new + 1, b);
			faces.set(3 * n_faces_new + 2, bc_midpoint);
			n_faces_new++;
		}
		n_faces = n_faces_new;
	}
}
