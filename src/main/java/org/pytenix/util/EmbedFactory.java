package org.pytenix.util;

import net.dv8tion.jda.api.EmbedBuilder;
import org.pytenix.model.ServerStatus;

import java.awt.*;
import java.time.Instant;

public class EmbedFactory {
    public static EmbedBuilder createStatusEmbed(ServerStatus status) {
        EmbedBuilder embed = new EmbedBuilder()
                .setTitle("Server Status: " + status.ip())
                .setTimestamp(Instant.now())
                .setFooter("Powered by MinecraftStatus API");

        if (status.isOnline()) {
            embed.setColor(Color.GREEN)
                    .setDescription("✅ Server is **online**.")
                    .addField("Status", "Online", true)
                    .addField("Players", status.onlinePlayers() + " / " + status.maxPlayers(), true)
                    .addField("Version", status.version(), false);
        } else {
            embed.setColor(Color.RED)
                    .setDescription("❌ This server is **offline**.");
        }

        return embed;
    }
}
