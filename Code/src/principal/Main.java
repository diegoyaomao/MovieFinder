package principal;

public class Main {
	/**
	 * Launch the application.
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			Principal window = Principal.getInstance();
			window.open();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
