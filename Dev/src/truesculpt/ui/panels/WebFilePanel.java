package truesculpt.ui.panels;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpVersion;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.ContentBody;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.CoreProtocolPNames;
import org.apache.http.util.EntityUtils;

import truesculpt.main.Managers;
import truesculpt.main.R;
import truesculpt.main.TrueSculptApp;
import truesculpt.mesh.Mesh;
import android.app.Activity;
import android.net.ParseException;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import truesculpt.utils.Utils;

public class WebFilePanel extends Activity
{
	private Button mOpenFromWebBtn;
	private Button mPublishToWebBtn;
	private String mStrBaseWebSite="http://truesculpt.appspot.com";
	
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.webfile);			
		
		mOpenFromWebBtn=(Button)findViewById(R.id.open_from_web);
		mOpenFromWebBtn.setOnClickListener(new View.OnClickListener()
		{			
			@Override
			public void onClick(View v)
			{				
				getManagers().getUsageStatisticsManager().TrackEvent("OpenFromWeb", "", 1);
				Utils.ShowURLInBrowser(WebFilePanel.this, mStrBaseWebSite);
				//TODO use webview and webapps javascript interaction to load obj into app
				//http://developer.android.com/guide/webapps/webview.html
				//WebView webView = (WebView) findViewById(R.id.webview);
				//webView.addJavascriptInterface(new JavaScriptInterface(this), "Android");
			}
		});
		
		mPublishToWebBtn=(Button)findViewById(R.id.publish_to_web);
		mPublishToWebBtn.setOnClickListener(new View.OnClickListener()
		{			
			@Override
			public void onClick(View v)
			{
				String name="lap1";//getManagers().getMeshManager().getName();
				getManagers().getMeshManager().setName(name);
				
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

				/*
				//get upload url
				String strUploadURL = "";				
				try
				{
					String fetchURL=mStrBaseWebSite+"/upload";
					
		            HttpGet httpget = new HttpGet(fetchURL);
		            System.out.println("executing request " + httpget.getURI());
		            HttpClient httpclient = new DefaultHttpClient();
		            // Create a response handler
		            ResponseHandler<String> responseHandler = new BasicResponseHandler();
		            strUploadURL = httpclient.execute(httpget, responseHandler);
				} 
				catch (Exception e)
				{					
					e.printStackTrace();
				}
						
				System.out.println( "received upload url: " + strUploadURL );
				
				strUploadURL = strUploadURL.substring(0,strUploadURL.length()-2);
				
				//add title as a parameter of the url
				//strUploadURL=strUploadURL+"?title="+name;
				
				System.out.println( "formatted upload url: " + strUploadURL );
				
				*/
				
				//add title as a parameter of the url
				//strUploadURL=strUploadURL+"?title="+name;
				
				//upload saved file
				String strBaseFileName=getManagers().getUtilsManager().GetBaseFileName();
				String strObjFileName=strBaseFileName+"Mesh.obj";			
				String strPictureFileName=strBaseFileName+"Image.png";
				
				
				try
				{
					uploadPicture(strPictureFileName,strUploadURL,name);
				} 
				catch (Exception e)
				{					
					e.printStackTrace();
				}
			}			
		});
	}
	
	private void uploadPicture( String picturePath, String uploadURL, String title) throws ParseException, IOException, URISyntaxException 
	{
	    HttpClient httpclient = new DefaultHttpClient();
	    //httpclient.getParams( ).setParameter( CoreProtocolPNames.PROTOCOL_VERSION, HttpVersion.HTTP_1_1 );

	    HttpPost httppost = new HttpPost( uploadURL );
	    httppost.addHeader("title", title);
	    
	    //BasicHttpParams basicHttpParams = new BasicHttpParams();
	    //basicHttpParams.setParameter("title", title);
	    //httppost.setParams(basicHttpParams);

	    File file = new File( picturePath );

	    MultipartEntity mpEntity  = new MultipartEntity( HttpMultipartMode.STRICT );
	    ContentBody cbFile        = new FileBody( file);
	    //ContentBody cbTitle     = new StringBody( title );

	    mpEntity.addPart( "file",       cbFile        );
	    //mpEntity.addPart( "title",      cbTitle     );        

	    httppost.setEntity( mpEntity );

	    System.out.println( "executing request " + httppost.getRequestLine( ) );
	    HttpResponse response = httpclient.execute( httppost );
	    
	    /*
	    HttpEntity resEntity = response.getEntity( );

	    // DEBUG
	    System.out.println( response.getStatusLine( ) );
	    if (resEntity != null) 
	    {
	      System.out.println( EntityUtils.toString( resEntity ) );
	    } 

	    if (resEntity != null) 
	    {
	      resEntity.consumeContent( );
	    } 

	    httpclient.getConnectionManager( ).shutdown( );
	    */
	}

	
	public Managers getManagers()
	{
		return ((TrueSculptApp) getApplicationContext()).getManagers();
	}

}
