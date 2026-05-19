package org.pytenix.service;

import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;

import java.time.Duration;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class RateLimitService {

    private final Map<Long, Bucket> buckets = new ConcurrentHashMap<>();


    public boolean tryConsume(long userId) {
        return resolveBucket(userId).tryConsume(1);
    }

    private Bucket resolveBucket(long userId) {
        return buckets.computeIfAbsent(userId, k -> Bucket.builder()
                .addLimit(Bandwidth.builder()
                        .capacity(3)
                        .refillIntervally(1, Duration.ofSeconds(5))
                        .build())
                .build());
    }
}
