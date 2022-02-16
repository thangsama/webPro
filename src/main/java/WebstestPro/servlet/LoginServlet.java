package WebstestPro.servlet;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

import WebstestPro.beans.UserAccount;
import WebstestPro.utils.DBUtils;
import WebstestPro.utils.MyUtils;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet(urlPatterns = {"/login"})
public class LoginServlet extends HttpServlet{
private static final long serialVersionUID = 1L;
public LoginServlet() {
	super();
}

//Show login page
@Override
protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
	//forward to /web-inf/views/loginView.jsp
	RequestDispatcher dispatcher = this.getServletContext().getRequestDispatcher("/WEB-INF/views/loginView.jsp");
	dispatcher.forward(request, response);
	
}

//After submiting user account's info
@Override
protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	String userName = request.getParameter("userName");
	String password = request.getParameter("password");
	String rememberMeStr = request.getParameter("rememberMe");
	boolean remember ="Y".equals(rememberMeStr);
	
	UserAccount user = null;
	boolean hasError=false;
	String errorString = null;
	
	if(userName ==null||password ==null|| userName.length()==0||password.length()==0) {
		hasError = true;
		errorString="Required username and password!";
	}else {
		Connection conn = MyUtils.getStoredConnection(request);
		try {
			// Find user in Database
			user = DBUtils.findUser(conn, userName, password);
			if(user==null) {
				hasError = true;
				errorString = " User Name or password invalid";
			}
		}catch(SQLException e) {
			e.printStackTrace();
			hasError=true;
			errorString = e.getMessage();
		}
	}
	
	// If there is error, it will forward to /web-inf/views/login.jsp
		if(hasError) {
			user= new UserAccount();
			user.setUserName(userName);
			user.setPassword(password);
			
			//Save data to "request" before forwarding
			request.setAttribute("errorString", errorString);
			request.setAttribute("user", user);
			
			//Forward to /web-inf/views/login.jsp
			RequestDispatcher dispatcher = this.getServletContext()
.getRequestDispatcher("/WEB-INF/views/loginView.jsp");
			dispatcher.forward(request, response);
		
	}
		// If ther is not error, it will save user's info to SESSION and redirect to userinfo page
		else{
			HttpSession session =request.getSession();
			MyUtils.storeLoginedUser(session, user);
			
			// If checked "remember me" option
			if(remember) {
				MyUtils.storeUserCookie(response, user);
			}
			// If didnt check "remember me" option, it would delete cookie
			else {
				MyUtils.deleteUserCookie(response);
				
			}
			
			//Redirect to /userinfo
			response.sendRedirect(request.getContextPath()+"/userInfo");
			
			}
		}
	}

