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
package com.spoqa.jsonwebtoken.impl.compression;

import com.spoqa.jsonwebtoken.JwtBuilder;
import com.spoqa.jsonwebtoken.JwtParser;
import com.spoqa.jsonwebtoken.lang.Assert;
import com.spoqa.jsonwebtoken.lang.Strings;
import com.spoqa.jsonwebtoken.CompressionCodec;
import com.spoqa.jsonwebtoken.CompressionCodecResolver;
import com.spoqa.jsonwebtoken.CompressionException;
import com.spoqa.jsonwebtoken.Header;

/**
 * Default implementation of {@link CompressionCodecResolver} that supports the following:
 * <p>
 * <ul>
 * <li>If the specified JWT {@link Header} does not have a {@code calg} header, this implementation does
 * nothing and returns {@code null} to the caller, indicating no compression was used.</li>
 * <li>If the header has a {@code calg} value of {@code DEF}, a {@link DeflateCompressionCodec} will be returned.</li>
 * <li>If the header has a {@code calg} value of {@code GZIP}, a {@link GzipCompressionCodec} will be returned.</li>
 * <li>If the header has any other {@code calg} value, a {@link CompressionException} is thrown to reflect an
 * unrecognized algorithm.</li>
 * </ul>
 *
 * <p>If you want to use a compression algorithm other than {@code DEF} or {@code GZIP}, you must implement your own
 * {@link CompressionCodecResolver} and specify that when
 * {@link JwtBuilder#compressWith(CompressionCodec) building} and
 * {@link JwtParser#setCompressionCodecResolver(CompressionCodecResolver) parsing} JWTs.</p>
 *
 * @see DeflateCompressionCodec
 * @see GzipCompressionCodec
 * @since 0.6.0
 */
public class DefaultCompressionCodecResolver implements CompressionCodecResolver {

    @Override
    public CompressionCodec resolveCompressionCodec(Header header) {
        String cmpAlg = getAlgorithmFromHeader(header);

        final boolean hasCompressionAlgorithm = Strings.hasText(cmpAlg);

        if (!hasCompressionAlgorithm) {
            return null;
        }
        if (CompressionCodecs.DEFLATE.getAlgorithmName().equalsIgnoreCase(cmpAlg)) {
            return CompressionCodecs.DEFLATE;
        }
        if (CompressionCodecs.GZIP.getAlgorithmName().equalsIgnoreCase(cmpAlg)) {
            return CompressionCodecs.GZIP;
        }

        throw new CompressionException("Unsupported compression algorithm '" + cmpAlg + "'");
    }

    private String getAlgorithmFromHeader(Header header) {
        Assert.notNull(header, "header cannot be null.");

        return header.getCompressionAlgorithm();
    }
}
