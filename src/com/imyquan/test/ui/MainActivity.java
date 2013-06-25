package com.imyquan.test.ui;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.imyquan.test.Person;
import com.imyquan.test.R;
import com.imyquan.test.R.id;
import com.imyquan.test.adapter.PersonsAdapter;

public class MainActivity extends Activity implements OnItemClickListener, OnClickListener {

	private static final String TAG = "MainActivity";
	public static List<Person> list = new ArrayList<Person>();
	private ListView lv;
	private PersonsAdapter adapter;
	
	private Button finishbt;
	private Button detailbt;
	private Button choosebt;
	private TextView countTv;
	private int count;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		initView();
		count = 0;
		// to get message
		new PersonAsyncTask().execute("http://imyquan.com/test.php");
		
	}
		
	private void initView() {
	    lv = (ListView) findViewById(R.id.list);
        adapter = new PersonsAdapter(list, MainActivity.this);
        lv.setAdapter((ListAdapter) adapter);
        lv.setOnItemClickListener(this);
        
        finishbt = (Button) findViewById(R.id.finishbt);
        detailbt = (Button) findViewById(R.id.detailbt);
        choosebt = (Button) findViewById(R.id.choose_all);
        countTv = (TextView) findViewById(R.id.choose_count);
        
        
        finishbt.setOnClickListener(this);
        detailbt.setOnClickListener(this);
        choosebt.setOnClickListener(this);
    }

    @Override
	public boolean onCreateOptionsMenu(Menu menu) {
		return true;
	}
	
	private class PersonAsyncTask extends AsyncTask<String, Void, List<Person>>{
	    
	    private ProgressDialog dialog;
	    @Override
	    protected void onPreExecute() {
	        dialog = new ProgressDialog(MainActivity.this);
	        dialog.setMessage("Please wait while loading...");
	        dialog.setIndeterminate(true);
	        dialog.setCancelable(true);
	        dialog.show();
	        super.onPreExecute();
	    }
		@Override
		protected List<Person> doInBackground(String... params) {
			String result = post(params[0]);
			try {
				return Person.constructStatuses(result);
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				Log.i(TAG, "has exception " + e.getCause());
				return null;
			}
		}
		
		@Override
		protected void onPostExecute(List<Person> result) {
		    dialog.dismiss();
			if(result == null){
				return;
			}else{
			    list = result;
				adapter.setList(result);
				adapter.notifyDataSetChanged();
			}
		}
	}
	
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Log.i(TAG, "onItemClick  position " +position);
        Person person = list.get(position);
        person.setCheck(!person.isCheck());
        CheckBox cb = (CheckBox) view.findViewById(R.id.checkbox);
        cb.setChecked(person.isCheck());
        
    }
    
    public void dispatchCheckChage(boolean isCheck){
      
        if (isCheck && count < list.size()) {
            count ++;
        }
        if (!isCheck && count > 0) {
            count--;
        }
        
        if (count != 0) {
            countTv.setText(Integer.toString(count));
        }else{
            countTv.setText("");
        }
       
    }
    
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.finishbt:
                this.finish();
                break;
            case R.id.choose_all:
                boolean check = !(count == list.size());
                if (check) {
                    count = list.size();
                }else{
                    count = 0;
                }
                for (int i = 0; i < list.size(); i++) {
                    list.get(i).setCheck(check);
                }
                adapter.notifyDataSetChanged();
                if (count !=0 ) {
                    countTv.setText(Integer.toString(count));
                }else{
                    countTv.setText("");
                }
                break;
            case R.id.detailbt:
                Intent intent = new Intent(MainActivity.this, InviteDetailActivity.class);
                startActivity(intent);
                break;
            default:
                break;
        }
    }
    
    
    
    public static String post(String url) {
        DefaultHttpClient client = new DefaultHttpClient();
        HttpPost post = new HttpPost(url);
        HttpResponse response;
        try {
            response = client.execute(post);
            if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                String result = EntityUtils.toString(response.getEntity(), "UTF-8");
                Log.i(TAG, "result:" + result);
                return result;
            }else
                return "";
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return "";
        }
    }
}
