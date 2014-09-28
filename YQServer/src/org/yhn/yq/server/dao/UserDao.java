package org.yhn.yq.server.dao;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import org.yhn.yq.common.User;

public class UserDao {
	public boolean login(int account, String password) {
		try {
			String sql = "select * from yq_user where uaccount=? and upassword=?";
			Connection conn = DBUtil.getDBUtil().getConnection();
			PreparedStatement ps = conn.prepareStatement(sql);
			ps.setInt(1, account);
			ps.setString(2, password);
			ResultSet rs = ps.executeQuery();
			if (rs != null && rs.next() == true) {
				return true;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}
	
	public boolean register(User u) {
		try {
			String sql = "insert into yq_user values(?,?,?,?,?,?,?,?,?)";
			Connection conn = DBUtil.getDBUtil().getConnection();
			PreparedStatement ps = conn.prepareStatement(sql);
			ps.setInt(1, u.getAccount());
			ps.setString(2, u.getPassword());
			ps.setString(3, u.getNick());
			ps.setInt(4, u.getAvatar());
			ps.setString(5, u.getTrends());
			ps.setString(6, u.getSex());
			ps.setInt(7, u.getAge());
			ps.setInt(8, u.getLev());
			ps.setString(9, u.getTime());
			int r = ps.executeUpdate();
			if (r > 0) {
				return true;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}
	
	public String getUser(){
		String res="";
		try {
			String sql = "select * from yq_user";
			Connection conn = DBUtil.getDBUtil().getConnection();
			PreparedStatement ps = conn.prepareStatement(sql);
			ResultSet rs = ps.executeQuery();
			while(rs.next()){
				res=res+rs.getInt("uaccount")+"_"+rs.getString("unick")+"_"
						+rs.getString("uavatar")+"_"+rs.getString("utrends")+" ";
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return res;
	}
	public String getUser(int account){
		String res="";
		try {
			String sql = "select * from yq_user where uaccount="+account;
			Connection conn = DBUtil.getDBUtil().getConnection();
			PreparedStatement ps = conn.prepareStatement(sql);
			ResultSet rs = ps.executeQuery();
			while(rs.next()){
				res=res+rs.getInt("uaccount")+"_"+rs.getString("unick")+"_"
						+rs.getString("uavatar")+"_"+rs.getString("utrends")+"_"
						+rs.getString("usex")+"_"+rs.getInt("uage")+"_"+rs.getInt("ulev");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return res;
	}
	
}