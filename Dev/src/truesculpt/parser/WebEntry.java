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
	private String objectURL;
	private Double objectSizeKo;
	private Boolean isFeatured;
	
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
	public String getObjectURL() {
		return objectURL;
	}
	public void setObjectURL(String objectURL) {
		this.objectURL = objectURL;
	}	
	public Double getObjectSizeKo() {
		return objectSizeKo;
	}
	public void setObjectSizeKo(Double objectSizeKo) {
		this.objectSizeKo = objectSizeKo;
	}
	public Boolean getIsFeatured() {
		return isFeatured;
	}
	public void setIsFeatured(Boolean isFeatured) {
		this.isFeatured = isFeatured;
	}
}