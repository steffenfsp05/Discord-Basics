package org.pytenix.service;

import lombok.Getter;
import org.pytenix.config.BotConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.yaml.snakeyaml.LoaderOptions;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.Constructor;

import java.io.*;

public class ConfigService {

    private static final Logger logger = LoggerFactory.getLogger(ConfigService.class);
    private static final String CONFIG_FILE = "config.yml";

    @Getter
    private final BotConfig config;

    public ConfigService() {
        this.config = loadConfig();
    }

    private BotConfig loadConfig() {
        LoaderOptions options = new LoaderOptions();
        Yaml yaml = new Yaml(new Constructor(BotConfig.class, options));

        File file = new File(CONFIG_FILE);
        if(!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }



        try (InputStream inputStream = new FileInputStream(CONFIG_FILE)) {
            BotConfig loadedConfig = yaml.load(inputStream);

            if (loadedConfig == null || loadedConfig.getToken() == null || loadedConfig.getToken().isEmpty()) {
                throw new IllegalStateException("Token is empty in the config!");
            }

            logger.info("Successfully load the configuration!");
            return loadedConfig;

        }  catch (Exception e) {
            logger.error("Error while reading the configuration file.", e);
            throw new RuntimeException("Error while reading the configuration file.", e);
        }
    }

}
