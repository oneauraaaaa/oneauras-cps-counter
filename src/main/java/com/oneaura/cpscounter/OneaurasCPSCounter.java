package com.oneaura.cpscounter;

import net.fabricmc.api.ModInitializer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class OneaurasCPSCounter implements ModInitializer {
	public static final String MOD_ID = "oneauras-cps-counter";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	@Override
	public void onInitialize() {
		LOGGER.info("your mom");
	}
}