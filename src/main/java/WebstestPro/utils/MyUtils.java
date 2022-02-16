package WebstestPro.utils;

import java.sql.Connection;

import WebstestPro.beans.UserAccount;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

public class MyUtils {
	public static final String ATT_NAME_CONNECTION = "ATTRIBUTE_FOR_CONNECTION";
	private static final String ATT_NAME_USER_NAME = "ATTRIBUTE_FOR_STORE_USER_NAME_IN_COOKIE";

	// Save connection to attribute of request
	/// Those info just stand there during the request time
	//// Until the site give data to user
	public static void storeConnection(ServletRequest request, Connection conn) {
		request.setAttribute(ATT_NAME_CONNECTION, conn);

	}

	// Take the Connection which saved in Attribute of request
	public static Connection getStoredConnection(ServletRequest request) {
		Connection conn = (Connection) request.getAttribute(ATT_NAME_CONNECTION);
		return conn;
	}

	// Save user login info to SESSION
	public static void storeLoginedUser(HttpSession session, UserAccount loginedUser) {
		// Can connect via ${loginedUser} on JSP
		session.setAttribute("loginedUser", loginedUser);
	}

	// Take user login info from SESSION
	public static UserAccount getLoginedUser(HttpSession session) {
		UserAccount loginedUser = (UserAccount) session.getAttribute("loginedUser");
		return loginedUser;

	}

	// Save User's info to COOKIE
	public static void storeUserCookie(HttpServletResponse response, UserAccount user) {
		System.out.println("Store user cookie");
		Cookie cookieUserName = new Cookie(ATT_NAME_USER_NAME, user.getUserName());
		// 1 day = 86.400 seconds
		cookieUserName.setMaxAge(24 * 60 * 60);
		response.addCookie(cookieUserName);

	}

	// Take user login info from COOKIE
	public static String getUserNameInCookie(HttpServletRequest request) {
		Cookie[] cookies = request.getCookies();
		if (cookies != null) {
			for (Cookie cookie : cookies) {
				if (ATT_NAME_USER_NAME.equals(cookie.getName())) {
					return cookie.getValue();
				}
			}
		}
		return null;
	}

	// Delete user's COOKIE
	public static void deleteUserCookie(HttpServletResponse response) {
		Cookie cookieUserName = new Cookie(ATT_NAME_USER_NAME, null);
		// Imitatively = now
		cookieUserName.setMaxAge(0);
		response.addCookie(cookieUserName);

	}
}
