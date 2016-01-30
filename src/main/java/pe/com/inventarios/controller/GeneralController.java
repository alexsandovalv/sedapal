package pe.com.inventarios.controller;

import java.io.IOException;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.multipart.MultipartException;
import org.springframework.web.servlet.ModelAndView;

import pe.com.inventarios.exception.ActivoConflictException;
import pe.com.inventarios.exception.ActivoNotFoundException;
import pe.com.inventarios.model.CustomErrorType;


@Controller
@ControllerAdvice
public class GeneralController {

    private static Log logger = LogFactory.getLog(GeneralController.class);

    @RequestMapping("/")
    public String index() {
        return "activo/formularioAnexo";
    }

//    @RequestMapping("/error")
//    public String errorRouter() {
//        return "error";
//    }

    @RequestMapping(value = "/router")
    public String accessDeniedRouter(@RequestParam("q") String resource) {
        logger.debug("In accessDeniedRouter resource = " + resource);

        return "redirect:/" + resource;
    }

    @RequestMapping(value = "/unauthorized")
    public ModelAndView accessDenied() {
        logger.debug("In accessDenied");

        ModelAndView mav = new ModelAndView();
        mav.addObject("timestamp", new Date());
        mav.setViewName("error/unauthorized");
        return mav;
    }
    
    @ResponseStatus(value=HttpStatus.NOT_FOUND)
    @ExceptionHandler(ActivoNotFoundException.class)
    public void activoNotFound(ActivoNotFoundException e){
    	logger.debug("en.. activoNotFound");
    	logger.error(".. error activoNotFound ..", e);
    }
    
    @ExceptionHandler(ActivoConflictException.class)
    public ResponseEntity<?> activoConflict(ActivoConflictException e){
    	logger.debug("en.. activoConcflict");
    	logger.error(".. error activoConflict ..", e);
    	return new ResponseEntity<>(e.getMessage(), HttpStatus.CONFLICT);
    }
    
    @ExceptionHandler(MultipartException.class)
    @ResponseStatus(value = HttpStatus.PRECONDITION_FAILED)
    @ResponseBody
    protected ResponseEntity<?> handleMaxUploadSizeExceededException(final HttpServletRequest request,
            final HttpServletResponse response, final Throwable e)
            throws IOException{
    	HttpStatus status = getStatus(request);
    	logger.warn(e.getMessage());
        return new ResponseEntity<>(e.getCause().getCause().getMessage(), status);
    }
    
    private HttpStatus getStatus(HttpServletRequest request) {
        Integer statusCode = (Integer) request.getAttribute("javax.servlet.error.status_code");
        if (statusCode == null) {
            return HttpStatus.INTERNAL_SERVER_ERROR;
        }
        return HttpStatus.valueOf(statusCode);
    }
    
    @ExceptionHandler(Exception.class)
    public ModelAndView handleException(HttpServletRequest req, Exception e) {
        logger.debug("In handleException");
        logger.error("Request: " + req.getRequestURL() + " raised " + e);
        logger.error("",e);
        ModelAndView mav = new ModelAndView();
        mav.addObject("exception", e);
        mav.addObject("timestamp", new Date());
        mav.addObject("url", req.getRequestURL());
        mav.setViewName("exception");
        return mav;
    }

    @RequestMapping(value = "/oups", method = RequestMethod.GET)
    public String triggerException() {
        throw new RuntimeException("Expected: controller used to showcase what " +
                "happens when an exception is thrown");
    }
}
