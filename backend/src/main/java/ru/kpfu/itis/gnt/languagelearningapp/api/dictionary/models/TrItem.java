package ru.kpfu.itis.gnt.languagelearningapp.api.dictionary.models;

import java.util.List;
import com.google.gson.annotations.SerializedName;

public class TrItem{

	@SerializedName("gen")
	private String gen;

	@SerializedName("pos")
	private String pos;

	@SerializedName("mean")
	private List<MeanItem> mean;

	@SerializedName("text")
	private String text;

	@SerializedName("fr")
	private int fr;

	@SerializedName("syn")
	private List<SynItem> syn;

	public String getGen(){
		return gen;
	}

	public String getPos(){
		return pos;
	}

	public List<MeanItem> getMean(){
		return mean;
	}

	public String getText(){
		return text;
	}

	public int getFr(){
		return fr;
	}

	public List<SynItem> getSyn(){
		return syn;
	}

	@Override
 	public String toString(){
		return 
			"TrItem{" + 
			"gen = '" + gen + '\'' + 
			",pos = '" + pos + '\'' + 
			",mean = '" + mean + '\'' + 
			",text = '" + text + '\'' + 
			",fr = '" + fr + '\'' + 
			",syn = '" + syn + '\'' + 
			"}";
		}
}