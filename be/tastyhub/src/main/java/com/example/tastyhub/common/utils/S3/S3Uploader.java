package com.example.tastyhub.common.utils.S3;

import java.io.IOException;
import java.net.URL;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.DeleteObjectRequest;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;


@Service
@RequiredArgsConstructor
@Slf4j
public class S3Uploader {

  private final AmazonS3Client amazonS3Client;

  @Value("${cloud.aws.s3.bucket}")
  private String bucket;

  // 핵심 비즈니스 로직
  public String upload(MultipartFile multipartFile, String dirName) throws IOException {

    String originalFileName = multipartFile.getOriginalFilename();
    String fileName = dirName + "/" + UUID.randomUUID() + originalFileName;

    ObjectMetadata metadata = new ObjectMetadata();
    metadata.setContentLength(multipartFile.getSize());
    metadata.setContentType(multipartFile.getContentType());

    amazonS3Client.putObject(new PutObjectRequest(bucket, fileName, multipartFile.getInputStream(), metadata)
        .withCannedAcl(CannedAccessControlList.PublicRead));
    return amazonS3Client.getUrl(bucket, fileName).toString();
  }

  // 파일 이름을 기존 파일과 겹치지 않도록 UUID를 사용하여 만들고 확장자를 뒤에 붙여 이미지 확장자를 유지한다
  public String createStoreFileName(String originalFilename) {
    return UUID.randomUUID() + "." + extractExt(originalFilename);
  }

  // 사용자가 업로드한 파일에서 확장자를 추출
  public String extractExt(String originalFilename) {
    int pos = originalFilename.lastIndexOf(".");
    return originalFilename.substring(pos + 1);
  }

  // S3에 업로드된 이미지 지우기
  public void delete(String imgUrl) throws IOException {
    try {
      URL url = new URL(imgUrl);
      String path = url.getPath().substring(1);
      amazonS3Client.deleteObject(new DeleteObjectRequest(bucket, path));
    } catch (Exception e) {
        throw new IOException("S3 이미지 삭제에 실패했습니다.", e);
    }
}
    
}
