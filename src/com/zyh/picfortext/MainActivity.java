package com.zyh.picfortext;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.android.volley.RequestQueue;
import com.android.volley.Response.Listener;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

public class MainActivity extends FragmentActivity {

	private EditText et;
	private ViewPager vp;
	private List<String> imgUrls;
	private MyPagerAdapter adapter;
	private RequestQueue requestQueue;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		et = (EditText) findViewById(R.id.et);
		vp = (ViewPager) findViewById(R.id.vp);
		imgUrls = new ArrayList<String>();
		adapter = new MyPagerAdapter(getSupportFragmentManager());
		vp.setAdapter(adapter);

		requestQueue = Volley.newRequestQueue(this);
	}

	public void search(View v) {
		String keyword = et.getText().toString().trim();
		try {
			// 注意进行编码
			String key = URLEncoder.encode(keyword, "utf-8");
			String url = "http://api.myhug.cn/se/pic?content=" + key
					+ "&from=sdktbandroid";
			Log.d("", url);
			JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(url,
					null, new Listener<JSONObject>() {
						@Override
						public void onResponse(JSONObject response) {
							Log.d("", response.toString());
							parseJson(response);
						}
					}, null);
			requestQueue.add(jsonObjectRequest);
			
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
	}

	/*
	 * 
	 * { "error": {"errno":200, "errmsg":"SUCC", "usermsg":"" }, "picList": {
	 * "picNum":9, "hasMore":0, "badCase":0, "picUrl": [
	 * "http:\/\/pub.myhug.cn\/spic\/%E4%B9%88%E4%B9%88%E5%93%92\/1392746346002.jpg",
	 * "http:\/\/pub.myhug.cn\/spic\/%E4%B9%88%E4%B9%88%E5%93%92\/1392746346003.jpg",
	 * "http:\/\/pu.myhug.cn\/spic\/%E4%B9%88%E4%B9%88%E5%93%92\/1393847337017.jpg",
	 * "http:\/\/puc.myhug.cn\/spic\/%E4%B9%88%E4%B9%88%E5%93%92\/1393847337022.jpg",
	 * "http:\/\/pub.myhug.cn\/spic\/%E4%B9%88%E4%B9%88%E5%93%92\/1393847337007.jpg",
	 * "http:\/\/pud.myhug.cn\/rpic\/r008\/521.jpeg",
	 * "http:\/\/pub.myhug.cn\/rpic\/r001\/088.jpg",
	 * "http:\/\/pu.myhug.cn\/rpic\/r002\/169.jpg",
	 * "http:\/\/pud.myhug.cn\/rpic\/r012\/215.jpg" ] }, "showText":"" }
	 */
	protected void parseJson(JSONObject json) {
		try {
			JSONObject picList = json.getJSONObject("picList");
			JSONArray picUrl = picList.getJSONArray("picUrl");
			int len = picUrl.length();
			imgUrls.clear();
			for (int i = 0; i < len; i++) {
				imgUrls.add(picUrl.getString(i));
			}
			Log.d("", "图片数量：" + imgUrls.size());
			adapter.notifyDataSetChanged();
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	class MyPagerAdapter extends FragmentPagerAdapter {

		public MyPagerAdapter(FragmentManager fm) {
			super(fm);
		}

		@Override
		public Fragment getItem(int arg0) {
			PicTextFragment f = new PicTextFragment();
			Log.d("", "创建Fragment：" + imgUrls.get(arg0));
			Bundle bundle = new Bundle();
			bundle.putString("imgUrl", imgUrls.get(arg0));
			f.setArguments(bundle);
			return f;
		}

		@Override
		public int getCount() {
			return imgUrls.size();
		}

	}

}
