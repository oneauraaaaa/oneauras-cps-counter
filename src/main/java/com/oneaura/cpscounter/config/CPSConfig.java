package com.oneaura.cpscounter.config;

import com.oneaura.cpscounter.OneaurasCPSCounterClient; // Ana sınıfı import ediyoruz
import com.oneaura.cpscounter.configlib.Comment;
import com.oneaura.cpscounter.configlib.ConfigHolder;

public class CPSConfig extends ConfigHolder {

    @Comment("Enable or disable the CPS Counter on the screen.")
    public boolean enabled = true;

    @Comment("The position of the counter on the screen.")
    public OneaurasCPSCounterClient.HudPosition hudPosition = OneaurasCPSCounterClient.HudPosition.TOP_LEFT;

    @Comment("Show the 'CPS' label next to the numbers.")
    public boolean showLabel = true;

    @Comment("The color of the text as a Hex code (e.g., FFFFFF for white).")
    public String textColor = "FFFFFF";

    @Comment("Show the background.")
    public boolean showBackground = true;

    @Comment("Show text shadow.")
    public boolean textShadow = true;

    @Comment("The style of the text (e.g., bold, italic).")
    public OneaurasCPSCounterClient.TextStyle textStyle = OneaurasCPSCounterClient.TextStyle.NONE;

    @Comment("The color and opacity of the background as a Hex code (e.g., 80000000 for semi-transparent black).")
    public String backgroundColor = "80000000";

    @Comment("The padding (space) between the text and the edge of the screen, in pixels.")
    public int padding = 5;

    @Comment("The corner radius for the background, in pixels. Set to 0 for sharp corners.")
    public int backgroundCornerRadius = 0;
}
