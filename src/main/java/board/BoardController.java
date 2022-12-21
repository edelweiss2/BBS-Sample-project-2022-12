package board;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import db.BoardDao;
import db.ReplyDao;

/**
 * Servlet implementation class BoardController
 */
@WebServlet({ "/board/list", "/board/search", "/board/write", "/board/update",
			  "/board/detail", "/board/delete", "/board/deleteConfirm",
			  "/board/reply" })
public class BoardController extends HttpServlet {

	protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.setCharacterEncoding("utf-8");
		String[] uri = request.getRequestURI().split("/");
		String action = uri[uri.length - 1];
		BoardDao dao = new BoardDao();
		ReplyDao rdao = new ReplyDao();
		HttpSession session = request.getSession();
		String sessionUid = (String) session.getAttribute("uid");
		session.setAttribute("menu", "board");
		
		response.setCharacterEncoding("utf-8");
		response.setContentType("text/html; charset=utf-8");
		String title = null, content = null, files = null, uid = null;
		Board board = null;
		RequestDispatcher rd = null;
		
		switch(action) {
		case "list":
			int page = Integer.parseInt(request.getParameter("page"));
			List<Board> list = dao.listBoard("title", "", page);
			
			session.setAttribute("currentBoardPage", page);
			int totalBoardNo = dao.getBoardCount();
			int totalPages = (int) Math.ceil(totalBoardNo / 10.);
			List<String> pageList = new ArrayList<>();
			for (int i = 1; i <= totalPages; i++)
				pageList.add(String.valueOf(i));
			request.setAttribute("pageList", pageList);
			
			String today = LocalDate.now().toString();		// 2022-12-20
			request.setAttribute("today", today);
			request.setAttribute("boardList", list);
			rd = request.getRequestDispatcher("/board/list.jsp");
			rd.forward(request, response);
			break;
			
		case "detail":
			uid = request.getParameter("uid");
			int bid = Integer.parseInt(request.getParameter("bid"));
			String option = request.getParameter("option");
			// 조회수 증가, 댓글 작성후거나 본인 조회수 제외
			if((!uid.equals(sessionUid)) && option == null ) {
				dao.increaseViewCount(bid);
			}
			board = dao.getBoardDetail(bid);
			request.setAttribute("board", board);
			List<Reply> replyList = rdao.getReplies(bid);
			request.setAttribute("replyList", replyList);
			
			
			rd = request.getRequestDispatcher("/board/detail.jsp");
			rd.forward(request, response);
			break;
			
		case "write":	
			if (request.getMethod().equals("GET")) {
				response.sendRedirect("/bbs/board/write.jsp");
			} else {
				title = request.getParameter("title");
				content = request.getParameter("content");
				files = request.getParameter("files");
				
				board = new Board(sessionUid, title, content, files);
				dao.insertBoard(board);
				response.sendRedirect("/bbs/board/list?page=1");
			}
			break;
			
		case "update":
			if (request.getMethod().equals("GET")) {
				bid = Integer.parseInt(request.getParameter("bid"));
				board = dao.getBoardDetail(bid);
				request.setAttribute("board", board);
				rd = request.getRequestDispatcher("/board/update.jsp");
				rd.forward(request, response);
			} else {
				bid = Integer.parseInt(request.getParameter("bid"));
				title = request.getParameter("title");
				content = request.getParameter("content");
				files = request.getParameter("files");
				uid = request.getParameter("uid");
				
				board = new Board(bid, title, content, files);
				dao.updateBoard(board);
				response.sendRedirect("/bbs/board/detail?bid=" + bid + "&uid=" + uid + "&option=DNI");
			}
			break;
			
		case "reply":
			content = request.getParameter("content");
			bid = Integer.parseInt(request.getParameter("bid"));
			uid = request.getParameter("uid");	// 게시글을 작성한 사람의 uid
			// 게시글 uid와 로그인한 사용자 uid(sessionUid)가 같으면 isMine=1
			int isMine = (uid.equals(sessionUid)) ? 1 : 0;	
			Reply reply = new Reply(content, isMine, sessionUid, bid);  // 댓글을 쓴사람 = sessionUid
			rdao.insert(reply);
			dao.increaseReplyCount(bid);
			response.sendRedirect("/bbs/board/detail?bid=" + bid + "&uid=" + uid + "&option=DNI");
			break;
		
		case "delete":
			bid = Integer.parseInt(request.getParameter("bid"));
			response.sendRedirect("/bbs/board/delete.jsp?bid=" + bid);
			break;
			
		case "deleteConfirm":
			bid = Integer.parseInt(request.getParameter("bid"));
			dao.deleteBoard(bid);
			response.sendRedirect("/bbs/board/list?page=" + session.getAttribute("currentBoardPage"));
			break;
		
		
		default:
			System.out.println(request.getMethod() + " 잘못된 경로");
		}
	}
	
}