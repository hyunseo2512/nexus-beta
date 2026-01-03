package com.community.demo.handler;


import com.community.demo.dto.FileDTO;
import com.community.demo.service.BoardService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.File;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@EnableScheduling
@RequiredArgsConstructor
@Component
public class FileSweeper {
    // 매일 정해진 시간에 스케줄러 실행

    private final BoardService boardService;
    private final String BASE_PATH = "/home/zxcvne/forum-project/_myProject/_java/_fileUpload/";

    // cron 방식 = 초 분 시 일 월 요일 년도(생략가능)
    @Scheduled(cron = "0 0 0 * * *")
    public void fileSweeper() {
        log.info(">>> fileSweeper Start >> {}", LocalDateTime.now());

        LocalDate now = LocalDate.now();
        String today = now.toString().replace("-", File.separator);

        List<FileDTO> dbFileList = boardService.getTodayFileList(today);
        log.info(">>> dbFileList >> {}", dbFileList);

        List<String> currFile = new ArrayList<>();
        for(FileDTO fileDTO : dbFileList){
            String fileName = today+File.separator+fileDTO.getUuid()+"_"+fileDTO.getFileName();
            currFile.add(BASE_PATH+fileName);
            if(fileDTO.getFileType() == 1){
                String thFileName =today + File.separator+fileDTO.getUuid()
                        + "_th_"+fileDTO.getFileName();
                currFile.add(BASE_PATH+thFileName);
            }
        }

        File dir = Paths.get(BASE_PATH+today).toFile();

        File[] allFileObject = dir.listFiles();

        for(File file : allFileObject){
            String storedFileName = file.toPath().toString();
            if(!currFile.contains(storedFileName)){
                file.delete(); // 리스트에 없다면 삭제
                log.info(">>> delete file >> {}", storedFileName);
            }
        }
        log.info(">>> fileSweeper End >> {}", LocalDateTime.now());
    }
}
