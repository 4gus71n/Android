package ar.com.aleatoria.ue.rest;

public interface CredentialsProvider {
	Credentials getCredentials(String realm);
}
