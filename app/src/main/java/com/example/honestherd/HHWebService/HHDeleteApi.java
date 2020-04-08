package com.example.honestherd.HHWebService;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Build;
import android.util.Log;

import androidx.annotation.RequiresApi;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.example.honestherd.HHGlobal.Utils;

import org.json.JSONObject;

import java.util.Base64;
import java.util.HashMap;

import static android.content.ContentValues.TAG;

public class HHDeleteApi  extends AsyncTask<String, JSONObject, JSONObject> {

    private OnUpdateListener onUpdateListener;
    private Context context;
    private JSONObject jsonObject;
    private int intDialogShow = 0;
    private ProgressDialog progressDialog;
    private String url;
    private HashMap<String, String> hashMap;
    private HashMap<String, String> hashMap_header;

    @RequiresApi(api = Build.VERSION_CODES.O)

    public HHDeleteApi(Context context, String url, OnUpdateListener onUpdateListener) {
        this.context = context;
        this.url = url;
        this.onUpdateListener = onUpdateListener;
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
                    .delete(url)
                    .addHeaders(headr)
                    .setPriority(Priority.HIGH)
                    .build()
                    .getAsJSONObject(new JSONObjectRequestListener() {
                        @Override
                        public void onResponse(JSONObject response) {
                            Log.e("TAG", "onResponse: 1" );
                            if (response != null) {
                                try {
                                    Log.e("TAG", "onResponse: 2" );
                                    onUpdateListener.onUpdateComplete(response,true);
                                    Log.e(TAG, "onResponse: "+response.toString() );

                                } catch (Exception e) {
                                    e.printStackTrace();
                                    Log.e("TAG", "onResponse: 3" );
                                }finally {
                                    if (progressDialog != null) {
                                        progressDialog.dismiss();
                                        Log.e("TAG", "onResponse: 4" );
                                    }
                                }
                            }else {
                                Log.e("TAG", "onResponse: 5" );
                                onUpdateListener.onUpdateComplete(response,false);
                            }

                        }

                        @Override
                        public void onError(ANError error) {
                            if (progressDialog != null) {
                                progressDialog.dismiss();
                            }
                            JSONObject response = new JSONObject();
                            onUpdateListener.onUpdateComplete(response,false);
                            Log.e("TAG", "onResponse: 6"+ error.getErrorCode() );
                            Log.e("TAG", "onResponse: 6"+ error.getErrorDetail() );
                            Log.e("TAG", "onResponse: 6"+ error.getErrorBody() );
//                            Log.e(TAG, "onError: "+error.getErrorBody().toString() );
//                            Log.e(TAG, "onError: "+error.getErrorDetail() );

                        }
                    });

        } catch (Exception e) {
            e.printStackTrace();
            Log.e("TAG", "onResponse: 7" );
        }
        return null;
    }
}

