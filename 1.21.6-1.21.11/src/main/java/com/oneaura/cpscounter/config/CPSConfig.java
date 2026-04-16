package com.oneaura.cpscounter.config;

import com.oneaura.cpscounter.OneaurasCPSCounterClient;
import com.oneaura.cpscounter.configlib.Comment;
import com.oneaura.cpscounter.configlib.ConfigHolder;

public class CPSConfig extends ConfigHolder {
    public enum DisplayMode {
        BOTH,
        LEFT,
        RIGHT
    }

    @Comment("Enable Or Disable The CPS Counter On The Screen.")
    public boolean enabled = true;

    @Comment("Choose what click values are rendered: BOTH, LEFT, or RIGHT.")
    public DisplayMode displayMode = DisplayMode.BOTH;

    @Comment("The X Position Of The Counter On The Screen, In Pixels.")
    public int hudX = 5;

    @Comment("The Y Position Of The Counter On The Screen, In Pixels.")
    public int hudY = 5;

    @Comment("The Custom Text Label To Show After The Numbers. Leave Blank To Hide.")
    public String labelText = " CPS";

    // @Comment("Show The 'CPS' Label Next To The Numbers.")
    // public boolean showLabel = true;

    @Comment("The Color Of The Text As A Hex Code (e.g., FFFFFF For White).")
    public String textColor = "FFFFFF";

    @Comment("Show The Background.")
    public boolean showBackground = true;

    @Comment("Show Text Shadow.")
    public boolean textShadow = true;

    @Comment("The Style Of The Text (e.g., Bold, Italic).")
    public OneaurasCPSCounterClient.TextStyle textStyle = OneaurasCPSCounterClient.TextStyle.NONE;

    @Comment("The Color And Opacity Of The Background As A Hex Code (e.g., 80000000 For Semi-Transparent Black).")
    public String backgroundColor = "80000000";

    @Comment("The Corner Radius For The Background, In Pixels. Set To 0 For Sharp Corners.")
    public int backgroundCornerRadius = 0;
}
