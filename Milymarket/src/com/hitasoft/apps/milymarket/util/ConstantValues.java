package com.hitasoft.apps.milymarket.util;

import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

/*************************************************************
 * 
 * 
 * @author 'Hitasoft Technologies'
 * 
 *         Description: This Class is for the values using in the app.
 * 
 *         Revision History: Version 1.0 - Initial Version
 * 
 * 
 ************************************************************/

public class ConstantValues {
	public static SharedPreferences pref;
	public static Editor editor;
	// public static final String url = "http://fancyclone.net/dev/";
	// public static final String url = "http://199.230.52.9/fantacy/";
	// public static final String url = "http://toutemarue.fr/";
	//public static final String url = "http://199.230.52.7/dev/";
    public static final String url = "http://milymarket.com/";
	public static final String avatarUrl = "http://fancyclone.net/deTAG_ITEM_URL_MAIN_350mo/media/";
	public static final String signIn = url + "api/login";
	public static final String signUp = url + "api/signup";
	public static final String home = url + "api/home";
	public static final String shopComments = url + "api/shop_comments";
	public static final String userProfile = url + "api/userdetails?userId=";
	public static final String userProfileFantacied = url
			+ "api/item_favorited?userId=";
	public static final String shopCategory = url + "api/shop";
	public static final String commentSendUrl = url + "api/comments";
	public static final String socialLoginUrl = url + "api/loginwithsocial";
	public static final String follwersUrl = url + "api/followers?userId=";
	public static final String follwingUrl = url + "api/following?userId=";
	public static final String fantacyUrl = url + "api/item_like?userId=";
	public static final String searchUrl = url + "api/searchitem?keyword=";
	public static final String shopFilter = url + "api/shopfilter?";
	public static final String MyOrders = url + "api/myorders?userId=";
	public static final String urcollection = url + "api/collections?userId=";
	public static final String cart = url + "api/cart?userId=";
	public static final String emailsettingUrl = url + "api/setsettings";
	public static final String changepasswordUrl = url + "api/changepassword";
	public static final String getsettings = url + "api/getsettings?userId=";
	public static final String getcart = url + "api/getcartdetails?userId=";
	public static final String Remove = url + "api/removecartitem?userId=";
	public static final String getlist = url + "api/getlist?userId=";
	public static final String addtoCart = url + "api/addtocart?userId=";
	public static final String changeItemCount = url
			+ "api/changecartquantity?userId=";
	public static final String addtoList = url + "api/additemtolist?userId=";
	public static final String ProfilePageList = url
			+ "api/getprofilelist?userId=";
	public static final String UpdateList = url + "api/additemtolisthome";
	public static final String ProfilePageListDetail = url
			+ "api/getprofilelistdetails?userId=";
	public static final String findfriendsUrl = url + "api/findfriends?userId=";
	public static final String findFriendsSearch = url
			+ "api/findfriends?username=";
	public static final String sellerComments = url
			+ "api/shop_comments?";
	public static final String find_place = url + "api/findplaces?place=";
	public static final String userimage_upload = url + "api/userimagechange";
	public static final String Push_Notification="api/adddeviceid/";
	public static final String Push_Notification_stop="api/pushsignout";
	

	/* Login */
	public static final int normalLogin = 0;
	public static final int twitterLogin = 1;
	public static final int facebookLogin = 2;
	public static final int googlePlusLogin = 3;

	public static final String status = "status";
	public static final String TAG_USERID = "userid";
	public static final String TAG_FULLNAME = "firstName";

	public static final String TAG_PROFNAME = "userName";
	public static final String msg = "message";
	public static final String payment = url + "api/getpaymentdetails?userId=";

	/*
	 * Facebook
	 */
	private static String APP_ID = "166699250182095";

	/*
	 * Home page
	 */
	public static final String TAG_STATUS = "status";
	public static final String TAG_RESULT = "result";
	public static final String TAG_ITEMS = "items";
	public static final String TAG_ID = "id";
	// public static final String TAG_USER_ID = "userid";
	public static final String TAG_TITLE = "item_title";
	public static final String TAG_DESC = "item_description";
	public static final String TAG_PRICE = "price";
	public static final String TAG_SELLERID = "userId";
	public static final String TAG_QUANTITY = "quantity";
	public static final String TAG_FAVORITES = "favorites";
	public static final String TAG_LIKED = "liked";
	public static final String TAG_SELLER = "sellername";
	public static final String TAG_SHOP = "shop_address";
	public static final String TAG_PHOTOS = "photos";
	public static final String TAG_ITEM_URL_MAIN_70 = "item_url_main_70";
	public static final String TAG_USER_URL_MAIN_70 = "user_url_main_70";
	public static final String TAG_ITEM_URL_MAIN_350 = "item_url_main_350";
	public static final String TAG_USER_URL_MAIN_350 = "user_url_main_70";
	public static final String TAG_ITEM_URL_MAIN_ORIGINAL = "item_url_main_original";
	public static final String TAG_ITEM_URL_SUB_350 = "item_url_sub_350";
	public static final String TAG_HEIGHT = "height";
	public static final String TAG_WIDTH = "width";
	public static final String TAG_COMMENTS = "comments";
	public static final String TAG_COMMENT_ID = "comment_id";
	public static final String TAG_COMMENT = "comment";
	public static final String TAG_USER_ID = "user_id";
	public static final String TAG_USER_IMG = "user_img";
	public static final String TAG_USERNAME = "username";
	public static final String TAG_FASHIONCOUNT = "fashioncount";
	public static final String TAG_FASHIONUSER = "fashionuser";
	public static final String TAG_FID = "fId";
	public static final String TAG_FIMG = "user_img";
	public static final String uploadInfoPhoto = url + "userfashion.php";
	public static final String fashion_upload = url
			+ "api/addfashionuser?userId=";
	public static final String add_photo = url + "api/addshopphotos?userId=";
	public static final String TAG_LIKED_USERS="likedusers";
	// public static final String TAG_FULLNAME = "username";

	/*
	 * User Profile Page
	 */
	public static final String TAG_PROFILE_USERID = "userId";
	public static final String TAG_PROFILE_USERNAME = "userName";
	public static final String TAG_PROFILE_FULLNAME = "fullName";
	public static final String TAG_PROFILE_ABOUT = "about";
	public static final String TAG_PROFILE_LAT = "shop_latitude";
	public static final String TAG_PROFILE_LON = "shop_longitude";
	public static final String TAG_PROFILE_IMAGENAME = "imageName";
	public static final String TAG_PROFILE_FOLLOWING = "following";
	public static final String TAG_PROFILE_FAVOURATED = "itemfavorited";
	public static final String TAG_PROFILE_ADDED = "itemAddedCount";
	public static final String TAG_PROFILE_FOLLOWERS = "followers";
	public static final String TAG_PROFILE_SIMILARUSER = "similarUser";
	public static final String TAG_PROFILE_PROFILEIMAGE = "profileImage";
	public static final String TAG_PROFILE_FIRSTNAME = "firstName";
	public static boolean notifyFlag = false;
	public static final String profile_imageloader = url + "api/item_favorited";

	/*
	 * Default values
	 */
	public static final int CartRefresh = 3;
	public static final int LoginRefresh = 4;

	/*
	 * Profile list page
	 */

	public static final String TAG_STATUS_LIST = "status";
	public static final String TAG_RESULT_LIST = "result";
	public static final String TAG_ITEMS_LIST = "list";
	public static final String TAG_LIST_ID = "listId";
	public static final String TAG_LIST_NAME = "listName";
	public static final String TAG_LIST_ITEM = "listItem";
	public static final String TAG_LIST_IMAGEURL = "imageUrl";

	/*
	 * Add product Page
	 */

	public static final String getproduct = url + "api/productbeforeadd";
	public static final String uploadurl = url + "imageuploadapi.php";
	//public static final String sendproduct = url
	//		+ "api/addproduct?userId=2&itemName=TN%20Produt&itemDescription=TN%20of%20tn&itemQuantity=10&itemPrice=1&categoryIdc=2&superCatId=0&subCatId=0&gender=1&relationShip=1&businessday=1d&everyWhereCost=10&countryId=100&shipingCost=100&imageName=1391509420.jpg";
	public static final String sendproduct = url+"api/addproduct";
	public static final String sellerSignup = url+"api/sellersignup";
	// public static final String TAG_CAT_STATUS = "status";
	// public static final String TAG_CAT_RESULT = "result";
	// public static final String TAG_CATEGORY = "Category";
	// public static final String TAG_CAT_PARENT = "parent";
	// public static final String TAG_CAT_ID = "id";
	// public static final String TAG_CAT_NAME = "name";
	// public static final String TAG_SUB_CATEGORY = "subcategory";
	// public static final String TAG_SUB_ID = "id";
	// public static final String TAG_SUB_NAME = "name";
	// public static final String TAG_SUBCAT_SUBCATEGORY = "subcategory";
	// public static final String TAG_SUBCAT_SUB_ID = "id";
	// public static final String TAG_SUBCAT_SUB_NAME = "name";
	//
	// public static final String TAG_RELATIONSHIP = "relationShip";
	// public static final String TAG_RELATION_ID = "id";
	// public static final String TAG_RELATION_NAME = "relationShipName";
	//
	// public static final String TAG_COUNTRY = "Country";
	// public static final String TAG_COUNTRY_ID = "id";
	// public static final String TAG_COUNTRY_CODE = "code";
	// public static final String TAG_COUNTRY_NAME = "CountryName";
	//
	// public static final String TAG_GENDER = "gender";
	// public static final String TAG_GENDER_ID = "id";
	// public static final String TAG_GENDER_NAME = "Name";
	//
	// public static final String TAG_SHIPPING = "shipDeliveryTime";
	// public static final String TAG_SHIPPING_ID = "id";
	// public static final String TAG_SHIPPING_TIME = "Time";

	/*
	 * Nearme Page
	 */

	public static final String getnearme = url + "api/nearme";

	/*
	 * Notification page
	 */

	public static final String getnotification = url + "api/pushnotifications";
	// public static final String getnotification = url +
	// "api/pushnotifications";
	public static final String TAG_STATUS_NOTE = "status";
	public static final String TAG_RESULT_NOTE = "result";
	public static final String TAG_NOTIFICATION = "notifications";
	public static final String TAG_TYPE = "type";
	public static final String TAG_USER_NAME = "username";
	public static final String TAG_TITLE_NOTE = "title";
	public static final String TAG_ITEM_ID = "itemId";
	public static final String TAG_USERIMAGE = "userimage";
	// public static final String TAG_PRODUCTIMAGE = "item_url_sub_70";
	// public static final String TAG_MAIN_IMAGE = "item_url_main_70";
	public static final String TAG_SUB_IMAGE = "item_url_main_70";
	public static final String TAG_COMMENT_NOTE = "comments";

	/*
	 * Messages page
	 */
	public static final String GetMessages = url + "api/pushnotifications";
	public static final String TAG_STATUS_MESSAGE = "status";
	public static final String TAG_RESULT_MESSAGE = "result";
	public static final String TAG_NOTIFICATION_MESSAGE = "notifications";
	public static final String TAG_TYPE_MESSAGE = "type";
	public static final String TAG_USER_NAME_MESSAGE = "username";
	public static final String TAG_USER_ID_MESSAGE = "userId";
	public static final String TAG_DAYTIME_MESSAGE = "dayAndTime";
	public static final String TAG_USERIMAGE_MESSAGE = "userimage";
	public static final String TAG_SUB_IMAGE_MESSAGE = "item_url_main_70";
	public static final String TAG_SELLER_MESSAGE = "seller_message";
	public static final String TAG_ITEM_ID_MESSAGE = "itemId";
	public static final String TAG_COMMENT_MESSAGE = "comments";
	public static final String TAG_TITLE_MESSAGE = "title";

	/*
	 * Location Page
	 */
	public static final String NearmeProducturl = url + "api/api/nearme"; // ?lat=48.890903&long=2.349680&distance=100&limit=20&userId=3";
	public static final String TAG_STATUS_NEARPRODUCT = "status";
	public static final String TAG_RESULT_NEARPRODUCT = "result";
	public static final String TAG_TYPE_NEARPRODUCT = "type";
	public static final String TAG_ITEMS_NEARPRODUCT = "items";
	public static final String TAG_ID_NEARPRODUCT = "id";
	public static final String TAG_ITEM_TITLE_NEARPRODUCT = "item_title";
	public static final String TAG_ITEM_DESC_NEARPRODUCT = "item_description";
	public static final String TAG_PRICE_NEARPRODUCT = "price";
	public static final String TAG_QUANTITY_NEARPRODUCT = "quantity";
	public static final String TAG_USERID_NEARPRODUCT = "userId";
	public static final String TAG_SELLERNAME_NEARPRODUCT = "sellername";
	public static final String TAG_SHOPADDRESS_NEARPRODUCT = "shop_address";
	public static final String TAG_FAVOURITES_NEARPRODUCT = "favorites";
	public static final String TAG_COMMENT_COUNT_NEARPRODUCT = "commentcount";
	public static final String TAG_FASHION_COUNT_NEARPRODUCT = "fashioncount";
	public static final String TAG_APPROVE_NEARPRODUCT = "approve";
	public static final String TAG_LIKED_NEARPRODUCT = "liked";

	public static final String NearmeShop = url + "api/api/nearmeshop"; // ?lat=48.890903&long=2.349680&distance=100&limit=20&userId=3";
	public static final String TAG_STATUS_SHOP = "status";
	public static final String TAG_RESULT_NEARSHOP = "result";
	public static final String TAG_USERID_NEARSHOP = "UserId";
	public static final String TAG_USERNAME_NEARSHOP = "userName";
	public static final String TAG_FULLNAME_NEARSHOP = "fullName";
	public static final String TAG_ADDRESS_NEARSHOP = "shopAddress";
	public static final String TAG_STATUSREPORT_NEARSHOP = "status";
	public static final String TAG_IMAGENAME_NEARSHOP = "imageName";

	/*
	 * Information page
	 */

	public static final String informationpageurl = url
			+ "api/moreinfos?userId=";
	public static final String TAG_STATUS_INFO = "status";
	public static final String TAG_RESULT_INFO = "result";
	public static final String TAG_SELLER_INFO = "sellers";
	public static final String TAG_LAT_INFO = "shop_latitude";
	public static final String TAG_LON_INFO = "shop_longitude";
	public static final String TAG_OTIME_INFO = "open_time";
	public static final String TAG_NAME_INFO = "Name";
	public static final String TAG_DESC_INFO = "descriptions";
	public static final String TAG_ADDRESS_INFO = "shop_address";
	public static final String TAG_PHONE_INFO = "phone_no";
	public static final String TAG_FB_INFO = "fb_acc";
	public static final String TAG_TW_INFO = "twt_acc";
	public static final String TAG_WEB_INFO = "websites";
	public static final String TAG_SIMG_INFO = "sellerimage";
	public static final String TAG_SHOP_INFO = "shopcomments";
	public static final String TAG_UIMG_INFO = "user_image";
	public static final String TAG_CMT_INFO = "comments";
	public static final String TAG_SPU_INFO = "shopPhotoUser";
	public static final String TAG_USIMG_INFO = "user_img";
	public static final String TAG_SELLER_TYPE = "sellertype";
	public static final String TAG_ENABLE_PAIEMENT = "enablepaiement";

	/*
	 * Follow UnFollow
	 */

	public static final String Follows = url + "api/followuser";
	public static final String Unfollows = url + "api/unfollowuser";
	public static final String FollowUserIDs = url + "api/followingid";
	public static final String Category = url + "api/morecategoryitems";
	public static final String TAG_CHECKED = "checked";
	
	/*
	 * Address
	 * 
	 */
	public static final String updateShippingAddress = url + "api/updateshippingaddress";
	public static final String getShippingAddress = url + "api/getshippingaddress?userId=";
	public static final String TAG_ADDRESS_SHIPPINGID = "shippingid";
	public static final String TAG_ADDRESS_NICKNAME = "nickname";
	public static final String TAG_ADDRESS_ADDRESS1 = "address1";
	public static final String TAG_ADDRESS_ADDRESS2 = "address2";
	public static final String TAG_ADDRESS_TOWN = "town";
	public static final String TAG_ADDRESS_STATE = "state";
	public static final String TAG_ADDRESS_POSTALCODE = "postalcode";
	public static final String TAG_ADDRESS_PHONE = "phone";
	public static final String TAG_ADDRESS_COUNTRYID = "countryid";

}
