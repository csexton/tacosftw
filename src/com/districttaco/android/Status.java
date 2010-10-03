package com.districttaco.android;

import java.util.Date;

public class Status {
	private int postId;
	private double lat;
	private double lng;
	private String locationName;
	private String locationDescription;
	private String statusText;
	private Date lastUpdate;
	private String infoTitle;
	private String infoHeader;
	private String infoBody;
	
	public void setLocationName(String val) {
		locationName = val;
	}
	
	public void setLocationDescription(String val) {
		locationDescription = val;
	}
	
	public void setLatitude(double val) {
		lat = val;
	}
	
	public void setLongitude(double val) {
		lng = val;
	}
	
	public void setStatusText(String val) {
		statusText = val;
	}
	
	public void setLastUpdate(Date val) {
		lastUpdate = val;
	}
	
	public void setInfoTitle(String val) {
		infoTitle = val;
	}
	
	public void setInfoHeader(String val) {
		infoHeader = val;
	}
	
	public void setInfoBody(String val) {
		infoBody = val;
	}
	
	public void setPostId(int val) {
		postId = val;
	}
	
	public String getLocationName() {
		return locationName;
	}
	
	public String getLocationDescription() {
		return locationDescription;
	}
	
	public double getLatitude() {
		return lat;
	}
	
	public double getLongitude() {
		return lng;
	}
	
	public String getStatusText() {
		return statusText;
	}
	
	public Date getLastUpdate() {
		return lastUpdate;
	}
	
	public String getInfoTitle() {
		return infoTitle;
	}
	
	public String getInfoHeader() {
		return infoHeader;
	}
	
	public String getInfoBody() {
		return infoBody;
	}
	
	public int getPostId() {
		return postId;
	}
}
