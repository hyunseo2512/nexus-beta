package com.community.demo.controller;

import com.community.demo.dto.BoardDTO;
import com.community.demo.service.BoardService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Slf4j
@Controller
public class HomeController {
    private final BoardService boardService;

    @GetMapping("/")
    public String index(Model model) {

        List<BoardDTO> boardList = boardService.getList();
        log.info(">>> boardList >> {}", boardList);
        model.addAttribute("boardList", boardList);

        return "index";
    }
}

