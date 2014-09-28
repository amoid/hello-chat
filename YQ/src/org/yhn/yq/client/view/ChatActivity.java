package org.yhn.yq.client.view;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;

import org.yhn.yq.R;
import org.yhn.yq.client.model.ManageClientConServer;
import org.yhn.yq.common.MyTime;
import org.yhn.yq.common.YQMessage;
import org.yhn.yq.common.YQMessageType;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class ChatActivity extends Activity {
	EditText et_input;
	private String chatContent;//消息内容
	ListView chatListView;
	public List<ChatEntity> chatEntityList=new ArrayList<ChatEntity>();//所有聊天内容
	private int myAccount;
	private int chatAccount;
	private String chatNick;
	public static int[] avatar=new int[]{R.drawable.avatar_default,R.drawable.h001,R.drawable.h002,R.drawable.h003,
			R.drawable.h004,R.drawable.h005,R.drawable.h006};
	MyBroadcastReceiver br;
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_chat);
		//设置top面板信息
		int chatAvatar=getIntent().getIntExtra("avatar", 0);
		chatAccount=getIntent().getIntExtra("account", 0);
		chatNick=getIntent().getStringExtra("nick");
		ImageView avatar_iv=(ImageView) findViewById(R.id.chat_top_avatar);
		avatar_iv.setImageResource(avatar[chatAvatar]);
		TextView nick_tv=(TextView) findViewById(R.id.chat_top_nick);
		nick_tv.setText(chatNick);
		
		
		et_input=(EditText) findViewById(R.id.et_input);
		findViewById(R.id.ib_send).setOnClickListener(new OnClickListener(){
			public void onClick(View v) {
				myAccount=MoreActivity.me.getAccount();
				ObjectOutputStream oos;
				try {
					oos = new ObjectOutputStream
					//通过account找到该线程，从而得到OutputStream
					(ManageClientConServer.getClientConServerThread(myAccount).getS().getOutputStream());
					//得到输入的数据，并清空EditText
					chatContent=et_input.getText().toString();
					et_input.setText("");
					//发送消息
					YQMessage m=new YQMessage();
					m.setType(YQMessageType.COM_MES);
					m.setSender(myAccount);
					m.setSenderNick(MoreActivity.me.getNick());
					m.setSenderAvatar(MoreActivity.me.getAvatar());
					m.setReceiver(chatAccount);
					m.setContent(chatContent);
					m.setSendTime(MyTime.geTimeNoS());
					oos.writeObject(m);
					//更新聊天内容
					updateChatView(new ChatEntity(
							MoreActivity.me.getAvatar(),
							chatContent,
				    		MyTime.geTime(),
				    		false));
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		});
		 //注册广播
		IntentFilter myIntentFilter = new IntentFilter(); 
        myIntentFilter.addAction("org.yhn.yq.mes");
        br=new MyBroadcastReceiver();
        registerReceiver(br, myIntentFilter);
		ManageActivity.addActiviy("ChatActivity", this);
	}
	@Override
	public void finish() {
		 unregisterReceiver(br);
		super.finish();
	}
	
	//广播接收器
	public class MyBroadcastReceiver extends BroadcastReceiver {
		@Override
		public void onReceive(Context context, Intent intent) {
			String[] mes = intent.getStringArrayExtra("message");
		    //更新聊天内容
		    updateChatView(new ChatEntity(
		    		Integer.parseInt(mes[2]),
		    		mes[3],
		    		mes[4],
		    		true));
		}
	}
	public void updateChatView(ChatEntity chatEntity){
		chatEntityList.add(chatEntity);
		chatListView=(ListView) findViewById(R.id.lv_chat);
		chatListView.setAdapter(new ChatAdapter(this,chatEntityList));
	}

}
