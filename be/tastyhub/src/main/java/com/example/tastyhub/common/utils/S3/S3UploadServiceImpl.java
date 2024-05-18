package com.example.tastyhub.common.utils.S3;

import java.io.File;
import java.io.IOException;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.PutObjectRequest;

import lombok.RequiredArgsConstructor;


@Service
@RequiredArgsConstructor
public class S3UploadServiceImpl implements S3UploadService {

    private final AmazonS3Client amazonS3Client;

  @Value("${spring.file-dir}")
  private String fileDirectory;

  // 핵심 비즈니스 로직
  @Override
  public String upload(MultipartFile multipartFile, String bucket, String dirName) throws IOException {
    File uploadFile = convert(multipartFile).orElseThrow(
        () -> new IllegalArgumentException("파일 변환에 실패했습니다")
    );
    String fileName = dirName + "/" + UUID.randomUUID() + uploadFile.getName();
    String putS3 = putS3(uploadFile, bucket, fileName);
    removeFile(uploadFile);
    return putS3;
  }

  // S3로 업로드 ~ PutObjectFile 타입: 버킷 업로드 목적으로 만드는 객체의 URL을 반환
  // cannedAcl : 버킷 객체를 버킷 정책에 맞게 설정
  @Override
  public String putS3(File uploadFile, String bucket, String fileName) {
    amazonS3Client.putObject(new PutObjectRequest(bucket, fileName, uploadFile)
        .withCannedAcl(CannedAccessControlList.PublicRead));
    return amazonS3Client.getUrl(bucket, fileName).toString();
  }

  // 로컬에 저장된 이미지 지우기
  @Override
  public void removeFile(File savedLocalFile) {
    if (savedLocalFile.delete()) {
      System.out.println(savedLocalFile.getName() + "이 삭제되었습니다");
    } else {
      System.out.println(savedLocalFile.getName() + "이 삭제되지 않았습니다");
    }
  }

  // 이미지를 File 타입으로 변환하여 Optional로 반환
  @Override
  public Optional<File> convert(MultipartFile multipartFile) throws IOException {
    if(multipartFile.isEmpty()) return Optional.empty();

    // 파일 이름 조회
    String originalFilename = multipartFile.getOriginalFilename();

    // 이름변경, 형식을 file로 변경 후 파일 타입 변환
    File file = new File(fileDirectory + createStoreFileName(originalFilename));
    multipartFile.transferTo(file);

    return Optional.of(file);
  }

  // 파일 이름을 기존 파일과 겹치지 않도록 UUID를 사용하여 만들고 확장자를 뒤에 붙여 이미지 확장자를 유지한다
  @Override
  public String createStoreFileName(String originalFilename) {
    return UUID.randomUUID() + "." + extractExt(originalFilename);
  }

  // 사용자가 업로드한 파일에서 확장자를 추출
  @Override
  public String extractExt(String originalFilename) {
    int pos = originalFilename.lastIndexOf(".");
    return originalFilename.substring(pos + 1);
  }
    
}
