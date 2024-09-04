package com.example.demo.utility;



public class Constants {

	public enum UserRole {
		ROLE_USER("User"), ROLE_MANAGER("Manager");

		private String role;

		private UserRole(String role) {
			this.role = role;
		}

		public String value() {
			return this.role;
		}
	}

	public enum UserStatus {
		ACTIVE("Active"), DEACTIVATED("Deactivated");

		private String status;

		private UserStatus(String status) {
			this.status = status;
		}

		public String value() {
			return this.status;
		}
	}
	
	public enum EventStatus {
		ACTIVE("Active"), DEACTIVATED("Deactivated");

		private String status;

		private EventStatus(String status) {
			this.status = status;
		}

		public String value() {
			return this.status;
		}
	}
}