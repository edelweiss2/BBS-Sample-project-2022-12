package db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.DataSource;

import board.Reply;

public class ReplyDao {
	public static Connection getConnection() {
		Connection conn;
		try {
			Context initContext = new InitialContext();
			DataSource ds = (DataSource) initContext.lookup("java:comp/env/jdbc/project");
			conn = ds.getConnection();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		return conn;
	}
	
	public List<Reply> getReplies(int bid) {
		Connection conn = getConnection();
		String sql = "SELECT r.rid, r.content, r.regDate, r.ismine, r.uid, r.bid, u.uname"
				+ "	 FROM reply AS r "
				+ "	 JOIN users AS u "
				+ "	 ON r.uid = u.uid"
				+ "	 WHERE bid=?;";
		List<Reply> list = new ArrayList<>();
		try {
			PreparedStatement pStmt = conn.prepareStatement(sql);
			pStmt.setInt(1, bid);
			
			ResultSet rs = pStmt.executeQuery();
			while (rs.next()) {
				Reply r = new Reply();
				r.setRid(rs.getInt(1));
				r.setContent(rs.getString(2));
				r.setRegDate(LocalDateTime.parse(rs.getString(3).replace(" ", "T")));
				r.setIsMine(rs.getInt(4));
				r.setUid(rs.getString(5));
				r.setBid(rs.getInt(6));
				r.setUname(rs.getString(7));
				list.add(r);				
			}
			rs.close();
			pStmt.close();
			conn.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}


}
