package com.mobilefirst.honestherd.HHWebService;

import org.json.JSONObject;

public interface OnUpdateListener {


	void onUpdateComplete(JSONObject jsonObject, boolean isSuccess);
}
