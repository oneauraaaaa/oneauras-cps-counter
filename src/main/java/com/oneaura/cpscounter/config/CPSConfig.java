package com.oneaura.cpscounter.config;

import com.oneaura.cpscounter.OneaurasCPSCounterClient; // Ana sınıfı import ediyoruz
import com.oneaura.cpscounter.configlib.Comment;
import com.oneaura.cpscounter.configlib.ConfigHolder;

public class CPSConfig extends ConfigHolder {

    @Comment("Do you want to enable CPS Counter?")
    public boolean enabled = true;

    @Comment("HUD Position")
    public OneaurasCPSCounterClient.HudPosition hudPosition = OneaurasCPSCounterClient.HudPosition.TOP_LEFT;

}
