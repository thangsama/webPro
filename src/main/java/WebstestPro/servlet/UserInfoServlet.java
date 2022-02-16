package WebstestPro.servlet;

import java.io.IOException;

import WebstestPro.beans.UserAccount;
import WebstestPro.utils.MyUtils;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet(urlPatterns = { "/userInfo" })
public class UserInfoServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	public UserInfoServlet() {
		super();

	}

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		HttpSession session = request.getSession();

//Check if user logined or not yet
		UserAccount loginedUser = MyUtils.getLoginedUser(session);

//If not yet, direct to login page
		if (loginedUser == null) {
			// redirect to login page
			response.sendRedirect(request.getContextPath() + "/login");
			return;
		}
//Save data to "Request attribute" before forwarding
		request.setAttribute("user", loginedUser);

// If user logined, it would redirect to web-inf/views/userInfoView.jsp
		RequestDispatcher dispatcher = this.getServletContext().getRequestDispatcher("/WEB-INF/views/userInfoView.jsp");
		dispatcher.forward(request, response);

	}
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
		doGet(request, response);
	}
}
