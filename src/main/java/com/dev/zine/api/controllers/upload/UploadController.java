package com.dev.zine.api.controllers.upload;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.dev.zine.api.model.images.ImagesUploadRes;
import com.dev.zine.dao.MediaDAO;
import com.dev.zine.exceptions.NotFoundException;
import com.dev.zine.model.Media;
import com.dev.zine.utils.CloudinaryUtil;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;

@RestController
@RequestMapping("/file")
public class UploadController {
    @Autowired
    private CloudinaryUtil util;
    @Autowired
    private MediaDAO mediaDAO;

    @PostMapping("/upload")
    public ResponseEntity<?> uploadFile(@RequestParam MultipartFile file, @RequestParam String description,
            @RequestParam(required = false) String folder) {
        try {
            List<String> validFolders = List.of("blog-images", "room-dp", "user-dp", "chat-file");

            if (folder == null || !validFolders.contains(folder)) {
                String contentType = file.getContentType();

                if (contentType != null) {
                    if (contentType.startsWith("image")) {
                        folder = "images";
                    } else if (contentType.equals("application/pdf") || contentType.startsWith("application/msword")) {
                        folder = "docs";
                    } else {
                        folder = "others";
                    }
                } else {
                    return ResponseEntity.badRequest().body(Map.of("message", "Unsupported file type."));
                }
            }

            ImagesUploadRes res = util.uploadFile(file, folder);

            Media newMedia = new Media();
            newMedia.setDescription(description);
            newMedia.setName(file.getOriginalFilename());
            newMedia.setPublicId(res.getPublicId());
            newMedia.setUrl(res.getUrl());
            mediaDAO.save(newMedia);

            return ResponseEntity.ok().body(res);
        } catch (IOException e) {
            return ResponseEntity.badRequest().body(Map.of("message", e.getMessage()));
        }
    }

    @PostMapping("/delete")
    public ResponseEntity<?> deleteFile(@RequestParam(required = false) String publicKey,
            @RequestParam(required = false) String url) {
        try {
            if (publicKey == null && url == null) {
                return ResponseEntity.badRequest().body(Map.of("message", "Either publicKey or url must be provided."));
            }

            if (publicKey == null) {
                try {
                    Media media = mediaDAO.findByUrl(url).orElseThrow(() -> new NotFoundException("Media",0L));
                    publicKey = media.getPublicId();
                } catch (Exception e) {
                    return ResponseEntity.badRequest()
                            .body(Map.of("message", "Invalid URL or unable to extract publicKey."));
                }
            }

            String res = util.deleteImage(publicKey);
            return ResponseEntity.ok().body(Map.of("message", res));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(Map.of("message", e.getMessage()));
        }
    }

}
