package com.zyh.picfortext;

import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.android.volley.RequestQueue;
import com.android.volley.Response.Listener;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.Volley;

public class PicTextFragment extends Fragment {

	private ImageView iv;
	private RequestQueue requestQueue;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.fragment_pictext, container, false);
		init(v);
		return v;
	}

	private void init(View v) {
		String url = getArguments().getString("imgUrl");
		iv = (ImageView) v.findViewById(R.id.iv);

		requestQueue = Volley.newRequestQueue(getActivity());

		ImageRequest imageRequest = new ImageRequest(url,
				new Listener<Bitmap>() {
					@Override
					public void onResponse(Bitmap response) {
						iv.setImageBitmap(response);
					}
				}, 480, 800, Config.ARGB_8888, null);

		requestQueue.add(imageRequest);
	}
}
