package edu.upc.eetac.dsa.asantamaria.beans;

import java.io.Serializable;

public class ClienteBean implements Serializable{

	public String getNick() {
		return nick;
	}

	public void setNick(String nick) {
		this.nick = nick;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public String getMensaje() {
		return mensaje;
	}

	public void setMensaje(String mensaje) {
		this.mensaje = mensaje;
	}

	private String nick, ip, mensaje;
	
	public ClienteBean() {
		
		
		// TODO Auto-generated constructor stub
	}

}
