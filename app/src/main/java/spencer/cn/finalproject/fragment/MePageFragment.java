package spencer.cn.finalproject.fragment;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.squareup.picasso.Picasso;

import java.util.HashMap;

import spencer.cn.finalproject.R;
import spencer.cn.finalproject.acview.ChangeUserInfoActivity;
import spencer.cn.finalproject.acview.HistoryDetailActivity;
import spencer.cn.finalproject.acview.LoginActivity;
import spencer.cn.finalproject.application.BaseApplication;
import spencer.cn.finalproject.dojo.GsonNews;
import spencer.cn.finalproject.dojo.UserInfo;
import spencer.cn.finalproject.dojo.resp.CurPointBean;
import spencer.cn.finalproject.dojo.resp.VersionResp;
import spencer.cn.finalproject.iexport.NewsCallBack;
import spencer.cn.finalproject.manager.CommonUtil;
import spencer.cn.finalproject.manager.LocalDataManager;
import spencer.cn.finalproject.manager.NetWorkManager;
import spencer.cn.finalproject.util.DialogUtil;
import spencer.cn.finalproject.util.LoadingWaitUtils;
import spencer.cn.finalproject.util.PublicVar;

/**
 * Created by Administrator on 2017/3/5.
 */

public class MePageFragment extends Fragment {
    private ImageView icon;
    private TextView login;//用户名
    private TextView signForPoints;//签到
    private TextView usrPoints;
    private Button history;
    private Button logout;
    private Button changePass;
    private Button forgetPass;
    private Button rulesPoint;
    private Button contactService;
    private Button pointsDetail;
    private Button collect;
    private Button changeName;
    private Button checkVersion;

    private LoadingWaitUtils waiting;

    private Context context;

    private Gson parser = new GsonBuilder().serializeNulls().create();
    private Handler executor = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            waiting.cancel();
            if (msg.what == 0xfc1){
                String loginStrings = (String) msg.obj;
                Log.e("e", loginStrings);
                GsonNews logoutBean = parser.fromJson(loginStrings, GsonNews.class);
                if (logoutBean.getCode() == 200){
                    //清除缓存
                    LocalDataManager.clearAccessToken(getActivity());
                    BaseApplication.setLoginBean(null);

                    BaseApplication.setConfig(null);
                    BaseApplication.setInfo(null);

                    Intent loginActivity = new Intent(getActivity(), LoginActivity.class);
                    getActivity().startActivity(loginActivity);
                }else{
                    Toast.makeText(getActivity(), "登出超时："+logoutBean.getMessage(), Toast.LENGTH_LONG).show();
                }

            }else if(msg.what == 0xfc2){
                waiting.cancel();
                String loginStrings = (String) msg.obj;
                GsonNews checkBean = parser.fromJson(loginStrings, GsonNews.class);
                Toast.makeText(getActivity(), checkBean.getMessage(), Toast.LENGTH_LONG).show();
            }else if (msg.what == 0xfc3){
                String pointsString = (String) msg.obj;
                CurPointBean checkBean = parser.fromJson(pointsString, CurPointBean.class);
                if (checkBean != null)
                    DialogUtil.dialog(getActivity(), "您当前的积分：" + checkBean.getData().getIntegral() + "\n当前排名："+checkBean.getData().getSeq());
            }else if (msg.what == 0xfc4){
                String versionInfo = (String) msg.obj;
                VersionResp result = parser.fromJson(versionInfo, VersionResp.class);
                if (result != null){
//                    判断版本是否更新
                    Uri uri = Uri.parse("http://" + result.getData().getUrl());
                    startActivity(new Intent(Intent.ACTION_VIEW,uri));
                }
//               {"code":200,"message":"成功",
// "data":{"version":"1.1","url":"www.baodu.com","remarks":"版本更新"}}

            }
        }
    };
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onStart() {
        super.onStart();
        if(login!=null && BaseApplication.getInfo()!=null){
            String newName = BaseApplication.getInfo().getUsername();
            String oldName = login.getText().toString();
            if (TextUtils.isEmpty(oldName) || oldName.equals("请登录")){
                if (!TextUtils.isEmpty(newName)){
                    login.setText(newName);
                }
            }
        }
        if (icon != null && BaseApplication.getInfo()!=null){
            String pic = BaseApplication.getInfo().getAvatar();
            String url = getActivity().getResources().getString(R.string.url_download_small_img)+pic;
            Picasso.with(getActivity()).load(url).into(icon);
        }
        if (BaseApplication.getInfo() == null){
            login.setText("请登录");
            icon.setImageResource(R.mipmap.ic_launcher);
        }

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.page_me_layout, container,false);
        waiting = new LoadingWaitUtils(getActivity());
        this.initViews(view);
        this.setInterreactions();
        this.refreshDatas();
        return view;
    }

    @Override
    public void onPause() {
        super.onPause();
    }
    //初始化控件
    public void initViews(View v){
        icon = (ImageView) v.findViewById(R.id.iv_player);
        login = (TextView) v.findViewById(R.id.btn_name);
        history = (Button) v.findViewById(R.id.btn_me_history_record);
        logout = (Button) v.findViewById(R.id.btn_logout);
        changePass = (Button) v.findViewById(R.id.btn_change_password);
        forgetPass = (Button) v.findViewById(R.id.btn_forget_password);
        rulesPoint = (Button) v.findViewById(R.id.btn_rules_point);
        contactService = (Button) v.findViewById(R.id.btn_customer_service);
        signForPoints = (TextView) v.findViewById(R.id.btn_sign_every_day);
        usrPoints = (TextView) v.findViewById(R.id.btn_check_points);
        pointsDetail = (Button) v.findViewById(R.id.btn_points_detail);
        collect = (Button) v.findViewById(R.id.btn_my_collect);
        changeName = (Button) v.findViewById(R.id.btn_change_user_name);
        checkVersion = (Button) v.findViewById(R.id.btn_check_version);
    }
    public void refreshDatas(){

//        LoginBean loginBean = BaseApplication.getLoginBean();
        UserInfo userInfo = BaseApplication.getInfo();
        if (userInfo!=null){
            String username = userInfo.getUsername();
            username = (username==null) ? "请登录" : username;
            login.setText(username);
            String pic = userInfo.getAvatar();
            String url = getActivity().getResources().getString(R.string.url_download_small_img)+pic;
            Picasso.with(getActivity()).load(url).into(icon);
        }else {
            login.setText("请登录");
        }
    }

    //控件行为
    public void setInterreactions(){
        icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!CommonUtil.isLogin(getActivity())){
                    Intent intent = new Intent(getActivity(), LoginActivity.class);
                    getActivity().startActivity(intent);
                    return;
                }
//                Intent changeIcon = new Intent(getActivity(), ChangeIconActivity.class);
//                getActivity().startActivityForResult(changeIcon, PublicVar.CHANGE_ICON_REQUEST_CODE);
                getIconFromGalley();
            }
        });
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //先判断状态
//                if (!CommonUtil.isLogin(getActivity())){
//                    Intent intent = new Intent(getActivity(), LoginActivity.class);
//                    getActivity().startActivity(intent);
//                    return;
//                }
                Intent loginIntent = new Intent(getActivity(), LoginActivity.class);
                getActivity().startActivity(loginIntent);
            }
        });
        history.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent historyIntent = new Intent(getActivity(), HistoryDetailActivity.class);
                getActivity().startActivity(historyIntent);
            }
        });
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!CommonUtil.isLogin(getActivity())){
                    Intent intent = new Intent(getActivity(), LoginActivity.class);
                    getActivity().startActivity(intent);
                    return;
                }
                waiting.show();
                String url = getActivity().getResources().getString(R.string.url_get_logout);
                String accessToken = LocalDataManager.getAccessToken(getActivity());
                NetWorkManager.doGet(url + accessToken, new NewsCallBack() {
                    @Override
                    public void onNewsReturn(String gstring) {
                        Message msg = new Message();
                        msg.what = 0xfc1;
                        msg.obj = gstring;
                        executor.sendMessage(msg);
                    }
                });
            }
        });
        changePass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!CommonUtil.isLogin(getActivity())){
                    Intent intent = new Intent(getActivity(), LoginActivity.class);
                    getActivity().startActivity(intent);
                    return;
                }
                Intent cui = new Intent(getActivity(), ChangeUserInfoActivity.class);
                cui.putExtra(PublicVar.VIEW_NAME, PublicVar.VIEW_CHANGE_PASSWORD);
                startActivity(cui);
            }
        });
        changeName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!CommonUtil.isLogin(getActivity())){
                    Intent intent = new Intent(getActivity(), LoginActivity.class);
                    getActivity().startActivity(intent);
                    return;
                }
                Intent cui = new Intent(getActivity(), ChangeUserInfoActivity.class);
                cui.putExtra(PublicVar.VIEW_NAME, PublicVar.VIEW_CHANGE_NAME);
                startActivity(cui);
            }
        });
        forgetPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent cui = new Intent(getActivity(), ChangeUserInfoActivity.class);
                cui.putExtra(PublicVar.VIEW_NAME, PublicVar.VIEW_FORGET_PASSWORD);
                startActivity(cui);
//                Intent intent = new Intent(getActivity(), BaseActionBarActivity.class);
//                startActivity(intent);
            }
        });
        pointsDetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!CommonUtil.isLogin(getActivity())){
                    Intent intent = new Intent(getActivity(), LoginActivity.class);
                    getActivity().startActivity(intent);
                    return;
                }
                Intent cui = new Intent(getActivity(), ChangeUserInfoActivity.class);
                cui.putExtra(PublicVar.VIEW_NAME, PublicVar.VIEW_POINTS_DETAIL);
                startActivity(cui);
            }
        });
        rulesPoint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!CommonUtil.isLogin(getActivity())){
                    Intent intent = new Intent(getActivity(), LoginActivity.class);
                    getActivity().startActivity(intent);
                    return;
                }
                Intent cui = new Intent(getActivity(), ChangeUserInfoActivity.class);
                cui.putExtra(PublicVar.VIEW_NAME, PublicVar.VIEW_POINTS_RULES);
                startActivity(cui);
            }
        });
        contactService.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!CommonUtil.isLogin(getActivity())){
                    Intent intent = new Intent(getActivity(), LoginActivity.class);
                    getActivity().startActivity(intent);
                    return;
                }
                Intent cui = new Intent(getActivity(), ChangeUserInfoActivity.class);
                cui.putExtra(PublicVar.VIEW_NAME, PublicVar.VIEW_CUSTOMER_SERVICE);
                startActivity(cui);
            }
        });
        signForPoints.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!CommonUtil.isLogin(getActivity())){
                    Intent intent = new Intent(getActivity(), LoginActivity.class);
                    getActivity().startActivity(intent);
                    return;
                }
                String url = getActivity().getResources().getString(R.string.url_post_daily_check_in);
                String accessToken = LocalDataManager.getAccessToken(getActivity());
                HashMap<String, String> params = new HashMap<String, String>();
                waiting.show();
                NetWorkManager.doPost(url+accessToken, params, new NewsCallBack() {
                    @Override
                    public void onNewsReturn(String gstring) {
                        Message msg = new Message();
                        msg.what = 0xfc2;
                        msg.obj = gstring;
                        executor.sendMessage(msg);
                    }
                });
            }
        });
        usrPoints.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!CommonUtil.isLogin(getActivity())){
                    Intent intent = new Intent(getActivity(), LoginActivity.class);
                    getActivity().startActivity(intent);
                    return;
                }
                String url = getActivity().getResources().getString(R.string.url_get_user_integral);
                String accessToken = LocalDataManager.getAccessToken(getActivity());
                waiting.show();
                NetWorkManager.doGet(url + accessToken, new NewsCallBack() {
                    @Override
                    public void onNewsReturn(String gstring) {
                        Message msg = new Message();
                        msg.what = 0xfc3;
                        msg.obj = gstring;
                        executor.sendMessage(msg);
                    }
                });
            }
        });
        collect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!CommonUtil.isLogin(getActivity())){
                    Intent intent = new Intent(getActivity(), LoginActivity.class);
                    getActivity().startActivity(intent);
                    return;
                }
                Intent cui = new Intent(getActivity(), ChangeUserInfoActivity.class);
                cui.putExtra(PublicVar.VIEW_NAME, PublicVar.VIEW_MY_COLLECT);
                startActivity(cui);
            }
        });
        checkVersion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = getActivity().getResources().getString(R.string.url_get_check_version);
                String type = "xiaozhong-client";
                String connatUrl = url + "?type=" + type;
                NetWorkManager.doGet(connatUrl, new NewsCallBack() {
                    @Override
                    public void onNewsReturn(String gstring) {
                        Message msg = new Message();
                        msg.what = 0xfc4;
                        msg.obj = gstring;
                        executor.sendMessage(msg);
                    }
                });
            }
        });
    }

    public void setPlayerIcon(int iconId){
        icon.setBackgroundResource(iconId);
        Bitmap bitmap = BitmapFactory.decodeResource(getActivity().getResources(), iconId);
        icon.setImageBitmap(bitmap);
    }
    public void setPlayerIcon(Bitmap iconBitmap){
        icon.setImageBitmap(iconBitmap);
    }
    public void getIconFromGalley(){
        getActivity().startActivityForResult(new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI),
                PublicVar.CHANGE_GALLEY_ICON_REQUEST_CODE);
    }
}
