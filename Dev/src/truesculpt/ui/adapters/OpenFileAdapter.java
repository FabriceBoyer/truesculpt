package truesculpt.ui.adapters;

import java.util.ArrayList;

import truesculpt.main.R;
import truesculpt.managers.FileManager;
import truesculpt.managers.FileManager.FileElem;
import truesculpt.ui.adapters.ImageLoader.ImageLoadListener;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

public class OpenFileAdapter extends BaseAdapter implements ImageLoadListener
{
	private ArrayList<FileManager.FileElem> mFileList = null;
	private Handler mHandler = null;
	private ImageLoader mImageLoader = null;
	private LayoutInflater inflater = null;

	class ViewHolder
	{
		public TextView title = null;
		public ImageView image = null;
		public FileManager.FileElem currElem = null;
	}

	public OpenFileAdapter(Context context, ArrayList<FileManager.FileElem> fileList)
	{
		mImageLoader = new ImageLoader(this);
		// mImageLoader.setPriority(Thread.MIN_PRIORITY);
		mImageLoader.start();
		while (!mImageLoader.IsReady())
		{
			try
			{
				Thread.sleep(100);
			}
			catch (InterruptedException e)
			{
				e.printStackTrace();
			}
		}
		mHandler = new Handler();

		mFileList = fileList;
		inflater = LayoutInflater.from(context);
	}

	@Override
	public int getCount()
	{
		return mFileList.size();
	}

	@Override
	public Object getItem(int position)
	{
		return mFileList.get(position);
	}

	@Override
	public long getItemId(int position)
	{
		return position;
	}

	// create a new ImageView for each item referenced by the Adapter
	@Override
	public View getView(int position, View convertView, ViewGroup parent)
	{
		ViewHolder holder;

		FileElem elem = mFileList.get(position);

		if (convertView == null)
		{
			holder = new ViewHolder();
			convertView = inflater.inflate(R.layout.openfileitem, null);
			convertView.setLayoutParams(new GridView.LayoutParams(100, 200));
			holder.title = (TextView) convertView.findViewById(R.id.fileopentitle);
			holder.image = (ImageView) convertView.findViewById(R.id.fileopenimage);
			holder.image.setScaleType(ImageView.ScaleType.FIT_CENTER);
			convertView.setTag(holder);
		}
		else
		{
			holder = (ViewHolder) convertView.getTag();
		}

		holder.currElem = elem;

		if (elem.bmp == null)
		{
			mImageLoader.queueImageLoad(holder, elem);
		}

		holder.image.setImageBitmap(elem.bmp);
		holder.title.setText(elem.name);
		holder.title.setEnabled(true);
		holder.title.setVisibility(View.VISIBLE);

		return convertView;
	}

	@Override
	public void handleImageLoaded(final ViewHolder holder, final FileElem elem, final Bitmap aBitmap)
	{
		// The enqueue the following in the UI thread
		mHandler.post(new Runnable()
		{
			@Override
			public void run()
			{
				// set the bitmap in the ImageView
				if (holder.currElem == elem)
				{
					holder.image.setImageBitmap(aBitmap);
				}
				elem.bmp = aBitmap;
			}
		});
	}

	@Override
	protected void finalize() throws Throwable
	{

		super.finalize();

		// stop the thread we started
		mImageLoader.stopThread();
	}

}
