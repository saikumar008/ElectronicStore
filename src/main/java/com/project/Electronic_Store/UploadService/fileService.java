package com.project.Electronic_Store.UploadService;

import org.springframework.web.multipart.MultipartFile;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

public interface fileService {

    String uploadFile(MultipartFile file, String path) throws IOException;

//    List<String> uploadFiles(List<MultipartFile> files, String path) throws IOException;

    InputStream getResource(String path, String name) throws FileNotFoundException;

//    List<InputStream> getMultiResources(String path, List<String> names) throws FileNotFoundException;

//    byte[] getImageData(String imageName);
}
