package com.community.demo.service;

import com.community.demo.dto.CommentDTO;
import com.community.demo.entity.Board;
import com.community.demo.entity.Comment;
import com.community.demo.repository.BoardRepository;
import com.community.demo.repository.CommentRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.awt.print.Pageable;
import java.util.List;

@RequiredArgsConstructor
@Slf4j
@Service
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository;
    private final BoardRepository boardRepository;

    @Override
    public long post(CommentDTO commentDTO) {
        // 저장 대상은 Entity CommentDTO => Entity로 변환
        // save()
        // 해당 comment에 board 객체를 가져와서 cmtQty + 1 업데이트
        Board board = boardRepository.findById(commentDTO.getBno())
                .orElseThrow(() -> new EntityNotFoundException());
        board.setCmtQty(board.getCmtQty() + 1);

        return commentRepository.save(convertDtoToEntity(commentDTO)).getCno();
    }

    @Override
    public List<CommentDTO> getList(Long bno) {
        List<Comment> list = commentRepository.findByBno(bno);

        return list.stream()
                .map(comment -> convertEntityToDto(comment))
                .toList();
    }

    @Override
    @Transactional
    public int modify(CommentDTO commentDTO) {
        // modify
        Comment comment = commentRepository.findById(commentDTO.getCno())
                .orElseThrow(() -> new EntityNotFoundException("Comment not found"));
        comment.setContent(commentDTO.getContent());
        // modify 메서드는 Dirty Checking에 의해 트랜잭션 종료 시 업데이트 쿼리가 실행됨
        // 하지만 명시적으로 save를 호출해도 무방하며, 성공 여부를 반환하기 위해 간단히 처리
        commentRepository.save(comment);
        return 1;
    }

    @Override
    @Transactional
    public int remove(Long cno) {
        // remove
        Comment comment = commentRepository.findById(cno)
                .orElseThrow(() -> new EntityNotFoundException("Comment not found"));

        // Board의 댓글 수 감소
        Board board = boardRepository.findById(comment.getBno())
                .orElseThrow(() -> new EntityNotFoundException("Board not found"));
        board.setCmtQty(Math.max(0, board.getCmtQty() - 1));

        commentRepository.delete(comment);
        return 1;
    }
}
