package truesculpt.mesh;

import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.util.StringTokenizer;

import truesculpt.utils.Utils;

public class MeshSerializer
{
	public static void Import(String strFileName, Mesh mesh) throws FileNotFoundException
	{
		int nCount = 0;

		LineNumberReader input = new LineNumberReader(new InputStreamReader(new FileInputStream(strFileName)));
		String line = null;
		try
		{
			int[] face = new int[3];
			int[] val = new int[3];
			;
			float[] coord = new float[3];
			float[] norm = new float[3];

			for (line = input.readLine(); line != null; line = input.readLine())
			{
				if (line.length() > 0)
				{
					if (line.startsWith("v "))
					{
						StringTokenizer tok = new StringTokenizer(line);
						tok.nextToken();
						coord[0] = Float.parseFloat(tok.nextToken());
						coord[1] = Float.parseFloat(tok.nextToken());
						coord[2] = Float.parseFloat(tok.nextToken());
						Vertex vertex = new Vertex(coord, mesh.mVertexList.size());

						mesh.mVertexList.add(vertex);

						if (tok.hasMoreTokens())// not in norm, specific obj hack
						{
							int color = Integer.parseInt(tok.nextToken());
							vertex.Color = color;
						}
					}
					else if (line.startsWith("vt "))
					{
						StringTokenizer tok = new StringTokenizer(line);
						tok.nextToken();
						coord[0] = Float.parseFloat(tok.nextToken());
						coord[1] = Float.parseFloat(tok.nextToken());

						// m.addTextureCoordinate(coord);
					}
					else if (line.startsWith("f "))
					{
						StringTokenizer tok = new StringTokenizer(line);
						tok.nextToken();
						Utils.parseIntTriple(tok.nextToken(), val);
						face[0] = val[0];
						Utils.parseIntTriple(tok.nextToken(), val);
						face[1] = val[0];
						Utils.parseIntTriple(tok.nextToken(), val);
						face[2] = val[0];

						mesh.mFaceList.add(new Face(face[0], face[1], face[2], mesh.mFaceList.size(), 0));
					}
					else if (line.startsWith("vn "))
					{
						nCount++;
						StringTokenizer tok = new StringTokenizer(line);
						tok.nextToken();
						norm[0] = Float.parseFloat(tok.nextToken());
						norm[1] = Float.parseFloat(tok.nextToken());
						norm[2] = Float.parseFloat(tok.nextToken());

						// m.addNormal(norm);
					}
				}
			}
		}
		catch (Exception ex)
		{
			System.err.println("Error parsing file:");
			System.err.println(input.getLineNumber() + " : " + line);
		}
	}

	public static void Export(String strFileName, Mesh mesh)
	{
		try
		{
			BufferedWriter file = new BufferedWriter(new FileWriter(strFileName));

			file.write("#Generated by TrueSculpt version " + mesh.getManagers().getUpdateManager().getCurrentVersion().toString() + "\n");
			file.write("#http://code.google.com/p/truesculpt/\n");

			file.write("\n");
			file.write("# List of Vertices, with (x,y,z[,w]) coordinates, w is optional\n");
			for (Vertex vertex : mesh.mVertexList)
			{
				String str = "v " + String.valueOf(vertex.Coord[0]) + " " + String.valueOf(vertex.Coord[1]) + " " + String.valueOf(vertex.Coord[2]) + " " + String.valueOf(vertex.Color) + "\n";
				file.write(str);
			}

			file.write("\n");
			file.write("# Texture coordinates, in (u,v[,w]) coordinates, w is optional\n");
			file.write("\n");

			file.write("# Normals in (x,y,z) form; normals might not be unit\n");
			for (Vertex vertex : mesh.mVertexList)
			{
				String str = "vn " + String.valueOf(vertex.Normal[0]) + " " + String.valueOf(vertex.Normal[1]) + " " + String.valueOf(vertex.Normal[2]) + "\n";
				file.write(str);
			}

			file.write("\n");
			file.write("# Face Definitions\n");
			for (Face face : mesh.mFaceList)
			{
				int n0 = face.E0.V0;
				int n1 = face.E1.V0;
				int n2 = face.E2.V0;

				// A valid vertex index starts from 1 and match first vertex
				// element of vertex list previously defined. Each face can
				// contain more than three elements.
				String str = "f " + String.valueOf(n0 + 1) + "//" + String.valueOf(n0 + 1) + " " + String.valueOf(n1 + 1) + "//" + String.valueOf(n1 + 1) + " " + String.valueOf(n2 + 1) + "//" + String.valueOf(n2 + 1) + "\n";

				file.write(str);
			}

			file.write("\n");
			file.close();

		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}
}
