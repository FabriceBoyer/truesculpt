package truesculpt.ui.adapters;

import truesculpt.ui.adapters.OpenFileAdapter.ViewHolder;
import truesculpt.ui.panels.OpenFilePanel.FileElem;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

public class ImageLoader extends Thread
{
	public interface ImageLoadListener
	{
		void handleImageLoaded(final ViewHolder holder, final FileElem elem, Bitmap aBitmap);
	}

	private static final String TAG = ImageLoader.class.getSimpleName();
	ImageLoadListener mListener = null;
	private Handler handler;

	/**
	 * Image loader takes an object that extends ImageLoadListener
	 * 
	 * @param lListener
	 */
	ImageLoader(ImageLoadListener lListener)
	{
		mListener = lListener;
	}

	@Override
	public void run()
	{
		try
		{
			// preparing a looper on current thread
			// the current thread is being detected implicitly
			Looper.prepare();

			// Looper gets attached to the current thread by default
			handler = new Handler();

			Looper.loop();
			// Thread will start

		}
		catch (Throwable t)
		{
			Log.e(TAG, "ImageLoader halted due to a error: ", t);
		}
	}

	/**
	 * Method stops the looper and thus the thread
	 */
	public synchronized void stopThread()
	{
		// Use the handler to schedule a quit on the looper
		handler.post(new Runnable()
		{
			@Override
			public void run()
			{
				// This runs on the ImageLoader thread
				Log.i(TAG, "DownloadThread loop quitting by request");

				Looper.myLooper().quit();
			}
		});
	}

	/**
	 * Method queues the image at path to load Note that the actual loading
	 * takes place in the UI thread the ImageView and ViewSwitcher are just
	 * references for the UI thread.
	 * 
	 * @param aPath
	 *            - Path where the bitmap is located to load
	 * @param aImageView
	 *            - The ImageView the UI thread will load
	 * @param aViewSwitcher
	 *            - The ViewSwitcher that needs to display the imageview
	 */
	public synchronized void queueImageLoad(final ViewHolder holder, final FileElem elem)
	{
		// Wrap DownloadTask into another Runnable to track the statistics
		handler.postAtFrontOfQueue(new Runnable()
		{
			@Override
			public void run()
			{
				try
				{
					synchronized (holder)
					{
						// make sure this thread is the only one performing
						// activities on
						// this imageview
						BitmapFactory.Options lOptions = new BitmapFactory.Options();
						lOptions.inSampleSize = 4;
						Bitmap lBitmap = BitmapFactory.decodeFile(elem.imagefilename, lOptions);

						// Load the image here
						signalUI(holder, elem, lBitmap);
					}
				}
				catch (Exception e)
				{
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Method is called when the bitmap is loaded. The UI thread adds the bitmap
	 * to the imageview.
	 * 
	 * @param aViewSwitcher
	 *            - The ViewSwitcher that needs to display the imageview
	 * @param aImageView
	 *            - The ImageView the UI thread will load
	 * @param aImage
	 *            - The Bitmap that gets loaded into the ImageView
	 */
	private void signalUI(final ViewHolder holder, final FileElem elem, Bitmap aImage)
	{

		if (mListener != null)
		{
			// we have an object that implements ImageLoadListener
			mListener.handleImageLoaded(holder, elem, aImage);
		}
	}

	public boolean IsReady()
	{
		return handler != null;
	}

}
