package rs.tridanwebshop.tridan.fcm;

/**
 * Created by 1 on 4/19/2016.
 */
public class Config {

    // broadcast receiver intent filters
    public static final String SENT_TOKEN_TO_SERVER = "sentTokenToServer";
    public static final String REGISTRATION_COMPLETE = "registrationComplete";
    public static final String PUSH_NOTIFICATION = "pushNotification";
    public static final String SET_USER_INFO = "setUserInfo";
    public static final String CLEAR_USER_INFO = "clearUserInfo";
    public static final String UPDATE_CART_TOOLBAR_ICON = "updateCartToolbarIcon";

    public static final String SHOW_ARTICLE_DETAILS = "showArticleDetails";
    // type of push messages
    public static final int PUSH_TYPE_CHATROOM = 1;
    public static final int PUSH_TYPE_USER = 2;
    // id to handle the notification in the notification try
    public static final int NOTIFICATION_ID = 100;
    public static final int NOTIFICATION_ID_BIG_IMAGE = 101;
    // flag to identify whether to show single line
    // or multi line text in push notification tray
    public static boolean appendNotificationMessages = true;
}