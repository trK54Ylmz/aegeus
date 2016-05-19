/*
 * Copyright 2015 Tarık Yılmaz
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

package com.aegeus.aws;

import com.aegeus.config.format.S3ConfigObject;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.AmazonS3Exception;
import com.amazonaws.services.s3.model.GetObjectRequest;
import com.amazonaws.services.s3.model.ListObjectsRequest;
import com.amazonaws.services.s3.model.ObjectListing;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectSummary;

import com.google.common.base.Joiner;
import com.google.common.base.Strings;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SimpleStorageService {
    private S3ConfigObject config;

    private final AmazonS3Client s3;

    public SimpleStorageService(S3ConfigObject config) {
        this.config = config;

        s3 = new AmazonS3Client(new BasicAWSCredentials(
                config.getAccessKey(),
                config.getSecretKey()));
        s3.setRegion(Region.getRegion(Regions.fromName(config.getRegion())));
    }

    public boolean bucketExists() {
        return s3.doesBucketExist(config.getBucket());
    }

    public boolean objectExists(String bucket, String key) {
        try {
            s3.getObjectMetadata(bucket, key);
            return true;
        } catch (AmazonS3Exception e) {
            return false;
        }
    }

    /**
     * List all files under the current directory based on bucket and prefix parameters
     *
     * @param bucket Bucket name
     * @param prefix Prefix name
     * @return File list
     */
    public List<S3ObjectSummary> listObjects(String bucket, String prefix) {
        ListObjectsRequest request = new ListObjectsRequest().withBucketName(bucket);
        if (prefix != null) {
            request.setPrefix(prefix);
        }
        List<S3ObjectSummary> summaries = new ArrayList<>();
        ObjectListing listing;

        do {
            listing = s3.listObjects(request);
            summaries.addAll(listing.getObjectSummaries());
            request.setMarker(listing.getNextMarker());
        } while (listing.isTruncated());

        return summaries;
    }

    /**
     * Split S3 file path with `/` separator and return`
     *
     * @param url Object url
     * @return File path parts
     */
    protected String[] getParts(String url) {
        if (Strings.isNullOrEmpty(url)) {
            return null;
        }

        return url.replace("s3n://", "").split("/");
    }

    /**
     * Get the S3 bucket name from object url
     *
     * @param url Object url
     * @return The bucket name
     */
    public String getBucket(String url) {
        String[] parts = getParts(url);
        return parts == null ? null : parts[0];
    }

    /**
     * Get the S3 file path from object url
     *
     * @param url Object url
     * @return The file path without bucket name
     */
    public String getKey(String url) {
        String[] parts = getParts(url);
        return parts == null ? null : '/' + Joiner.on('/').join(Arrays.copyOfRange(parts, 1, parts.length));
    }

    /**
     * Copy object from source to destination
     *
     * @param sourceBucket Source bucket name
     * @param sourceKey    Source prefix
     * @param destBucket   Destination bucket
     * @param destKey      Destination prefix
     */
    public void copyObject(String sourceBucket, String sourceKey, String destBucket, String destKey) {
        s3.copyObject(sourceBucket, sourceKey, destBucket, destKey);
    }

    /**
     * Delete one or more object(s)
     *
     * @param bucket Bucket name
     * @param key    Prefix
     */
    public void deleteObject(String bucket, String key) {
        s3.deleteObject(bucket, key);
    }

    public S3Object getObject(String bucket, String key) {
        return s3.getObject(new GetObjectRequest(bucket, key));
    }

    public InputStream download(String bucket, String key) throws IOException {
        S3Object object = s3.getObject(bucket, key);
        return object.getObjectContent();
    }

    public void upload(String bucket, String key, File file) {
        PutObjectRequest request = new PutObjectRequest(bucket, key, file);
        s3.putObject(request);
    }

    public void createFolder(String bucket, String key) {
        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentLength(0);

        InputStream nullObject = new ByteArrayInputStream(new byte[0]);
        PutObjectRequest putObjectRequest = new PutObjectRequest(
                bucket,
                key + "/",
                nullObject,
                metadata);

        s3.putObject(putObjectRequest);
    }
}