package com.milfrost.frek.modul.loginPage;

import android.app.ProgressDialog;
import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.milfrost.frek.R;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class LoginPage extends AppCompatActivity implements LoginInterface.View {

    public static final int ERROR_EMAIL_PASSWORD = 1;
    public static final int ERROR_EMAIL = 2;
    public static final int ERROR_PASSWORD = 3;

    LoginPresenter loginPresenter;

    EditText emailText;
    EditText passwordText;

    TextView loginBtn;
    TextView registerText;
    TextView forgotPwdText;
    TextView errorMessage;

    ProgressDialog progressDialog;

    boolean error = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_page);
        initViews();
        initObjects();
        setEvents();
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    private void initObjects(){
        progressDialog = new ProgressDialog(LoginPage.this);
        progressDialog.setMessage(getString(R.string.communicating_w_server));
        loginPresenter = new LoginPresenter(LoginPage.this);
        loginPresenter.setViewInterface(this);
    }

    private void initViews(){
        emailText = (EditText) findViewById(R.id.login_username);
        passwordText = (EditText) findViewById(R.id.login_password);

        loginBtn = (TextView)findViewById(R.id.login_login_btn);
        registerText = (TextView)findViewById(R.id.login_register);
        forgotPwdText = (TextView)findViewById(R.id.login_forgot_pwd);
        errorMessage = (TextView)findViewById(R.id.login_error_msg);
        errorMessage.setVisibility(View.GONE);
    }

    private void setEvents(){
        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loginPresenter.login(emailText.getText().toString(),passwordText.getText().toString());
            }
        });

        forgotPwdText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loginPresenter.openForgotPasswordPage();
            }
        });

        registerText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loginPresenter.openRegisterPage();
            }
        });

        View.OnTouchListener fieldTouch = new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if(error)
                    dismissErrorMessage();
                return false;
            }
        };

        emailText.setOnTouchListener(fieldTouch);
        passwordText.setOnTouchListener(fieldTouch);
    }

    private void dismissErrorMessage(){
        emailText.setTextColor(getResources().getColor(R.color.white));
        passwordText.setTextColor(getResources().getColor(R.color.white));
        errorMessage.setVisibility(View.GONE);
        error = false;
    }


    @Override
    public void showError(int code,String message) {
        switch (code){
            case ERROR_EMAIL_PASSWORD:
                emailText.setTextColor(getResources().getColor(R.color.red));
                passwordText.setTextColor(getResources().getColor(R.color.red));
                break;
            case ERROR_EMAIL:
                emailText.setTextColor(getResources().getColor(R.color.red));
                break;
            case ERROR_PASSWORD:
                passwordText.setTextColor(getResources().getColor(R.color.red));
                break;
        }

        errorMessage.setText(message);
        errorMessage.setVisibility(View.VISIBLE);
        error = true;
    }

    @Override
    public void showProgressDialog() {
        progressDialog.show();
    }

    @Override
    public void dismissProgressDialog(){
        progressDialog.dismiss();
    }


    @Override
    public void showErrorMessageDialog(String message) {}

    @Override
    public void finishActivity() {
        finish();
    }
}
