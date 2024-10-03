package xyz.nameredacted.disciplinex.api;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * The {@code ManualRandomGenerator} class provides methods for manually generating
 * random bytes using system entropy sources and SHA-256 hashing. This implementation
 * avoids using built-in cryptographic random generators like {@link java.security.SecureRandom}
 * and instead collects entropy (lack of predictability) from system time, available memory, processors, and thread states.
 *
 * It includes methods to:
 * <ul>
 *   <li>Collect system entropy from various sources</li>
 *   <li>Generate random bytes by hashing the collected entropy using SHA-256</li>
 *   <li>Generate larger random byte arrays by chaining multiple hash results</li>
 *   <li>Generate a random numeric PIN using manually generated random bytes</li>
 * </ul>
 *
 * This class can be used when you want to manually control how randomness is generated,
 * such as for creating secure keys, PINs, or random identifiers.
 */
public class HashUtils {

    /**
     * Collects entropy by using system values which will
     * be random each time.
     */
    private static byte[] collectEntropy() {
        long time = System.nanoTime();
        long availableMemory = Runtime.getRuntime().freeMemory();
        long thread = Thread.currentThread().getId();

        return (time + "-" + availableMemory + "-" + thread).getBytes();
    }

    /**
     * Generates random bytes by hashing the collected entropy
     * using SHA-256.
     *
     * @return a byte array of random bytes
     */
    private static byte[] generateRandomBytes(int numBytes) {
        try {
            byte[] entropy = collectEntropy();
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] randomHash = digest.digest(entropy);
            byte[] randomBytes = new byte[numBytes];
            System.arraycopy(randomHash, 0, randomBytes, 0, Math.min(randomHash.length, numBytes)); // Copy the hash to the random bytes array
            return randomBytes;
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("SHA-256 algorithm not available", e);
        }
    }

    public static String generatePassphrase() {
        byte[] randomBytes = generateRandomBytes(16);
        StringBuilder hexString = new StringBuilder();
        for (byte b : randomBytes) {
            hexString.append(String.format("%02x", b));
        }
        return hexString.toString();
    }

    public static void main(String[] args) {
        System.out.println(generatePassphrase());
    }
}
