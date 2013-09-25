package com.example.rest.model;

import com.google.gson.annotations.SerializedName;

public class Company {
	@SerializedName("id")
	private Long id;
	@SerializedName("name")
	private String name;
	@SerializedName("description")
	private String info;

	public Company() {
		super();
	}

	public Company(Long id, String name, String info) {
		super();
		this.id = id;
		this.name = name;
		this.info = info;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getInfo() {
		return info;
	}

	public void setInfo(String info) {
		this.info = info;
	}

	@Override
	public String toString() {
		return "Company [id=" + id + ", name=" + name + ", info=" + info + "]";
	}

}
