package ru.kpfu.itis.gnt.languagelearningapp.api.dictionary.models;

import java.util.List;
import com.google.gson.annotations.SerializedName;

public class DefItem{

	@SerializedName("pos")
	private String pos;

	@SerializedName("text")
	private String text;

	@SerializedName("tr")
	private List<TrItem> tr;

	@SerializedName("ts")
	private String ts;

	public String getPos(){
		return pos;
	}

	public String getText(){
		return text;
	}

	public List<TrItem> getTr(){
		return tr;
	}

	public String getTs(){
		return ts;
	}

	@Override
 	public String toString(){
		return 
			"DefItem{" + 
			"pos = '" + pos + '\'' + 
			",text = '" + text + '\'' + 
			",tr = '" + tr + '\'' + 
			",ts = '" + ts + '\'' + 
			"}";
		}
}