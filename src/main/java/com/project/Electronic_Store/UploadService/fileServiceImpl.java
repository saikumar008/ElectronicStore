package com.project.Electronic_Store.UploadService;

import com.project.Electronic_Store.ExceptionHandling.BadRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;


@Service
public class fileServiceImpl implements fileService {

    private Logger logger = LoggerFactory.getLogger(fileServiceImpl.class);
    @Override
    public String uploadFile(MultipartFile file, String path) throws IOException {

        String originalFileName = file.getOriginalFilename();
        logger.info("FileName : {}" , originalFileName);
        String fileName = UUID.randomUUID().toString();
        String extension = originalFileName.substring(originalFileName.lastIndexOf("."));
        String filenameWithExtension = fileName + extension;
        String fullPathWithExtension = path + filenameWithExtension;

        if(extension.equalsIgnoreCase(".jpg") || extension.equalsIgnoreCase(".png") || extension.equalsIgnoreCase("jpeg")){
                File folder = new File(path);

                if(!folder.exists()){
                    folder.mkdirs();
                }

            Files.copy(file.getInputStream(), Paths.get(fullPathWithExtension));

                return filenameWithExtension;
        }else{
            throw new BadRequest("File with this" + extension + "not allowed");
        }

    }


    @Override
    public InputStream getResource(String path, String name) throws FileNotFoundException {
        String fullPath = path+File.separator+name;
        InputStream inputStream = new FileInputStream(fullPath);
        return inputStream;
    }

}
