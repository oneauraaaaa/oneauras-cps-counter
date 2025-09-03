package com.oneaura.cpscounter.config;

import com.oneaura.cpscounter.OneaurasCPSCounterClient; // Ana sınıfı import ediyoruz
import com.oneaura.cpscounter.configlib.Comment;
import com.oneaura.cpscounter.configlib.ConfigHolder;

public class CPSConfig extends ConfigHolder {

    @Comment("CPS Sayacı ekranda görünsün mü?")
    public boolean enabled = true;

    @Comment("Sayacın ekrandaki konumu.")
    public OneaurasCPSCounterClient.HudPosition hudPosition = OneaurasCPSCounterClient.HudPosition.TOP_LEFT;

}
