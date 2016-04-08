package com.login.demo;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class LoginServlet
 */

public class LoginServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

    /**
     * Default constructor. 
     */
    public LoginServlet() {
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		
		String user=request.getParameter("user");
		String pass=request.getParameter("pass");
		
		LoginService l=new LoginService ();
		boolean result=l.autenticati(user, pass);
		
		if (result)
		{
			User utente=l.getUserDetails(user);		
			request.getSession().setAttribute("user", utente);		//Setto nella sessione
			request.setAttribute("user", utente);			//Setto nella richiesta
			RequestDispatcher rD= request.getRequestDispatcher("loggato.jsp");
			rD.forward(request, response);			//Meglio del redirect. Invia anche richiesta e risposta
		//	response.sendRedirect("loggato.jsp");
			return;
		}
		else
		{
			response.sendRedirect("login.jsp");
			return;
		}
	}

}
