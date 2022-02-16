package WebstestPro.servlet;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

import WebstestPro.utils.DBUtils;
import WebstestPro.utils.MyUtils;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet(urlPatterns={"/deleteProduct"})
public class DeleteProductServlet extends HttpServlet{
private static final long serialVersionUID=1L;	

public DeleteProductServlet() {
	super();
}
@Override
protected void doGet(HttpServletRequest request, HttpServletResponse response)
throws ServletException,IOException{
	Connection conn= MyUtils.getStoredConnection(request);
	String code=request.getParameter("code");
	String errorString = null;
	try {
		DBUtils.deleteProduct(conn, code);
	}catch(SQLException e) {
		e.printStackTrace();
		errorString =e.getMessage();
	}
	
	//If there is error, forward to errorpage
	if(errorString!=null) {
		//Save info to REquest Attribute before forwarding
		request.setAttribute("errorString",errorString);
		
		RequestDispatcher dispatcher=request.getServletContext()
				.getRequestDispatcher("/WEB-INF/views/deleteProductErrorView.jsp");
		dispatcher.forward(request, response);
	}
	//If not redirect to productlistpage
	else {
		response.sendRedirect(request.getContextPath()+"/productList");
		
	}
		
		
		
	}
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
	throws ServletException, IOException{
		doGet(request,response);
	}

}



