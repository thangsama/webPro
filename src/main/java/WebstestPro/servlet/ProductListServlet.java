package WebstestPro.servlet;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import WebstestPro.beans.Product;
import WebstestPro.utils.DBUtils;
import WebstestPro.utils.MyUtils;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet(urlPatterns = {"/productList" })
public class ProductListServlet extends HttpServlet{
	private static final long serialVersionUID =1L;
	public ProductListServlet() {
		super();
	}
	
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)throws ServletException, IOException
	{
		Connection conn= MyUtils.getStoredConnection(request);
		String errorString = null;
		List<Product> list = null;
		try {
			list = DBUtils.queryProduct(conn);
			
		}catch(SQLException e) {
			e.printStackTrace();
			errorString = e.getMessage();
			
		}
		// Save info to " Request ATTRIBUTE " before forwarding to Views
		request.setAttribute("errorString", errorString);
		request.setAttribute("productList", list);
		
		//Forward to /web-inf/views/productListView.jsp
		RequestDispatcher dispatcher = request.getServletContext().getRequestDispatcher("/WEB-INF/views/productListView.jsp");
		dispatcher.forward(request, response);
		}
	
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
		doGet(request, response);
	}
}
