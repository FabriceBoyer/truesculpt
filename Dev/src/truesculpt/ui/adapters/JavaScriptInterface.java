package truesculpt.ui.adapters;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import truesculpt.main.Managers;
import truesculpt.main.R;
import truesculpt.mesh.Mesh;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;

public class JavaScriptInterface
{
    Context mContext;
    Managers mManagers;

    public JavaScriptInterface(Context c, Managers managers)
    {
        mContext = c;
        mManagers=managers;
    }

    public void openObjFileInAndroid(final String name, final String strImagefileURL, final String strObjectFileURL) 
    {
		Log.i("WEB", "openObjFileInAndroid image : " + strImagefileURL + "\n object : " + strObjectFileURL );
		
		String newName=getManagers().getUtilsManager().GetRootDirectory()+name;
		File newFileName=new File(newName);
		if (newFileName.exists())		  
	    {
			AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
			builder.setMessage("File already exist, do you want to overwrite ?").setCancelable(false).setPositiveButton(R.string.yes, new DialogInterface.OnClickListener()
			{
				@Override
				public void onClick(DialogInterface dialog, int id)
				{
					ImportFile(name, strImagefileURL, strObjectFileURL );
				}
			})
			.setNegativeButton(R.string.no, new DialogInterface.OnClickListener()
			{
				@Override
				public void onClick(DialogInterface dialog, int id)
				{
					
				}
			});
			builder.show();	
	    }
	    else
	    {
			//ensure dir
	    	newFileName.mkdirs();
	    	ImportFile(name, strImagefileURL, strObjectFileURL);
	    }
    }
    
    public Managers getManagers()
	{
		return mManagers;
	}
    

	public void ImportFile(String name, String strImagefileURL, String strObjectfileURL)
	{
		String newName="web_"+name;
		Mesh mesh=getManagers().getMeshManager().getMesh();
		if (mesh!=null && getManagers().getMeshManager().IsInitOver())
		{
			getManagers().getMeshManager().setName(newName);
			
			try
			{
				//import image from URL to disk
				String imageFile=getManagers().getUtilsManager().GetBaseFileName()+"Image.png";
				URLtoDisk(strImagefileURL,imageFile);
				
				//import obj from URL to disk
				String objectFile=getManagers().getUtilsManager().GetBaseFileName()+"Mesh.obj";
				URLtoDisk(strObjectfileURL,objectFile);			
			
				mesh.ImportFromOBJ(objectFile);				
			} 
			catch (IOException e)
			{			
				e.printStackTrace();
			}					
		}		
	}
	
	public void URLtoDisk(String url, String disk) throws ClientProtocolException, IOException
	{
		HttpClient httpclient = new DefaultHttpClient();
		HttpGet httpget = new HttpGet(url);
		HttpResponse response = httpclient.execute(httpget);
		HttpEntity entity = response.getEntity();
		if (entity != null)
		{
			InputStream stream = entity.getContent();
			byte buf[] = new byte[1024 * 1024];
			int numBytesRead;
			BufferedOutputStream fos = new BufferedOutputStream(new
			FileOutputStream(disk));
			do
			{
				numBytesRead = stream.read(buf);
				if (numBytesRead > 0)
				{
					fos.write(buf, 0, numBytesRead);
				}
			} 
			while (numBytesRead > 0);
			fos.flush();
			fos.close();
			stream.close();
			buf = null;
			httpclient.getConnectionManager().shutdown();
		}
	}
}