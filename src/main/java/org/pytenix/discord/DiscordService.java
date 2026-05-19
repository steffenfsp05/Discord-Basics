package org.pytenix.discord;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import org.pytenix.MinecraftStatusBot;
import org.pytenix.discord.command.StatusCommand;
import org.pytenix.service.MinecraftApiService;
import org.pytenix.service.RateLimitService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DiscordService {

    private static final Logger logger = LoggerFactory.getLogger(DiscordService.class);

    final MinecraftStatusBot minecraftStatusBot;
    final MinecraftApiService minecraftApiService;
    final RateLimitService rateLimitService;

    public DiscordService(MinecraftStatusBot minecraftStatusBot)
    {
        this.minecraftStatusBot = minecraftStatusBot;
        this.minecraftApiService = minecraftStatusBot.getMinecraftApiService();
        this.rateLimitService = minecraftStatusBot.getRateLimitService();

        StatusCommand statusCommand = new StatusCommand(rateLimitService, minecraftApiService);

        try {
            JDA jda = JDABuilder.createLight("token")
                    .addEventListeners(statusCommand)
                    .build();

            // Slash Command Registrierung
            jda.updateCommands().addCommands(
                    Commands.slash("status", "Shows the status of an Minecraft-Server")
                            .addOption(OptionType.STRING, "ip", "The Server-IP (e.g. hypixel.net)", true)
            ).queue();

            logger.info("Successfully started the application");
        } catch (Exception e) {
            logger.error("Critical Error", e);
        }
    }

}
