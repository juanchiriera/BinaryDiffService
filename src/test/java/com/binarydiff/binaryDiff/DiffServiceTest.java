package com.binarydiff.binaryDiff;

import com.binarydiff.binaryDiff.model.LeftItem;
import com.binarydiff.binaryDiff.model.RightItem;
import com.binarydiff.binaryDiff.repositories.LeftItemRepository;
import com.binarydiff.binaryDiff.repositories.RightItemRepository;
import com.binarydiff.binaryDiff.services.DiffService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;
import java.util.Base64;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class DiffServiceTest {
    @Mock
    LeftItemRepository leftItemRepository;
    @Mock
    RightItemRepository rightItemRepository;
    @InjectMocks
    DiffService diffService;

    @Test
    void testCreatingLeftItem() throws JSONException {
        Integer id = 1;
        String valueString = "{\"value\":\"Some String\"}";
        byte[] value = Base64.getEncoder().encode(valueString.getBytes());
        when(leftItemRepository.save(any(LeftItem.class))).thenReturn(new LeftItem(id, new JSONObject(valueString)));
        LeftItem result = diffService.createLeftItem(id, value);
        Assertions.assertTrue(result instanceof LeftItem);
        Assertions.assertEquals(1, result.getId());
        Assertions.assertEquals(valueString, result.getValue().toString());
    }

    @Test
    void testCreatingRightItem() throws JSONException {
        Integer id = 1;
        String valueString = "{\"value\":\"Some String\"}";
        byte[] value = Base64.getEncoder().encode(valueString.getBytes());
        when(rightItemRepository.save(any(RightItem.class))).thenReturn(new RightItem(id, new JSONObject(valueString)));
        RightItem result = diffService.createRightItem(id, value);
        Assertions.assertTrue(result instanceof RightItem);
        Assertions.assertEquals(1, result.getId());
        Assertions.assertEquals(valueString, result.getValue().toString());
    }

    @Test
    void compareEqualItemsTest() throws JSONException, IOException {
        JSONObject jsonValue = new JSONObject("{\"value\":\"Some String\"}");
        LeftItem leftItem = new LeftItem(1, jsonValue);
        RightItem rightItem = new RightItem(1, jsonValue);
        JSONObject result = diffService.compareItems(leftItem, rightItem);
        Assertions.assertEquals(jsonValue, result);
    }

    @Test
    void compareDifferentSizeItemsTestWithSameNodes() throws JSONException, IOException {
        JSONObject leftJson = new JSONObject("{\"value\":\"Some String\"}");
        JSONObject rightJson = new JSONObject("{\"value\":\"Some other String\"}");
        ObjectMapper mapper = new ObjectMapper();
        LeftItem leftItem = new LeftItem(1, leftJson);
        LeftItem rightItem = new LeftItem(1, rightJson);
        JSONObject result = diffService.compareItems(leftItem, rightItem);
        JSONObject expected = new JSONObject("{\"value\":\"(Some String, Some other String)\"}");
        Assertions.assertEquals(mapper.readTree(expected.toString()), mapper.readTree(result.toString()));
    }

    @Test
    void compareEqualSizeItemsTest() throws JSONException, IOException {
        JSONObject leftJson = new JSONObject("{\"value\":\"Some String\"}");
        JSONObject rightJson = new JSONObject("{\"value\":\"Some Values\"}");
        ObjectMapper mapper = new ObjectMapper();
        LeftItem leftItem = new LeftItem(1, leftJson);
        LeftItem rightItem = new LeftItem(1, rightJson);
        JSONObject result = diffService.compareItems(leftItem, rightItem);
        JSONObject expected = new JSONObject("{\"value\":\"(Some String, Some Values)\"}");
        Assertions.assertEquals(mapper.readTree(expected.toString()), mapper.readTree(result.toString()));
    }

    @Test
    void compareLargerLeftItemDiff() throws JSONException, IOException {
        JSONObject leftJson = new JSONObject("{\"value\":\"Some String\", \"secondValue\": \"Some other String\"}");
        JSONObject rightJson = new JSONObject("{\"value\":\"Some String\"}");
        ObjectMapper mapper = new ObjectMapper();
        LeftItem leftItem = new LeftItem(1, leftJson);
        LeftItem rightItem = new LeftItem(1, rightJson);
        JSONObject result = diffService.compareItems(leftItem, rightItem);
        JSONObject expected = new JSONObject("{\"secondValue\":\"Some other String\"}");
        Assertions.assertEquals(mapper.readTree(expected.toString()), mapper.readTree(result.toString()));
    }
}
