package WebstestPro.servlet;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

import WebstestPro.beans.Product;
import WebstestPro.utils.DBUtils;
import WebstestPro.utils.MyUtils;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet(urlPatterns= {"/editProduct"})
public class EditProductServlet extends HttpServlet{
	private static final long serialVersionUID=1L;
	public EditProductServlet() {
		super();
	}
	
	//Show productlist apge
	@Override
	protected void doGet(HttpServletRequest request,HttpServletResponse response)
			throws ServletException,IOException{
		Connection conn=MyUtils.getStoredConnection(request);
		String code=request.getParameter("code");
		Product product = null;
		String errorString = null;
		try {
			product = DBUtils.findProduct(conn, code);
			
		}catch(SQLException e) {
			e.printStackTrace();
			errorString= e.getMessage();
					
		}
		
		//if there is error, it will redirect prodcutlist page (eg: no product...)
		if(errorString!=null&&product==null) {
			response.sendRedirect(request.getServletPath()+"/productList");
			return;
		}
		
		
		//Save info to REquest Attribute before redirect to views
		request.setAttribute("errorString", errorString);
		request.setAttribute("product", product);
		
		RequestDispatcher dispatcher=request.getServletContext()
				.getRequestDispatcher("/WEB-INF/views/editProductView.jsp");
		dispatcher.forward(request, response);}
		
		//After submiting new info for product (edited)
		@Override
		protected void doPost(HttpServletRequest request, HttpServletResponse response)
		throws ServletException,IOException	{
			Connection conn= MyUtils.getStoredConnection(request);
			String code =request.getParameter("code");
			String name =request.getParameter("name");
			String priceStr =request.getParameter("price");
			float price = 0;
			try {
				price = Float.parseFloat(priceStr);
				
			}catch(Exception e) {
				
			}
			Product product =new Product(code,name,price);
			String errorString = null;
			try {
				DBUtils.updateProduct(conn, product);
			}catch(SQLException e) {
				e.printStackTrace();
				errorString = e.getMessage();
			}
			
			//Save info to RequestAttribute before redirect to VIEWS
			request.setAttribute("errorString", errorString);
			request.setAttribute("product", product);
			
			//If there is errors, stay at edit page
			if(errorString!=null) {
				RequestDispatcher dispatcher = request.getServletContext()
						.getRequestDispatcher("/WEB-INF/views/editProductView.jsp");
				dispatcher.forward(request, response);
			}
			
			// or redirect to productlist page
			else {
				response.sendRedirect(request.getContextPath()+"/productList");
			}
		}
		
	}


