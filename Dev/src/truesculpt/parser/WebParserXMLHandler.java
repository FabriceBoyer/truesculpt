package truesculpt.parser;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public class WebParserXMLHandler extends DefaultHandler 
{
	// nom des tags XML
	private final String ITEM = "item";
	private final String TITLE = "title";
	private final String DESCRIPTION = "description";	
	private final String DOWNLOAD_COUNT = "downloadCount";
	private final String CREATION_TIME = "creationTime";
	private final String INSTALLATION_ID = "installationID";
	private final String IMAGE_URL = "imageURL";
	private final String IMAGE_THUMBNAIL_URL = "imageThumbnailURL";
	private final String OBJECT_SIZE_KO = "objectSizeKo";
	private final String IS_FEATURED = "isFeatured";

	// Array list de feeds
	private ArrayList entries;

	// Boolean permettant de savoir si nous sommes à l'intérieur d'un item
	private boolean inItem;

	// Feed courant
	private WebEntry currentEntry;

	// Buffer permettant de contenir les données d'un tag XML
	private StringBuffer buffer;

	@Override
	public void processingInstruction(String target, String data) throws SAXException 
	{
		super.processingInstruction(target, data);
	}

	public WebParserXMLHandler() 
	{
		super();
	}

	// * Cette méthode est appelée par le parser une et une seule
	// * fois au démarrage de l'analyse de votre flux xml.
	// * Elle est appelée avant toutes les autres méthodes de l'interface,
	// * à l'exception unique, évidemment, de la méthode setDocumentLocator.
	// * Cet événement devrait vous permettre d'initialiser tout ce qui doit
	// * l'être avant ledébut du parcours du document.

	@Override
	public void startDocument() throws SAXException 
	{
		super.startDocument();
		entries = new ArrayList();

	}

	/*
	 * Fonction étant déclenchée lorsque le parser trouve un tag XML
	 * C'est cette méthode que nous allons utiliser pour instancier un nouveau feed
 	*/
	@Override
	public void startElement(String uri, String localName, String name,	Attributes attributes) throws SAXException 
	{
		// Nous réinitialisons le buffer a chaque fois qu'il rencontre un item
		buffer = new StringBuffer();		

		// Ci dessous, localName contient le nom du tag rencontré

		// Nous avons rencontré un tag ITEM, il faut donc instancier un nouveau feed
		if (localName.equalsIgnoreCase(ITEM)){
			this.currentEntry = new WebEntry();
			inItem = true;
		}
		
		// Vous pouvez définir des actions à effectuer pour chaque item rencontré
		if (localName.equalsIgnoreCase(TITLE)){
			// Nothing to do
		}
		if (localName.equalsIgnoreCase(DESCRIPTION)){
			// Nothing to do
		}
		if (localName.equalsIgnoreCase(DOWNLOAD_COUNT)){
			// Nothing to do
		}
		if (localName.equalsIgnoreCase(CREATION_TIME)){
			// Nothing to do
		}
		if(localName.equalsIgnoreCase(INSTALLATION_ID)){
			// Nothing to do
		}
		if (localName.equalsIgnoreCase(IMAGE_URL)){
			// Nothing to do
		}
		if(localName.equalsIgnoreCase(IMAGE_THUMBNAIL_URL)){
			// Nothing to do
		}
		if (localName.equalsIgnoreCase(OBJECT_SIZE_KO)){
			// Nothing to do
		}
		if(localName.equalsIgnoreCase(IS_FEATURED)){
			// Nothing to do
		}
	}

	// * Fonction étant déclenchée lorsque le parser à parsé
	// * l'intérieur de la balise XML La méthode characters
	// * a donc fait son ouvrage et tous les caractère inclus
	// * dans la balise en cours sont copiés dans le buffer
	// * On peut donc tranquillement les récupérer pour compléter
	// * notre objet currentFeed

	@Override
	public void endElement(String uri, String localName, String name) throws SAXException
	{		
		if (localName.equalsIgnoreCase(TITLE)){
			if(inItem){
				// Les caractères sont dans l'objet buffer
				this.currentEntry.setTitle(buffer.toString());
				buffer = null;
			}
		}
		if (localName.equalsIgnoreCase(DESCRIPTION)){
			if(inItem){
				this.currentEntry.setDescription(buffer.toString());
				buffer = null;
			}
		}
		if (localName.equalsIgnoreCase(DOWNLOAD_COUNT)){
			if(inItem){
				this.currentEntry.setDownloadCount(Integer.decode(buffer.toString()));
				buffer = null;
			}
		}
		if (localName.equalsIgnoreCase(CREATION_TIME)){
			if(inItem){				
				this.currentEntry.setCreationTime(new Date(buffer.toString()));
				buffer = null;
			}
		}
		if(localName.equalsIgnoreCase(INSTALLATION_ID)){
			if(inItem){
				this.currentEntry.setInstallationID(buffer.toString());
				buffer = null;
			}
		}
		if (localName.equalsIgnoreCase(IMAGE_URL)){
			if(inItem){
				try {
					this.currentEntry.setImageURL(new URL(buffer.toString()));
				} catch (MalformedURLException e) {
					e.printStackTrace();
				}
				buffer = null;
			}
		}
		if(localName.equalsIgnoreCase(IMAGE_THUMBNAIL_URL)){
			if(inItem){
				try {
					this.currentEntry.setImageThumbnailURL(new URL(buffer.toString()));
				} catch (MalformedURLException e) {
					e.printStackTrace();
				}
				buffer = null;
			}
		}
		if(localName.equalsIgnoreCase(OBJECT_SIZE_KO)){
			if(inItem){
				this.currentEntry.setObjectSizeKo(Double.parseDouble(buffer.toString()));
				buffer = null;
			}
		}
		if(localName.equalsIgnoreCase(IS_FEATURED)){
			if(inItem){
				this.currentEntry.setIsFeatured(Boolean.parseBoolean(buffer.toString()));
				buffer = null;
			}
		}
		if (localName.equalsIgnoreCase(ITEM)){
			entries.add(currentEntry);
			inItem = false;
		}
	}

	// * Tout ce qui est dans l'arborescence mais n'est pas partie
	// * intégrante d'un tag, déclenche la levée de cet événement.
	// * En général, cet événement est donc levé tout simplement
	// * par la présence de texte entre la balise d'ouverture et
	// * la balise de fermeture

	public void characters(char[] ch,int start, int length)	throws SAXException{
		String lecture = new String(ch,start,length);
		if(buffer != null) buffer.append(lecture);
	}

	// cette méthode nous permettra de récupérer les données
	public ArrayList getData(){
		return entries;
	}
}