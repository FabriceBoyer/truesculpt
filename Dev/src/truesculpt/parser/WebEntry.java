package truesculpt.parser;

import java.net.URL;
import java.util.Date;

public class WebEntry 
{	
	private String title;
	private String description;
	private Integer downloadCount;
	private Date creationTime;
	private String installationID;
	private URL imageURL;
	private URL imageThumbnailURL;
	
	public WebEntry(String title, String description, Integer downloadCount,
			Date creationTime, String installationID, URL imageURL,
			URL imageThumbnailURL) {
		super();
		this.title = title;
		this.description = description;
		this.downloadCount = downloadCount;
		this.creationTime = creationTime;
		this.installationID = installationID;
		this.imageURL = imageURL;
		this.imageThumbnailURL = imageThumbnailURL;
	}
	
	public WebEntry() {

	}
	
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public Integer getDownloadCount() {
		return downloadCount;
	}
	public void setDownloadCount(Integer downloadCount) {
		this.downloadCount = downloadCount;
	}
	public Date getCreationTime() {
		return creationTime;
	}
	public void setCreationTime(Date creationTime) {
		this.creationTime = creationTime;
	}
	public String getInstallationID() {
		return installationID;
	}
	public void setInstallationID(String installationID) {
		this.installationID = installationID;
	}
	public URL getImageURL() {
		return imageURL;
	}
	public void setImageURL(URL imageURL) {
		this.imageURL = imageURL;
	}
	public URL getImageThumbnailURL() {
		return imageThumbnailURL;
	}
	public void setImageThumbnailURL(URL imageThumbnailURL) {
		this.imageThumbnailURL = imageThumbnailURL;
	}
}