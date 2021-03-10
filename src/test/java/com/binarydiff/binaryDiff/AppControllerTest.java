package com.binarydiff.binaryDiff;

import com.binarydiff.binaryDiff.model.LeftItem;
import com.binarydiff.binaryDiff.model.RightItem;
import com.binarydiff.binaryDiff.services.DiffService;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Base64;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class AppControllerTest {
    @InjectMocks
    private AppController appController;
    @Mock
    private DiffService diffService;


    @Test
    void createLeftJSONItemTest() throws JSONException {
        String input = "{\"value\":\"Some input\"}";
        Integer id = 1;
        byte[] value = Base64.getEncoder().encode(input.getBytes());
        when(diffService.createLeftItem(any(Integer.class), any(byte[].class))).thenReturn(new LeftItem(id, new JSONObject(input)));
        ResponseEntity<String> response = appController.createLeftItem(id, value);
        Assertions.assertEquals(HttpStatus.CREATED, response.getStatusCode());
    }

    @Test
    void createRightItemTest() throws JSONException {
        String input = "{\"value\":\"Some input\"}";
        Integer id = 1;
        byte[] value = Base64.getEncoder().encode(input.getBytes());
        when(diffService.createRightItem(any(Integer.class), any(byte[].class))).thenReturn(new RightItem(id, new JSONObject(input)));
        ResponseEntity<String> response = appController.createRightItem(id, value);
        Assertions.assertEquals(HttpStatus.CREATED, response.getStatusCode());
    }

}
