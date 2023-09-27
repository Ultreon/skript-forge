package com.ultreon.commons.skript.Todo;

public class Todo extends RuntimeException {
	public Todo(String message) {
		super(message);
	}

	public Todo(String message, Throwable cause) {
		super(message, cause);
	}

	public Todo(Throwable cause) {
		super(cause);
	}
}
