// Java implementation for multithreaded chat client
// Save file as Client.java

import java.io.*;
import java.net.*;
import java.util.Base64;
import java.util.Scanner;
import java.util.Vector;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

public class Client {
	final static int ServerPort = 3433;
	static String name;
	static boolean a = false;
	static SecretKey deskey;

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

	public static String endcode(byte[] b) {
		return Base64.getEncoder().encodeToString(b);
	}

	public static byte[] decode(String s) {
		return Base64.getDecoder().decode(s);
	}

	public static void sendrecievemsg(String name, Socket s, DataInputStream dis, DataOutputStream dos) {
		Scanner scn = new Scanner(System.in);

		Thread sendMessage = new Thread(new Runnable() {
			@Override
			public void run() {
				while (true) {

					// read the message to deliver.
					String msg = scn.nextLine();
					try {
						// write on the output stream
						KeyGenerator kg = KeyGenerator.getInstance("DES");
						Client.deskey = kg.generateKey();
						Encrpytion en = new Encrpytion();
						if (msg.contains("#")) {
							String[] splits = msg.split("#");
							byte[] a = en.encmessgage(splits[0], deskey);
							msg = new String(a) + "#" + splits[1];
						}
						dos.writeUTF(msg);
						if (msg.contains("#")) {
							dos.writeUTF(endcode(deskey.getEncoded()));
							
						}
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
							Decrpytion de = new Decrpytion();
							if (line.contains("#")) {
								System.out.println("Active users");
								String[] y = line.split("#");
								for (String a : y) {
									System.out.println(a);
								}
								continue;
							}
							if (line.contains(" : ")) {
								String[] splits = line.split(" : ");
								line = splits[0] + " : ";

								String k = dis.readUTF();
					
								SecretKeySpec s = new SecretKeySpec(decode(k), "DES");
								Decrpytion de1 = new Decrpytion();
								line += de1.decmessgage(splits[1].getBytes(), s);
							}
							System.out.println(line);

						}
					}

					catch (Exception u) {
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
