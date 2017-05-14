package spencer.cn.finalproject.acview;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.TextInputLayout;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.HashMap;

import spencer.cn.finalproject.R;
import spencer.cn.finalproject.application.BaseApplication;
import spencer.cn.finalproject.dojo.GsonNews;
import spencer.cn.finalproject.dojo.LoginBean;
import spencer.cn.finalproject.iexport.NewsCallBack;
import spencer.cn.finalproject.manager.NetWorkManager;
import spencer.cn.finalproject.util.PublicVar;

public class LoginActivity extends BaseActionBarActivity {
    private int curStatus;
    private ViewGroup loginLayout;
    private ViewGroup registLayout;
    Gson parser = new GsonBuilder().serializeNulls().create();
    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 120) {
                String gsonStrings = (String) msg.obj;
                GsonNews registResp = parser.fromJson(gsonStrings, GsonNews.class);
                Toast.makeText(LoginActivity.this, registResp.getMessage(), Toast.LENGTH_LONG).show();
            }else if (msg.what == 0xffb){
                String registStrings = (String) msg.obj;
                GsonNews registObj = parser.fromJson(registStrings, GsonNews.class);
                Toast.makeText(LoginActivity.this, registObj.getMessage(), Toast.LENGTH_LONG).show();
                if (registObj.getCode() == 200){
                    changeStatus(PublicVar.LOGIN_STATUS);
                }
            }else if (msg.what == 0xffc) {
                String loginStrings = (String) msg.obj;
                LoginBean loginBean = parser.fromJson(loginStrings, LoginBean.class);
                if (loginBean.getCode() == 200) {
                    BaseApplication application = (BaseApplication) getApplication();
                    application.setLoginBean(loginBean);
                    storeUserInfo(loginBean);
//                    新处理
                    BaseApplication.setConfig(loginBean.getData().getUserConfig());
                    BaseApplication.setInfo(loginBean.getData().getUser());
                    SharedPreferences settings = getSharedPreferences(PublicVar.SHARED_FILE, MODE_PRIVATE);
                    SharedPreferences.Editor editor = settings.edit();
                    editor.putString(PublicVar.ACCESSTOKEN, loginBean.getData().getAccessToken());
                    editor.commit();
                    finish();
                } else {
                    Toast.makeText(LoginActivity.this, "登陆超时", Toast.LENGTH_LONG).show();
                }
            }
        }
    };
    /////////////////////////////////////////////////登陆
    private TextInputLayout login_email;
    private TextInputLayout login_pass;
    private Button login;
    private Button regist;


    //////////////////////////////////////////////////注册
    private TextInputLayout email;
    private TextInputLayout passward;
    private TextInputLayout passwordagain;
    private TextInputLayout nickname;
    private TextInputLayout inviteCode;
    private EditText registerCode;
    private Button getRegistCode;
    private Button regist_ok;
    private Button regist_cancel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_login_register);
        loginLayout = (ViewGroup) findViewById(R.id.layout_login);
        registLayout = (ViewGroup) findViewById(R.id.layout_regist);
        initLoginViews();
        initRegistViews();
    }

    @Override
    protected void onStart() {
        super.onStart();
        curStatus = PublicVar.LOGIN_STATUS;
        changeStatus(PublicVar.LOGIN_STATUS);
    }
    private void changeStatus(int status){
        if (status == PublicVar.LOGIN_STATUS){
            loginLayout.setVisibility(ViewGroup.VISIBLE);
            registLayout.setVisibility(ViewGroup.GONE);
        }else{
            loginLayout.setVisibility(ViewGroup.GONE);
            registLayout.setVisibility(ViewGroup.VISIBLE);
        }
    }
    private void storeUserInfo(LoginBean loginBean){
        SharedPreferences settings = getSharedPreferences(PublicVar.SHARED_FILE, MODE_PRIVATE);
        SharedPreferences.Editor editor = settings.edit();
        editor.putString(PublicVar.ACCESSTOKEN, loginBean.getData().getAccessToken());
        editor.commit();
    }
    private void initLoginViews() {
        login_email = (TextInputLayout) findViewById(R.id.login_mail);
        login_pass = (TextInputLayout) findViewById(R.id.login_password);
        login = (Button) findViewById(R.id.email_login_in_button);
        regist = (Button) findViewById(R.id.email_sign_in_button);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String errMsg = "不能为空";
                if (checkText(login_email, errMsg)) return;
                if (checkText(login_pass, errMsg)) return;
                String url = getResources().getString(R.string.url_post_login);
                HashMap<String, String> params = new HashMap<String, String>();
                params.put("deviceId", "1");
                params.put("email", login_email.getEditText().getText().toString());
                String pass = login_pass.getEditText().getText().toString();
                params.put("password", login_pass.getEditText().getText().toString());
                NetWorkManager.doPost(url, params, new NewsCallBack() {
                    @Override
                    public void onNewsReturn(String gstring) {
                        Message msg = new Message();
                        msg.what = 0xffc;
                        msg.obj = gstring;
                        handler.sendMessage(msg);
                    }
                });
            }
        });

        regist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                curStatus = PublicVar.REGIST_STATUS;
                changeStatus(PublicVar.REGIST_STATUS);
            }
        });
    }

    private void initRegistViews(){
        email = (TextInputLayout) findViewById(R.id.sign_in_email);
        passward = (TextInputLayout) findViewById(R.id.sign_in_password);
        passwordagain = (TextInputLayout) findViewById(R.id.sign_in_password_again);
        nickname = (TextInputLayout) findViewById(R.id.sign_in_nick_name);
        inviteCode = (TextInputLayout) findViewById(R.id.sign_in_request_code);
        registerCode = (EditText) findViewById(R.id.edt_regist_code);
        getRegistCode = (Button) findViewById(R.id.btn_getRegistCode);
        regist_ok = (Button) findViewById(R.id.email_sign_in_ok);
        regist_cancel = (Button) findViewById(R.id.email_sign_in_back);

        regist_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkNull()) return;
                if (checkPassword() ) return;
                String url = getResources().getString(R.string.url_post_regist);
                HashMap<String, String> params = new HashMap<String, String>();
                params.put("email", email.getEditText().getText().toString());
                params.put("password", passward.getEditText().getText().toString());
                params.put("username", nickname.getEditText().getText().toString());
                params.put("verificationCode", registerCode.getText().toString());
                String iEmail = inviteCode.getEditText().getText().toString();
                if (!TextUtils.isEmpty(iEmail)){
                    params.put("refEmail", iEmail);
                }
                NetWorkManager.doPost(url, params, new NewsCallBack() {
                    @Override
                    public void onNewsReturn(String gstring) {
                        Message msg = new Message();
                        msg.what = 0xffb;
                        msg.obj = gstring;
                        handler.sendMessage(msg);
                    }
                });
            }
        });
        regist_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        getRegistCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String emailTxt = email.getEditText().getText().toString();
                if (!TextUtils.isEmpty(emailTxt)){
                    String url = getResources().getString(R.string.url_get_regist_code);
                    HashMap<String, String> params = new HashMap<String, String>();
                    params.put("codeType", "1");
                    params.put("email", emailTxt);
                    NetWorkManager.doPost(url, params, new NewsCallBack(){

                        @Override
                        public void onNewsReturn(String gstring) {
                            Message msg = new Message();
                            msg.what = 120;
                            msg.obj = gstring;
                            handler.sendMessage(msg);
                        }
                    });
                }else {
                    Toast.makeText(LoginActivity.this, "邮箱不能为空", Toast.LENGTH_LONG).show();
                }
            }
        });
    }
    public boolean checkNull(){
        String txtregisterCode = registerCode.getText().toString();
        String errMsg = "不能为空";
        if ( checkText(email, errMsg)) return true;
        if ( checkText(passward, errMsg)) return true;
        if ( checkText(passwordagain, errMsg)) return true;
        if ( checkText(nickname, errMsg)) return true;
        if (TextUtils.isEmpty(txtregisterCode)){
            Toast.makeText(this, errMsg, Toast.LENGTH_LONG).show();
            return true;
        }
        return false;

    }
    public boolean checkText(TextInputLayout til, String msg){
        String text = til.getEditText().getText().toString();
        if (TextUtils.isEmpty(text)){
            til.setError(msg);
            return true;
        }else {
            til.setError("");
            return false;
        }
    }
    public boolean checkPassword(){
        String _pass = passward.getEditText().getText().toString();
        String _pass2 = passwordagain.getEditText().getText().toString();
        if (!_pass.equals(_pass2) || TextUtils.isEmpty(_pass) || TextUtils.isEmpty(_pass2)){
            passward.setError("两次密码不一致");
            return true;
        }else{
            passward.setError("");
            return  false;
        }
    }

    @Override
    public void finish() {
        if (curStatus == PublicVar.REGIST_STATUS){
            curStatus = PublicVar.LOGIN_STATUS;
            changeStatus(PublicVar.LOGIN_STATUS);
        }else{
            super.finish();
        }
    }
}
