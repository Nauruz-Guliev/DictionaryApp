package ru.kpfu.itis.gnt.languagelearningapp.api.image.models;

import com.google.gson.annotations.SerializedName;

public class ProfileImage{

	@SerializedName("small")
	private String small;

	@SerializedName("large")
	private String large;

	@SerializedName("medium")
	private String medium;

	public String getSmall(){
		return small;
	}

	public String getLarge(){
		return large;
	}

	public String getMedium(){
		return medium;
	}

	@Override
 	public String toString(){
		return 
			"ProfileImage{" + 
			"small = '" + small + '\'' + 
			",large = '" + large + '\'' + 
			",medium = '" + medium + '\'' + 
			"}";
		}
}