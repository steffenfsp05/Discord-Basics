package org.pytenix;

import lombok.Getter;
import org.pytenix.discord.DiscordService;
import org.pytenix.service.ConfigService;
import org.pytenix.service.MinecraftApiService;
import org.pytenix.service.RateLimitService;

@Getter
public class MinecraftStatusBot {
    public static void main(String[] args) {
        new MinecraftStatusBot();
    }

    final RateLimitService rateLimitService;
    final MinecraftApiService minecraftApiService;

    final ConfigService configService;
    final DiscordService discordService;



    public MinecraftStatusBot()
    {

        this.rateLimitService = new RateLimitService();
        this.minecraftApiService = new MinecraftApiService();

        this.configService = new ConfigService();
        this.discordService = new DiscordService(this);

    }
}