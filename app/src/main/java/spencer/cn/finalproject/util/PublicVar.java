package spencer.cn.finalproject.util;

import spencer.cn.finalproject.R;

/**
 * Created by Administrator on 2017/3/26.
 */

public class PublicVar {
    public static int CHANGE_ICON_REQUEST_CODE = 0x111;
    public static int CHANGE_GALLEY_ICON_REQUEST_CODE = 0x112;
    public static String CHANGE_ICON_NAME = "change_icon_name";
    public static int [] ICONS = {
            R.mipmap.ic_action_emo_angry,
            R.mipmap.ic_action_emo_basic,
            R.mipmap.ic_action_emo_cool,
            R.mipmap.ic_action_emo_err,
            R.mipmap.ic_action_emo_cry,
            R.mipmap.ic_action_emo_evil,
            R.mipmap.ic_action_emo_kiss,
            R.mipmap.ic_action_emo_laugh,
            R.mipmap.ic_action_emo_shame,
            R.mipmap.ic_action_emo_tongue,
            R.mipmap.ic_action_emo_wink,
            R.mipmap.ic_action_emo_wonder
    };
    public static boolean [] ICON_SELECTED = {
        false, false, false, false, false, false, false, false, false, false, false, false
    };

    public static  int FIRST_PATE_INDEX = 0;
    public static  int GROUP_PATE_INDEX = 1;
    public static  int ME_PATE_INDEX = 2;

    public static String SHARED_FILE = "local_shared";
    public static String IS_FIRST_OPEN = "isFirst";
    public static String ACCESSTOKEN = "accesstoken";


    public static int LOGIN_STATUS = 1;
    public static int REGIST_STATUS = 2;

    public static int TABS_MY_TITLES = 0;
    public static int TABS_TOTAL_TITLES = 1;

    ////历史记录保存文件名
    public static String HISTORY = "_history.txt";
    ////游客配置
    public static String BASECONFIG = "_base_confg.txt";


    public static String NEWS_CATEGORY = "news_category";
    public static String VIEW_NAME = "view_name";

    public static int VIEW_DEFAULT = 0;

    public static int VIEW_CHANGE_PASSWORD = 0;
    public static int VIEW_FORGET_PASSWORD = 1;
}
//mysql -h 120.77.34.81 -uroot -pwulizhou
