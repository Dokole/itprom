package com.testtask.itprom.controller;

import com.testtask.itprom.exceptions.BadRequestException;
import com.testtask.itprom.exceptions.NotFoundException;
import com.testtask.itprom.model.ErrorMessage;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@ControllerAdvice
public class GlobalControllerExceptionHandler {

    @ExceptionHandler(value = NotFoundException.class)
    protected ModelAndView handleNotFound(NotFoundException e) {
        ErrorMessage errorMessage = new ErrorMessage("NOT_FOUND", e.getLocalizedMessage(),
                HttpStatus.NOT_FOUND.value(), getLocalTimeFormatted());
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("errorMessage", errorMessage);
        modelAndView.setStatus(HttpStatus.NOT_FOUND);
        modelAndView.setViewName("customErrorMessage");
        return modelAndView;
    }

    @ExceptionHandler(value = BadRequestException.class)
    protected ModelAndView handleBadRequest(BadRequestException e) {
        ErrorMessage errorMessage = new ErrorMessage("BAD_REQUEST", e.getLocalizedMessage(),
                HttpStatus.BAD_REQUEST.value(), getLocalTimeFormatted());
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("errorMessage", errorMessage);
        modelAndView.setStatus(HttpStatus.BAD_REQUEST);
        modelAndView.setViewName("customErrorMessage");
        return modelAndView;
    }

//    @ExceptionHandler(value = RuntimeException.class)
//    protected ModelAndView somethingHappened(RuntimeException e) {
//        ErrorMessage errorMessage = new ErrorMessage("INTERNAL_SERVER_ERROR", e.getLocalizedMessage(),
//                HttpStatus.INTERNAL_SERVER_ERROR.value(), getLocalTimeFormatted());
//        ModelAndView modelAndView = new ModelAndView();
//        modelAndView.addObject("errorMessage", errorMessage);
//        modelAndView.setStatus(HttpStatus.INTERNAL_SERVER_ERROR);
//        modelAndView.setViewName("customErrorMessage");
//        return modelAndView;
//    }

    private String getLocalTimeFormatted() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd hh:mm:ss");
        return LocalDateTime.now().format(formatter);
    }
}
