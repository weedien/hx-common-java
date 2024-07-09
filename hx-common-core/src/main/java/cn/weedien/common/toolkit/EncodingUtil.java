package cn.weedien.common.toolkit;

import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.ByteBuffer;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.CodingErrorAction;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;


public class EncodingUtil {

    public static String hexEncode(byte[] input) {
        char[] hexChars = new char[input.length * 2];
        for (int j = 0; j < input.length; j++) {
            int v = input[j] & 0xFF;
            hexChars[j * 2] = Character.toUpperCase(Character.forDigit(v >>> 4, 16));
            hexChars[j * 2 + 1] = Character.toUpperCase(Character.forDigit(v & 0x0F, 16));
        }
        return new String(hexChars);
    }

    public static byte[] hexDecode(String input) {
        byte[] bytes = new byte[input.length() / 2];
        for (int i = 0; i < input.length(); i += 2) {
            bytes[i / 2] = (byte) ((Character.digit(input.charAt(i), 16) << 4)
                    + Character.digit(input.charAt(i + 1), 16));
        }
        return bytes;
    }

    public static String binaryEncode(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        for (byte b : bytes) {
            sb.append(String.format("%08d", Integer.parseInt(Integer.toBinaryString(b & 0xFF))));
        }
        return sb.toString();
    }

    public static byte[] binaryDecode(String bin) {
        byte[] bytes = new byte[bin.length() / 8];
        for (int i = 0; i < bin.length(); i += 8) {
            bytes[i / 8] = (byte) Integer.parseInt(bin.substring(i, i + 8), 2);
        }
        return bytes;
    }

    public static String hexEncodeWithBlockSize(byte[] bytes, int blockSize) {

        String hex = hexEncode(bytes);

        // one hex = 2 chars
        blockSize = blockSize * 2;

        // better idea how to print this?
        List<String> result = new ArrayList<>();
        int index = 0;
        while (index < hex.length()) {
            result.add(hex.substring(index, Math.min(index + blockSize, hex.length())));
            index += blockSize;
        }

        return result.toString();

    }

    public static String base64Encode(String input) {
        return Base64.getEncoder().encodeToString(input.getBytes());
    }

    public static byte[] base64Encode(byte[] input) {
        return Base64.getEncoder().encode(input);
    }

    public static String base64EncodeToString(byte[] input) {
        return Base64.getEncoder().encodeToString(input);
    }

    public static String base64Decode(String input) {
        byte[] decoded = Base64.getDecoder().decode(input);
        // 判断是否是文本
        boolean validUTF8 = isValidUTF8(decoded);
        if (validUTF8) {
            return new String(decoded, StandardCharsets.UTF_8);
        }
        // 将字节数组以01形式输出
        System.out.println("\033[33mWARN\033[0m: The decoded data is not valid UTF-8 text.");
        return binaryEncode(decoded);
    }

    public static byte[] base64Decode(byte[] input) {
        return Base64.getDecoder().decode(input);
    }

    public static String urlEncode(String input) {
        return URLEncoder.encode(input, StandardCharsets.UTF_8);
    }

    public static String urlDecode(String input) {
        return URLDecoder.decode(input, StandardCharsets.UTF_8);
    }

    public static boolean isValidUTF8(byte[] input) {
        CharsetDecoder cs = StandardCharsets.UTF_8.newDecoder();
        cs.onMalformedInput(CodingErrorAction.REPORT);
        cs.onUnmappableCharacter(CodingErrorAction.REPORT);
        try {
            cs.decode(ByteBuffer.wrap(input));
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}