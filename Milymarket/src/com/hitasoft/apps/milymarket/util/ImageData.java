package com.hitasoft.apps.milymarket.util;

public class ImageData {

	int id;
	String data;
	byte[] image;

	public ImageData() {
		super();
	}

	public ImageData(int id, String data, byte[] image) {
		super();
		this.id = id;
		this.data = data;
		this.image = image;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getData() {
		return data;
	}

	public void setData(String data) {
		this.data = data;
	}

	public byte[] getImage() {
		return image;
	}

	public void setImage(byte[] image) {
		this.image = image;
	}

}
