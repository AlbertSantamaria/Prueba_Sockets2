package edu.upc.eetac.dsa.asantamaria.beans;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JTextArea;

public class frmservidor extends JFrame implements Runnable, ActionListener {

	JTextArea mensajes;
	
	ServerSocket servidor=null;
	
	//Para asegurar cerrar puertos
	JButton btnsalir;
	
	public frmservidor() {
		
		btnsalir = new JButton("Salir");
		btnsalir.setBounds(10,10,400,20);
		btnsalir.addActionListener(this);
		
		mensajes = new JTextArea();
		mensajes.setBounds(10, 30, 400, 400);
		add(mensajes);
		add(btnsalir);
		


		setLayout(null);
		setSize(450, 450);
		setVisible(true);

		Thread hilo = new Thread(this);
		// .start llama al run() creado
		hilo.start();
	}

	public static void main(String[] args) {

		
		new frmservidor();

	}


	public void run() {
		try {
			// Apertura Socket
			servidor = new ServerSocket(9121);
			Socket cli;
			ClienteBean objeto_rx;

			while (true) {
				// Acepta cx entrantes
				cli = servidor.accept();

				// Lee Objeto
				ObjectInputStream flujo_entrada = new ObjectInputStream(
						cli.getInputStream());
				objeto_rx = new ClienteBean();
				
				//Hay que parsearlo para recibirlo en la instancia local
				objeto_rx=(ClienteBean) flujo_entrada.readObject();
				String iprx = objeto_rx.getMensaje();
				// pone mensaje en el formulario
				mensajes.append("\n" + cli.getInetAddress()
						+ " "+objeto_rx.getNick()+ " envia Mensaje: " + objeto_rx.getMensaje() + " a "
						+ objeto_rx.getIp());

				//Respondiendo
				
				Socket client_send = new Socket(objeto_rx.getIp(),9123);
				ObjectOutputStream flujo_respuesta=new ObjectOutputStream(client_send.getOutputStream());
				
				flujo_respuesta.writeObject(objeto_rx);
				client_send.close();
				
				// cierra
				cli.close();
				if (objeto_rx.getMensaje().equalsIgnoreCase("FIN")) {
					servidor.close();
					break;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}


	public void actionPerformed(ActionEvent arg0) {

		if(arg0.getSource()== btnsalir){
			
			try {
				servidor.close();
				dispose();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
	}

}

