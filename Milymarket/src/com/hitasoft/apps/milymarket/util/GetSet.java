package com.hitasoft.apps.milymarket.util;

public class GetSet {
	private static boolean isLogged = false;
	private static String userId = null;
	private static String userName = null;
	private static String fullName = null;
	private static String Email = null;
	private static String Password = null;
	private static String profileUserId = null;
	private static String cart = null;
	private static String imageUrl = null;

	public static boolean isLogged() {
		return isLogged;
	}

	public static void setLogged(boolean isLogged) {
		GetSet.isLogged = isLogged;
	}

	public static String getUserId() {
		return userId;
	}

	public static void setUserId(String userId) {
		GetSet.userId = userId;
	}

	public static String getUserName() {
		return userName;
	}

	public static void setUserName(String userName) {
		GetSet.userName = userName;
	}

	public static String getEmail() {
		return Email;
	}

	public static void setEmail(String email) {
		Email = email;
	}

	public static String getPassword() {
		return Password;
	}

	public static void setPassword(String password) {
		Password = password;
	}

	public static String getProfileUserId() {

		return profileUserId;
	}

	public static void setProfileUserId(String profileUserId) {
		GetSet.profileUserId = profileUserId;
	}

	public static String getCart() {
		return cart;
	}

	public static void setCart(String cart) {
		GetSet.cart = cart;
	}

	public static String getImageUrl() {
		return imageUrl;
	}

	public static void setImageUrl(String imageUrl) {
		GetSet.imageUrl = imageUrl;
	}

	public static String getFullName() {
		return fullName;
	}

	public static void setFullName(String fullName) {
		GetSet.fullName = fullName;
	}

	public static void reset() {
		GetSet.setLogged(false);
		GetSet.setEmail(null);
		GetSet.setPassword(null);
		GetSet.setProfileUserId(null);
		GetSet.setUserId(null);
		GetSet.setUserName(null);
		GetSet.setCart(null);
		GetSet.setImageUrl(null);
		GetSet.setFullName(null);
	}

}
