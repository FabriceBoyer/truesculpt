package truesculpt.mesh;

import truesculpt.utils.MatrixUtils;

public class MeshMathsUtils
{

	public static float square(float f)
	{
		return (f * f);
	};

	// recycled vectors for time critical function where new are too long
	private static float[] dir = new float[3];
	private static float[] n = new float[3];
	private static float SMALL_NUM = 0.00000001f; // anything that avoids division overflow
	private static float[] u = new float[3];
	private static float[] v = new float[3];
	private static float[] w = new float[3];
	private static float[] w0 = new float[3];
	private static float[] zero = { 0, 0, 0 };
	private static float[] temp = new float[3];
	private static float[] temp2 = new float[3];

	// intersect_RayTriangle(): intersect a ray with a 3D triangle
	// Input: a ray R (R0 and R1), and a triangle T (V0,V1)
	// Output: *I = intersection point (when it exists)
	// Return: -1 = triangle is degenerate (a segment or point)
	// 0 = disjoint (no intersect)
	// 1 = intersect in unique point I1
	// 2 = are in the same plane
	public static int intersect_RayTriangle(float[] R0, float[] R1, float[] V0, float[] V1, float[] V2, float[] Ires)
	{
		float r, a, b; // params to calc ray-plane intersect

		// get triangle edge vectors and plane normal
		MatrixUtils.minus(V1, V0, u);
		MatrixUtils.minus(V2, V0, v);

		MatrixUtils.cross(u, v, n); // cross product
		if (n == zero)
		{
			return -1; // do not deal with this case
		}

		MatrixUtils.minus(R1, R0, dir); // ray direction vector

		boolean bBackCullTriangle = MatrixUtils.dot(dir, n) > 0;// ray dir and normal have same direction
		if (bBackCullTriangle)
		{
			return 0;
		}

		MatrixUtils.minus(R0, V0, w0);
		a = -MatrixUtils.dot(n, w0);
		b = MatrixUtils.dot(n, dir);
		if (Math.abs(b) < SMALL_NUM)
		{ // ray is parallel to triangle plane
			if (a == 0)
			{
				return 2;
			}
			return 0; // ray disjoint from plane
		}

		// get intersect point of ray with triangle plane
		r = a / b;
		if (r < 0.0)
		{
			return 0; // => no intersect
			// for a segment, also test if (r > 1.0) => no intersect
		}

		MatrixUtils.scalarMultiply(dir, r);
		MatrixUtils.plus(R0, dir, Ires);

		// is I inside T?
		float uu, uv, vv, wu, wv, D;
		uu = MatrixUtils.dot(u, u);
		uv = MatrixUtils.dot(u, v);
		vv = MatrixUtils.dot(v, v);
		MatrixUtils.minus(Ires, V0, w);
		wu = MatrixUtils.dot(w, u);
		wv = MatrixUtils.dot(w, v);
		D = uv * uv - uu * vv;

		// get and test parametric coords
		float s, t;
		s = (uv * wv - vv * wu) / D;
		if (s < 0.0 || s > 1.0)
		{
			return 0;
		}
		t = (uv * wu - uu * wv) / D;
		if (t < 0.0 || s + t > 1.0)
		{
			return 0;
		}

		return 1; // I is in T
	}

	// dist_Point_to_Segment(): get the distance of a point to a segment.
	// Input: a Point P and a Segment S (in any dimension)
	// Return: the shortest distance from P to S
	public static float squaredist_Point_to_Segment(float[] P, float[] S0, float[] S1, float[] PRes)
	{
		MatrixUtils.minus(S1, S0, v);
		MatrixUtils.minus(P, S0, w);

		float c1 = MatrixUtils.dot(w, v);
		if (c1 <= 0)
		{
			MatrixUtils.copy(S0, PRes);
			return MatrixUtils.squaredistance(P, S0);
		}

		float c2 = MatrixUtils.dot(v, v);
		if (c2 <= c1)
		{
			MatrixUtils.copy(S1, PRes);
			return MatrixUtils.squaredistance(P, S1);
		}

		float b = c1 / c2;
		MatrixUtils.scalarMultiply(v, b, temp);
		MatrixUtils.plus(S0, temp, PRes);

		return MatrixUtils.squaredistance(P, PRes);
	}

	// x1,y1,z1 P1 coordinates (point of line)
	// x2,y2,z2 P2 coordinates (point of line)
	// x3,y3,z3, r P3 coordinates and radius (sphere)
	public static boolean sphere_line_intersection(float x1, float y1, float z1, float x2, float y2, float z2, float x_sphere, float y_sphere, float z_sphere, float r_sphere)
	{
		float a, b, c, i;

		a = square(x2 - x1) + square(y2 - y1) + square(z2 - z1);
		b = 2 * ((x2 - x1) * (x1 - x_sphere) + (y2 - y1) * (y1 - y_sphere) + (z2 - z1) * (z1 - z_sphere));
		c = square(x_sphere) + square(y_sphere) + square(z_sphere) + square(x1) + square(y1) + square(z1) - 2 * (x_sphere * x1 + y_sphere * y1 + z_sphere * z1) - square(r_sphere);
		i = b * b - 4 * a * c;

		if (i < 0.0)
		{
			// no intersection
			return (false);
		}
		return true;
	}

	// Smits method
	public static boolean ray_box_intersect(OctreeNode box, final float[] rayOrig, final float[] rayDir, float t0, float t1)
	{
		float tmin, tmax, tymin, tymax, tzmin, tzmax;
		if (rayDir[0] >= 0)
		{
			tmin = (box.Min[0] - rayOrig[0]) / rayDir[0];
			tmax = (box.Max[0] - rayOrig[0]) / rayDir[0];
		}
		else
		{
			tmin = (box.Max[0] - rayOrig[0]) / rayDir[0];
			tmax = (box.Min[0] - rayOrig[0]) / rayDir[0];
		}
		if (rayDir[1] >= 0)
		{
			tymin = (box.Min[1] - rayOrig[1]) / rayDir[1];
			tymax = (box.Max[1] - rayOrig[1]) / rayDir[1];
		}
		else
		{
			tymin = (box.Max[1] - rayOrig[1]) / rayDir[1];
			tymax = (box.Min[1] - rayOrig[1]) / rayDir[1];
		}
		if ((tmin > tymax) || (tymin > tmax)) return false;
		if (tymin > tmin) tmin = tymin;
		if (tymax < tmax) tmax = tymax;
		if (rayDir[2] >= 0)
		{
			tzmin = (box.Min[2] - rayOrig[2]) / rayDir[2];
			tzmax = (box.Max[2] - rayOrig[2]) / rayDir[2];
		}
		else
		{
			tzmin = (box.Max[2] - rayOrig[2]) / rayDir[2];
			tzmax = (box.Min[2] - rayOrig[2]) / rayDir[2];
		}
		if ((tmin > tzmax) || (tzmin > tmax)) return false;
		if (tzmin > tmin) tmin = tzmin;
		if (tzmax < tmax) tmax = tzmax;
		return ((tmin < t1) && (tmax > t0));
	}

}
