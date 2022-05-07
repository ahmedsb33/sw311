// Java implementation for multithreaded chat client
// Save file as Client.java

import java.io.*;
import java.net.*;
import java.util.Scanner;
import java.util.Vector;

import javax.crypto.SecretKey;



public class Client {
	final static int ServerPort = 3433;
	static String name;
	static boolean a = false;
	SecretKey deskey;

	public static void main(String args[]) throws Exception {
		Scanner scn = new Scanner(System.in);

		// getting localhost ip
		InetAddress ip = InetAddress.getByName("localhost");

		// establish the connection
		Socket s = new Socket(ip, ServerPort);

		// obtaining input and out streams
		DataInputStream dis = new DataInputStream(s.getInputStream());
		DataOutputStream dos = new DataOutputStream(s.getOutputStream());
		
		//
		Thread login = new Thread(new Runnable() {
			@Override
			public void run() {
				// read the message to deliver.
				System.out.print("Enter username: ");
				String name = scn.nextLine();
				try {
					// write on the output stream
					dos.writeUTF(name);
					Client.name = name;
					System.out.println("welcome : " + Client.name);
					boolean loggedin = true;
					dos.writeBoolean(loggedin);
					sendrecievemsg(Client.name, s, dis, dos);	
				} catch (Exception u) {
					System.out.println(u);
					System.exit(0);
				}
			}
		});
		login.start();
		
	}
	public static void sendrecievemsg(String name, Socket s, DataInputStream dis, DataOutputStream dos) {
		Scanner scn = new Scanner(System.in);

		Thread sendMessage = new Thread(new Runnable() {
			@Override
			public void run() {
				//System.out.println("You are now chaating with " + user);
				while (true) {

					// read the message to deliver.
					String msg = scn.nextLine();
					try {
						// write on the output stream

						dos.writeUTF(msg);
					} catch (Exception u) {
						System.out.println(u);
						System.exit(0);
					}
				}
			}
		});
		
		Thread readMessage = new Thread(new Runnable() {
			@Override
			public void run() {
				while (true) {
					try {
						String line;
						// read the message sent to this client
						while ((line = dis.readUTF()) != null) {
							if(line.contains("#")) {
								System.out.println("Active users");
								String[] y = line.split("#");
								for(String a :y) {
									System.out.println(a);
								}
								continue;
								}
							System.out.println(line);
						}
						
					} catch (Exception u) {
						System.out.println(u);
						System.exit(0);
					}

				}
			}
		});
		// readMessage thread
		sendMessage.start();
		readMessage.start();
		
		
	}
}
