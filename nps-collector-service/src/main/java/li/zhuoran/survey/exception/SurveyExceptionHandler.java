package li.zhuoran.survey.exception;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.servlet.http.HttpServletRequest;

import static java.util.Objects.isNull;

@ControllerAdvice
public class SurveyExceptionHandler {

    private Logger logger = Logger.getLogger(getClass());

    @ExceptionHandler(value = Exception.class)
    public ResponseEntity<ErrorInfo<String>> defaultErrorHandler(HttpServletRequest req, Exception ex) {

        logger.error("Request: " + req.getRequestURL() + " raised " + ex);

        ex.printStackTrace();

        return getErrorInfoResponseEntity(req, ex, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(value = QuestionNotFoundException.class)
    public ResponseEntity<ErrorInfo<String>> questionNotFoundErrorHandler(HttpServletRequest req, QuestionNotFoundException ex) {

        logger.error("Request: " + req.getRequestURL() + " raised " + ex);

        ex.printStackTrace();

        return getErrorInfoResponseEntity(req, ex, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(value = QuestionAnsweredException.class)
    public ResponseEntity<ErrorInfo<String>> questionAnsweredErrorHandler(HttpServletRequest req, QuestionAnsweredException ex) {

        logger.warn("Request: " + req.getRequestURL() + " raised " + ex);

        return getErrorInfoResponseEntity(req, ex, HttpStatus.PRECONDITION_FAILED);
    }

    @ExceptionHandler(value = SurveyException.class)
    public ResponseEntity<ErrorInfo<String>> surveyErrorHandler(HttpServletRequest req, SurveyException ex) {

        logger.error("Request: " + req.getRequestURL() + " raised " + ex);

        return getErrorInfoResponseEntity(req, ex, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    private ResponseEntity<ErrorInfo<String>> getErrorInfoResponseEntity(HttpServletRequest req, Exception ex, HttpStatus httpStatus) {
        ErrorInfo<String> r = new ErrorInfo<>();

        r.setMessage(isNull(ex.getMessage()) ? ex.toString() : ex.getMessage());

        r.setCode(httpStatus);

        r.setUrl(req.getRequestURL().toString());

        r.setData(ExceptionUtils.getStackTrace(ex));

        return r.toResponseEntity();
    }
}