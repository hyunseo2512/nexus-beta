package com.community.demo.controller;

import com.community.demo.dto.BoardDTO;
import com.community.demo.entity.Board;
import com.community.demo.service.BoardService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@RequestMapping("/board/*")
@Controller
public class BoardController {
    private final BoardService boardService;

    @GetMapping("/register")
    public void register(){}

    @PostMapping("/register")
    public String register(BoardDTO boardDTO, Model model){
         Long bno = boardService.insert(boardDTO);
         log.info(">>> insert id >> {}", bno);
        List<Board> boardList = new ArrayList<>();
        model.addAttribute("board", boardList);
        return "redirect:/";
    }

    @GetMapping("/detail")
    public void detail(@RequestParam("bno") long bno, Model model){
        BoardDTO boardDTO = boardService.getDetail(bno);
        model.addAttribute("boardDTO" , boardDTO);
    }
}
