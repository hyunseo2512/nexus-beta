package com.community.demo.service;

import com.community.demo.dto.BoardDTO;
import com.community.demo.dto.BoardFileDTO;
import com.community.demo.dto.FileDTO;
import com.community.demo.entity.Board;
import com.community.demo.entity.File;

import java.util.List;

public interface BoardService {

    /* convertDtoToEntity */
    default Board convertDtoToEntity(BoardDTO boardDTO){
        return Board.builder()
                .bno(boardDTO.getBno())
                .title(boardDTO.getTitle())
                .writer(boardDTO.getWriter())
                .content(boardDTO.getContent())
                .readCount(boardDTO.getReadCount())
                .cmtQty(boardDTO.getCmtQty())
                .fileQty(boardDTO.getFileQty())
                .build();
    }

    /* convertEntityToDto */
    default BoardDTO convertEntityToDto(Board board){
        return BoardDTO.builder()
                .bno(board.getBno())
                .title(board.getTitle())
                .writer(board.getWriter())
                .content(board.getContent())
                .readCount(board.getReadCount())
                .cmtQty(board.getCmtQty())
                .fileQty(board.getFileQty())
                .regDate(board.getRegDate())
                .modDate(board.getModDate())
                .build();
    }

    default File convertDtoToEntity(FileDTO fileDTO){
        return File.builder()
                .uuid(fileDTO.getUuid())
                .saveDir(fileDTO.getSaveDir())
                .fileName(fileDTO.getFileName())
                .fileType(fileDTO.getFileType())
                .bno(fileDTO.getBno())
                .fileSize(fileDTO.getFileSize())
                .build();
    }

    default FileDTO convertEntityToDto(File file){
        return FileDTO.builder()
                .uuid(file.getUuid())
                .saveDir(file.getSaveDir())
                .fileName(file.getFileName())
                .fileType(file.getFileType())
                .bno(file.getBno())
                .fileSize(file.getFileSize())
                .regDate(file.getRegDate())
                .modDate(file.getModDate())
                .build();
    }


    Long insert(BoardDTO boardDTO);

    List<BoardDTO> getList();

    BoardDTO getDetail(long bno);

    Long insert(BoardFileDTO boardFileDTO);

    List<FileDTO> getTodayFileList(String today);
}
