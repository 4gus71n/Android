package com.example.rest.model;

import java.util.ArrayList;

import com.google.gson.annotations.SerializedName;

public class ServerWrapper {
	@SerializedName("st")
	private String status;
	@SerializedName("ob")
	private ArrayList<Company> object;

	public ServerWrapper() {
		super();
	}

	public ServerWrapper(String status, ArrayList<Company> object) {
		super();
		this.status = status;
		this.object = object;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public ArrayList<Company> getObject() {
		return object;
	}

	public void setObject(ArrayList<Company> object) {
		this.object = object;
	}

	@Override
	public String toString() {
		return "ServerWrapper [status=" + status + ", object=" + object.toString() + "]";
	}

	
}
