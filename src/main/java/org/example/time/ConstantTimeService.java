package org.example.time;

import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
public class ConstantTimeService implements TimeService {

    private Instant now = Instant.now();

    @Override
    public Instant getNow() {
        return now;
    }

    @Override
    public void setNow(Instant newNow) {
        if (newNow != null) {
            now = newNow;
        } else {
            now = Instant.now();
        }
    }
}
