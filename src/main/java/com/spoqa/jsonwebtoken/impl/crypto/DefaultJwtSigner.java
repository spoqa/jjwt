/*
 * Copyright (C) 2014 jsonwebtoken.io
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
package com.spoqa.jsonwebtoken.impl.crypto;

import com.spoqa.jsonwebtoken.lang.Assert;
import com.spoqa.jsonwebtoken.SignatureAlgorithm;
import com.spoqa.jsonwebtoken.impl.TextCodec;

import java.nio.charset.Charset;
import java.security.Key;

public class DefaultJwtSigner implements JwtSigner {

    private static final Charset US_ASCII = Charset.forName("US-ASCII");

    private final Signer signer;

    public DefaultJwtSigner(SignatureAlgorithm alg, Key key) {
        this(DefaultSignerFactory.INSTANCE, alg, key);
    }

    public DefaultJwtSigner(SignerFactory factory, SignatureAlgorithm alg, Key key) {
        Assert.notNull(factory, "SignerFactory argument cannot be null.");
        this.signer = factory.createSigner(alg, key);
    }

    @Override
    public String sign(String jwtWithoutSignature) {

        byte[] bytesToSign = jwtWithoutSignature.getBytes(US_ASCII);

        byte[] signature = signer.sign(bytesToSign);

        return TextCodec.BASE64URL.encode(signature);
    }
}
