package truesculpt.ui.adapters;

import java.io.File;
import java.util.ArrayList;

import truesculpt.main.Managers;
import truesculpt.main.R;
import truesculpt.main.TrueSculptApp;
import truesculpt.managers.FileManager.FileElem;
import truesculpt.parser.WebEntry;
import truesculpt.ui.views.CoverFlow;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.LinearGradient;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Shader.TileMode;
import android.graphics.drawable.BitmapDrawable;
import android.os.Environment;
import android.os.Handler;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Gallery.LayoutParams;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ImageView.ScaleType;

import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiscCache;
import com.nostra13.universalimageloader.cache.memory.impl.UsingFreqLimitedMemoryCache;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;


public class StreamingCoverFlowAdapter extends BaseAdapter
{	
	private final Context mContext;
	private ArrayList<WebEntry> mEntries;
	private DisplayImageOptions options;
	private ImageLoader imageLoader;
	
	public StreamingCoverFlowAdapter(Context c)
	{
		mContext = c;
		mEntries=getManagers().getWebManager().getWebEntries();		
		
		File cacheDir = new File(Environment.getExternalStorageDirectory(), "Truesculpt/Cache");
		cacheDir.mkdir();
		
		imageLoader = ImageLoader.getInstance();
		ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(mContext)
        .maxImageWidthForMemoryCache(800)
        .maxImageHeightForMemoryCache(800)
        .httpConnectTimeout(5000)
        .httpReadTimeout(30000)
        .threadPoolSize(5)
        .threadPriority(Thread.MIN_PRIORITY + 2)
        .denyCacheImageMultipleSizesInMemory()
        .memoryCache(new UsingFreqLimitedMemoryCache(2000000)) 
        .discCache(new UnlimitedDiscCache(cacheDir)) 
        .defaultDisplayImageOptions(DisplayImageOptions.createSimple())
        .build();
		imageLoader.init(config);
		
		options=new DisplayImageOptions.Builder()
		.showImageForEmptyUrl(R.drawable.busy)
		.showStubImage(R.drawable.busy)
		.cacheInMemory()
		.cacheOnDisc()
		.build();		
	}

	public Managers getManagers()
	{
		return ((TrueSculptApp) mContext.getApplicationContext()).getManagers();
	}

	@Override
	public int getCount()
	{
		return mEntries.size();
	}

	@Override
	public Object getItem(int position)
	{
		return mEntries.get(position);
	}

	@Override
	public long getItemId(int position)
	{
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent)
	{
		ImageView imageView = (ImageView) convertView;
		if (imageView == null) 
		{
			imageView = new ImageView(mContext);
			imageView.setLayoutParams(new CoverFlow.LayoutParams(200, 200));
			imageView.setScaleType(ScaleType.MATRIX);
		}
		String imageUrl=mEntries.get(position).getImageThumbnailURL().toString();
		imageLoader.displayImage(imageUrl, imageView, options);
		return imageView;
	}	
}