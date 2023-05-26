package ru.kpfu.itis.gnt.languagelearningapp.api.dictionary.models;

import com.google.gson.annotations.SerializedName;

public class MeanItem{

	@SerializedName("text")
	private String text;

	public String getText(){
		return text;
	}

	@Override
 	public String toString(){
		return 
			"MeanItem{" + 
			"text = '" + text + '\'' + 
			"}";
		}
}