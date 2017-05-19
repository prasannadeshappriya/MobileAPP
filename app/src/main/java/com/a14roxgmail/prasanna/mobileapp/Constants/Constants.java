package com.a14roxgmail.prasanna.mobileapp.Constants;

/**
 * Created by Prasanna Deshappriya on 1/16/2017.
 */
public abstract class Constants {
    //University of Moratuwa Moodle Server
    public static final String SERVER = "http://online.mrt.ac.lk/";

    //Token based user authentication
    public static final String SERVER_GET_TOKEN = SERVER + "login/token.php";
    public static final String SERVER_GET_USER_INFO = SERVER + "webservice/rest/server.php";
    public static final String SERVER_REST_URL = SERVER + "webservice/rest/server.php";

    //Testing Log Details
    public static final String LOG_TAG = "TAG";

    //Database Name
    public static final String DATABASE_NAME = "MobileAPP";
    public static final int DATABASE_VERSION = 1;

    //Constant Values
    public static final String USER_LOGIN_FLAG = "1";
    public static final String USER_LOGOUT_FLAG = "0";
    public static final String SGPA_FLAG = "sgpa";
    public static final String GPA_FLAG = "gpa";

    //Lms links
    public static final String LMS_LOGIN_URL = "https://lms.mrt.ac.lk/login.php";
    public static final String LMS_ENROLMENTS_URL = "https://lms.mrt.ac.lk/enrolments.php";

    //Web scripting data
    public static final String LMS_SOURCE_TAG = "noramlTableCell";
    public static final String LMS_COOKIE_ID = "PHPSESSID";
    public static final String MOODLE_COOKIE_ID = "MoodleSession";
    public static final String LMS_USERNAME_ID = "LearnOrgUsername";
    public static final String LMS_PASSWORD_ID = "LearnOrgPassword";
    public static final String MOODLE_USERNAME_ID = "username";
    public static final String MOODLE_PASSWORD_ID = "password";

    public static final String LMS_LOGIN_ID = "LearnOrgLogin";
    public static final String LMS_LOGIN_ID_VALUE = "Login";

    //Moodle links
    public static final String MOODLE_LOGIN_URL = "https://online.mrt.ac.lk/login/index.php";
    public static final String MOODLE_CALENDAR_URL = "http://online.mrt.ac.lk/calendar/view.php";

    //Local server links for emulator
//    public static final String SERVER_NOTIFY_URL = "http://10.0.2.2/MyBlogHeroku/public/dashboard/sync";
//    public static final String SERVER_SYNC_DATA_URL = "http://10.0.2.2/MyBlogHeroku/public/dashboard/syncdetails";
//    public static final String SERVER_SYNC_COURSE_URL = "http://10.0.2.2/MyBlogHeroku/public/dashboard/synccourse";

    public static final String SERVER_NOTIFY_URL = "http://192.168.43.101/MyBlogHeroku/public/dashboard/sync";
    public static final String SERVER_SYNC_DATA_URL = "http://192.168.43.101/MyBlogHeroku/public/dashboard/syncdetails";
    public static final String SERVER_SYNC_COURSE_URL = "http://192.168.43.101/MyBlogHeroku/public/dashboard/synccourse";
}
