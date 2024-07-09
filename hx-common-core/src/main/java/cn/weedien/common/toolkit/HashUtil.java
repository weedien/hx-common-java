package cn.weedien.common.toolkit;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class HashUtil {

    /**
     * 计算字符串的MD5哈希值
     *
     * @param input 输入字符串
     * @return MD5哈希值(32位十六进制字符串)
     */
    public static String getMD5Hash(String input) {
        return getHash(input, "MD5");
    }

    public static String getMD5Hash(byte[] input) {
        return getHash(input, "MD5");
    }

    /**
     * 计算字符串的SHA-1哈希值
     *
     * @param input 输入字符串
     * @return SHA-1哈希值(40位十六进制字符串)
     */
    public static String getSHA1Hash(String input) {
        return getHash(input, "SHA-1");
    }

    /**
     * 计算字符串的SHA-256哈希值
     *
     * @param input 输入字符串
     * @return SHA-256哈希值(64位十六进制字符串)
     */
    public static String getSHA256Hash(String input) {
        return getHash(input, "SHA-256");
    }

    /**
     * 计算字符串的SHA-384哈希值
     *
     * @param input 输入字符串
     * @return SHA-384哈希值(96位十六进制字符串)
     */
    public static String getSHA384Hash(String input) {
        return getHash(input, "SHA-384");
    }

    /**
     * 计算字符串的SHA-512哈希值
     *
     * @param input 输入字符串
     * @return SHA-512哈希值(128位十六进制字符串)
     */
    public static String getSHA512Hash(String input) {
        return getHash(input, "SHA-512");
    }

    /**
     * 计算字符串的哈希值(支持MD5、SHA-1、SHA-256、SHA-384、SHA-512等算法),并可选择加盐
     *
     * @param input     输入字符串
     * @param algorithm 哈希算法(MD5、SHA-1、SHA-256、SHA-384、SHA-512等)
     * @param salt      盐值(可选)
     * @return 哈希值
     */
    public static String getHash(byte[] input, String algorithm, String salt) {
        try {
            MessageDigest md = MessageDigest.getInstance(algorithm);
            if (salt != null && !salt.isEmpty()) {
                md.update(salt.getBytes());
            }
            byte[] hash = md.digest(input);
            return EncodingUtil.hexEncode(hash);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("无法找到指定的哈希算法: " + algorithm, e);
        }
    }

    /**
     * 计算字符串的哈希值(支持MD5、SHA-1、SHA-256、SHA-384、SHA-512等算法)
     *
     * @param input     输入字符串
     * @param algorithm 哈希算法(MD5、SHA-1、SHA-256、SHA-384、SHA-512等)
     * @return 哈希值(十六进制字符串)
     */
    public static String getHash(String input, String algorithm) {
        return getHash(input.getBytes(), algorithm, null);
    }

    public static String getHash(byte[] input, String algorithm) {
        return getHash(input, algorithm, null);
    }

    public static void main(String[] args) {
        String input = "Hello, World!";
        System.out.println("MD5 Hash: " + getMD5Hash(input));
        System.out.println("SHA-1 Hash: " + getSHA1Hash(input));
        System.out.println("SHA-256 Hash: " + getSHA256Hash(input));
        System.out.println("SHA-384 Hash: " + getSHA384Hash(input));
        System.out.println("SHA-512 Hash: " + getSHA512Hash(input));
        System.out.println("SHA-256 Hash (with salt): " + getHash(input.getBytes(), "sha256", "123456"));
    }
}
