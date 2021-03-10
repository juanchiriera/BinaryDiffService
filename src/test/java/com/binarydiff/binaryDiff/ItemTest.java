package com.binarydiff.binaryDiff;

import com.binarydiff.binaryDiff.model.LeftItem;
import com.binarydiff.binaryDiff.model.RightItem;
import com.binarydiff.binaryDiff.repositories.LeftItemRepository;
import com.binarydiff.binaryDiff.repositories.RightItemRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Base64;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class ItemTest {

    @Autowired
    LeftItemRepository leftItemRepository;
    @Autowired
    RightItemRepository rightItemRepository;

    @Test
    void createLeftItem() throws JSONException {
        String input = "{\"value\":\"Left Item\"}";
        LeftItem lefItem = new LeftItem(1, new JSONObject(input));
        Assertions.assertEquals(1, lefItem.getId());
        Assertions.assertEquals(input, lefItem.getValue().toString());
    }

    @Test
    void createRightItem() throws JSONException {
        String input = "{\"value\":\"Right Item\"}";
        RightItem rightItem = new RightItem(1, new JSONObject(input));
        Assertions.assertEquals(1, rightItem.getId());
        Assertions.assertEquals(input, rightItem.getValue().toString());
    }

    @Test
    void createLeftItemAndSave() throws JSONException, JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        String input = "{\"value\":\"Left Item\"}";
        LeftItem lefItem = leftItemRepository.save(new LeftItem(1, new JSONObject(input)));
        Iterable<LeftItem> leftItems = leftItemRepository.findAll();
        assertThat(leftItems).hasSize(1).contains(lefItem);
    }

    @Test
    void createRightItemAndSave() throws JSONException, JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        String input = "{\"value\":\"Right Item\"}";
        RightItem lefItem = rightItemRepository.save(new RightItem(1, new JSONObject(input)));
        Iterable<RightItem> rightItems = rightItemRepository.findAll();
        assertThat(rightItems).hasSize(1).contains(lefItem);
    }

}
