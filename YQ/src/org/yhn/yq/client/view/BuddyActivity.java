package org.yhn.yq.client.view;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.yhn.yq.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

public class BuddyActivity extends Activity {
	ListView listView;
	public static String buddyStr=""; 

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.activity_buddy);
		listView = (ListView) findViewById(R.id.listview);
        //填充数据
		BuddyAdapter ba=new BuddyAdapter(this,jieXi(buddyStr));
        listView.setAdapter(ba);
        listView.setOnItemClickListener(new OnItemClickListener(){
			public void onItemClick(AdapterView<?> a, View v, int position,long l) {
				BuddyEntity temp= (BuddyEntity) listView.getItemAtPosition(position);
				//打开聊天页面
				Intent intent=new Intent(BuddyActivity.this,ChatActivity.class);
				intent.putExtra("avatar", temp.getAvatar());
				intent.putExtra("account",temp.getAccount());
				intent.putExtra("nick", temp.getNick());
				startActivity(intent);
			}
        });
	}
	
	private List<BuddyEntity> jieXi(String s){
		List<BuddyEntity> buddyEntityList = new ArrayList<BuddyEntity>();
        String ss[] = buddyStr.split(" ");
        for(String a: ss){
        	if(a!=""){
	        	String b[]=a.split("_");
	            buddyEntityList.add(new BuddyEntity(Integer.parseInt(b[2]), Integer.parseInt(b[0]), b[1], b[3]));
        	}
        }
		return buddyEntityList;
	}
}
