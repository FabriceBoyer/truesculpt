package truesculpt.managers;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.Date;
import java.util.UUID;

import truesculpt.main.TrueSculptApp;
import truesculpt.ui.dialogs.HSLColorPickerDialog;
import truesculpt.ui.dialogs.HSLColorPickerDialog.OnAmbilWarnaListener;
import truesculpt.ui.dialogs.ToolPickerDialog;
import truesculpt.utils.Utils;
import android.app.WallpaperManager;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.os.Handler;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

public class UtilsManager extends BaseManager
{
	public void InitHandler()
	{
		mHandler = new Handler();
	}

	Runnable mShowMessageTask = new Runnable()
	{
		@Override
		public void run()
		{
			Toast toast = Toast.makeText(getbaseContext(), mShowMsg, Toast.LENGTH_LONG);
			toast.show();
		}
	};

	private String mShowMsg = "";

	public UtilsManager(Context baseContext)
	{
		super(baseContext);
		// TODO Auto-generated constructor stub
	}

	public static String GetRootDirectory()
	{
		return Environment.getExternalStorageDirectory() + "/Truesculpt/Sculptures/";
	}

	public String GetObjectFileName()
	{
		return GetBaseFileName() + "Mesh.obj";
	}

	public String GetImageFileName()
	{
		return GetBaseFileName() + "Image.png";
	}

	public static boolean CheckSculptureExist(String name)
	{
		boolean bRes = false;

		File file = new File(GetRootDirectory() + name);
		bRes = file.exists();

		return bRes;
	}

	public String GetBaseFileName()
	{
		String name = getManagers().getMeshManager().getName();
		String strBasePath = GetRootDirectory() + name + "/";

		// have the object build the directory structure, if needed.
		File basePath = new File(strBasePath);
		basePath.mkdirs();

		return strBasePath;
	}

	public String CreateSnapshotFileName()
	{
		String strBasePath = Environment.getExternalStorageDirectory() + "/Truesculpt/Screenshots/";

		// have the object build the directory structure, if needed.
		File basePath = new File(strBasePath);
		basePath.mkdirs();

		Date date = new Date();
		String name = getManagers().getMeshManager().getName();
		String strFileName = strBasePath + "Img_" + name + "_" + date.toGMTString() + ".png";
		strFileName = strFileName.replaceAll(":", "_");
		strFileName = strFileName.replaceAll(" ", "_");
		return strFileName;
	}

	public static String GetDefaultFileName()
	{
		Date date = new Date();
		String strFileName = "Sculpt_" + date.toGMTString();
		strFileName = strFileName.replaceAll(":", "_");
		strFileName = strFileName.replaceAll(" ", "_");
		return strFileName;
	}

	@Override
	public void onCreate()
	{

	}

	@Override
	public void onDestroy()
	{

	}

	public boolean SetImageAsWallpaper(String strFileName)
	{
		boolean bRes = false;

		Bitmap bitmap = BitmapFactory.decodeFile(strFileName);

		if (bitmap != null)
		{
			try
			{
				WallpaperManager wp = (WallpaperManager) getbaseContext().getSystemService(Context.WALLPAPER_SERVICE);
				wp.setBitmap(bitmap);
				bRes = true;

			}
			catch (IOException e)
			{
				e.printStackTrace();
			}
		}
		return bRes;
	}

	// to come back in UI thread from a manager
	public void ShowToastMessage(String msg)
	{
		mShowMsg = msg;
		if (mHandler != null)
		{
			mHandler.post(mShowMessageTask);
		}
	}

	public class MailRunnable implements Runnable
	{
		public ArrayList<String> filePaths = new ArrayList<String>();

		@Override
		public void run()
		{
			Utils.SendEmail(getbaseContext(), "fabrice.boyer@gmail.com", "", "My sculpture", "Check it out it's really great", filePaths);
		}

		public void AddFilePath(String path)
		{
			filePaths.add(path);
		}
	}

	public static void ShowHSLColorPickerDialog(Context context)
	{
		// initialColor is the initially-selected color to be shown in the
		// rectangle on the left of the arrow.
		// for example, 0xff000000 is black, 0xff0000ff is blue. Please be aware
		// of the initial 0xff which is the alpha.
		HSLColorPickerDialog dialog = new HSLColorPickerDialog(context, ((TrueSculptApp) (context.getApplicationContext())).getManagers().getToolsManager().getColor(), new OnAmbilWarnaListener()
		{
			@Override
			public void onCancel(HSLColorPickerDialog dialog)
			{
				// cancel was selected by the user
			}

			@Override
			public void onOk(HSLColorPickerDialog dialog, int color)
			{
				((TrueSculptApp) (dialog.getContext().getApplicationContext())).getManagers().getToolsManager().setColor(color, true, true);
			}
		});

		dialog.show();
	}

	public static void ShowToolPickerDialog(Context context)
	{
		ToolPickerDialog dialog = new ToolPickerDialog(context);
		dialog.show();
	}

	public void updateFullscreenWindowStatus(Window wnd)
	{
		if (getManagers().getOptionsManager().getFullScreenApplication())
		{
			wnd.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		}
	}

	public static class Installation
	{
		private static String sID = null;
		private static final String INSTALLATION = "INSTALLATION";

		public synchronized static String id(Context context)
		{
			if (sID == null)
			{
				File installation = new File(context.getFilesDir(), INSTALLATION);
				try
				{
					if (!installation.exists()) writeInstallationFile(installation);
					sID = readInstallationFile(installation);
				}
				catch (Exception e)
				{
					throw new RuntimeException(e);
				}
			}
			return sID;
		}

		private static String readInstallationFile(File installation) throws IOException
		{
			RandomAccessFile f = new RandomAccessFile(installation, "r");
			byte[] bytes = new byte[(int) f.length()];
			f.readFully(bytes);
			f.close();
			return new String(bytes);
		}

		private static void writeInstallationFile(File installation) throws IOException
		{
			FileOutputStream out = new FileOutputStream(installation);
			String id = UUID.randomUUID().toString();
			out.write(id.getBytes());
			out.close();
		}
	}
}
