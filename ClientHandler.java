import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.BindException;
import java.net.Socket;
import java.net.SocketException;
import java.util.NoSuchElementException;
import java.util.StringTokenizer;

class ClientHandler extends Thread {
	private String name;
	final DataInputStream dis;
	final DataOutputStream dos;
	Socket s;
	boolean isloggedin;

	// constructor
	public ClientHandler(Socket s, String name, DataInputStream dis, DataOutputStream dos) {
		this.dis = dis;
		this.dos = dos;
		this.name = name;
		this.s = s;
		this.isloggedin = true;
	}

	public String getuserName() {
		return name;
	}

	@Override
	public void run() {
		this.message();
		try {
			// closing resources
			this.dis.close();
			this.dos.close();

		} catch (IOException e) {
			e.printStackTrace();
			System.exit(0);
		}
	}

	void message() {
		String received;
		String decoder = null;
		String clients = "";
		boolean a = false;
		while (true) {
			try {
				decoder = null;
				a = false;
				// receive the string
				received = dis.readUTF();
				System.out.println(received);
				// to change to another person.
               
				if (received.contains("#")) {
					decoder = dis.readUTF();
					System.out.println(decoder);
					a = true;
				}
				

				if (received.equals("show")) {
					clients = "";
					for (ClientHandler ch : Server.ar) {
						if (ch.isloggedin == true)
							clients += ch.name + "#";
					}
					try {
						dos.writeUTF(clients);
						continue;
					} catch (IOException e3) {
						e3.printStackTrace();
					}
				}
				if (received.contains("logout")) {
					this.isloggedin = false;
					for (int i = 0; i < Server.ar.size(); i++) {
						if (Server.ar.get(i).getuserName().equals(name)) {
							System.out.println(name + " has logged out");
						}
					}
					this.s.close();
					break;
				}
				// break the string into message and recipient part
				StringTokenizer st = new StringTokenizer(received, "#");
				String MsgToSend = st.nextToken();
				String recipient = st.nextToken();
				// search for the recipient in the connected devices list.
				// ar is the vector storing client of active users
				for (ClientHandler mc : Server.ar) {
					// if the recipient is found, write on its
					// output stream
					if (mc.name.equals(recipient) && mc.isloggedin == true) {
						String y = "[" + this.name + "] : " + MsgToSend;
						mc.dos.writeUTF(y);
						if (y.contains(" : ") || a) {
							mc.dos.writeUTF(decoder);
						}
						break;
					}
				}
			} catch (NoSuchElementException e) {
				e.printStackTrace();
				message();
			} catch (BindException e) {
				e.printStackTrace();
				System.exit(0);
			} catch (SocketException e) {
				try {
					this.isloggedin = false;
					dis.close();
					dos.close();
					this.s.close();

				} catch (IOException e1) {
					e1.printStackTrace();
				}

			} catch (Exception e) {
				e.printStackTrace();
				System.exit(0);
			}
		}
	}

}