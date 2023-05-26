package ru.kpfu.itis.gnt.languagelearningapp.api.image.models;

import com.google.gson.annotations.SerializedName;

public class TagsItem{

	@SerializedName("source")
	private Source source;

	@SerializedName("type")
	private String type;

	@SerializedName("title")
	private String title;

	public Source getSource(){
		return source;
	}

	public String getType(){
		return type;
	}

	public String getTitle(){
		return title;
	}

	@Override
 	public String toString(){
		return 
			"TagsItem{" + 
			"source = '" + source + '\'' + 
			",type = '" + type + '\'' + 
			",title = '" + title + '\'' + 
			"}";
		}
}