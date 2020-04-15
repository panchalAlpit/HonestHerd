package com.example.honestherd.HHWebService;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Build;
import android.provider.Settings;
import android.util.Log;

import androidx.annotation.RequiresApi;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.example.honestherd.HHGlobal.Utils;
import com.example.honestherd.R;

import org.json.JSONObject;

import java.util.Base64;
import java.util.HashMap;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import static android.content.ContentValues.TAG;

public class HHApiCall  extends AsyncTask<String, JSONObject, JSONObject> {

    private OnUpdateListener onUpdateListener;
    private Context context;
    private JSONObject jsonObject;
    private int intDialogShow = 0;
    private ProgressDialog progressDialog;
    private String url;
    private HashMap<String, String> hashMap;
    private HashMap<String, String> hashMap_header;

    @RequiresApi(api = Build.VERSION_CODES.O)

    public HHApiCall(Context context, String url, OnUpdateListener onUpdateListener) {
        this.context = context;
        this.url = url;
        this.onUpdateListener = onUpdateListener;

        Log.e("CheckURl", "HHApiCall: "+url );
    }
    @Override
    protected void onPreExecute() {
       /* if (!ConnectivityDetector.isConnectingToInternet(context)) {
            AlertDialogUtility.showInternetAlert(context);
            return;
        }*/
//        if (intDialogShow == 1)
        {

            progressDialog = new ProgressDialog(context);
            progressDialog.setCancelable(true);
            progressDialog.setMessage("Please Wait..");
            progressDialog.setTitle("");
            progressDialog.show();
        }
        super.onPreExecute();
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected JSONObject doInBackground(String... strings) {
        try {

            String authString = "Basic " +
                    Base64.getEncoder().encodeToString(
                            String.format("%s:%s", Utils.Account_id,Utils.SecretKey)
                                    .getBytes()
                    );

            HashMap<String,String> headr = new HashMap<>();
            headr.put("Authorization",authString);

            AndroidNetworking
                    .get(url)
                    .addHeaders(headr)
                    .setPriority(Priority.HIGH)
                    .build()
                    .getAsJSONObject(new JSONObjectRequestListener() {
                        @Override
                        public void onResponse(JSONObject response) {

                            if (response != null) {
                                try {
                                     onUpdateListener.onUpdateComplete(response,true);
                                    Log.e(TAG, "onResponse: "+response.toString() );

                                } catch (Exception e) {
                                    e.printStackTrace();
                                }finally {
                                    if (progressDialog != null) {
                                        progressDialog.dismiss();
                                    }
                                }
                            }else {
                                onUpdateListener.onUpdateComplete(response,false);
                            }

                        }

                        @Override
                        public void onError(ANError error) {
                            if (progressDialog != null) {
                                progressDialog.dismiss();
                            }
                            Log.e(TAG, "onError: "+error.getErrorBody().toString() );
                            Log.e(TAG, "onError: "+error.getErrorDetail() );

                        }
                    });

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
