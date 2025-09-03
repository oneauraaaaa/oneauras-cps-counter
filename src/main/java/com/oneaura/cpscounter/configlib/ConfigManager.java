package com.oneaura.cpscounter.configlib;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.fabricmc.loader.api.FabricLoader;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Field;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class ConfigManager {
    private static final Map<String, ConfigHolder> configs = new HashMap<>();
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private static final Path CONFIG_DIR = FabricLoader.getInstance().getConfigDir();

    public static <T extends ConfigHolder> Optional<T> register(String modId, Class<T> configClass) {
        try {
            Path path = CONFIG_DIR.resolve(modId + ".json");
            T configInstance = configClass.getDeclaredConstructor().newInstance();

            if (path.toFile().exists()) {
                load(configInstance, path);
            }

            // Her zaman kaydet, yeni eklenen ayarlar dosyaya yazılsın
            save(configInstance, path);
            configs.put(modId, configInstance);

            return Optional.of(configInstance);
        } catch (Exception e) {
            e.printStackTrace();
            return Optional.empty();
        }
    }

    public static ConfigHolder get(String modId) {
        return configs.get(modId);
    }

    /**
     * Ayar ekranından çağrılmak üzere tasarlanmış YENİ PUBLIC SAVE metodu.
     * @param modId Kaydedilmesi gereken modun kimliği.
     */
    public static void save(String modId) {
        ConfigHolder config = configs.get(modId);
        if (config != null) {
            Path path = CONFIG_DIR.resolve(modId + ".json");
            try {
                // Dahili (private) save metodunu çağırır.
                save(config, path);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    // Bu metot artık private ve sadece dahili olarak kullanılıyor.
    private static void save(ConfigHolder config, Path path) throws IOException {
        try (FileWriter writer = new FileWriter(path.toFile())) {
            GSON.toJson(config, writer);
        }
    }

    private static void load(ConfigHolder config, Path path) throws IOException, IllegalAccessException {
        try (FileReader reader = new FileReader(path.toFile())) {
            ConfigHolder loadedConfig = GSON.fromJson(reader, config.getClass());
            if (loadedConfig != null) {
                for (Field field : config.getClass().getDeclaredFields()) {
                    field.setAccessible(true);
                    Object loadedValue = field.get(loadedConfig);
                    if (loadedValue != null) {
                        field.set(config, loadedValue);
                    }
                }
            }
        }
    }
}

