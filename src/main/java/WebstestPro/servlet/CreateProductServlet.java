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

@WebServlet(urlPatterns = { "/createProduct" })
public class CreateProductServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	public CreateProductServlet() {
		super();
	}

	// Show create produc page
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)throws ServletException,IOException{
		RequestDispatcher dispatcher = request.getServletContext().getRequestDispatcher("/WEB-INF/views/createProductView.jsp");
		dispatcher.forward(request, response);
		
	}

	// After submit all product info
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		Connection conn = MyUtils.getStoredConnection(request);
		String code = (String)request.getParameter("code");
		String name = (String)request.getParameter("name");
		String priceStr = (String) request.getParameter("price");
		float price = 0;
		try {
			price = Float.parseFloat(priceStr);
		} catch (Exception e) {

		}
		Product product = new Product(code, name, price);
		String errorString = null;
		String regex = "\\w+";
		if (code == null || !code.matches(regex)) {
			errorString = "Product Code invalid";
		}
		if (errorString == null) {
			try {
				DBUtils.insertProduct(conn, product);
			} catch (SQLException e) {
				e.printStackTrace();
				errorString = e.getMessage();
			}

		}
		// Save info to REQUEST ATTRIBUTE before redirecting to VIEWS
		request.setAttribute("errorString", errorString);
		request.setAttribute("product", product);

		// If there is error, it will lead to Edit page
		if (errorString != null) {
			RequestDispatcher dispatcher = request.getServletContext()
					.getRequestDispatcher("/WEB-INF/views/createProductView.jsp");
			dispatcher.forward(request, response);
		}
		// If fills all info correctly, it will redirect to Product List page
		else {
			response.sendRedirect(request.getContextPath() + "/productList");
		}

	}

}
