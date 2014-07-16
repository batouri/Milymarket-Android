package com.hitasoft.apps.milymarket.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class AppConstant {
	// Facebook App_ID
	public static final String App_ID = "470091279731392";

	// Number of columns of Grid View
	public static final int NUM_OF_COLUMNS = 3;

	// Gridview image padding
	public static final int GRID_PADDING = 8; // in dp

	// SD card image directory
	public static final String PHOTO_ALBUM = "androidhive";

	// supported file formats
	public static final List<String> FILE_EXTN = Arrays.asList("jpg", "jpeg",
			"png");
	
	//For push Notification
		public static final String  sender_Id="415738329725";
		public static final String EXTRA_MESSAGE = "message";
		public static String Register_Id="";

		

	public static boolean more_productpage = false;
	public static HashMap<String, ArrayList<String>> Detailurls = new HashMap<String, ArrayList<String>>();
	public static ArrayList<HashMap<String, String>> MorePageItems = new ArrayList<HashMap<String, String>>();
}
