package truesculpt.ui.adapters;

import java.io.File;
import java.util.ArrayList;

import truesculpt.main.Managers;
import truesculpt.main.R;
import truesculpt.main.TrueSculptApp;
import truesculpt.ui.views.CoverFlow;
import android.content.Context;
import android.os.Environment;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;

import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiscCache;
import com.nostra13.universalimageloader.core.DecodingType;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;


public class StreamingCoverFlowAdapter extends BaseAdapter
{	
	private final Context mContext;
	private ArrayList<String> mEntries;
	private DisplayImageOptions options;
	private ImageLoader imageLoader;
	
	public StreamingCoverFlowAdapter(Context c )
	{
		mContext = c;
		mEntries = null;
		
		File cacheDir = new File(Environment.getExternalStorageDirectory(), "Truesculpt/Cache");
		cacheDir.mkdir();
		
		options=new DisplayImageOptions.Builder()
		.showImageForEmptyUrl(R.drawable.busy)
		.showStubImage(R.drawable.earth)
		.decodingType(DecodingType.MEMORY_SAVING)
		.cacheInMemory()
		.cacheOnDisc()
		.build();	
		
		imageLoader = ImageLoader.getInstance();
		ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(mContext)
		.maxImageWidthForMemoryCache(800)
        .maxImageHeightForMemoryCache(800)
        .httpConnectTimeout(5000)
        .httpReadTimeout(30000)
        .threadPoolSize(5)
        .threadPriority(Thread.NORM_PRIORITY - 2)
        .denyCacheImageMultipleSizesInMemory()
        .memoryCacheSize(1500000) // 1.5 Mb
		.discCacheSize(50000000) // 50 Mb
        .discCache(new UnlimitedDiscCache(cacheDir)) 
        .defaultDisplayImageOptions(options)
        .build();
		imageLoader.init(config);	
	}

	public Managers getManagers()
	{
		return ((TrueSculptApp) mContext.getApplicationContext()).getManagers();
	}

	@Override
	public int getCount()
	{
		if (mEntries!=null)		
			return mEntries.size();
		else
			return 0;
	}

	@Override
	public Object getItem(int position)
	{
		if (mEntries!=null)	
			return mEntries.get(position);
		else
			return null;
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
		String imageUrl=mEntries.get(position);
		imageLoader.displayImage(imageUrl, imageView, options);
		return imageView;
	}

	public ArrayList<String> getEntries() {
		return mEntries;
	}

	public void setEntries(ArrayList<String> entries) {
		this.mEntries = entries;
	}	
}