package org.zerock.mreview.controller;

import lombok.extern.log4j.Log4j2;
import net.coobird.thumbnailator.Thumbnailator;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.zerock.mreview.dto.UploadResultDTO;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;
import java.net.URLDecoder;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@RestController
@Log4j2
public class UploadController {

    @Value("${org.zerock.upload.path}")
    private String uploadPath;

    @PostMapping("/uploadAjax")
    public ResponseEntity<List<UploadResultDTO>> uploadFile(MultipartFile[] uploadFiles) {
        // 결과를 담을 객체 생성
        List<UploadResultDTO> resultDTOList = new ArrayList<>();

        for(MultipartFile multipartFile : uploadFiles) {
            // 파일 확장자 체크
            if(!checkType(multipartFile)) {
                log.warn("not image type..");
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }

            // IE 나 Edge 는 전체 경로가 들어온다.
            String originalName = multipartFile.getOriginalFilename();
            log.info("originalName : " + originalName);
            String fileName = originalName.substring(originalName.lastIndexOf("\\")+1);

            log.info("fileName : " + fileName);
            // 날짜 폴더 생성
            String folderPath = makeFolder();
            // UUID
            String uuid = UUID.randomUUID().toString();
            // 파일이름 중간에 "_"를 사용해 구분
            String saveName = uploadPath + File.separator + folderPath + File.separator + uuid + "_" + fileName;

            File saveFile = new File(saveName);
            log.info("saveFile .. : " + saveFile);

            try {
                multipartFile.transferTo(saveFile);
                // 썸네일 생성
                String thumbnailSaveName =
                        uploadPath + File.separator + folderPath + File.separator + "s_" + uuid + "_" + fileName;

                File thumbnailFile = new File(thumbnailSaveName);
                // saveFile이 Path 타입일 경우 saveFile.toFile()
                Thumbnailator.createThumbnail(saveFile, thumbnailFile, 100, 100);
                // 파일이름 , uuid , 저장 경로
                resultDTOList.add(new UploadResultDTO(fileName, uuid, folderPath));

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return  new ResponseEntity<>(resultDTOList, HttpStatus.OK);
    }

    @GetMapping("/display")
    public ResponseEntity<byte[]> getFile(String fileName, String size) {
        log.info("fileName : " + fileName);

        ResponseEntity<byte[]> result = null;

        try {
            String srcFileName = URLDecoder.decode(fileName, "UTF-8");
            log.info("srcFileName : " + srcFileName);
            // uploadPath = C:\\upload
            File file = new File(uploadPath + File.separator + srcFileName);
            log.info("dispaly file : " +file);

            if(size != null && size.equals("1")) {
                file = new File(file.getParent(), file.getName().substring(2));
            }

            HttpHeaders headers = new HttpHeaders();

            headers.add("Content-Type",Files.probeContentType(file.toPath()));
            result = new ResponseEntity<>(FileCopyUtils.copyToByteArray(file), headers, HttpStatus.OK);

        } catch(Exception e ) {
            e.printStackTrace();
        }

        return result;
    }

    @PostMapping("/removeFile")
    public ResponseEntity<Boolean> remove(String fileName) {
        log.info("to remove fileName : " + fileName);

        String srcFileName = null;

        try {
            srcFileName = URLDecoder.decode(fileName, "UTF-8");
            File removeFile = new File(uploadPath + File.separator + srcFileName);
            // 우선 원본파일 삭제
            boolean result = removeFile.delete();
            // removeFile.getAbsolutePath().replace("s_", "")
            // File file = new File(removeFile) -> file.delete();
            File thumbnail = new File(removeFile.getParent(), "s_" +removeFile.getName());

            result = thumbnail.delete();

            return new ResponseEntity<>(result, HttpStatus.OK);

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    private boolean checkType(MultipartFile file) {

        return file.getContentType().startsWith("image");
    }

    private String makeFolder() {
        String str = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy/MM/dd"));

        String folderPath = str.replace("//", File.separator);
        // 폴더 생성
        File uploadPathFolder = new File(uploadPath, folderPath);

        if(!uploadPathFolder.exists()) {
            uploadPathFolder.mkdirs();
        }
        return folderPath;
    }
}
