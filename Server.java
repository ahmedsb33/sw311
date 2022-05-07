// Java implementation of Server side
// It contains two classes : Server and ClientHandler
// Save file as Server.java

import java.io.*;
import java.util.*;
import java.net.*;

// Server class
public class Server {

	// Vector to store active clients
	static Vector<ClientHandler> ar = new Vector<>();

	// counter for clients
	static int i = 0;

	public static void main(String[] args) throws Exception {
		// server is listening on port 5056
		ServerSocket ss = new ServerSocket(3433);

		Socket s;

		// running infinite loop for getting
		// client request
		while (true) {
			// Accept the incoming request
			s = ss.accept();

			System.out.println("New client request received : " + s);

			// obtain input and output streams
			DataInputStream dis = new DataInputStream(s.getInputStream());
			DataOutputStream dos = new DataOutputStream(s.getOutputStream());

			System.out.println("Creating a new handler for this client...");
			auth user = new auth(s, dis, dos);
			Thread tt = new Thread(user);
			tt.start();
			// Create a new handler object for handling this request.
		}
	}
}

/////////////////

class auth extends Thread {
	private String name;
	final DataInputStream dis;
	final DataOutputStream dos;
	Socket s;

	// constructor
	public auth(Socket s, DataInputStream dis, DataOutputStream dos) {
		this.dis = dis;
		this.dos = dos;
		this.s = s;
	}

	public String getuserName() {
		return name;
	}

	@Override
	public void run() {
		try {
			//read username
			String name = dis.readUTF();
			ClientHandler mtch = new ClientHandler(s, name, dis, dos);

			// Create a new Thread with this object.
			Thread t = new Thread(mtch);

			System.out.println("Adding this client to active client list");
			// add this client to active clients list
			Server.ar.add(mtch);
			// start the thread.
			for (ClientHandler ch : Server.ar) {
				System.out.println("active users\n" + ch.getuserName());
			}
			// if the user is logged in then he can use the following.
			boolean x = false;
			try {
				x = dis.readBoolean();

			} catch (IOException e) {
				e.printStackTrace();
			}
			if (x)
				t.start();

		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}

