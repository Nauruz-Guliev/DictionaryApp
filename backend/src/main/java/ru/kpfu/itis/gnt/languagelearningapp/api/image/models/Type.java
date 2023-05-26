package ru.kpfu.itis.gnt.languagelearningapp.api.image.models;

import com.google.gson.annotations.SerializedName;

public class Type{

	@SerializedName("pretty_slug")
	private String prettySlug;

	@SerializedName("slug")
	private String slug;

	public String getPrettySlug(){
		return prettySlug;
	}

	public String getSlug(){
		return slug;
	}

	@Override
 	public String toString(){
		return 
			"Type{" + 
			"pretty_slug = '" + prettySlug + '\'' + 
			",slug = '" + slug + '\'' + 
			"}";
		}
}