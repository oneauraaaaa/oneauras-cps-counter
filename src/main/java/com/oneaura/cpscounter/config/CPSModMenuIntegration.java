package com.oneaura.cpscounter.config;

import com.oneaura.cpscounter.OneaurasCPSCounterClient;
import com.oneaura.cpscounter.configlib.gui.ClothConfigScreenBuilder;
import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;

public class CPSModMenuIntegration implements ModMenuApi {
    @Override
    public ConfigScreenFactory<?> getModConfigScreenFactory() {
        return parent -> ClothConfigScreenBuilder.create(parent, OneaurasCPSCounterClient.MOD_ID);
    }
}
