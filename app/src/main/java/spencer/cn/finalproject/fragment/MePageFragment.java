package spencer.cn.finalproject.fragment;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
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

import spencer.cn.finalproject.R;
import spencer.cn.finalproject.acview.ChangeUserInfoActivity;
import spencer.cn.finalproject.acview.HistoryDetailActivity;
import spencer.cn.finalproject.acview.LoginActivity;
import spencer.cn.finalproject.application.BaseApplication;
import spencer.cn.finalproject.dojo.GsonNews;
import spencer.cn.finalproject.dojo.LoginBean;
import spencer.cn.finalproject.iexport.NewsCallBack;
import spencer.cn.finalproject.manager.LocalDataManager;
import spencer.cn.finalproject.manager.NetWorkManager;
import spencer.cn.finalproject.util.PublicVar;

/**
 * Created by Administrator on 2017/3/5.
 */

public class MePageFragment extends Fragment {
    private ImageView icon;
    private TextView login;
    private Button history;
    private Button logout;
    private Button changePass;
    private Button forgetPass;
    private Context context;
    private Gson parser = new GsonBuilder().serializeNulls().create();
    private Handler executor = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 0xfc1){
                String loginStrings = (String) msg.obj;
                Log.e("e", loginStrings);
                GsonNews logoutBean = parser.fromJson(loginStrings, GsonNews.class);
                if (logoutBean.getCode() == 200){
                    //清除缓存
                    LocalDataManager.clearAccessToken(getActivity());
                    BaseApplication.setLoginBean(null);
                    Intent loginActivity = new Intent(getActivity(), LoginActivity.class);
                    getActivity().startActivity(loginActivity);
                }else{
                    Toast.makeText(getActivity(), "登出超时："+logoutBean.getMessage(), Toast.LENGTH_LONG).show();
                }

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

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.page_me_layout, container,false);
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



    }
    public void refreshDatas(){

        LoginBean loginBean = BaseApplication.getLoginBean();
        if (loginBean!=null && loginBean.getData()!=null && loginBean.getData().getUser()!=null){
            String username = loginBean.getData().getUser().getUsername();
            username = (username==null) ? "null" : username;
            login.setText(username);
            String pic = loginBean.getData().getUser().getAvatar();
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
//                Intent changeIcon = new Intent(getActivity(), ChangeIconActivity.class);
//                getActivity().startActivityForResult(changeIcon, PublicVar.CHANGE_ICON_REQUEST_CODE);
                getIconFromGalley();
            }
        });
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //先判断状态
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
                Intent cui = new Intent(getActivity(), ChangeUserInfoActivity.class);
                cui.putExtra(PublicVar.VIEW_NAME, PublicVar.VIEW_CHANGE_PASSWORD);
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
    }

    public void setPlayerIcon(int iconId){
        icon.setBackgroundResource(iconId);
        Bitmap bitmap = BitmapFactory.decodeResource(getActivity().getResources(), iconId);
        icon.setImageBitmap(bitmap);
//        Toast.makeText(getActivity(), "hello icon", Toast.LENGTH_LONG).show();
    }
    public void setPlayerIcon(Bitmap iconBitmap){
        icon.setImageBitmap(iconBitmap);
    }
    public void getIconFromGalley(){
        getActivity().startActivityForResult(new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI),
                PublicVar.CHANGE_GALLEY_ICON_REQUEST_CODE);
    }
}
