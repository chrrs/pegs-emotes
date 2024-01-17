package me.chrr.pegsemotes;

import java.util.Optional;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

public class Futures {
    private Futures() {
    }

    public static <T> Optional<T> poll(Future<T> future) {
        if (future.isDone()) {
            try {
                return Optional.of(future.get());
            } catch (InterruptedException | ExecutionException e) {
                throw new RuntimeException(e);
            }
        } else {
            return Optional.empty();
        }
    }
}
