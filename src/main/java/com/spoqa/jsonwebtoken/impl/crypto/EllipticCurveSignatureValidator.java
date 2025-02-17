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
package com.spoqa.jsonwebtoken.impl.crypto;

import java.security.InvalidKeyException;
import java.security.Key;
import java.security.PublicKey;
import java.security.Signature;
import java.security.interfaces.ECPublicKey;

import com.spoqa.jsonwebtoken.SignatureException;
import com.spoqa.jsonwebtoken.lang.Assert;
import com.spoqa.jsonwebtoken.SignatureAlgorithm;

public class EllipticCurveSignatureValidator extends EllipticCurveProvider implements SignatureValidator {

    private static final String EC_PUBLIC_KEY_REQD_MSG =
        "Elliptic Curve signature validation requires an ECPublicKey instance.";

    public EllipticCurveSignatureValidator(SignatureAlgorithm alg, Key key) {
        super(alg, key);
        Assert.isTrue(key instanceof ECPublicKey, EC_PUBLIC_KEY_REQD_MSG);
    }

    @Override
    public boolean isValid(byte[] data, byte[] signature) {
        Signature sig = createSignatureInstance();
        PublicKey publicKey = (PublicKey) key;
        try {
            int expectedSize = getSignatureByteArrayLength(alg);
            /**
             *
             * If the expected size is not valid for JOSE, fall back to ASN.1 DER signature.
             * This fallback is for backwards compatibility ONLY (to support tokens generated by previous versions of jjwt)
             * and backwards compatibility will possibly be removed in a future version of this library.
             *
             * **/
            byte[] derSignature = expectedSize != signature.length && signature[0] == 0x30 ? signature : transcodeSignatureToDER(signature);
            return doVerify(sig, publicKey, data, derSignature);
        } catch (Exception e) {
            String msg = "Unable to verify Elliptic Curve signature using configured ECPublicKey. " + e.getMessage();
            throw new SignatureException(msg, e);
        }
    }

    protected boolean doVerify(Signature sig, PublicKey publicKey, byte[] data, byte[] signature)
        throws InvalidKeyException, java.security.SignatureException {
        sig.initVerify(publicKey);
        sig.update(data);
        return sig.verify(signature);
    }

}
