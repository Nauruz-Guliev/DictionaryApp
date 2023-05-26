package ru.kpfu.itis.gnt.languagelearningapp.api.image.models;

import com.google.gson.annotations.SerializedName;

public class Links{

	@SerializedName("followers")
	private String followers;

	@SerializedName("portfolio")
	private String portfolio;

	@SerializedName("following")
	private String following;

	@SerializedName("self")
	private String self;

	@SerializedName("html")
	private String html;

	@SerializedName("photos")
	private String photos;

	@SerializedName("likes")
	private String likes;

	@SerializedName("download")
	private String download;

	@SerializedName("download_location")
	private String downloadLocation;

	public String getFollowers(){
		return followers;
	}

	public String getPortfolio(){
		return portfolio;
	}

	public String getFollowing(){
		return following;
	}

	public String getSelf(){
		return self;
	}

	public String getHtml(){
		return html;
	}

	public String getPhotos(){
		return photos;
	}

	public String getLikes(){
		return likes;
	}

	public String getDownload(){
		return download;
	}

	public String getDownloadLocation(){
		return downloadLocation;
	}

	@Override
 	public String toString(){
		return 
			"Links{" + 
			"followers = '" + followers + '\'' + 
			",portfolio = '" + portfolio + '\'' + 
			",following = '" + following + '\'' + 
			",self = '" + self + '\'' + 
			",html = '" + html + '\'' + 
			",photos = '" + photos + '\'' + 
			",likes = '" + likes + '\'' + 
			",download = '" + download + '\'' + 
			",download_location = '" + downloadLocation + '\'' + 
			"}";
		}
}