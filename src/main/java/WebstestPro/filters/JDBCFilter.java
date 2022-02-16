package WebstestPro.filters;

import java.io.IOException;
import java.sql.Connection;
import java.util.Collection;
import java.util.Map;

import WebstestPro.conn.ConnectionUtils;
import WebstestPro.utils.MyUtils;
import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.FilterConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRegistration;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;


@WebFilter(filterName = "jdbcFilter", urlPatterns = { "/*" })

public class JDBCFilter implements Filter {
	public JDBCFilter() {

	}

	@Override
	public void init(FilterConfig fConfig) throws ServletException {
	}

	@Override
	public void destroy() {
	}

	// Check the request target is Servlet or not
	private boolean needJDBC(HttpServletRequest request) {
		System.out.println("JDBC Filter");
		// Servlet Url-pattern:/spath/*
		// => /spath
		String servletPath = request.getServletPath();
		// =>/abc/mnp
		String pathInfo = request.getPathInfo();

		String urlPattern = servletPath;

		if (pathInfo!=null) {
			// =>/spath/*
			urlPattern = servletPath + "/*";
		}

		// Key: servletName
		// Value: ServletRegistration

		Map<String, ? extends ServletRegistration> servletRegistrations = request.getServletContext()
				.getServletRegistrations();

		// Collec all servlet
		Collection<? extends ServletRegistration> values = servletRegistrations.values();
		for (ServletRegistration sr : values) {
			Collection<String> mappings = sr.getMappings();
			if (mappings.contains(urlPattern)) {
				return true;
			}
		}
		return false;

	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		HttpServletRequest req = (HttpServletRequest) request;
		//Just open connection for special request
		
		if(this.needJDBC(req)) {
			System.out.println("Open connection for: " +req.getServletPath());
			Connection conn=null;
			try {
				//connect to database
				conn= ConnectionUtils.getConnection();
				conn.setAutoCommit(false);
				
				//Save info to Attribute of request
				MyUtils.storeConnection(request, conn);
				
				//allow continuing request
				chain.doFilter(request, response);
				
				//call method commit() to complete the DB connection
				conn.commit();
			}catch (Exception e) {
				e.printStackTrace();
				ConnectionUtils.rollbackQuietly(conn);
				throw new ServletException();
			}finally{
				ConnectionUtils.closeQuietly(conn);
			}
					
		}
		//Not opening connection for simple request(html,inmage...)
		else {
			//allow continuing request
			chain.doFilter(request, response);
		}
		
	}
}
