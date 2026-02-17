
package com.janani.contentrecommendation.config;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AwsConfig {

    @Value("${aws.accessKeyId}")//getting the accesskeyId from application.properties
    private String accessKey;

    @Value("${aws.secretKey}")//getting the secretKey from application.properties
    private String secretKey;

    @Value("${aws.region}")//getting the region from application.properties
    private String region;

    @Bean
    public AmazonS3 amazonS3() {
        BasicAWSCredentials creds = new BasicAWSCredentials(accessKey, secretKey);
        return AmazonS3ClientBuilder.standard()
                .withRegion("ap-south-2")
                .withCredentials(new AWSStaticCredentialsProvider(creds))
                .build();
    }
}
