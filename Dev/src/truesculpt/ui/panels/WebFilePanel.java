package truesculpt.ui.panels;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;

import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.ContentBody;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.impl.client.DefaultHttpClient;

import truesculpt.main.Managers;
import truesculpt.main.R;
import truesculpt.main.TrueSculptApp;
import android.app.Activity;
import android.content.Context;
import android.net.ParseException;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;

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

	    public void openObjFileInAndroid(String imagefile, String objectFile) 
	    {
	       
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
				String name=getManagers().getMeshManager().getName();
				
				getManagers().getUsageStatisticsManager().TrackEvent("PublishToWeb", name, 1);				

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
				String strBaseFileName=getManagers().getUtilsManager().GetBaseFileName();
				String strObjFileName=strBaseFileName+"Mesh.obj";			
				String strPictureFileName=strBaseFileName+"Image.png";				
				
				try
				{
					uploadPicture(strPictureFileName, strObjFileName, strUploadURL,name,"");
				} 
				catch (Exception e)
				{					
					e.printStackTrace();
				}
				
				mWebView.loadUrl(mStrBaseWebSite);
			}			
		});
	}
	
	private void uploadPicture( String picturePath, String objectPath, String uploadURL, String title, String description) throws ParseException, IOException, URISyntaxException 
	{
	    HttpClient httpclient = new DefaultHttpClient();

	    HttpPost httppost = new HttpPost( uploadURL );
	    httppost.addHeader("title", title);
	    httppost.addHeader("description", description);
	    
	    File imagefile = new File( picturePath );
	    File objectfile = new File( objectPath );

	    MultipartEntity mpEntity  = new MultipartEntity( HttpMultipartMode.STRICT );
	    ContentBody cbImageFile        = new FileBody( imagefile);
	    ContentBody cbObjectFile        = new FileBody( objectfile);

	    mpEntity.addPart( "imagefile", cbImageFile );
	    mpEntity.addPart( "objectfile", cbObjectFile );
        
	    httppost.setEntity( mpEntity );

	    System.out.println( "executing request " + httppost.getRequestLine( ) );
	    httpclient.execute( httppost );  
	}
	
	public Managers getManagers()
	{
		return ((TrueSculptApp) getApplicationContext()).getManagers();
	}

}
