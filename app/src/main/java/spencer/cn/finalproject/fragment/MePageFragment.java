package spencer.cn.finalproject.fragment;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import spencer.cn.finalproject.R;
import spencer.cn.finalproject.acview.HistoryDetailActivity;
import spencer.cn.finalproject.acview.LoginActivity;
import spencer.cn.finalproject.application.BaseApplication;
import spencer.cn.finalproject.dojo.LoginBean;
import spencer.cn.finalproject.util.PublicVar;

/**
 * Created by Administrator on 2017/3/5.
 */

public class MePageFragment extends Fragment {
    private ImageView icon;
    private Button login;
    private Button history;
    private Context context;
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
        login = (Button) v.findViewById(R.id.btn_name);
        history = (Button) v.findViewById(R.id.btn_me_history_record);


    }
    public void refreshDatas(){

        LoginBean loginBean = BaseApplication.getLoginBean();
        if (loginBean!=null && loginBean.getData()!=null && loginBean.getData().getUser()!=null){
            String username = loginBean.getData().getUser().getUsername();
            username = username==null ? "null" : username;
            login.setText(username);
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
