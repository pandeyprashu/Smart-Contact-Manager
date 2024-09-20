package com.scm.services;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

public interface ImageService {

    String uploadImage(MultipartFile profileImage,String fileName);

    String getUrlFromPublicId(String publicId);
}
