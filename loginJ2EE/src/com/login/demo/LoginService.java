//dto
package com.login.demo;

import java.util.HashMap;

public class LoginService {
	
	HashMap <String, String> users=new HashMap <String, String> ();
	
	public LoginService ()
	{
		users.put("aaa", "Massimo Perini");
		users.put("qwe", "cde");
		users.put("zxc", "fgh");
	}
	
	public boolean autenticati (String nome, String password)
	{
		nome=nome.trim();
		password=password.trim();
		
		if (password.equals("asd") && nome.equals("aaa"))
			return true;
		else
			return false;
	}
	
	public User getUserDetails (String userId)
	{
		User user = new User (users.get(userId), "asd");
		return user ;
	}
}
