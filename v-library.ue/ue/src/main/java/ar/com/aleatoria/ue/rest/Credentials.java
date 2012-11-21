package ar.com.aleatoria.ue.rest;

/**
 * Just a custom credential class. In the future if you want add some extra-data to the
 * credentials this is quite the right place to put them.
 * @author astinx
 * @see ar.com.aleatoria.ue#CredentialsProvider
 */
public class Credentials {
		protected String userName;
		protected String password;
		
		public Credentials(String userName, String password) {
			super();
			this.userName = userName;
			this.password = password;
		}
		
		public String getUserName() {
			return userName;
		}
		public void setUserName(String userName) {
			this.userName = userName;
		}
		public String getPassword() {
			return password;
		}
		public void setPassword(String password) {
			this.password = password;
		}
		  
}
