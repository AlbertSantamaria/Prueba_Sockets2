package edu.upc.eetac.dsa.asantamaria.beans;


import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JTextArea;
import javax.swing.JTextField;

/*
 Ejemplo envia objetos
 */

public class frmcliente extends JFrame implements ActionListener, Runnable {

	JTextField txtnick, txtip, txtmsg;
	JButton btnenviar;
	JButton btnsalir;
	JTextArea txtmensajes_rx;

	ServerSocket servidor = null;

	public frmcliente() {
		txtnick = new JTextField();
		txtnick.setBounds(10, 10, 400, 20);

		txtip = new JTextField();
		txtip.setBounds(10, 30, 400, 20);

		txtmsg = new JTextField();
		txtmsg.setBounds(10, 50, 400, 20);

		// Espacio de Respuestas
		txtmensajes_rx = new JTextArea();
		txtmensajes_rx.setBounds(10, 120, 400, 300);
		// .......

		btnenviar = new JButton("Enviar");
		btnenviar.setBounds(10, 70, 300, 40);
		btnenviar.addActionListener(this);

		btnsalir = new JButton("Salir");
		btnsalir.setBounds(310, 70, 100, 40);
		btnsalir.addActionListener(this);

		add(btnenviar);
		add(btnsalir);
		add(txtnick);
		add(txtmsg);
		add(txtip);
		// Respuestas
		add(txtmensajes_rx);

		setLayout(null);
		setSize(420, 420);
		setVisible(true);

		Thread hilo = new Thread(this);
		hilo.start();
	}

	public static void main(String[] args) {
		new frmcliente();

	}


	public void actionPerformed(ActionEvent arg0) {
		
		if (arg0.getSource() == btnsalir) {

			try {
				servidor.close();
				dispose();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
		if (arg0.getSource() == btnenviar) {
			try {
				Socket cli = new Socket("192.168.1.4", 9121);
				ClienteBean bean = new ClienteBean();

				bean.setNick(txtnick.getText());
				bean.setIp(txtip.getText());
				bean.setMensaje(txtmsg.getText());

				// System.out.println(bean.getMensaje());

				ObjectOutputStream flujo_objetos = new ObjectOutputStream(
						cli.getOutputStream());
				flujo_objetos.writeObject(bean);

				cli.close();

			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}


		}

	}


	public void run() {
		// TODO Auto-generated method stub
		try {
			// Apertura Socket
			servidor = new ServerSocket(9123);
			Socket serv;
			ClienteBean objeto_rx;

			while (true) {
				// Acepta cx entrantes
				serv = servidor.accept();

				// Lee Objeto
				ObjectInputStream flujo_entrada = new ObjectInputStream(
						serv.getInputStream());
				objeto_rx = new ClienteBean();

				// Hay que parsearlo para recibirlo en la instancia local
				objeto_rx = (ClienteBean) flujo_entrada.readObject();
				String iprx = objeto_rx.getMensaje();

				// pone mensaje en el formulario
				txtmensajes_rx.append("\n" + objeto_rx.getNick() + " dice: "
						+ objeto_rx.getMensaje());

				// cierra
				serv.close();
				if (objeto_rx.getMensaje().equalsIgnoreCase("FIN")) {
					servidor.close();
					break;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}

