/**
 * ��������ĳ���ͻ��˵�ͨ���߳�
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
				//�Դӿͻ���ȡ�õ���Ϣ���������жϣ�����Ӧ�Ĵ���
				if(m.getType().equals(YQMessageType.COM_MES)){//�������ͨ��Ϣ��
					//ȡ�ý����˵�ͨ���߳�
					ServerConClientThread scc=ManageServerConClient.getClientThread(m.getReceiver());
					ObjectOutputStream oos=new ObjectOutputStream(scc.s.getOutputStream());
					//������˷�����Ϣ
					oos.writeObject(m);
				}else if(m.getType().equals(YQMessageType.GET_ONLINE_FRIENDS)){//�������������б�
					//�������ݿ⣬���ﷵ�ص������е��û��б������ſӣ��Ժ�����Ӻ���
					//��ʱ��������string���ͣ��Ժ���Ը�Ϊjson
					String res=new UserDao().getUser();
					//���ͺ����б��ͻ���
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
