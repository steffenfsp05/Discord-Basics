package org.pytenix.discord.command;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.pytenix.service.MinecraftApiService;
import org.pytenix.service.RateLimitService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.*;

public class StatusCommand extends ListenerAdapter {

    private static final Logger logger = LoggerFactory.getLogger(StatusCommand.class);

    private final RateLimitService rateLimitService;
    private final MinecraftApiService apiService;

    public StatusCommand(RateLimitService rateLimitService, MinecraftApiService apiService) {
        this.rateLimitService = rateLimitService;
        this.apiService = apiService;
    }

    @Override
    public void onSlashCommandInteraction(SlashCommandInteractionEvent event) {
        if (!event.getName().equals("status")) return;

        long userId = event.getUser().getIdLong();

        if (!rateLimitService.tryConsume(userId)) {
            logger.warn("Ratelimit for: {}", event.getUser().getAsTag());
            event.reply("⏳ Please slow down.")
                    .setEphemeral(true)
                    .queue();
            return;
        }

        String serverIp = event.getOption("ip").getAsString();
        event.deferReply().queue();

        apiService.checkServerStatusAsync(serverIp)
                .thenAccept(isOnline -> {
                    EmbedBuilder embed = new EmbedBuilder()
                            .setTitle("Minecraft Server Status")
                            .addField("IP-Adress", serverIp, false);

                    if (isOnline) {
                        embed.setColor(Color.GREEN).setDescription("✅ This server is **online**!");
                    } else {
                        embed.setColor(Color.RED).setDescription("❌ This server is **offline**.");
                    }

                    event.getHook().editOriginalEmbeds(embed.build()).queue();
                    logger.info("Status for {} sent. Online: {}", serverIp, isOnline);
                })
                .exceptionally(throwable -> {
                    logger.error("Error while checking the status for " + serverIp, throwable);
                    event.getHook().editOriginal("Error while retrieving infos.").queue();
                    return null;
                });
    }

}
