package spencer.cn.finalproject.fragment;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.HashMap;

import spencer.cn.finalproject.R;
import spencer.cn.finalproject.dojo.resp.CurPointBean;
import spencer.cn.finalproject.iexport.NewsCallBack;
import spencer.cn.finalproject.manager.CommonUtil;
import spencer.cn.finalproject.manager.ImageUploadManager;
import spencer.cn.finalproject.manager.LocalDataManager;
import spencer.cn.finalproject.manager.NetWorkManager;
import spencer.cn.finalproject.util.BitmapUtil;
import spencer.cn.finalproject.util.LoadingWaitUtils;
import spencer.cn.finalproject.util.PublicVar;

/**
 * Created by Administrator on 2017/4/24.
 */

public class CreateNews extends BaseFragment {
    private ImageView pic;
    private EditText title;
    private EditText content;
    private Button post;
    private Bitmap _targetBitmap;
    private LoadingWaitUtils wait;

    Gson parser = new GsonBuilder().serializeNulls().create();
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 0xe12){
                wait.cancel();
                String artical = (String) msg.obj;
                CurPointBean postResult = parser.fromJson(artical, CurPointBean.class);
                Log.e("xx", artical);
                if (postResult.getCode() == 200){
                    title.setText("");
                    content.setText("");
                    Toast.makeText(getActivity(), "发表成功，再来一篇吧", Toast.LENGTH_LONG).show();
                }else{
                    Toast.makeText(getActivity(), postResult.getMessage(), Toast.LENGTH_LONG).show();
                }
            }else if (msg.what == 0xe22){
                String quality = (String) msg.obj;
                CurPointBean postResult = parser.fromJson(quality, CurPointBean.class);
                if (postResult.getCode() != 200){
                    Toast.makeText(getActivity(), postResult.getMessage(), Toast.LENGTH_LONG).show();
                }
            }
        }
    };

    @Override
    public void onStart() {
        super.onStart();
        if (CommonUtil.isLogin(getActivity())){
            String url = getActivity().getResources().getString(R.string.url_get_post_quality);
            String accessToken = LocalDataManager.getAccessToken(getActivity());
            NetWorkManager.doGet(url + accessToken, new NewsCallBack() {
                @Override
                public void onNewsReturn(String gstring) {
                    Message msg = new Message();
                    msg.what = 0xe22;
                    msg.obj = gstring;
                    handler.sendMessage(msg);
                }
            });
        }
    }

    @Override
    public void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater,  ViewGroup container,  Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.page_create_news, container, false);
        initViews(view);
        wait = new LoadingWaitUtils(getActivity());
        return view;
    }

    private void initViews(View view) {
        pic = (ImageView) view.findViewById(R.id.iv_xiaozhong_icon);
        title = (EditText) view.findViewById(R.id.edt_edit_xiaozhong_title);
        content = (EditText) view.findViewById(R.id.edt_edit_xiaozhong_content);
        post = (Button) view.findViewById(R.id.btn_post);

        pic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().startActivityForResult(new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI),
                        PublicVar.GET_PIC_FOR_XIAOZHONG);
            }
        });
        post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String titleText = title.getText().toString();
                String contentText = content.getText().toString();
                if (TextUtils.isEmpty(titleText) || TextUtils.isEmpty(contentText)){
                    Toast.makeText(getActivity(), "标题或内容不能为空", Toast.LENGTH_LONG).show();
                    return;
                }
                if (_targetBitmap == null){
                    Toast.makeText(getActivity(), "请选择一张图片", Toast.LENGTH_LONG).show();
                    return;
                }
                Bitmap compressBitmap = BitmapUtil.createZoomInBitmap(_targetBitmap, 100);
                String url = getActivity().getResources().getString(R.string.url_post_artical);
                String accessToken = LocalDataManager.getAccessToken(getActivity());
                HashMap<String, String> params = new HashMap<String, String>();
                params.put("title", titleText);
                params.put("content", contentText);
                wait.show();
                ImageUploadManager.postArtical(url + accessToken, getActivity(), compressBitmap, params, new NewsCallBack() {
                    @Override
                    public void onNewsReturn(String gstring) {
                        Message msg = new Message();
                        msg.what = 0xe12;
                        msg.obj = gstring;
                        handler.sendMessage(msg);
                    }
                });
            }
        });
    }

    public void setArticleBitmap(Bitmap bit){
        pic.setImageBitmap(bit);
        _targetBitmap = bit;
    }

    @Override
    public void onPause() {
        super.onPause();
    }
}
