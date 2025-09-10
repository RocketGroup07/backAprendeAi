package br.com.aprendeai.enums;

import lombok.Getter;

@Getter
public enum PapelEnum {
	ADMIN("admin"),
	USER("user");
	
	private String role;

	private PapelEnum(String role) {
		this.role = role;
	}

}
