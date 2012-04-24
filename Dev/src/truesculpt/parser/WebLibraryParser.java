package truesculpt.parser;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import android.util.Log;

public class WebLibraryParser
{	

	public WebLibraryParser()
	{		

	}
	
	public static ArrayList<WebEntry> getWebLibrary()
	{
		ArrayList<WebEntry> entries = null;
		
		SAXParserFactory fabrique = SAXParserFactory.newInstance();
		SAXParser parseur = null;

		try {
			parseur = fabrique.newSAXParser();
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		} catch (SAXException e) {
			e.printStackTrace();
		}

		URL url = null;
		try {
			url = new URL("http://truesculpt-hrd.appspot.com/xml");
		} catch (MalformedURLException e1) {
			e1.printStackTrace();
		}

		DefaultHandler handler = new WebParserXMLHandler();
		try {
			InputStream input = url.openStream();
			if(input==null)
				Log.e("erreur android","null");
			else{
				parseur.parse(input, handler);
				entries = ((WebParserXMLHandler) handler).getData();
			}
		} catch (SAXException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return entries;
	}

}
