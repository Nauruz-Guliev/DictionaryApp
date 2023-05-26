package ru.kpfu.itis.gnt.languagelearningapp.api.image.models;

import com.google.gson.annotations.SerializedName;

public class Social{

	@SerializedName("twitter_username")
	private String twitterUsername;

	@SerializedName("paypal_email")
	private Object paypalEmail;

	@SerializedName("instagram_username")
	private String instagramUsername;

	@SerializedName("portfolio_url")
	private String portfolioUrl;

	public String getTwitterUsername(){
		return twitterUsername;
	}

	public Object getPaypalEmail(){
		return paypalEmail;
	}

	public String getInstagramUsername(){
		return instagramUsername;
	}

	public String getPortfolioUrl(){
		return portfolioUrl;
	}

	@Override
 	public String toString(){
		return 
			"Social{" + 
			"twitter_username = '" + twitterUsername + '\'' + 
			",paypal_email = '" + paypalEmail + '\'' + 
			",instagram_username = '" + instagramUsername + '\'' + 
			",portfolio_url = '" + portfolioUrl + '\'' + 
			"}";
		}
}