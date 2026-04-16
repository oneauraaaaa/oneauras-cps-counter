package com.oneaura.cpscounter.configlib;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.fabricmc.loader.api.FabricLoader;

import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

public final class ConfigManager {
	private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
	private static final Path CONFIG_DIR = FabricLoader.getInstance().getConfigDir();
	private static final Map<String, Object> CACHE = new ConcurrentHashMap<>();

	private ConfigManager() {
	}

	public static <T> Optional<T> register(String modId, Class<T> type) {
		Path path = CONFIG_DIR.resolve(modId + ".json");

		try {
			T value = load(path, type).orElseGet(() -> instantiate(type));
			save(path, value);
			CACHE.put(modId, value);
			return Optional.of(value);
		} catch (Exception e) {
			return Optional.empty();
		}
	}

	public static void save(String modId) {
		Object value = CACHE.get(modId);
		if (value == null) {
			return;
		}

		try {
			save(CONFIG_DIR.resolve(modId + ".json"), value);
		} catch (IOException e) {
			throw new RuntimeException("Failed to save config for " + modId, e);
		}
	}

	public static <T> T get(String modId, Class<T> type) {
		Object value = CACHE.get(modId);
		if (type.isInstance(value)) {
			return type.cast(value);
		}
		return null;
	}

	private static <T> Optional<T> load(Path path, Class<T> type) throws IOException {
		if (!Files.exists(path)) {
			return Optional.empty();
		}

		try (Reader reader = Files.newBufferedReader(path)) {
			return Optional.ofNullable(GSON.fromJson(reader, type));
		}
	}

	private static void save(Path path, Object config) throws IOException {
		Files.createDirectories(path.getParent());
		try (Writer writer = Files.newBufferedWriter(path)) {
			GSON.toJson(config, writer);
		}
	}

	private static <T> T instantiate(Class<T> type) {
		try {
			return type.getDeclaredConstructor().newInstance();
		} catch (Exception e) {
			throw new IllegalStateException("Could not create config: " + type.getName(), e);
		}
	}
}
