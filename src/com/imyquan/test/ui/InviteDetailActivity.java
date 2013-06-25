package com.imyquan.test.ui;

import java.util.List;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.imyquan.test.Person;
import com.imyquan.test.R;

public class InviteDetailActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detail);
        TextView tv = (TextView) findViewById(R.id.invitetv);
        Button button = (Button) findViewById(R.id.backbt);
        button.setOnClickListener(new OnClickListener() {
            
            @Override
            public void onClick(View v) {
                InviteDetailActivity.this.finish();
            }
        });
        List list = MainActivity.list;
        int size = list.size();
        Person person;
        StringBuffer strBuffer = new StringBuffer("我刚刚邀请了：");
        for (int i = 0; i < size; i++) {
            person = (Person) list.get(i);
            if (person.isCheck()) {
                strBuffer.append("\n" +person.getName());
            }
        }
        tv.setText(strBuffer.toString());
    }
    
}
