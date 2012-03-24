package com.jewelz.checkinhelper;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.StringTokenizer;

public class CheckinServer {
	
	final static String NAMELIST_FILE_NAME = "namelist";
	HashMap<String, String> NameList = new HashMap<String, String>();
	ServerSocket server;
	Thread ServerThread;
	
	public void start() {
		BufferedReader reader;
		try {
			reader = new BufferedReader(new FileReader(NAMELIST_FILE_NAME));
			String line = reader.readLine();
			while (line != null) {
				line = new String(line.getBytes(), "UTF-8");
				StringTokenizer tokenizer = new StringTokenizer(line);
				NameList.put(tokenizer.nextToken(), tokenizer.nextToken());			
				line = reader.readLine();
			}
			reader.close();
			server = new ServerSocket(33333);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
//			File file = new File(NAMELIST_FILE_NAME);
//			try {
//				file.createNewFile();
//			} catch (IOException e1) {
//				e1.printStackTrace();
//			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		ServerThread = new Thread(new ServerRunnable());
		ServerThread.start();
	}
	
	public void addMember(String uid, String name) {
		NameList.put(uid, name);
	}
	
	public String removeMember(String uid) {
		return NameList.remove(uid);
	}
	
	public void stop() throws IOException {
		ServerThread.interrupt();
		PrintWriter writer = new PrintWriter(
				new BufferedWriter(new FileWriter(NAMELIST_FILE_NAME)));
		for (String uid : NameList.keySet()) {
			writer.println(uid + " " + NameList.get(uid));
		}
		writer.close();
	}
	
	class ServerRunnable implements Runnable {

		@Override
		public void run() {
			while (true) {
				try {
					Socket socket = server.accept();
					BufferedReader reader = new BufferedReader(
							new InputStreamReader(socket.getInputStream()));
					PrintWriter writer = new PrintWriter(
							new OutputStreamWriter(
									new BufferedOutputStream(
											socket.getOutputStream()), "UTF-8"));
					StringTokenizer line = new StringTokenizer(reader.readLine());
					String command = line.nextToken();
					String data = line.nextToken();
					
					if (command.equals("request")) {
						if (data.equals("namelist")) {
							for (String uid : NameList.keySet()) {
								writer.println(uid + " " + NameList.get(uid));
							}							
						} else if (data.equals("")) {
							// TODO
						}
					} else if (command.equals("check")) {
						// TODO
//						System.out.println(NameList.get(data) + " just checked in!");
//						System.out.println(NameList.get(data) + " just checked out!");
					} else if (command.equals("addmember")) {
						String name = line.nextToken();
						addMember(data, name);
						System.out.println("added a new member: " + name);
					} else if (command.equals("removemember")) {
						System.out.println("removed a member: " + removeMember(data));
					}
					writer.flush();
					writer.close();
					reader.close();
					socket.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
				
			}
		}
		
	}

	@Override
	protected void finalize() throws Throwable {
		this.stop();
		super.finalize();
	}

	/**
	 * @param args
	 * @throws IOException 
	 */
	public static void main(String[] args) throws IOException {
		CheckinServer server = new CheckinServer();
		server.start();
		//server.stop();
	}

}
