package com.wallo.roulette_android;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.Bundle;
import android.content.Intent;
import android.util.Base64;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.igaworks.IgawCommon;
import com.kakao.auth.Session;
import com.kakao.auth.KakaoSDK;

import com.kakao.auth.ISessionCallback;
import com.kakao.util.exception.KakaoException;
import com.kakao.util.helper.log.Logger;
import com.wallo.roulette_android.common.BaseActivity;
import com.wallo.roulette_android.network.CustomRequest;
import com.wallo.roulette_android.network.MyVolley;

import org.json.JSONObject;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;

import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.kakao.util.helper.Utility.getPackageInfo;

/**
 * Created by Administrator on 2016-11-16.
 */

public class SplashActivity extends BaseActivity {
    private SessionCallback callback;
    public String server_url = "";

    @OnClick (R.id.btn_network_test) void networkTest(){
        Log.d("network", "test");
        final RequestQueue queue = MyVolley.getInstance(this).getRequestQueue();
        Map<String, String> params = new HashMap<String, String>();
        params.put("os","android");
        CustomRequest myReq = new CustomRequest(Request.Method.POST, server_url, params, networkSuccessListener(), networkErrorListener());
        queue.add(myReq);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getKeyHash(this);
        callback = new SessionCallback();
        Session.getCurrentSession().addCallback(callback);

        if (!Session.getCurrentSession().checkAndImplicitOpen()) {
            setContentView(R.layout.activity_splash);
        } else {
            setContentView(R.layout.activity_splash);
        }

        IgawCommon.startApplication(SplashActivity.this);
        ButterKnife.bind(this);

    }

    public static String getKeyHash(final Context context) {
        PackageInfo packageInfo = getPackageInfo(context, PackageManager.GET_SIGNATURES);
        if (packageInfo == null)
            return null;

        for (Signature signature : packageInfo.signatures) {
            try {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.d("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT));
                return android.util.Base64.encodeToString(md.digest(), android.util.Base64.NO_WRAP);
            } catch (NoSuchAlgorithmException e) {
                Log.d("hash", "Unable to get MessageDigest. signature=" + signature, e);
            }
        }
        return null;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (Session.getCurrentSession().handleActivityResult(requestCode, resultCode, data)) {
            return;
        }

        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Session.getCurrentSession().removeCallback(callback);
    }

    private class SessionCallback implements ISessionCallback {

        @Override
        public void onSessionOpened() {
            Log.d("kakao", "onSessionOpened");
//            Intent intent = new Intent(SplashActivity.this, MainActivity.class);
//            startActivity(intent);
//            finish();
        }

        @Override
        public void onSessionOpenFailed(KakaoException exception) {
            if(exception != null) {
                Logger.e(exception);
            }

            setContentView(R.layout.activity_splash);
        }
    }

    private Response.Listener<JSONObject> networkSuccessListener(){
        return new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.d("network", response.toString());
            }
        };
    }

    private Response.ErrorListener networkErrorListener(){
        return new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("network", error.toString());
            }
        };
    }
}
