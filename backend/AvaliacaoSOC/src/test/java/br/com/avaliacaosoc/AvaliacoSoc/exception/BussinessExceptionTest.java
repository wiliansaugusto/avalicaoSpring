package br.com.avaliacaosoc.AvaliacoSoc.exception;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.HttpRequestMethodNotSupportedException;

import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.*;

class BussinessExceptionTest {

    BussinessException exception = new BussinessException();
    @Test
    void handleLoginException() {
        BasicException basicException = new BasicException("Erro");
        ResponseEntity<String> responseEntity = exception.handleLoginException(basicException);
        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
    }

    @Test
    void noSuchElementExceptio() {
        NoSuchElementException noSuchElementException = new NoSuchElementException();
        ResponseEntity<String> responseEntity = exception.noSuchElementExceptio(noSuchElementException);
        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
    }

    @Test
    void jsonErro() {
        HttpMessageNotReadableException httpMessageNotReadableException = new HttpMessageNotReadableException("Json com erro");
        ResponseEntity<String> responseEntity = exception.jsonErro(httpMessageNotReadableException);
        assertEquals(HttpStatus.NOT_ACCEPTABLE, responseEntity.getStatusCode());
    }

    @Test
    void httpRequestMethodNotSupportedException() {
        ResponseEntity<String> responseEntity = exception.httpRequestMethodNotSupportedException(
                new HttpRequestMethodNotSupportedException(HttpStatus.NOT_ACCEPTABLE.toString()));
        assertEquals(HttpStatus.NOT_ACCEPTABLE, responseEntity.getStatusCode());
    }

    @Test
    void illegalArgumentException() {
        IllegalArgumentException illegalArgumentException  = new IllegalArgumentException();
        ResponseEntity<String> responseEntity = exception.illegalArgumentException(illegalArgumentException);
        assertEquals(HttpStatus.NOT_ACCEPTABLE, responseEntity.getStatusCode());
    }
}