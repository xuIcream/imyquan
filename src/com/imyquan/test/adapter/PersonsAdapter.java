package com.imyquan.test.adapter;

import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.AsyncTask;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.TextView;

import com.imyquan.test.Person;
import com.imyquan.test.R;
import com.imyquan.test.ui.MainActivity;
import com.imyquan.test.view.ImageItemView;

public class PersonsAdapter extends BaseAdapter {


	private static final String TAG = "PersonsAdapter";
	private List<Person> list = new ArrayList<Person>();
	private MainActivity mainActivity;
	


	public PersonsAdapter(List<Person> list, Context context) {
		this.list = list;
		this.mainActivity = (MainActivity) context;
	}

	@Override
	public int getCount() {
		return list.size();
	}

	public void setList(List<Person> list) {
		this.list = list;
	}

	@Override
	public Object getItem(int position) {
		return list.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder viewHolder = null;
		if (null == convertView) {
			convertView = LayoutInflater.from(mainActivity).inflate(R.layout.list_item, null);
			viewHolder = new ViewHolder();
			viewHolder.name = (TextView) convertView.findViewById(R.id.name);
			viewHolder.image = (ImageItemView) convertView.findViewById(R.id.image);
			viewHolder.cb = (CheckBox) convertView.findViewById(R.id.checkbox);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		final Person person = list.get(position);
		final String uavatar_url = person.getAvatar_url();
		viewHolder.name.setText(person.getName());
		viewHolder.image.setImageName(uavatar_url);
		viewHolder.cb.setChecked(person.isCheck());
		final int location = position;
		viewHolder.cb.setOnCheckedChangeListener(new OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                person.setCheck(isChecked);
                mainActivity.dispatchCheckChage(isChecked);
            }
        });
		
		if (ImageItemView.getBitmap(uavatar_url) != null) {
			Log.i(TAG, "get bitmap from  lruCache" + uavatar_url);
			viewHolder.image.setImageBitmap(ImageItemView.getBitmap(uavatar_url));
		} else {
			Log.i(TAG, "to download the bitmap"  + uavatar_url);
			viewHolder.image.toDownLoad(uavatar_url);
		}
		convertView.setTag(viewHolder);
		if (position%2 == 0) {
		    convertView.setBackgroundColor(Color.CYAN);
        }else{
            convertView.setBackgroundColor(Color.WHITE);
        }
		return convertView;
	}

	private final class ViewHolder {
	    int position;
	    TextView name;
		ImageItemView image;
		CheckBox cb;
	}

}

