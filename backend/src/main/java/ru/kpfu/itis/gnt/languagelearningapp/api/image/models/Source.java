package ru.kpfu.itis.gnt.languagelearningapp.api.image.models;

import com.google.gson.annotations.SerializedName;

public class Source{

	@SerializedName("meta_description")
	private String metaDescription;

	@SerializedName("ancestry")
	private Ancestry ancestry;

	@SerializedName("cover_photo")
	private CoverPhoto coverPhoto;

	@SerializedName("meta_title")
	private String metaTitle;

	@SerializedName("subtitle")
	private String subtitle;

	@SerializedName("description")
	private String description;

	@SerializedName("title")
	private String title;

	public String getMetaDescription(){
		return metaDescription;
	}

	public Ancestry getAncestry(){
		return ancestry;
	}

	public CoverPhoto getCoverPhoto(){
		return coverPhoto;
	}

	public String getMetaTitle(){
		return metaTitle;
	}

	public String getSubtitle(){
		return subtitle;
	}

	public String getDescription(){
		return description;
	}

	public String getTitle(){
		return title;
	}

	@Override
 	public String toString(){
		return 
			"Source{" + 
			"meta_description = '" + metaDescription + '\'' + 
			",ancestry = '" + ancestry + '\'' + 
			",cover_photo = '" + coverPhoto + '\'' + 
			",meta_title = '" + metaTitle + '\'' + 
			",subtitle = '" + subtitle + '\'' + 
			",description = '" + description + '\'' + 
			",title = '" + title + '\'' + 
			"}";
		}
}