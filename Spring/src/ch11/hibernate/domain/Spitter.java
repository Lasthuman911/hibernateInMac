package ch11.hibernate.domain;

import javax.persistence.*;


@Entity
@Table(name = "userprofile")
public class Spitter {
	
	private Spitter() {}

	@Id
	//@GeneratedValue(strategy=GenerationType.IDENTITY)
	private String id;

	@Column(name="username")
	private String username;

	@Column(name="password")
	private String password;


	public Spitter(String id, String username, String password) {
		this.id = id;
		this.username = username;
		this.password = password;
	}

	public String getId() {
		return id;
	}

	public String getUsername() {
		return username;
	}

	public String getPassword() {
		return password;
	}


}
