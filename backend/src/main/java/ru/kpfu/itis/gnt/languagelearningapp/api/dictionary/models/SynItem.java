package ru.kpfu.itis.gnt.languagelearningapp.api.dictionary.models;

import com.google.gson.annotations.SerializedName;

public class SynItem{

	@SerializedName("gen")
	private String gen;

	@SerializedName("pos")
	private String pos;

	@SerializedName("text")
	private String text;

	@SerializedName("fr")
	private int fr;

	public String getGen(){
		return gen;
	}

	public String getPos(){
		return pos;
	}

	public String getText(){
		return text;
	}

	public int getFr(){
		return fr;
	}

	@Override
 	public String toString(){
		return 
			"SynItem{" + 
			"gen = '" + gen + '\'' + 
			",pos = '" + pos + '\'' + 
			",text = '" + text + '\'' + 
			",fr = '" + fr + '\'' + 
			"}";
		}
}