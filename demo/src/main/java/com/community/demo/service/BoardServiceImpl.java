package com.community.demo.service;

import com.community.demo.dto.BoardDTO;
import com.community.demo.entity.Board;
import com.community.demo.repository.BoardRepository;
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

    @Override
    public Long insert(BoardDTO boardDTO) {
        Board board = convertDtoToEntity(boardDTO);
        Long bno = boardRepository.save(board).getBno();

        return bno;
    }

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
}
