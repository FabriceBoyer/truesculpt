package truesculpt.managers;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.ShortBuffer;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.microedition.khronos.opengles.GL10;
import javax.microedition.khronos.opengles.GL11;

import truesculpt.main.R;
import truesculpt.main.TrueSculptApp;
import truesculpt.ui.dialogs.HSLColorPickerDialog;
import truesculpt.ui.dialogs.HSLColorPickerDialog.OnAmbilWarnaListener;
import truesculpt.utils.Utils;
import android.app.WallpaperManager;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;
import android.os.Environment;
import android.os.Handler;
import android.widget.Toast;

public class UtilsManager extends BaseManager
{
	private Handler mHandler = null;
	
	public void InitHandler()
	{
		mHandler= new Handler();
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

	public String CreateObjExportFileName()
	{
		// TODO add sculpture name in filename
		Date date = new Date();
		String strBasePath = Environment.getExternalStorageDirectory() + "/Truesculpt/ObjExport/";

		// have the object build the directory structure, if needed.
		File basePath = new File(strBasePath);
		basePath.mkdirs();

		String strFileName = strBasePath + "Export_" + date.toGMTString() + ".obj";
		strFileName = strFileName.replaceAll(":", "_");
		strFileName = strFileName.replaceAll(" ", "_");
		return strFileName;
	}

	private String CreateSnapshotFileName()
	{
		// TODO add sculpture name in filename
		Date date = new Date();
		String strBasePath = Environment.getExternalStorageDirectory() + "/Truesculpt/Screenshots/";

		// have the object build the directory structure, if needed.
		File basePath = new File(strBasePath);
		basePath.mkdirs();

		String strFileName = strBasePath + "Img_" + date.toGMTString() + ".png";
		strFileName = strFileName.replaceAll(":", "_");
		strFileName = strFileName.replaceAll(" ", "_");
		return strFileName;
	}

	@Override
	public void onCreate()
	{
		// TODO Auto-generated method stub

	}

	@Override
	public void onDestroy()
	{
		// TODO Auto-generated method stub

	}

	public void SetImageAsWallpaper(String strFileName)
	{
		Bitmap bitmap = BitmapFactory.decodeFile(strFileName);

		try
		{
			WallpaperManager wp = (WallpaperManager) getbaseContext().getSystemService(Context.WALLPAPER_SERVICE);
			wp.setBitmap(bitmap);
		} catch (IOException e)
		{
			e.printStackTrace();
		}
	}

	// to come back in UI thread from a manager
	public void ShowToastMessage(String msg)
	{
		mShowMsg = msg;
		if (mHandler!=null)
		{
			mHandler.post(mShowMessageTask);
		}
	}

	public void TakeGLScreenshot(GL10 gl)
	{
		getManagers().getUsageStatisticsManager().TrackEvent("Screenshot", "Count", 0);

		int[] mViewPort = new int[4];
		GL11 gl2 = (GL11) gl;
		gl2.glGetIntegerv(GL11.GL_VIEWPORT, mViewPort, 0);

		int width = mViewPort[2];
		int height = mViewPort[3];

		int size = width * height;
		ByteBuffer buf = ByteBuffer.allocateDirect(size * 4);
		buf.order(ByteOrder.nativeOrder());
		gl.glReadPixels(0, 0, width, height, GL10.GL_RGBA, GL10.GL_UNSIGNED_BYTE, buf);
		int data[] = new int[size];
		buf.asIntBuffer().get(data);
		buf = null;
		Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);
		bitmap.setPixels(data, size - width, -width, 0, 0, width, height);
		data = null;

		short sdata[] = new short[size];
		ShortBuffer sbuf = ShortBuffer.wrap(sdata);
		bitmap.copyPixelsToBuffer(sbuf);
		for (int i = 0; i < size; ++i)
		{
			// BGR-565 to RGB-565
			short v = sdata[i];
			sdata[i] = (short) ((v & 0x1f) << 11 | v & 0x7e0 | (v & 0xf800) >> 11);
		}
		sbuf.rewind();
		bitmap.copyPixelsFromBuffer(sbuf);

		String strSnapshotFileName = CreateSnapshotFileName();
		try
		{
			FileOutputStream fos = new FileOutputStream(strSnapshotFileName);
			bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos);
			fos.flush();
			fos.close();
		} catch (Exception e)
		{
			e.printStackTrace();
			return;
		}

		String msg = getbaseContext().getString(R.string.snapshot_has_been_saved_to_) + strSnapshotFileName;
		ShowToastMessage(msg);

		// photo sound
		MediaPlayer mp = MediaPlayer.create(getbaseContext(), R.raw.photo_shutter);
		mp.start();

		// temp for test
		SetImageAsWallpaper(strSnapshotFileName);	
		
		ArrayList<String> filePaths=new ArrayList<String>();
		filePaths.add(strSnapshotFileName);
		Utils.SendEmail(getbaseContext(), "fabrice.boyer@gmail.com", "", "My true sculpture", "It's great", filePaths);
	}
	
	public class MailRunnable implements Runnable
	{
		public ArrayList<String> filePaths=new ArrayList<String>();			
		
		@Override
		public void run()
		{
			Utils.SendEmail(getbaseContext(), "fabrice.boyer@gmail.com", "", "My true sculpture", "It's great", filePaths);				
		}		
		
		public void AddFilePath(String path)
		{
			filePaths.add(path);
		}
	}
	
	public void ShowHSLColorPickerDialog(Context context)
	{
		// initialColor is the initially-selected color to be shown in the rectangle on the left of the arrow.
		// for example, 0xff000000 is black, 0xff0000ff is blue. Please be aware of the initial 0xff which is the alpha.
		HSLColorPickerDialog dialog = new HSLColorPickerDialog(context, ((TrueSculptApp)(context.getApplicationContext())).getManagers().getToolsManager().getColor(), new OnAmbilWarnaListener()
		{
			@Override
			public void onCancel(HSLColorPickerDialog dialog)
			{
				// cancel was selected by the user
			}

			@Override
			public void onOk(HSLColorPickerDialog dialog, int color)
			{
				((TrueSculptApp)(dialog.getContext().getApplicationContext())).getManagers().getToolsManager().setColor(color);
			}
		});

		dialog.show();
	}
	
	public void CheckUpdate(Context context)
	{
		boolean bStartUpdateActivity = false;
		if (getManagers().getOptionsManager().getCheckUpdateAtStartup() == true)
		{
			bStartUpdateActivity = true;
		}

		long timeOfLastUpdate = getManagers().getOptionsManager().getLastSoftwareUpdateCheckDate();
		long today = System.currentTimeMillis();
		long timeSinceLastUpdate = today - timeOfLastUpdate;

		long timeThresold = 31;
		timeThresold *= 24;
		timeThresold *= 3600;
		timeThresold *= 1000;// one month in millis
		if (timeSinceLastUpdate > timeThresold)
		{
			bStartUpdateActivity = true;// mandatory updates
		}

		if (bStartUpdateActivity)
		{
			Utils.StartMyActivity(context, truesculpt.ui.panels.UpdatePanel.class, false);
		}
	}
}
