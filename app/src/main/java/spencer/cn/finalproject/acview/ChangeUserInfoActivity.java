package spencer.cn.finalproject.acview;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.TextInputLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import java.util.HashMap;

import spencer.cn.finalproject.R;
import spencer.cn.finalproject.adapter.CollectionsAdapter;
import spencer.cn.finalproject.adapter.CommontsAdapter;
import spencer.cn.finalproject.adapter.PointsDescAdapter;
import spencer.cn.finalproject.application.BaseApplication;
import spencer.cn.finalproject.dojo.CollectionListResp;
import spencer.cn.finalproject.dojo.CommentInfoResp;
import spencer.cn.finalproject.dojo.GsonNews;
import spencer.cn.finalproject.dojo.IntegralInfoResp;
import spencer.cn.finalproject.dojo.UploadImgResp;
import spencer.cn.finalproject.dojo.resp.CollectListBean;
import spencer.cn.finalproject.dojo.resp.GetCommentsResp;
import spencer.cn.finalproject.dojo.resp.PointsDetailsBean;
import spencer.cn.finalproject.iexport.NewsCallBack;
import spencer.cn.finalproject.manager.LocalDataManager;
import spencer.cn.finalproject.manager.NetWorkManager;
import spencer.cn.finalproject.util.PublicVar;

import static spencer.cn.finalproject.manager.NetWorkManager.mapToGetParams;

public class ChangeUserInfoActivity extends BaseActionBarActivity {
    private Intent curIntent;

    ViewGroup changePassworld;
    TextInputLayout cp_oldPassword;
    TextInputLayout cp_newPassword;
    TextInputLayout cp_newPasswordAgain;
    Button cp_confirm;

    ViewGroup forgetPassword;
    TextInputLayout fp_oldPassword;
    TextInputLayout fp_newPassword;
    TextInputLayout fp_newPasswordAgain;
    EditText registCode;
    Button getRegistCode;
    Button fp_confirm;

    ViewGroup commentsView;
    SwipeRefreshLayout refreshComments;
    RecyclerView commentsContent;
    Long curUid;
    int curPage;
    CommontsAdapter adapter;
    ArrayList<CommentInfoResp> datas;

    ViewGroup pointRuleView;
    TextView pointRules;

    ViewGroup cusServiceViwe;
    TextView cusService;

//    积分获得情况
    ViewGroup pointDetailView;
    SwipeRefreshLayout refreshPointDetailView;
    RecyclerView pointsDetailContent;
    int curPointsDetailPage;
    PointsDescAdapter pointsDescAdapter;
    ArrayList<IntegralInfoResp> integralDatas;

//    adapter arrayList

    //my collect
    ViewGroup myCollectView;
    SwipeRefreshLayout refreshMyCollect;
    RecyclerView lvMyCollect;
    int curCollectPage;
    CollectionsAdapter collectAdapter;
    ArrayList<CollectionListResp> collectDatas;


    Gson userparser = new GsonBuilder().serializeNulls().setDateFormat("yyyy-MM-dd HH:mm:ss").create();
    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 0xf91){
                String cpgstring = (String) msg.obj;
                Log.e("e", cpgstring);
                GsonNews result = userparser.fromJson(cpgstring, GsonNews.class);
                if (result.getCode()==200){
                    Toast.makeText(ChangeUserInfoActivity.this, "修改成功", Toast.LENGTH_LONG).show();
                    finish();
                }else{
                    Toast.makeText(ChangeUserInfoActivity.this, "修改失败：" + result.getMessage(), Toast.LENGTH_LONG).show();
                }
            }else if (msg.what == 0xf92){
                String gsonStrings = (String) msg.obj;
                Log.e("e", gsonStrings);
                GsonNews registResp = userparser.fromJson(gsonStrings, GsonNews.class);
                Toast.makeText(ChangeUserInfoActivity.this, registResp.getMessage(), Toast.LENGTH_LONG).show();
            }else if (msg.what == 0xf93){
                String cpgstring = (String) msg.obj;
                Log.e("e", cpgstring);
                GsonNews result = userparser.fromJson(cpgstring, GsonNews.class);
                if (result.getCode()==200){
                    Toast.makeText(ChangeUserInfoActivity.this, "重置成功", Toast.LENGTH_LONG).show();
                    finish();
                }else{
                    Toast.makeText(ChangeUserInfoActivity.this, "重置失败：" + result.getMessage(), Toast.LENGTH_LONG).show();
                }
            }else if (msg.what == 0xf94){
                String rString = (String) msg.obj;
                UploadImgResp rules = userparser.fromJson(rString, UploadImgResp.class);
                Log.e("xxxx", rString);
                if (rules.getCode() == 200){
                    pointRules.setText(rules.getData());
                }else{
                    pointRules.setText("请求超时");
                }
            }else if (msg.what == 0xf95){
                String rString = (String) msg.obj;
                UploadImgResp rules = userparser.fromJson(rString, UploadImgResp.class);
                if (rules.getCode() == 200){
                    cusService.setText(rules.getData());
                }else{
                    cusService.setText("请求超时");
                }
            }else  if (msg.what == 0xf96){
                refreshComments.setRefreshing(false);
                String rString = (String) msg.obj;
                GetCommentsResp comments = userparser.fromJson(rString, GetCommentsResp.class);
                if (comments.getCode() == 200){
                    if (datas==null){
                        datas = comments.getData();
                        Long userId = BaseApplication.getLoginBean().getData().getUser().getUid();
                        adapter = new CommontsAdapter(ChangeUserInfoActivity.this, datas, userId);
                        commentsContent.setAdapter(adapter);
                    }else{
                        if ( comments.getData().size() <= 0){
                            Toast.makeText(ChangeUserInfoActivity.this, "这已经是全部评论喽", Toast.LENGTH_LONG).show();
                            return;
                        }
                        for(int i=0; i < comments.getData().size(); i++){
                            datas.add(comments.getData().get(i));
                        }
                        adapter.setItems(datas);
                    }
                }else{
                    Toast.makeText(ChangeUserInfoActivity.this, "请求超时", Toast.LENGTH_LONG).show();
                }
            }else if (msg.what == 0xf97){
                refreshPointDetailView.setRefreshing(false);
                String rString = (String) msg.obj;
                Log.e("xxx", rString);
                PointsDetailsBean bean = userparser.fromJson(rString, PointsDetailsBean.class);
                if (bean.getCode() == 200){
                    if (integralDatas==null){
                        integralDatas = bean.getData();
                        pointsDescAdapter = new PointsDescAdapter(ChangeUserInfoActivity.this, integralDatas);
                        pointsDetailContent.setAdapter(pointsDescAdapter);
                    }else{
                        if ( bean.getData().size() <= 0){
                            Toast.makeText(ChangeUserInfoActivity.this, "这已经是全部喽", Toast.LENGTH_LONG).show();
                            return;
                        }
                        for(int i=0; i < bean.getData().size(); i++){
                            integralDatas.add(bean.getData().get(i));
                        }
                        pointsDescAdapter.setItems(integralDatas);
                    }
                }else{
                    Toast.makeText(ChangeUserInfoActivity.this, "请求超时", Toast.LENGTH_LONG).show();
                }
            }else if (msg.what == 0xf98){
                refreshMyCollect.setRefreshing(false);
                String rString = (String) msg.obj;
                CollectListBean bean = userparser.fromJson(rString, CollectListBean.class);
                if (bean.getCode() == 200){
                    if (collectDatas==null){
                        collectDatas = bean.getData();
                        collectAdapter = new CollectionsAdapter(ChangeUserInfoActivity.this, collectDatas);
                        lvMyCollect.setAdapter(collectAdapter);
                    }else{
                        if ( bean.getData().size() <= 0){
                            Toast.makeText(ChangeUserInfoActivity.this, "这已经是全部喽", Toast.LENGTH_LONG).show();
                            return;
                        }
                        for(int i=0; i < bean.getData().size(); i++){
                            collectDatas.add(0, bean.getData().get(i));
                        }
                        collectAdapter.setItems(collectDatas);
                    }
                }else{
                    Toast.makeText(ChangeUserInfoActivity.this, "请求超时", Toast.LENGTH_LONG).show();
                }
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_user_info);
        curIntent = getIntent();
        initViews();

    }

    @Override
    protected void onStart() {
        super.onStart();

        switchViews(getIntent());
    }

    private void initViews() {
        changePassworld = (ViewGroup) findViewById(R.id.view_change_pass);
        forgetPassword = (ViewGroup) findViewById(R.id.view_forget_pass);
        commentsView = (ViewGroup) findViewById(R.id.view_commonts);
        pointRuleView = (ViewGroup) findViewById(R.id.view_points_rules);
        cusServiceViwe = (ViewGroup) findViewById(R.id.view_cus_service);
        pointDetailView = (ViewGroup) findViewById(R.id.view_points_detail);
        myCollectView = (ViewGroup) findViewById(R.id.view_my_collects);

        initChangePasswordViews();
        initForgetPasswordViews();
        initCommontsViews();
        initPointsRulesViews();
        initCusServiceViews();
        initPointsDetailViews();
        initMyCollectViews();
    }

    private void initMyCollectViews() {
        refreshMyCollect = (SwipeRefreshLayout) findViewById(R.id.srl_my_collects);
        lvMyCollect = (RecyclerView) findViewById(R.id.rv_my_colelcts);
        lvMyCollect.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        lvMyCollect.setItemAnimator(new DefaultItemAnimator());
        refreshMyCollect.setColorSchemeResources(android.R.color.holo_blue_light,android.R.color.holo_green_light);
        refreshMyCollect.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                //访问网络数据
//                refreshComments.setRefreshing(false);
                curCollectPage += 1;
                getCollectDatas(curCollectPage);
            }
        });
    }

    private void getCollectDatas(int curpage){
        String url = getResources().getString(R.string.url_get_my_collect);
        String accessToken = LocalDataManager.getAccessToken(this);
        HashMap<String, String> params = new HashMap<>();
        params.put("accessToken", accessToken);
        params.put("page", curpage+"");
        params.put("rows", 15+"");

        String tail = NetWorkManager.mapToGetParams(params);
        NetWorkManager.doGet(url + tail, new NewsCallBack() {
            @Override
            public void onNewsReturn(String gstring) {
                Message msg = new Message();
                msg.what = 0xf98;
                msg.obj = gstring;
                handler.sendMessage(msg);
            }
        });
    }

    private void initPointsDetailViews() {
        refreshPointDetailView = (SwipeRefreshLayout) findViewById(R.id.srl_refesh_points_detail);
        pointsDetailContent = (RecyclerView) findViewById(R.id.rv_points_detail);
        pointsDetailContent.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        pointsDetailContent.setItemAnimator(new DefaultItemAnimator());
        refreshPointDetailView.setColorSchemeResources(android.R.color.holo_blue_light,android.R.color.holo_green_light);
        refreshPointDetailView.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                //访问网络数据
//                refreshComments.setRefreshing(false);
                curPointsDetailPage += 1;
                getPointsDetail(curPointsDetailPage);
            }
        });
    }

    private void getPointsDetail(int curPage){
        String url = getResources().getString(R.string.url_get_user_integral_list);
        HashMap<String, String> params = new HashMap<>();
        params.put("accessToken", LocalDataManager.getAccessToken(this));
        params.put("page", curPage+"");
        params.put("rows", 15+"");
        String tail = mapToGetParams(params);
        NetWorkManager.doGet(url + tail, new NewsCallBack() {
            @Override
            public void onNewsReturn(String gstring) {
                Message msg = new Message();
                msg.what = 0xf97;
                msg.obj = gstring;
                handler.sendMessage(msg);
            }
        });
    }

    private void initCusServiceViews() {
        cusService = (TextView) findViewById(R.id.tv_customer_service);

        String url = getResources().getString(R.string.url_get_custom_service);
        NetWorkManager.doGet(url, new NewsCallBack() {
            @Override
            public void onNewsReturn(String gstring) {
                Message msg = new Message();
                msg.what = 0xf95;
                msg.obj = gstring;
                handler.sendMessage(msg);
            }
        });
    }

    private void initPointsRulesViews() {
        pointRules = (TextView) findViewById(R.id.tv_point_rule);


    }

    private void initCommontsViews() {
        refreshComments = (SwipeRefreshLayout) findViewById(R.id.srl_refesh_commonts);
        commentsContent = (RecyclerView) findViewById(R.id.rv_content_comments);
        commentsContent.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        commentsContent.setItemAnimator(new DefaultItemAnimator());

//        if (curUid <= 0L){
//            Toast.makeText(this, "等待新闻加载", Toast.LENGTH_LONG).show();
//            finish();
//            return;
//        }
        ///////////////////////加载新闻

        refreshComments.setColorSchemeResources(android.R.color.holo_blue_light,android.R.color.holo_green_light);
        refreshComments.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                //访问网络数据
//                refreshComments.setRefreshing(false);
                curPage += 1;
                getComments(curUid, curPage);
            }
        });

    }
    private void getComments(Long uid, int page){
        String url = getResources().getString(R.string.url_get_comments);
        HashMap<String, String> params = new HashMap<>();
        params.put("accessToken", LocalDataManager.getAccessToken(this));
        params.put("newId", uid+"");
        params.put("page", page+"");
        params.put("rows", 15+"");
        String tail = mapToGetParams(params);
        NetWorkManager.doGet(url + tail, new NewsCallBack() {
            @Override
            public void onNewsReturn(String gstring) {
                Message msg = new Message();
                msg.what = 0xf96;
                msg.obj = gstring;
                handler.sendMessage(msg);
            }
        });
    }

    private void initChangePasswordViews() {
        cp_oldPassword = (TextInputLayout) findViewById(R.id.change_password_new);
        cp_newPassword = (TextInputLayout) findViewById(R.id.change_password_old);
        cp_newPasswordAgain = (TextInputLayout) findViewById(R.id.change_password_old_again);
        cp_confirm = (Button) findViewById(R.id.btn_change_pass_commit);

        cp_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String text = "不能为空";
                if (checkNil(cp_oldPassword, text)) return;
                if (checkNil(cp_newPassword, text)) return;
                if (checkNil(cp_newPasswordAgain, text)) return;
                String oldPass = cp_oldPassword.getEditText().getText().toString();
                String newPass = cp_newPassword.getEditText().getText().toString();
                String newPass2 = cp_newPassword.getEditText().getText().toString();
                if (!newPass.equals(newPass2)){
                    Toast.makeText(ChangeUserInfoActivity.this, "两次输入的密码不一致", Toast.LENGTH_LONG).show();
                    return;
                }
                String url = getResources().getString(R.string.url_post_change_pass) + LocalDataManager.getAccessToken(ChangeUserInfoActivity.this);
                HashMap<String, String> params = new HashMap<String, String>();
                params.put("oldPassword", oldPass);
                params.put("newPassword", newPass);
                NetWorkManager.doPost(url, params, new NewsCallBack() {
                    @Override
                    public void onNewsReturn(String gstring) {
                        Message msg = new Message();
                        msg.what = 0xf91;
                        msg.obj = gstring;
                        handler.sendMessage(msg);
                    }
                });
            }
        });
    }

    private void initForgetPasswordViews() {
        fp_oldPassword = (TextInputLayout) findViewById(R.id.forget_email);
        fp_newPassword = (TextInputLayout) findViewById(R.id.forget_new_pass);
        fp_newPasswordAgain = (TextInputLayout) findViewById(R.id.forget_new_pass_again);
        fp_confirm = (Button) findViewById(R.id.btn_forget_change_commit);

        registCode = (EditText) findViewById(R.id.edt_forget_regist_code);
        getRegistCode = (Button) findViewById(R.id.btn_forget_getRegistCode);

        getRegistCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String emailTxt = fp_oldPassword.getEditText().getText().toString();
                if (!TextUtils.isEmpty(emailTxt)){
                    String url = getResources().getString(R.string.url_get_regist_code);
                    HashMap<String, String> params = new HashMap<String, String>();
                    params.put("codeType", "2");
                    params.put("email", emailTxt);
                    NetWorkManager.doPost(url, params, new NewsCallBack(){

                        @Override
                        public void onNewsReturn(String gstring) {
                            Message msg = new Message();
                            msg.what = 0xf92;
                            msg.obj = gstring;
                            handler.sendMessage(msg);
                        }
                    });
                }else {
                    Toast.makeText(ChangeUserInfoActivity.this, "邮箱不能为空", Toast.LENGTH_LONG).show();
                }
            }
        });
        fp_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String text = "不能为空";
                if (checkNil(fp_oldPassword, text)) return;
                if (checkNil(fp_newPassword, text)) return;
                if (checkNil(fp_newPasswordAgain, text)) return;
                String registCodeText = registCode.getText().toString();
                if (TextUtils.isEmpty(registCodeText)){
                    Toast.makeText(ChangeUserInfoActivity.this, "请输入验证码", Toast.LENGTH_LONG).show();
                    return;
                }
                String emailText = fp_oldPassword.getEditText().getText().toString();
                String newPassText = fp_newPassword.getEditText().getText().toString();
                String newPassTextAgain = fp_newPasswordAgain.getEditText().getText().toString();

                if (!newPassText.equals(newPassTextAgain)){
                    Toast.makeText(ChangeUserInfoActivity.this, "两次输入的密码不一致", Toast.LENGTH_LONG).show();
                    return;
                }

                String url = getResources().getString(R.string.url_post_forget_pass);
                HashMap<String, String> params = new HashMap<String, String>();
                params.put("email", emailText);
                params.put("password", newPassText);
                params.put("verificationCode", registCodeText);
                NetWorkManager.doPost(url, params, new NewsCallBack() {
                    @Override
                    public void onNewsReturn(String gstring) {
                        Message msg = new Message();
                        msg.what = 0xf93;
                        msg.obj = gstring;
                        handler.sendMessage(msg);
                    }
                });
            }
        });
    }
    public boolean checkNil(TextInputLayout tll, String errMsg){
        String text = tll.getEditText().getText().toString();
        if (TextUtils.isEmpty(text)){
            tll.setError(errMsg);
            return true;
        }else{
            tll.setError("");
            return false;
        }
    }

    private void switchViews(Intent intent) {
        int viewType = intent.getIntExtra(PublicVar.VIEW_NAME, PublicVar.VIEW_DEFAULT);
        changePassworld.setVisibility(View.GONE);
        forgetPassword.setVisibility(View.GONE);
        commentsView.setVisibility(View.GONE);
        pointRuleView.setVisibility(View.GONE);
        cusServiceViwe.setVisibility(View.GONE);
        pointDetailView.setVisibility(View.GONE);
        myCollectView.setVisibility(View.GONE);

        if (viewType == PublicVar.VIEW_CHANGE_PASSWORD){
            changePassworld.setVisibility(View.VISIBLE);

        }else if (viewType == PublicVar.VIEW_FORGET_PASSWORD){
            forgetPassword.setVisibility(View.VISIBLE);

        }else if (viewType == PublicVar.VIEW_CHECK_COMMENTS){
            commentsView.setVisibility(View.VISIBLE);
            curUid = curIntent.getLongExtra(PublicVar.NEWS_ID, 0);
            curPage = 1;
            refreshComments.setRefreshing(true);
            getComments(curUid, curPage);
        }else if (viewType == PublicVar.VIEW_POINTS_RULES){
            pointRuleView.setVisibility(View.VISIBLE);
            String url = getResources().getString(R.string.url_get_points_rules);
            NetWorkManager.doGet(url, new NewsCallBack() {
                @Override
                public void onNewsReturn(String gstring) {
                    Message msg = new Message();
                    msg.what = 0xf94;
                    msg.obj = gstring;
                    handler.sendMessage(msg);
                }
            });
        }else if (viewType == PublicVar.VIEW_CUSTOMER_SERVICE){
            cusServiceViwe.setVisibility(View.VISIBLE);

        }else if (viewType == PublicVar.VIEW_POINTS_DETAIL){
            pointDetailView.setVisibility(View.VISIBLE);
            curPointsDetailPage = 1;
            refreshPointDetailView.setRefreshing(true);
            getPointsDetail(curPointsDetailPage);
        }else if (viewType == PublicVar.VIEW_MY_COLLECT){
            myCollectView.setVisibility(View.VISIBLE);
            curCollectPage = 1;
            refreshMyCollect.setRefreshing(true);
            getCollectDatas(curCollectPage);
        }
    }
}
