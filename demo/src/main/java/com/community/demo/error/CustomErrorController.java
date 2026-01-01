package com.community.demo.error;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Slf4j
@Controller
public class CustomErrorController implements ErrorController {
private final String VIEW_PATH = "/error/";
@RequestMapping("/error")
public String handlerError(HttpServletRequest request) {
    Object status = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);
    log.info(">>> status >> {}", status);
    if (status != null) {
        int statusCode = Integer.parseInt(status.toString());
        if (statusCode == HttpStatus.NOT_FOUND.value()) {
            return VIEW_PATH + "404";
        }
        if (statusCode == HttpStatus.INTERNAL_SERVER_ERROR.value()) {
            return VIEW_PATH + "500";
        }
    }

    return "/";
  }
}