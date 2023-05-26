package ru.kpfu.itis.gnt.languagelearningapp.api.image.models;

import com.google.gson.annotations.SerializedName;

public class Ancestry{

	@SerializedName("type")
	private Type type;

	@SerializedName("category")
	private Category category;

	@SerializedName("subcategory")
	private Subcategory subcategory;

	public Type getType(){
		return type;
	}

	public Category getCategory(){
		return category;
	}

	public Subcategory getSubcategory(){
		return subcategory;
	}

	@Override
 	public String toString(){
		return 
			"Ancestry{" + 
			"type = '" + type + '\'' + 
			",category = '" + category + '\'' + 
			",subcategory = '" + subcategory + '\'' + 
			"}";
		}
}