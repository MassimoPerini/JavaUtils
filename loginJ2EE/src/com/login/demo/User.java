package com.login.demo;

public class User {
	private String username="";
	private String userId="";
	private String pass="";
	private String telefono ="";
	
	public User (String username, String pass)
	{
		this.username=username;
		this.pass=pass;
		this.creaAltriValori();
	}
	
	private void creaAltriValori ()
	{
		System.out.println(username);
		System.out.println(pass);
		
		int dim=(username.length()+pass.length())/2;
		for (int i=0;i<dim;i++)
		{
			userId+="a";
			telefono+="0";
		}
	}

	public String getUsername() {
		return username;
	}

	public String getUserId() {
		return userId;
	}

	public String getPass() {
		return pass;
	}

	public String getTelefono() {
		return telefono;
	}
	
	
}
