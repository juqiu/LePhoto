package com.little.framework.utils;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 *
 */
public class SecurityUtils {

    private SecurityUtils() {
    }

    private static final char[] digits = new char[]{
            '0', '1', '2', '3', '4',//
            '5', '6', '7', '8', '9',//
            'a', 'b', 'c', 'd', 'e',//
            'f'};

    /**
     * Encrypt source string with default MD5 algorithm.
     *
     * @param source
     * @return
     */
    public static String encrypt(String source) {
        return encrypt(source, "MD5");
    }

    /**
     * Encrypt source string with corresponding algorithm.
     *
     * @param source
     * @param algorithm such as "MD5" .etc.
     * @return
     */
    public static String encrypt(String source, String algorithm) {
        if (source == null) {
            return null;
        }
        String result = null;
        try {
            MessageDigest digest = MessageDigest.getInstance(algorithm);
            digest.update(source.getBytes());
            result = bytes2HexStr(digest.digest());

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * Encrypt source file with default MD5 algorithm.
     *
     * @param file
     * @return
     */
    public static String encrypt(File file) {
        return encrypt(file, "MD5");
    }

    /**
     * Encrypt source file with corresponding algorithm.
     *
     * @param file
     * @param algorithm such as "MD5" .etc.
     * @return
     */
    public static String encrypt(File file, String algorithm) {
        if (file == null || !file.exists() || !file.isFile()) {
            return null;
        }
        String result = null;
        try {
            result = encryptOrThrow(file, algorithm);

        } catch (IOException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * Encrypt source file with default MD5 algorithm.
     *
     * @param file
     * @return
     */
    public static String encryptOrThrow(File file) throws IOException, NoSuchAlgorithmException {
        return encryptOrThrow(file, "MD5");
    }

    /**
     * Encrypt source file with corresponding algorithm.
     *
     * @param file
     * @param algorithm such as "MD5" .etc.
     * @return
     */
    public static String encryptOrThrow(File file, String algorithm) throws IOException, NoSuchAlgorithmException {
        if (file == null) {
            return null;
        }
        String result = null;
        FileInputStream fis = null;
        try {
            MessageDigest digest = MessageDigest.getInstance(algorithm);
            fis = new FileInputStream(file);
            int count;
            byte[] buffer = new byte[1024];
            while ((count = fis.read(buffer)) > 0) {
                digest.update(buffer, 0, count);
            }
            result = bytes2HexStr(digest.digest());

        } finally {
            if (fis != null) {
                try {
                    fis.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return result;
    }

    private static String bytes2HexStr(byte[] bytes) {
        if (bytes == null || bytes.length == 0) {
            return null;
        }

        char[] buf = new char[2 * bytes.length];
        for (int i = 0; i < bytes.length; i++) {
            byte b = bytes[i];
            buf[2 * i + 1] = digits[b & 0xF];
            b = (byte) (b >>> 4);
            buf[(2 * i)] = digits[b & 0xF];
        }
        return new String(buf);
    }

    //------------- crc --------------------
    private static final long POLY64REV = 0x95AC9329AC4BC9B5L;
    private static final long INITIALCRC = 0xFFFFFFFFFFFFFFFFL;

    private static long[] sCrcTable = new long[256];

    /**
     * A function that returns a 64-bit crc for string
     *
     * @param in input string
     * @return a 64-bit crc value
     */
    public static long crc64Long(String in) {
        if (in == null || in.length() == 0) {
            return 0;
        }
        return crc64Long(getBytes(in));
    }

    static {
        // http://bioinf.cs.ucl.ac.uk/downloads/crc64/crc64.c
        long part;
        for (int i = 0; i < 256; i++) {
            part = i;
            for (int j = 0; j < 8; j++) {
                long x = ((int) part & 1) != 0 ? POLY64REV : 0;
                part = (part >> 1) ^ x;
            }
            sCrcTable[i] = part;
        }
    }

    public static long crc64Long(byte[] buffer) {
        long crc = INITIALCRC;
        for (int k = 0, n = buffer.length; k < n; ++k) {
            crc = sCrcTable[(((int) crc) ^ buffer[k]) & 0xff] ^ (crc >> 8);
        }
        return crc;
    }

    //---------simple bytes ---------------
    public static byte[] getBytes(String in) {
        byte[] result = new byte[in.length() * 2];
        int output = 0;
        char[] charArray = in.toCharArray();
        for (char ch : charArray) {
            result[output++] = (byte) (ch & 0xFF);
            result[output++] = (byte) (ch >> 8);
        }
        return result;
    }
}
