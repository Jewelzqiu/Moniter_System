package com.jewelz.checkinhelper;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.StringTokenizer;

public class CheckinServer {

	final static String NAMELIST_FILE_NAME = "namelist";
	HashMap<String, String> NameList = new HashMap<String, String>();
	ServerSocket server;
	Thread ServerThread;

	static DB database = new DB();

	public void start() {
		BufferedReader reader;
		try {
			InputStreamReader isr = new InputStreamReader(new FileInputStream(
					NAMELIST_FILE_NAME), "UTF-8");
			reader = new BufferedReader(isr);
			String line = reader.readLine();
			while (line != null) {
				System.out.println(line);
				StringTokenizer tokenizer = new StringTokenizer(line);
				NameList.put(tokenizer.nextToken(), tokenizer.nextToken());
				line = reader.readLine();
			}
			reader.close();
			server = new ServerSocket(33333);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			File file = new File(NAMELIST_FILE_NAME);
			try {
				file.createNewFile();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

		ServerThread = new Thread(new ServerRunnable());
		ServerThread.start();
	}

	public void addMember(String uid, String name) {
		NameList.put(uid, name);
		writeBack();
	}

	public String removeMember(String uid) {
		String name = NameList.remove(uid);
		writeBack();
		return name;
	}

	public void stop() throws IOException {
		ServerThread.interrupt();
		writeBack();
	}

	void writeBack() {
		try {
			OutputStreamWriter osw = new OutputStreamWriter(
					new FileOutputStream(NAMELIST_FILE_NAME), "UTF-8");
			BufferedWriter bw = new BufferedWriter(osw);
			PrintWriter writer = new PrintWriter(bw);
			for (String uid : NameList.keySet()) {
				writer.println(uid + " " + NameList.get(uid));
				System.out.println("wb: " + uid + " " + NameList.get(uid));
			}
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	class ServerRunnable implements Runnable {

		@Override
		public void run() {
			while (true) {
				try {
					Socket socket = server.accept();
					BufferedReader reader = new BufferedReader(
							new InputStreamReader(socket.getInputStream(),
									"UTF-8"));
					PrintWriter writer = new PrintWriter(
							new OutputStreamWriter(new BufferedOutputStream(
									socket.getOutputStream()), "UTF-8"));
					String line = reader.readLine();
					System.out.println(line);
					StringTokenizer tokenizer = new StringTokenizer(line);
					String command = tokenizer.nextToken();
					String data = tokenizer.nextToken();
					if (command.equals("request")) {
						writer.println(MD5("radlab"));
						for (String uid : NameList.keySet()) {
							writer.println(uid + " " + NameList.get(uid));
						}
					} else if (command.equals("check")) {
						System.out.println("checked in at "
								+ new Date(Long.parseLong(data)));
						ArrayList<String> names = new ArrayList<String>();
						while (tokenizer.hasMoreTokens()) {
							names.add(tokenizer.nextToken());
						}
						String result = database.checkIn(Long.parseLong(data),
								names);
						writer.println(result);
						System.out.println(names);
					} else if (command.equals("add")) {
						String name = tokenizer.nextToken();
						addMember(data, name);
						System.out.println("added a new member: " + name);
					} else if (command.equals("remove")) {
						System.out.println("removed a member: "
								+ removeMember(data));
					} else if (command.toLowerCase().equals("select")) {
						String result = database.query(line);
						System.out.print(result);
						writer.print(result);
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
		// server.stop();
	}

	public static String MD5(String inStr) {
		MessageDigest md5 = null;
		try {
			md5 = MessageDigest.getInstance("MD5");
		} catch (Exception e) {
			System.out.println(e.toString());
			e.printStackTrace();
			return "";
		}
		char[] charArray = inStr.toCharArray();
		byte[] byteArray = new byte[charArray.length];

		for (int i = 0; i < charArray.length; i++)
			byteArray[i] = (byte) charArray[i];

		byte[] md5Bytes = md5.digest(byteArray);

		StringBuffer hexValue = new StringBuffer();

		for (int i = 0; i < md5Bytes.length; i++) {
			int val = ((int) md5Bytes[i]) & 0xff;
			if (val < 16)
				hexValue.append("0");
			hexValue.append(Integer.toHexString(val));
		}

		return hexValue.toString();
	}

}
