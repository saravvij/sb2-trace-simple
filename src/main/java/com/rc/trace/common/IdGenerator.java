package com.rc.trace.common;

import java.util.UUID;

public class IdGenerator {
    public static String generateId() {
        return UUID.randomUUID().toString().replace("-", "");
    }
}

