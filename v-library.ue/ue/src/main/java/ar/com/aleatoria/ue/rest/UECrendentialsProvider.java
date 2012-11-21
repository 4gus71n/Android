package ar.com.aleatoria.ue.rest;

public class UECrendentialsProvider implements CredentialsProvider {
	private Credentials credentials;
	
	public UECrendentialsProvider(Credentials credentials) {
		this.credentials = credentials;
	}

	@Override
	public Credentials getCredentials(String realm) {
		return this.credentials;
	}

}
