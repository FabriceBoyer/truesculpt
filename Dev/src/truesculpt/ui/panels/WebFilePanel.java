package truesculpt.ui.panels;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.ContentBody;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.impl.client.DefaultHttpClient;

import truesculpt.main.Managers;
import truesculpt.main.R;
import truesculpt.main.TrueSculptApp;
import truesculpt.mesh.Mesh;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.ParseException;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;

//TODO thread all waiting phases
public class WebFilePanel extends Activity
{
	private String mStrBaseWebSite="http://truesculpt.appspot.com";
	
	private Button mPublishToWebBtn;	
	private WebView mWebView;
	
	public class JavaScriptInterface
	{
	    Context mContext;

	    JavaScriptInterface(Context c)
	    {
	        mContext = c;
	    }

	    public void openObjFileInAndroid(final String name, final String strImagefileURL, final String strObjectFileURL) 
	    {
			Log.i("WEB", "openObjFileInAndroid image : " + strImagefileURL + "\n object : " + strObjectFileURL );
			
			String newName=getManagers().getUtilsManager().GetRootDirectory()+name;
			File newFileName=new File(newName);
			if (newFileName.exists())		  
		    {
				AlertDialog.Builder builder = new AlertDialog.Builder(WebFilePanel.this);
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
	
	private class MyWebViewClient extends WebViewClient
	{
		@Override
		public boolean shouldOverrideUrlLoading(WebView view, String url)
		{
			view.loadUrl(url);
			return true;
		}
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) 
	{
	    // Check if the key event was the BACK key and if there's history
	    if ((keyCode == KeyEvent.KEYCODE_BACK) && mWebView.canGoBack())
	    {
	        mWebView.goBack();
	        return true;
	    }
	    // If it wasn't the BACK key or there's no web page history, bubble up to the default
	    // system behavior (probably exit the activity)
	    return super.onKeyDown(keyCode, event);
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.webfile);	
		
		getManagers().getUsageStatisticsManager().TrackEvent("OpenFromWeb", "", 1);
		
		mWebView = (WebView) findViewById(R.id.webview);	
		mWebView.setWebViewClient(new MyWebViewClient());
		WebSettings webSettings = mWebView.getSettings();
		webSettings.setJavaScriptEnabled(true);
		mWebView.addJavascriptInterface(new JavaScriptInterface(this), "Android");
		mWebView.loadUrl(mStrBaseWebSite);
		
		mPublishToWebBtn=(Button)findViewById(R.id.publish_to_web);
		mPublishToWebBtn.setOnClickListener(new View.OnClickListener()
		{			
			@Override
			public void onClick(View v)
			{		
				final String name=getManagers().getMeshManager().getName();
				String strBaseFileName=getManagers().getUtilsManager().GetBaseFileName();
				String strObjFileName=strBaseFileName+"Mesh.obj";			
				String strPictureFileName=strBaseFileName+"Image.png";	
			    final File imagefile = new File( strPictureFileName );
			    final File objectfile = new File( strObjFileName );
			    
				getManagers().getUsageStatisticsManager().TrackEvent("PublishToWeb", name, 1);	
				
			    if (imagefile.exists() && objectfile.exists())
			    {
			    	long size=0;
					try
					{
						size = new FileInputStream(imagefile).getChannel().size();
						size += new FileInputStream(objectfile).getChannel().size();
						size /=1000;
					} 
					catch (Exception e)
					{						
						e.printStackTrace();
					}
					
			    	AlertDialog.Builder builder = new AlertDialog.Builder(WebFilePanel.this);
					builder.setMessage(" You will upload " + size + " ko of data\nWhen clicking the ok button you accept to publish your sculpture under the terms of the http://creativecommons.org/licenses/by-nc-sa/3.0/ creative commons share alike, non commercial license. Do you want to proceed ?").setCancelable(false).setPositiveButton(R.string.yes, new DialogInterface.OnClickListener()
					{
						@Override
						public void onClick(DialogInterface dialog, int id)
						{
							PublishPicture(imagefile,objectfile,name);
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
			    	AlertDialog.Builder builder = new AlertDialog.Builder(WebFilePanel.this);
					builder.setMessage("File has not been saved, you need to save it before publishing. Do you want to proceed to save window ?").setCancelable(false).setPositiveButton(R.string.yes, new DialogInterface.OnClickListener()
					{
						@Override
						public void onClick(DialogInterface dialog, int id)
						{
							((FileSelectorPanel)getParent()).getTabHost().setCurrentTab(2);
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
			}			
		});
	}
	
	public void PublishPicture(File imagefile, File objectfile, String name)
	{
		String strUploadURL = "";
		try
		{					
			String myHeader="uploadUrl";
			HttpClient httpClient = new DefaultHttpClient();
			HttpPost httpPost = new HttpPost(mStrBaseWebSite+"/upload");
			HttpResponse httpResponse = httpClient.execute(httpPost);
			Header[] headers = httpResponse.getHeaders(myHeader);
			for (int i = 0; i < headers.length; i++) 
			{
				Header header = headers[i];
				if(header.getName().equals(myHeader)) strUploadURL = header.getValue();
			}
		} 
		catch (Exception e)
		{					
			e.printStackTrace();
		}		
		
		//upload saved file				
		try
		{
			uploadPicture(imagefile, objectfile, strUploadURL,name,"");
		} 
		catch (Exception e)
		{					
			e.printStackTrace();
		}
		
		mWebView.loadUrl(mStrBaseWebSite);
	}
	
	private void uploadPicture( File imagefile, File objectfile, String uploadURL, String title, String description) throws ParseException, IOException, URISyntaxException 
	{
	    HttpClient httpclient = new DefaultHttpClient();

	    HttpPost httppost = new HttpPost( uploadURL );
	    httppost.addHeader("title", title);
	    httppost.addHeader("description", description);

	    MultipartEntity mpEntity = new MultipartEntity( HttpMultipartMode.STRICT );
	    ContentBody cbImageFile = new FileBody( imagefile);
	    ContentBody cbObjectFile = new FileBody( objectfile);

	    mpEntity.addPart( "imagefile", cbImageFile );
	    mpEntity.addPart( "objectfile", cbObjectFile );
        
	    httppost.setEntity( mpEntity );

	    System.out.println( "executing request " + httppost.getRequestLine() );
	    httpclient.execute( httppost );  
	}
	
	public Managers getManagers()
	{
		return ((TrueSculptApp) getApplicationContext()).getManagers();
	}
}
