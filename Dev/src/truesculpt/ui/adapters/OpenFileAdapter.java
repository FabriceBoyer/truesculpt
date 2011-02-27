package truesculpt.ui.adapters;

import java.util.ArrayList;
import truesculpt.main.R;
import truesculpt.ui.panels.OpenFilePanel.FileElem;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

public class OpenFileAdapter extends BaseAdapter 
{
    private ArrayList<FileElem> mFileList=null;
    
    public OpenFileAdapter(Context context, ArrayList<FileElem> fileList) 
    {
    	mFileList = fileList;
    	inflater = LayoutInflater.from(context);
    }

    public int getCount()
    {
        return mFileList.size();
    }

    public Object getItem(int position)
    {
        return null;
    }

    public long getItemId(int position)
    {
        return position;
    }

    LayoutInflater inflater;
    
    // create a new ImageView for each item referenced by the Adapter
    public View getView(int position, View convertView, ViewGroup parent)
    {
		if (convertView == null)
		{	
			convertView = inflater.inflate(R.layout.openfileitem, null);
			convertView.setLayoutParams(new GridView.LayoutParams(200,200));
			convertView.setPadding(8, 8, 8, 8);
			TextView title = (TextView) convertView.findViewById(R.id.fileopentitle);

			ImageView image = (ImageView) convertView.findViewById(R.id.fileopenimage);
			image.setScaleType(ImageView.ScaleType.FIT_CENTER);	

			FileElem elem=mFileList.get(position);
			title.setText(elem.name);
			
			Bitmap bmp = BitmapFactory.decodeFile(elem.imagefilename);
			image.setImageBitmap(bmp);
			
			title.setEnabled(true);
			title.setVisibility(View.VISIBLE);
		} 
        
        return convertView;
    } 
}
