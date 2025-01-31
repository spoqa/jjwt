package com.spoqa.jsonwebtoken.impl

import org.junit.Test

class Base64UrlCodecTest {

    @Test
    void testRemovePaddingWithEmptyByteArray() {

        def codec = new Base64UrlCodec()

        byte[] empty = new byte[0];

        def result = codec.removePadding(empty)

        assertSame empty, result
    }
}
