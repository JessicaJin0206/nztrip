package com.fitibo.aotearoa.common;

import com.fitibo.aotearoa.exception.EncryptionException;

import javax.crypto.Cipher;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;

/**
 * Created by qianhao.zhou on 2/23/16.
 */
public final class AES {
    private static ThreadLocal<Cipher> cipherThreadLocal = new ThreadLocal<>();
    private final IvParameterSpec parameterSpec;
    private final SecretKeySpec key;

    public AES(String iv, String encryptionKey) {
        try {
            parameterSpec = new IvParameterSpec(iv.getBytes("UTF-8"));
        } catch (UnsupportedEncodingException e) {
            throw new EncryptionException(e);
        }
        try {
            key = new SecretKeySpec(encryptionKey.getBytes("UTF-8"), "AES");
        } catch (UnsupportedEncodingException e) {
            throw new EncryptionException(e);
        }
    }

    private Cipher getCipher() {
        if (cipherThreadLocal.get() == null) {
            try {
                cipherThreadLocal.set(Cipher.getInstance("AES/CBC/PKCS5Padding"));
            } catch (NoSuchAlgorithmException e) {
                throw new EncryptionException(e);
            } catch (NoSuchPaddingException e) {
                throw new EncryptionException(e);
            }
        }
        return cipherThreadLocal.get();
    }

    public byte[] encrypt(String message) throws EncryptionException {
        try {
            return encrypt(message.getBytes("UTF-8"));
        } catch (UnsupportedEncodingException e) {
            throw new EncryptionException(e);
        }
    }

    public byte[] encrypt(byte[] message) throws EncryptionException {
        Cipher cipher = getCipher();
        try {
            cipher.init(Cipher.ENCRYPT_MODE, key, parameterSpec);
            return cipher.doFinal(message);
        } catch (Exception e) {
            throw new EncryptionException(e);
        }
    }

    public byte[] decrypt(byte[] encryptedMessage) throws EncryptionException {
        Cipher cipher = getCipher();
        try {
            cipher.init(Cipher.DECRYPT_MODE, key, parameterSpec);
            return cipher.doFinal(encryptedMessage);
        } catch (Exception e) {
            throw new EncryptionException(e);
        }
    }

}
