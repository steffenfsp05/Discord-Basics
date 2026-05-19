package org.pytenix.discord.command;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.pytenix.service.MinecraftApiService;
import org.pytenix.service.RateLimitService;
import org.pytenix.util.EmbedFactory;
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
                .thenAccept(status -> {
                    EmbedBuilder embed = EmbedFactory.createStatusEmbed(status);

                    if (status.isOnline() && status.iconBase64() != null) {
                        try {
                            String cleanBase64 = status.iconBase64().split(",")[1];
                            byte[] bytes = java.util.Base64.getDecoder().decode(cleanBase64);

                            net.dv8tion.jda.api.utils.FileUpload upload =
                                    net.dv8tion.jda.api.utils.FileUpload.fromData(bytes, "icon.png");

                            embed.setThumbnail("attachment://icon.png");

                            event.getHook().editOriginalEmbeds(embed.build()).setFiles(upload).queue();
                        } catch (Exception e) {
                            logger.error("Error while decoding the server icon", e);
                        }
                    }

                    event.getHook().editOriginalEmbeds(embed.build()).queue();
                })
                .exceptionally(throwable -> {
                    logger.error("Error while status check.", throwable);
                    event.getHook().editOriginal("Error while status check.").queue();
                    return null;
                });
    }

}
