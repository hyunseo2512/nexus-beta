package com.community.demo.handler;

import com.community.demo.dto.FileDTO;
import lombok.extern.slf4j.Slf4j;
import net.coobird.thumbnailator.Thumbnails;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Slf4j
@Component
public class FileHandler {
    private final String UP_DIR = "/home/zxcvne/forum-project/_myProject/_java/_fileUpload";

    public List<FileDTO> uploadFile(MultipartFile[] files){
        List<FileDTO> fileList = new ArrayList<>();

        LocalDate date = LocalDate.now();
        String today = date.toString().replace("-", File.separator);

        File folders = new File(UP_DIR, today);

        if(!folders.exists()){
            folders.mkdirs();
        }

        for(MultipartFile file : files) {
            FileDTO fileDTO = new FileDTO();
            String orginalFileName = file.getOriginalFilename();
            fileDTO.setFileName(file.getOriginalFilename());
            fileDTO.setFileSize(file.getSize());
            fileDTO.setFileType(file.getContentType().startsWith("image")?1:0);
            fileDTO.setSaveDir(today);

            UUID uuid = UUID.randomUUID();
            String uuidString = uuid.toString();
            fileDTO.setUuid(uuid.toString());

            String fileName = uuidString + "_" + orginalFileName;
            String fileThName = uuidString + "_th_" + orginalFileName;

            // / ~/2025/12/24/uuid_name
            File storeFile = new File(folders, fileName);

            // 저장
            try {
                file.transferTo(storeFile);
                if(fileDTO.getFileType()==1){
                    File thumbnail = new File(folders, fileThName);
                    Thumbnails.of(storeFile).size(100,100).toFile(thumbnail);
                }
            }catch (Exception e){
                log.info("file save Error");
                e.printStackTrace();
            }
            fileList.add(fileDTO);
        }

        return fileList;
    }

}
