package org.pytenix.service;

import lombok.Getter;
import org.pytenix.config.BotConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.LoaderOptions;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.Constructor;
import org.yaml.snakeyaml.nodes.Tag;
import org.yaml.snakeyaml.representer.Representer;

import java.io.*;

public class ConfigService {

    private static final Logger logger = LoggerFactory.getLogger(ConfigService.class);
    private static final String CONFIG_FILE = "config.yml";

    @Getter
    private final BotConfig config;

    private final Yaml yaml;

    public ConfigService() {

        DumperOptions options = new DumperOptions();
        options.setDefaultFlowStyle(DumperOptions.FlowStyle.BLOCK);
        options.setIndent(2);
        options.setPrettyFlow(true);

        Representer representer = new Representer(options);
        representer.addClassTag(Object.class, Tag.MAP);

        LoaderOptions loaderOptions = new LoaderOptions();
        loaderOptions.setTagInspector(tag -> true);
        this.yaml = new Yaml(new Constructor(Object.class, loaderOptions), representer, options);

        this.config = loadConfig();
    }


    public void initConfig() {
        File file = new File(CONFIG_FILE);
        BotConfig botConfig = new BotConfig("ABC");
        try (Writer writer = new FileWriter(file)) {


            yaml.dump(botConfig, writer);
        } catch (IOException e) {
            logger.error("Error while saving the config.", e);
            throw new RuntimeException("Error while saving the config.", e);
        }

    }

    private BotConfig loadConfig() {

        File file = new File(CONFIG_FILE);
        if(!file.exists()) {
            try {
                file.createNewFile();
                initConfig();
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
