package com.example.loginsocial;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;

public class MainActivity extends AppCompatActivity {
    ImageView profilePictureView;
    LoginButton login_button;
    Button btLogOut, btChucNang;
    TextView txtName, txtEmail;

    CallbackManager callbackManager;

    String email, name, first_name, id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        callbackManager = CallbackManager.Factory.create();
        Anhxa();
//        FacebookSdk.sdkInitialize(getApplicationContext());
//        AppEventsLogger.activateApp(this);


        btLogOut.setVisibility(View.INVISIBLE);
        txtName.setVisibility(View.INVISIBLE);
        txtEmail.setVisibility(View.INVISIBLE);


        //xin quyền
        login_button.setReadPermissions(Arrays.asList("public_profile", "email"));
        setLogin_Button();
    }

    //hàm đăng nhập
    private void setLogin_Button() {
        login_button.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                login_button.setVisibility(View.INVISIBLE);
                btLogOut.setVisibility(View.VISIBLE);
                txtName.setVisibility(View.VISIBLE);
                txtEmail.setVisibility(View.VISIBLE);
                result();
            }

            @Override
            public void onCancel() {

            }

            @Override
            public void onError(FacebookException error) {

            }
        });
    }

    //hàm lấy dữ liệu
    private void result() {
        //chứng thực đăng nhập facebook
        GraphRequest graphRequest = GraphRequest.newMeRequest(AccessToken.getCurrentAccessToken(), new GraphRequest.GraphJSONObjectCallback() {
            @Override
            public void onCompleted(JSONObject object, GraphResponse response) {
                Log.d("Joson", response.getJSONObject().toString());

                try {
                    email = object.getString("email");
                    name = object.getString("name");
                    first_name = object.getString("first_name");
                    id = object.getString("id");

                    //lấy ảnh
                    String url = "https://graph.facebook.com/" + id + "/picture?type=large";
                    Picasso.with(MainActivity.this)
                            .load(url)
                            .into(profilePictureView);

                    //gán
                    txtEmail.setText(email);
                    txtName.setText(name);


                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
        Bundle parameters = new Bundle();
        parameters.putString("fields", "name, email, first_name");
        graphRequest.setParameters(parameters);
        graphRequest.executeAsync();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

    //khi run app sẽ về trạng thái chưa đăng nhập


    @Override
    protected void onStart() {
        LoginManager.getInstance().logOut();
        super.onStart();
    }

    public void Anhxa() {
        profilePictureView = findViewById(R.id.imageView);
        login_button = findViewById(R.id.login_button);
        btLogOut = findViewById(R.id.btnLogout);
        txtName = findViewById(R.id.txtName);

        txtEmail = findViewById(R.id.txtEmail);
    }

/*
    private void printKeyHash() {
        try {
            PackageInfo info = getPackageManager().getPackageInfo("com.example.loginsocial", PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.e("KeyHash", Base64.encodeToString(md.digest(), Base64.DEFAULT));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }*/
}
