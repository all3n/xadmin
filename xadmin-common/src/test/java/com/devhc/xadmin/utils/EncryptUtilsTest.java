package com.devhc.xadmin.utils;

import org.junit.jupiter.api.Test;

import static com.devhc.xadmin.utils.EncryptUtils.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class EncryptUtilsTest {
    @Test
    public void test() throws Exception {
        System.out.println(desEncrypt("123456"));
    }

    /**
     * 对称加密
     */
    @Test
    public void testDesEncrypt() {
        try {
            assertEquals("7772841DC6099402", desEncrypt("123456"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 对称解密
     */
    @Test
    public void testDesDecrypt() {
        try {
            assertEquals("123456", desDecrypt("7772841DC6099402"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
