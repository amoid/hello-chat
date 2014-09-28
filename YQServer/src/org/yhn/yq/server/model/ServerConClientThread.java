/**
 * 服务器和某个客户端的通信线程
 */
package org.yhn.yq.server.model;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.List;

import org.yhn.yq.common.YQMessage;
import org.yhn.yq.common.YQMessageType;
import org.yhn.yq.control.ManageServerConClient;
import org.yhn.yq.server.dao.UserDao;

public class ServerConClientThread extends Thread {
	Socket s;
	public ServerConClientThread(Socket s){
		this.s=s;
	}

	public void run() {
		while(true){
			ObjectInputStream ois = null;
			YQMessage m = null;
			try {
				ois=new ObjectInputStream(s.getInputStream());
				m=(YQMessage) ois.readObject();
				//对从客户端取得的消息进行类型判断，做相应的处理
				if(m.getType().equals(YQMessageType.COM_MES)){//如果是普通消息包
					//取得接收人的通信线程
					ServerConClientThread scc=ManageServerConClient.getClientThread(m.getReceiver());
					ObjectOutputStream oos=new ObjectOutputStream(scc.s.getOutputStream());
					//向接收人发送消息
					oos.writeObject(m);
				}else if(m.getType().equals(YQMessageType.GET_ONLINE_FRIENDS)){//如果是请求好友列表
					//操作数据库，这里返回的是所有的用户列表，先留着坑，以后填，增加好友
					//暂时将结果揉成string类型，以后可以改为json
					String res=new UserDao().getUser();
					//发送好友列表到客户端
					ServerConClientThread scc=ManageServerConClient.getClientThread(m.getSender());
					ObjectOutputStream oos=new ObjectOutputStream(scc.s.getOutputStream());
					YQMessage ms=new YQMessage();
					ms.setType(YQMessageType.RET_ONLINE_FRIENDS);
					ms.setContent(res);
					oos.writeObject(ms);
				}
			} catch (Exception e) {
				e.printStackTrace();
				try {
					s.close();
					ois.close();
				} catch (IOException e1) {
					
				}
			}
		}
	}
}
