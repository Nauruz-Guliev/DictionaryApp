package ru.kpfu.itis.gnt.languagelearningapp.api.image.models;

import com.google.gson.annotations.SerializedName;

public class ArchitectureInterior{

	@SerializedName("approved_on")
	private String approvedOn;

	@SerializedName("status")
	private String status;

	public String getApprovedOn(){
		return approvedOn;
	}

	public String getStatus(){
		return status;
	}

	@Override
 	public String toString(){
		return 
			"ArchitectureInterior{" + 
			"approved_on = '" + approvedOn + '\'' + 
			",status = '" + status + '\'' + 
			"}";
		}
}