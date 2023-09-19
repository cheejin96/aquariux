package com.toppan.model;

import java.sql.Timestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name="University")
public class University {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;
	
	@Column(name="name")
	private String name;
	
	@Column(name="country")
	private String country;
	
	@Column(name="webpages")
	private String webpages;
	
	@Column(name="isBookmark")
	private boolean isBookmark;
	
	@Column(name="isActive")
	private boolean isActive;
	
	@Column(name="created")
	private Timestamp created;
	
	@Column(name="lastModified")
	private Timestamp lastModified;
	
	@Column(name="deletedAt")
	private Timestamp deletedAt;

	public University() {
		this.isBookmark = false;
		this.isActive = true;
		Timestamp timestamp = new Timestamp(System.currentTimeMillis());
		this.created = timestamp;
		this.lastModified = timestamp;
		this.deletedAt = null;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public String getWebpages() {
		return webpages;
	}

	public void setWebpages(String webpages) {
		this.webpages = webpages;
	}

	public boolean isBookmark() {
		return isBookmark;
	}

	public void setBookmark(boolean isBookmark) {
		this.isBookmark = isBookmark;
	}

	public boolean isActive() {
		return isActive;
	}

	public void setActive(boolean isActive) {
		this.isActive = isActive;
	}

	public Timestamp getCreated() {
		return created;
	}

	public void setCreated(Timestamp created) {
		this.created = created;
	}

	public Timestamp getLastModified() {
		return lastModified;
	}

	public void setLastModified(Timestamp lastModified) {
		this.lastModified = lastModified;
	}

	public Timestamp getDeletedAt() {
		return deletedAt;
	}

	public void setDeletedAt(Timestamp deletedAt) {
		this.deletedAt = deletedAt;
	}

	@Override
	public String toString() {
		return "University [id=" + id + ", name=" + name + ", country=" + country + ", webpages=" + webpages
				+ ", isBookmark=" + isBookmark + ", isActive=" + isActive + ", created=" + created + ", lastModified="
				+ lastModified + ", deletedAt=" + deletedAt + "]";
	}
}
