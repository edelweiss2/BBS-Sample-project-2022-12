package db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.DataSource;

import board.Board;

public class BoardDao {
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
	
	public List<Board> listUsers(String field, String query, int page) {
		Connection conn = getConnection();
		int offset = (page - 1) * 10;
		String sql = "SELECT b.bid, b.uid, b.title, b.modtime, "
				+ "	b.viewCount, b.replyCount, u.uname FROM board AS b"
				+ "	JOIN users AS u "
				+ "	ON b.uid = u.uid"
				+ "	WHERE b.isDeleted=0 AND " + field + " LIKE ?"
				+ "	ORDER BY bid DESC"
				+ "	LIMIT 10 OFFSET ?;";
		List<Board> list = new ArrayList<>();
		try {
			PreparedStatement pStmt = conn.prepareStatement(sql);
			pStmt.setString(1, "%"+query+"%");
			pStmt.setInt(2, offset);
			
			ResultSet rs = pStmt.executeQuery();
			while (rs.next()) {
				Board b = new Board();
				b.setBid(rs.getInt(1));
				b.setUid(rs.getString(2));
				b.setTitle(rs.getString(3));
				//2022-12-20 14:09:54 ==> 2022-12-20T14:09:54
				b.setModTime(LocalDateTime.parse(rs.getString(4).replace(" ", "T")));
				b.setViewCount(rs.getInt(5));
				b.setReplyCount(rs.getInt(6));
				b.setUname(rs.getString(7));
				list.add(b);
				
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
