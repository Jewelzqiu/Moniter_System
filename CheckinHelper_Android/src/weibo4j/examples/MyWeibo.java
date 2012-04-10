package weibo4j.examples;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import weibo4j.Status;
import weibo4j.Weibo;
import weibo4j.WeiboException;
import weibo4j.http.RequestToken;

public class MyWeibo {

	public static void update(String mystatus) {
		try {
			System.setProperty("weibo4j.oauth.consumerKey", Weibo.CONSUMER_KEY);
			System.setProperty("weibo4j.oauth.consumerSecret",
					Weibo.CONSUMER_SECRET);
			Weibo weibo = new Weibo();
			RequestToken requestToken;
			requestToken = weibo.getOAuthRequestToken();

			System.out.println("Got request token.");
			System.out.println("Request token: " + requestToken.getToken());
			System.out.println("Request token secret: "
					+ requestToken.getTokenSecret());

			weibo.setToken("37c99e4670202a3cccc2e3e5865d16d4",
					"0232fab516389c7929f35afe78beadc2");

			Status status = weibo.updateStatus(mystatus);
			System.out.println("Successfully updated the status to ["
					+ status.getText() + "].");

			try {
				Thread.sleep(3000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		} catch (WeiboException e) {
			System.out.println("Failed to get timeline: " + e.getMessage());
			e.printStackTrace();
		}
	}

	public static void upload(String mystatus, String Filename) {
		try {

			System.setProperty("weibo4j.oauth.consumerKey", Weibo.CONSUMER_KEY);
			System.setProperty("weibo4j.oauth.consumerSecret",
					Weibo.CONSUMER_SECRET);
			Weibo weibo = new Weibo();

			weibo.setToken("37c99e4670202a3cccc2e3e5865d16d4",
					"0232fab516389c7929f35afe78beadc2");

			try {
//				byte[] content = readFileImage(Filename);
//				System.out.println("content length:" + content.length);
//				ImageItem pic = new ImageItem("pic", content);

				String s = java.net.URLEncoder.encode(mystatus, "utf-8");
				Status status = weibo.uploadStatus(s, new File(Filename));

				System.out.println("Successfully upload the status to ["
						+ status.getText() + status.getOriginal_pic() + "].");
			} catch (Exception e1) {
				e1.printStackTrace();
			}
		} catch (Exception ioe) {
			System.out.println("Failed to read the system input.");
		}
	}

	public static byte[] readFileImage(String filename) throws IOException {
		BufferedInputStream bufferedInputStream = new BufferedInputStream(
				new FileInputStream(filename));
		int len = bufferedInputStream.available();
		byte[] bytes = new byte[len];
		int r = bufferedInputStream.read(bytes);
		if (len != r) {
			bytes = null;
			throw new IOException("read file error");
		}
		bufferedInputStream.close();
		return bytes;
	}

	public static void main(String[] args) {
		MyWeibo.upload("sdkfjlaskdl", "party.jpg");
	}

}
