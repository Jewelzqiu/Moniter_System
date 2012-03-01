package osgi_interface;

public interface Services {
	
	public void sendImage(byte[] bytes, String filename);
	
	public byte[] getImage(String filename);
	
	public String getList();
	
}
