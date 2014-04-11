package com.raweng.utils;

import android.widget.ListView;

public class AppConstants {

	
	
	
	/*
    0--catalog
	1--skin
	2--logo
	3--background
	4--skin-meta 
	5--style*/
	
	public static boolean isPreviousAssetDownloadSuccessful = true; //specifically for new/edited assets downloading calls
	public static boolean isPreviousSkinFolderDownloadSuccessful = true; //specifically for new/edited assets downloading calls
	
	
	public static boolean isProductGalleryVisible = false; // to solve double gallery activity issue 
	
	
	public static String typeOfCall = "";
	public static boolean isMD5Enabled = false;
	public static boolean isDeleteingPreviousDataDone = true;

	public static String GET = "GET";
	public static String packageName = "com.raweng.tr3sco";


	//	device type
	public static String androidTablet = "androidt";
	public static String androidMobile = "androidm";
	public static String kindleFire = "kindle";

	public static String DeviceType = "" ;

	//density
	public static String DeviceDensity ;
	public static String XHighDensity = "DENSITY_XHIGH";
	public static String HighDensity = "DENSITY_HIGH";
	public static String MediumDensity = "DENSITY_MEDIUM";
	public static String LowDensity = "DENSITY_LOW";


	// CHECKS RESPONSE STATUS
	public static final int OK = 200;
	public static final int CREATED = 201;
	public static final String SUCCESS = "0000";


	public static final String NO_UPDATE = "0001";
	public static final String UPDATE_RECOMMENDED = "0002";
	public static final String UPDATE_MANDATORY = "0003";


	private static boolean INVALID_USER ;


	//	menuViewType
	public static final int categoryMenu = 1;
	public static final int clientMenu = 2;
	public static final int wishlistMenu = 3;
	public static final int productListMenu = 4;
	public static final int searchMenu = 5;

	// webTypes -- 
	public static final String WEB_TYPE_LOCAL = "local";
	public static final String WEB_TYPE_WEB = "web";
	public static final String WEB_TYPE_PDF = "pdf";


	public static String PRODUCT_LIST = "products_list";
	public static String PRODUCT = "product";
	public static String CATEGORY = "category";
	public static String WISH_LIST = "wish_list";


	public static boolean isLoginThroughDemoVersion = false;

	//	public static int currentVersionTimeStamp = 0;

	public static ListView listviewDataFragment ;
	public static ListView listviewMenuFragment ;

	//	handler-message

	public static int finishMessageHandler = 0;
	public static int failedMessageHandler = 1;
	public static int broadcastRelatedMessageHandler = 2;
	public static int compareMasterDataMessageHandler = 3;
	public static int completeUpdationMessageHandler = 4;
	public static int sendCallToDownloadAssetsMessageHandler = 5;
	public static int calltoSendXIDMessageHandler = 6;
	public static int sendCallToDownloadSkinFoldersMessageHandler = 7;
	


	//intent keys
	public static final String skinFileNameKey = "skinFileName";
	public static final String skinFileVersionKey = "skinFileVersion";

	//temp folder
	public static final String catalogXmlsURLKey = "catalogXmlsURL";
	public static final String catalogXmlsKey = "catalogXmls";
	public static final String catalogXmlsDestinationPathKey = "catalogXmlsDestinationPath";

	public static final String  skinNameKey = "skinName";
	public static final String  skinVersionKey = "skinVersion";
	public static final String  catalogNameKey = "catalogName"; 
	public static final String  catalogVersionKey = "catalogVersion";

	public static final String  skinFileURLKey = "skinFileURL";
	public static final String  skinFileDestinationPathKey = "skinFileDestinationPath";
	public static final String  skinLogoFileURLKey = "skinLogoFileURL";
	public static final String  skinBackgroundFileURLKey = "skinBackgroundFileURL";
	public static final String  skinStyleFileURLKey = "skinStyleFileURL";
	public static final String  skinMetaFileURLKey = "skinMetaFileURL"; 
			
	public static  String catalogFilesDirURL = null;


	//policy
	public static final String overwritePolicy = "overwrite";
	public static final String checkfilePolicy = "checkfile";





	//Broadcast
	public static final String DOWNLOADCOMPLETERECEIVER = "com.raweng.tr3sco.urlrequests.LoginRequest_DOWNLOAD_COMPLETED";
	public static final String LOGINSUCCESSRECEIVER = "com.raweng.tr3sco.urlrequests.LoginRequest_LOGIN_SUCCESS";
	public static final String GALLERYRECEIVER = "com.raweng.tr3sco.controllers.RootViewController.Broadcast";
	public static final String SENDMESSAGERECEIVER = "com.raweng.tr3sco.controllers.RootViewController_SEND_MESSAGE";
	public static final String NOTFICATION = "com.raweng.tr3sco.controllers.FromNotification_RootViewController_SEND_MESSAGE";
	public static final String DETELECOMPLETERECEIVER = "com.raweng.tr3sco.controllers.UILoginScreen.Broadcast_DELETE_COMPLETE";
	public static final String NOINTERNETCONNECTION ="com.raweng.tr3sco.urlrequests.DownloadFile_NO_INTERNET";

	public static final String VERSIONCONTROL = "com.raweng.tr3sco.urlrequests.VersionControlRequest";

	// Header tab
	public static String headerTabClientButtonText="Client";
	public static String headerTabWishlistButtonText="Wishlists";
	public static int CURRENT_TAB ; //  chk current tab is client/wishlist --- to remove data frm Db

	//GCM
	public static String XTIFY_APP_KEY = "f1efbb19-a033-438f-bf65-5cadddce98e2";
	public static final String C2DM_SENDER_ID = "711800558189";


	// messages
	public static String lodingMessage= "Loading...";


	///////////////Send Mail message & Body

	public static final String recipient = "info@capptalog.com";
	public static final String subject = "Feedback Capptalog";
	public static final String shareWithFriendSubject = "Download Capptalog !";
	public static final String shareWithFriendBody = "Hey!  I'm using Capptalog, and I'm loving it.  I think you'll love it too; you can download it at: http://www.capptalog.com/dowload";
	public static final String mainTitle = "Try Capptalog for Android!";


	public static int SMALL_WIDTH = 600;
	public static int LARGE_WIDE_WIDTH = 752;
	public static int STANDARD_MEDIUM = 768;
	public static int MEDIUM_WIDTH = 800;
	public static int LARGE_WIDTH = 1024;
	public static int XLARGE_WIDE_WIDTH = 1280;

	public static int FONT_SMALL = 12;
	public static int FONT_MEDIUM = 12;
	public static int FONT_LARGE = 16;

	public static int M_FONT_Text ;


	public static int SMALL_BUTTON_WIDTH = 100;
	public static int MEDIUM_BUTTON_WIDTH = 125;
	public static int LARGE_BUTTON_WIDTH = 130;
	public static int XLARGE_BUTTON_WIDTH = 180;
	public static int uLARGE_BUTTON_WIDTH = 200;



	public static int CATEGORY_CARD_WIDTH = 300;
	public static int PRODUCT_LIST_CARD_WIDTH = 300;
	public static int PRODUCT_CARD_WIDTH ;
	public static int WISHLIST_CARD_WIDTH = 240;


	public static Boolean debug = true;

	public static void setINVALID_USER(Boolean iNVALID_USER) {
		INVALID_USER = iNVALID_USER;
	}
	public static boolean getINVALID_USER() {
		return INVALID_USER;
	}

}
