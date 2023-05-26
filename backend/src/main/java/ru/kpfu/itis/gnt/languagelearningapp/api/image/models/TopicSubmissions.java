package ru.kpfu.itis.gnt.languagelearningapp.api.image.models;

import com.google.gson.annotations.SerializedName;

public class TopicSubmissions{

	@SerializedName("health")
	private Health health;

	@SerializedName("color-of-water")
	private ColorOfWater colorOfWater;

	@SerializedName("textures-patterns")
	private TexturesPatterns texturesPatterns;

	@SerializedName("wallpapers")
	private Wallpapers wallpapers;

	@SerializedName("architecture-interior")
	private ArchitectureInterior architectureInterior;

	public Health getHealth(){
		return health;
	}

	public ColorOfWater getColorOfWater(){
		return colorOfWater;
	}

	public TexturesPatterns getTexturesPatterns(){
		return texturesPatterns;
	}

	public Wallpapers getWallpapers(){
		return wallpapers;
	}

	public ArchitectureInterior getArchitectureInterior(){
		return architectureInterior;
	}

	@Override
 	public String toString(){
		return 
			"TopicSubmissions{" + 
			"health = '" + health + '\'' + 
			",color-of-water = '" + colorOfWater + '\'' + 
			",textures-patterns = '" + texturesPatterns + '\'' + 
			",wallpapers = '" + wallpapers + '\'' + 
			",architecture-interior = '" + architectureInterior + '\'' + 
			"}";
		}
}