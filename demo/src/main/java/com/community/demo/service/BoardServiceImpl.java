package com.community.demo.service;

import com.community.demo.dto.BoardDTO;
import com.community.demo.dto.BoardFileDTO;
import com.community.demo.dto.FileDTO;
import com.community.demo.entity.Board;
import com.community.demo.entity.File;
import com.community.demo.repository.BoardRepository;
import com.community.demo.repository.FileRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Service
public class BoardServiceImpl implements BoardService{
    private final BoardRepository boardRepository;
    private final FileRepository fileRepository;



    @Override
    public List<BoardDTO> getList() {
        List<Board> boardList = boardRepository.findAll(
                Sort.by(Sort.Direction.DESC,"bno"));

        List<BoardDTO> boardDTOList = boardList
                    .stream()
                    .map(board -> convertEntityToDto(board))
                    .toList();
        return boardDTOList;
    }

    @Override
    public BoardDTO getDetail(long bno) {
        Optional<Board> optional = boardRepository.findById(bno);
        if(optional.isPresent()){
            Board board = optional.get();
            board.setReadCount(board.getReadCount()+1);
            BoardDTO boardDTO = convertEntityToDto(board);

            return boardDTO;
        }
        return null;
    }

        @Override
        public Long insert(BoardDTO boardDTO) {
            Board board = convertDtoToEntity(boardDTO);
            Long bno = boardRepository.save(board).getBno();

            return bno;
        }

    @Transactional
    @Override
    public Long insert(BoardFileDTO boardFileDTO) {
        BoardDTO boardDTO = boardFileDTO.getBoardDTO();
        List<FileDTO> fileDTOList = boardFileDTO.getFileList();
        if(fileDTOList != null){
            boardDTO.setFileQty(fileDTOList.size());
        }

        Long bno = boardRepository.save(convertDtoToEntity(boardDTO)).getBno();

        if(bno > 0 &&  fileDTOList != null){
            for(FileDTO fileDTO : fileDTOList){
                fileDTO.setBno(bno);
                bno = fileRepository.save(convertDtoToEntity(fileDTO)).getBno();
            }
        }
        return bno;
    }

    @Override
    public List<FileDTO> getTodayFileList(String today) {
        Optional<List<File>> fileList = fileRepository.findBySaveDir(today);
        if(fileList.isPresent()){
            return fileList.get().stream()
                    .map(this :: convertEntityToDto)
                    .toList();
        }
        return null;
    }
}
