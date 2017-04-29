package spencer.cn.finalproject.acview;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.TextInputLayout;
import android.support.v4.widget.SwipeRefreshLayout;
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

import java.util.HashMap;

import spencer.cn.finalproject.R;
import spencer.cn.finalproject.dojo.GsonNews;
import spencer.cn.finalproject.dojo.UploadImgResp;
import spencer.cn.finalproject.iexport.NewsCallBack;
import spencer.cn.finalproject.manager.LocalDataManager;
import spencer.cn.finalproject.manager.NetWorkManager;
import spencer.cn.finalproject.util.PublicVar;

public class ChangeUserInfoActivity extends BaseActionBarActivity {
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

    ViewGroup pointRuleView;
    TextView pointRules;

    ViewGroup cusServiceViwe;
    TextView cusService;

    Gson userparser = new GsonBuilder().serializeNulls().create();
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
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_user_info);

        initViews();
        switchViews(getIntent());

    }

    private void initViews() {
        changePassworld = (ViewGroup) findViewById(R.id.view_change_pass);
        forgetPassword = (ViewGroup) findViewById(R.id.view_forget_pass);
        commentsView = (ViewGroup) findViewById(R.id.view_commonts);
        pointRuleView = (ViewGroup) findViewById(R.id.view_points_rules);
        cusServiceViwe = (ViewGroup) findViewById(R.id.view_cus_service);

        initChangePasswordViews();
        initForgetPasswordViews();
        initCommontsViews();
        initPointsRulesViews();
        initCusServiceViews();

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
    }

    private void initCommontsViews() {
        refreshComments = (SwipeRefreshLayout) findViewById(R.id.srl_refesh_commonts);
        commentsContent = (RecyclerView) findViewById(R.id.rv_content_comments);
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

        if (viewType == PublicVar.VIEW_CHANGE_PASSWORD){
            changePassworld.setVisibility(View.VISIBLE);

        }else if (viewType == PublicVar.VIEW_FORGET_PASSWORD){
            forgetPassword.setVisibility(View.VISIBLE);

        }else if (viewType == PublicVar.VIEW_CHECK_COMMENTS){
            commentsView.setVisibility(View.VISIBLE);
        }else if (viewType == PublicVar.VIEW_POINTS_RULES){
            pointRuleView.setVisibility(View.VISIBLE);

        }else if (viewType == PublicVar.VIEW_CUSTOMER_SERVICE){
            cusServiceViwe.setVisibility(View.VISIBLE);
        }
    }
}
