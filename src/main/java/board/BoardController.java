package board;

import java.io.IOException;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import db.BoardDao;


/**
 * Servlet implementation class BoardController
 */
@WebServlet({ "/board/list", "/board/search ", "/board/write", "/board/update",
			  "/board/detail", "/board/delete", "/board/deleteConfirm"})
public class BoardController extends HttpServlet {
	
	protected void service(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		request.setCharacterEncoding("utf-8");
		String[] uri = request.getRequestURI().split("/");
		String action = uri[uri.length - 1];
		BoardDao dao = new BoardDao();
		//ReplyDao dao = new ReplyDao();
		HttpSession session = request.getSession();
		session.setAttribute("menu", "board");

		response.setContentType("text/html; charset=utf-8");
		
		RequestDispatcher rd = null;
		
		switch(action) {
		case "list" :
			int page = Integer.parseInt(request.getParameter("page"));
			List<Board> list = dao.listUsers("title", "", page);
			
			request.setAttribute("boardList", list);
			rd = request.getRequestDispatcher("/board/list.jsp");
			rd.forward(request, response);
			break;
			
			
		default :
			System.out.println(request.getMethod() + "잘못된 경로 입니다.");
		}
	}
}
