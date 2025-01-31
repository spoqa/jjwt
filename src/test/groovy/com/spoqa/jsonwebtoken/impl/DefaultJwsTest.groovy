/*
 * Copyright (C) 2015 jsonwebtoken.io
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.spoqa.jsonwebtoken.impl


import com.spoqa.jsonwebtoken.Jwts
import com.spoqa.jsonwebtoken.SignatureAlgorithm
import com.spoqa.jsonwebtoken.impl.crypto.MacProvider
import org.junit.Test

class DefaultJwsTest {

    @Test
    void testConstructor() {

        com.spoqa.jsonwebtoken.JwsHeader header = Jwts.jwsHeader()
        def jws = new DefaultJws<String>(header, 'foo', 'sig')

        assertSame jws.getHeader(), header
        assertEquals jws.getBody(), 'foo'
        assertEquals jws.getSignature(), 'sig'
    }

    @Test
    void testToString() {
        //create random signing key for testing:
        byte[] key = MacProvider.generateKey().encoded
        String compact = Jwts.builder().claim('foo', 'bar').signWith(SignatureAlgorithm.HS256, key).compact();
        int i = compact.lastIndexOf('.')
        String signature = compact.substring(i + 1)
        def jws = Jwts.parser().setSigningKey(key).parseClaimsJws(compact)
        assertEquals 'header={alg=HS256},body={foo=bar},signature=' + signature, jws.toString()
    }
}
