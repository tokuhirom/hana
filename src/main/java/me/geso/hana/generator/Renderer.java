package me.geso.hana.generator;

public class Renderer {
	private final StringBuilder buf = new StringBuilder();
	
	@Override
	public String toString() {
		return buf.toString();
	}

	public void appendf(String format, Object... args) {
		buf.append(String.format(format, args));
	}

	public void append(String str) {
		buf.append(str);
	}
}
