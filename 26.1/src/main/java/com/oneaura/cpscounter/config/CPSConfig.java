package com.oneaura.cpscounter.config;

public class CPSConfig {
	public enum TextStyle {
		NONE,
		BOLD,
		ITALIC,
		UNDERLINED
	}

	public enum DisplayMode {
		BOTH,
		LEFT,
		RIGHT
	}

	public boolean enabled = true;
	public DisplayMode displayMode = DisplayMode.BOTH;
	public int hudX = 5;
	public int hudY = 5;
	public String labelText = " CPS";
	public String textColor = "FFFFFF";
	public boolean showBackground = true;
	public boolean textShadow = true;
	public TextStyle textStyle = TextStyle.NONE;
	public String backgroundColor = "80000000";
	public int backgroundCornerRadius = 0;
}
