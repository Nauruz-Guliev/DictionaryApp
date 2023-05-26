package ru.kpfu.itis.gnt.languagelearningapp.api.dictionary.models;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.google.gson.annotations.SerializedName;

public class YandexDictionaryResponse{


	@JsonIgnoreProperties("head")

	@SerializedName("def")
	private List<DefItem> def;



	public List<DefItem> getDef(){
		return def;
	}

	@Override
 	public String toString(){
		return 
			"YandexDictionaryResponse{" + 
			",def = '" + def + '\'' +
			"}";
		}
}