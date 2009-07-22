package zmanimbot.users;

public class UserTester {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		User test = new User("XMPP","testzmanimbot3@gmail.com");
		test.PrintInfo();
		test.setDefaultLocation("Palm Desert");
	}
}
