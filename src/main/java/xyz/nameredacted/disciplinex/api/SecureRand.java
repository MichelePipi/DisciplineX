package xyz.nameredacted.disciplinex.api;

import java.security.SecureRandom;

public class SecureRand {

    final SecureRandom secRand = new SecureRandom();

    public static byte[] getSecureRandBytes(int length) {
        byte[] bytes = new byte[length];
        new SecureRandom().nextBytes(bytes);
        return bytes;
    }

    /**
     * Convert to SHA256 hash given hash
     */


}

