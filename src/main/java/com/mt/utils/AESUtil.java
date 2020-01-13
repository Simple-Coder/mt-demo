package com.mt.utils;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.digest.DigestUtils;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.spec.SecretKeySpec;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.Key;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

public class AESUtil {
    // optional value AES/DES/DESede
    public static String DES = "AES";
    // optional value AES/DES/DESede
    public static String CIPHER_ALGORITHM = "AES";


    private static int keyLength = 32;

    private static String privateKey = "LS0tLS1CRUdJTiBDRVJUSUZJQ0FURS0tLS0tCk1JSUN2akNDQWllZ0F3SUJBZ0lKQU5EVHlsb1VZdFRDTUEwR0NTcUdTSWIzRFFFQkJRVUFNSGd4Q3pBSkJnTlYKQkFZVEFtTnVNUXN3Q1FZRFZRUUlEQUp6WXpFTE1Ba0dBMVVFQnd3Q1kyUXhDekFKQmdOVkJBb01BbXBrTVEwdwpDd1lEVlFRTERBUnFaR3B5TVJFd0R3WURWUVFEREFoallY";

    private static String DEFAULT_CIPHER_ALGORITHM = "AES/ECB/PKCS5Padding";

    public static Key getKey(String strKey) {
        try {
            if (strKey == null) {
                strKey = "";
            }
            KeyGenerator _generator = KeyGenerator.getInstance("AES");
            SecureRandom secureRandom = SecureRandom.getInstance("SHA1PRNG");
            secureRandom.setSeed(strKey.getBytes());
            _generator.init(128, secureRandom);
            return _generator.generateKey();
        } catch (Exception e) {
            throw new RuntimeException(" 密钥出现异常 ");
        }
    }

    public static String encrypt(String data, String key) throws Exception {
        /*SecureRandom sr = new SecureRandom();
        Key secureKey = getKey(key);
        Cipher cipher = Cipher.getInstance(CIPHER_ALGORITHM);
        cipher.init(Cipher.ENCRYPT_MODE, secureKey, sr);
        byte[] bt = cipher.doFinal(data.getBytes());
        String strS = Base64.encodeBase64String(bt);
        return strS;*/
        try {
            byte[] tmp = new byte[keyLength];
            for (int i = 0; i < keyLength; i++) {
                if (i < key.getBytes(Charset.forName("UTF-8")).length) {
                    tmp[i] = key.getBytes(Charset.forName("UTF-8"))[i];
                } else {
                    tmp[i] = 0x01;
                }
            }
            // 转换为AES专用密钥
            SecretKeySpec skey = new SecretKeySpec(tmp, "AES");
            // 创建密码器
            Cipher cipher = Cipher.getInstance(DEFAULT_CIPHER_ALGORITHM);
            byte[] byteContent = data.getBytes("utf-8");
            // 初始化为加密模式的密码器
            cipher.init(Cipher.ENCRYPT_MODE, skey);
            // 加密
            return parseByte2HexStr(cipher.doFinal(byteContent));
        } catch (Exception e) {
            e.printStackTrace();
            //.error("加密异常", e);
        }
        return null;
    }


    public static String decrypt(String message, String key) throws Exception {
        /*SecureRandom sr = new SecureRandom();
        Cipher cipher = Cipher.getInstance(CIPHER_ALGORITHM);
        Key secureKey = getKey(key);
        cipher.init(Cipher.DECRYPT_MODE, secureKey, sr);
        byte[] res = Base64.decodeBase64(message);
        res = cipher.doFinal(res);
        return new String(res);*/

        try {
            byte[] tmp = new byte[keyLength];
            for (int i = 0; i < keyLength; i++) {
                if (i < key.getBytes(Charset.forName("UTF-8")).length) {
                    tmp[i] = key.getBytes(Charset.forName("UTF-8"))[i];
                } else {
                    tmp[i] = 0x01;
                }
            }
            // 转换为AES专用密钥
            SecretKeySpec skey = new SecretKeySpec(tmp, "AES");
            // 创建密码器
            Cipher cipher = Cipher.getInstance(DEFAULT_CIPHER_ALGORITHM);
            // 初始化为解密模式的密码器
            cipher.init(Cipher.DECRYPT_MODE, skey);
            // 明文
            return new String(cipher.doFinal(parseHexStr2Byte(message)), "UTF-8");

        } catch (Exception e) {
            e.printStackTrace();
            //LOGGER.error("解密异常", e);
        }
        return null;
    }

    /**
     * 摘要处理
     *
     * @param data 待摘要数据
     * @return 摘要字符串
     */
    public static String shaHex(byte[] data) {
//        return DigestUtils.md5Hex(data);
//        byte[] bytes = (text + key).getBytes(Charset.forName("UTF-8"));
        StringBuilder sb = null;
        MessageDigest messageDigest = null;
        try {
            messageDigest = MessageDigest.getInstance("MD5");
            messageDigest.update(data);
            data = messageDigest.digest();

            sb = new StringBuilder();
            for (int i = 0; i < data.length; i++) {
                if ((data[i] & 0xff) < 0x10) {
                    sb.append("0");
                }

                sb.append(Long.toString(data[i] & 0xff, 16));
            }
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
//            LOGGER.error("md5-异常", e);
            return null;
        }
        return sb.toString().toLowerCase();
    }

    /**
     * MD5方法
     *
     * @param text 明文
     * @param key  密钥
     * @return 密文
     * @throws Exception
     */
    public static String md5(String text, String key) {
        byte[] bytes = (text + key).getBytes(Charset.forName("UTF-8"));
        StringBuilder sb = null;
        MessageDigest messageDigest = null;
        try {
            messageDigest = MessageDigest.getInstance("MD5");
            messageDigest.update(bytes);
            bytes = messageDigest.digest();

            sb = new StringBuilder();
            for (int i = 0; i < bytes.length; i++) {
                if ((bytes[i] & 0xff) < 0x10) {
                    sb.append("0");
                }

                sb.append(Long.toString(bytes[i] & 0xff, 16));
            }
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
//            LOGGER.error("md5-异常", e);
            return null;
        }
        return sb.toString().toLowerCase();
    }

    /**
     * 验证
     *
     * @param data          待摘要数据
     * @param messageDigest 摘要字符串
     * @return 验证结果
     */
    public static boolean vailidate(byte[] data, String messageDigest) {
        return messageDigest.equals(shaHex(data));
    }




    /**
     * 加密body
     *
     * @param body
     * @return
     */
    private String lsbEncrypt(String body) {
        try {
            byte[] tmp = new byte[keyLength];
            for (int i = 0; i < keyLength; i++) {
                if (i < privateKey.getBytes(Charset.forName("UTF-8")).length) {
                    tmp[i] = privateKey.getBytes(Charset.forName("UTF-8"))[i];
                } else {
                    tmp[i] = 0x01;
                }
            }
            // 转换为AES专用密钥
            SecretKeySpec key = new SecretKeySpec(tmp, "AES");
            // 创建密码器
            Cipher cipher = Cipher.getInstance(DEFAULT_CIPHER_ALGORITHM);
            byte[] byteContent = body.getBytes("utf-8");
            // 初始化为加密模式的密码器
            cipher.init(Cipher.ENCRYPT_MODE, key);
            // 加密
            return this.parseByte2HexStr(cipher.doFinal(byteContent));
        } catch (Exception e) {
            e.printStackTrace();
            //.error("加密异常", e);
        }
        return null;
    }

    /**
     * 解密body
     *
     * @param body
     * @return
     */
    public String lsbDecrypt(String body) {
        try {
            byte[] tmp = new byte[keyLength];
            for (int i = 0; i < keyLength; i++) {
                if (i < privateKey.getBytes(Charset.forName("UTF-8")).length) {
                    tmp[i] = privateKey.getBytes(Charset.forName("UTF-8"))[i];
                } else {
                    tmp[i] = 0x01;
                }
            }
            // 转换为AES专用密钥
            SecretKeySpec key = new SecretKeySpec(tmp, "AES");
            // 创建密码器
            Cipher cipher = Cipher.getInstance(DEFAULT_CIPHER_ALGORITHM);
            // 初始化为解密模式的密码器
            cipher.init(Cipher.DECRYPT_MODE, key);
            // 明文
            return new String(cipher.doFinal(this.parseHexStr2Byte(body)), "UTF-8");
        } catch (Exception e) {
            e.printStackTrace();
            //LOGGER.error("解密异常", e);
        }
        return null;
    }

    /**
     * 将二进制转换成16进制
     *
     * @param buf
     * @return
     */
    public static String parseByte2HexStr(byte[] buf) {
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < buf.length; i++) {
            String hex = Integer.toHexString(buf[i] & 0xFF);
            if (hex.length() == 1) {
                hex = '0' + hex;
            }
            sb.append(hex.toUpperCase());
        }
        return sb.toString();
    }

    /**
     * 将16进制转换为二进制
     *
     * @param hexStr
     * @return
     */
    public static byte[] parseHexStr2Byte(String hexStr) {
        if (hexStr == null || hexStr.length() < 1) {
            return null;
        }
        byte[] result = new byte[hexStr.length() / 2];
        for (int i = 0; i < hexStr.length() / 2; i++) {
            int high = Integer.parseInt(hexStr.substring(i * 2, i * 2 + 1), 16);
            int low = Integer.parseInt(hexStr.substring(i * 2 + 1, i * 2 + 2), 16);
            result[i] = (byte) (high * 16 + low);
        }
        return result;
    }


    public static void main(String[] args) throws Exception {

    }

    public static String encryptToBase64(String filePath) {
        if (filePath == null) {
            return null;
        }
        try {
            byte[] b = Files.readAllBytes(Paths.get(filePath));
            return java.util.Base64.getEncoder().encodeToString(b);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }
}
