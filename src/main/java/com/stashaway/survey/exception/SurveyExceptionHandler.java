package com.stashaway.survey.exception;

import com.stashaway.survey.dao.ErrorInfo;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;

@ControllerAdvice
public class SurveyExceptionHandler {

    @ExceptionHandler(value = Exception.class)
    public ModelAndView defaultErrorHandler(HttpServletRequest req, Exception e) throws Exception {

        ModelAndView mav = new ModelAndView();

        mav.addObject("exception", e);

        mav.addObject("url", req.getRequestURL());

        mav.setViewName("error");

        return mav;
    }

    @ExceptionHandler(value = NullPointerException.class)
    @ResponseBody
    public ErrorInfo<String> nullPointerExceptionErrorHandler(HttpServletRequest req, NullPointerException npe) throws Exception {
        ErrorInfo<String> r = new ErrorInfo<>();

        r.setMessage(npe.getMessage());

        r.setCode(HttpStatus.NOT_FOUND.value());

        r.setUrl(req.getRequestURL().toString());

        return r;
    }

    @ExceptionHandler(value = SurveyException.class)
    @ResponseBody
    public ErrorInfo<String> jsonErrorHandler(HttpServletRequest req, SurveyException e) throws Exception {

        ErrorInfo<String> r = new ErrorInfo<>();

        r.setMessage(e.getMessage());

        r.setCode(ErrorInfo.ERROR);

        r.setData("Some Data");

        r.setUrl(req.getRequestURL().toString());

        return r;
    }

}