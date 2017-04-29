package spencer.cn.finalproject.dojo;

import java.util.ArrayList;

/**
 *  created at 2016/7/1 18:20
 *  @author : 吴培健
 *  @todo   : 新闻属性
 */
public class NewsInfo {
    public static ArrayList<String> typesArgs;
    public static ArrayList<String> typesNames;
    public static ArrayList<String> typesUrls;
    public static final String URL = "url";//常量
    public static final String TITLE = "title";
    public static final String UNIQUEKEY = "uniquekey";
    public static final String PICTUREURL = "pictureurl";

    static{
        //初始化新闻类型请求参数
        typesArgs = new ArrayList<>();
        typesArgs.add("top");typesArgs.add("shehui");typesArgs.add("guonei");typesArgs.add("guoji");typesArgs.add("yule");
        typesArgs.add("tiyu");typesArgs.add("junshi");typesArgs.add("keji");typesArgs.add("caijing");typesArgs.add("shishang");

        //初始化新闻类型名
        typesNames = new ArrayList<>();
        typesNames.add("头条");typesNames.add("社会");typesNames.add("国内");typesNames.add("国际");typesNames.add("娱乐");
        typesNames.add("体育");typesNames.add("军事");typesNames.add("科技");typesNames.add("财经");typesNames.add("时尚");

        //初始化新闻请求url
        typesUrls = new ArrayList<>();
        typesUrls.add("http://v.juhe.cn/toutiao/index?type=top&key=aac5ceb82beb0fbdff4210f12a004608");
        typesUrls.add("http://v.juhe.cn/toutiao/index?type=shehui&key=aac5ceb82beb0fbdff4210f12a004608");
        typesUrls.add("http://v.juhe.cn/toutiao/index?type=guonei&key=aac5ceb82beb0fbdff4210f12a004608");
        typesUrls.add("http://v.juhe.cn/toutiao/index?type=guoji&key=aac5ceb82beb0fbdff4210f12a004608");
        typesUrls.add("http://v.juhe.cn/toutiao/index?type=yule&key=aac5ceb82beb0fbdff4210f12a004608");
        typesUrls.add("http://v.juhe.cn/toutiao/index?type=tiyu&key=aac5ceb82beb0fbdff4210f12a004608");
        typesUrls.add("http://v.juhe.cn/toutiao/index?type=junshi&key=aac5ceb82beb0fbdff4210f12a004608");
        typesUrls.add("http://v.juhe.cn/toutiao/index?type=keji&key=aac5ceb82beb0fbdff4210f12a004608");
        typesUrls.add("http://v.juhe.cn/toutiao/index?type=caijing&key=aac5ceb82beb0fbdff4210f12a004608");
        typesUrls.add("http://v.juhe.cn/toutiao/index?type=shishang&key=aac5ceb82beb0fbdff4210f12a004608");

    }
}
