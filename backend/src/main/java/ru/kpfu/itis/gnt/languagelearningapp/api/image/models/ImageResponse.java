package ru.kpfu.itis.gnt.languagelearningapp.api.image.models;

import java.util.List;
import com.google.gson.annotations.SerializedName;

public class ImageResponse{

	@SerializedName("total")
	private int total;

	@SerializedName("total_pages")
	private int totalPages;

	@SerializedName("results")
	private List<ResultsItem> results;

	public int getTotal(){
		return total;
	}

	public int getTotalPages(){
		return totalPages;
	}

	public List<ResultsItem> getResults(){
		return results;
	}

	@Override
 	public String toString(){
		return 
			"ImageResponse{" + 
			"total = '" + total + '\'' + 
			",total_pages = '" + totalPages + '\'' + 
			",results = '" + results + '\'' + 
			"}";
		}
}