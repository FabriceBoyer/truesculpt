package truesculpt.managers;

import java.io.File;
import java.util.ArrayList;

import android.content.Context;
import android.graphics.Bitmap;

//open and save a file, autosave?
public class FileManager extends BaseManager
{
	public class FileElem
	{
		public String name;
		public String objfilename;
		public String imagefilename;
		public Bitmap bmp = null;
	};

	public ArrayList<FileElem> getFileList()
	{
		getManagers().getUtilsManager();
		String strRootDir = UtilsManager.GetRootDirectory();

		ArrayList<FileElem> fileList = new ArrayList<FileElem>();
		File rootDir = new File(strRootDir);
		if (rootDir.exists())
		{
			File[] listFiles = rootDir.listFiles();
			for (File file : listFiles)
			{
				boolean bIsDir = file.isDirectory();
				if (bIsDir)
				{
					String strName = file.getName();
					String strObjName = file.getAbsolutePath() + "/" + "Mesh.obj";
					String strImageName = file.getAbsolutePath() + "/" + "Image.png";
					File ObjName = new File(strObjName);
					File ImageName = new File(strImageName);
					if (ObjName.exists() && ImageName.exists())
					{
						FileElem elem = new FileElem();
						elem.objfilename = strObjName;
						elem.name = strName;
						elem.imagefilename = strImageName;
						fileList.add(elem);
					}
				}
			}
		}
		return fileList;
	}

	public FileManager(Context baseContext)
	{
		super(baseContext);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void onCreate()
	{

	}

	@Override
	public void onDestroy()
	{

	}
}
