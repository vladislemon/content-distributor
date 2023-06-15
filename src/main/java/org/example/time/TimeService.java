package org.example.time;

import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;

import java.time.Instant;

public interface TimeService {

    @NonNull
    Instant getNow();

    void setNow(@Nullable Instant newNow);

}
