package com.binarydiff.binaryDiff;

import com.binarydiff.binaryDiff.model.LeftItem;
import com.binarydiff.binaryDiff.model.RightItem;
import com.binarydiff.binaryDiff.services.DiffService;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

/**
 * Encoded JSON diff finder application.
 *
 * The application exposes 3 endpoints in which you can create items or get their differences.
 * @author  Juan Cruz Riera
 * @version v0.1
 */

@RestController
@RequestMapping("/v1/diff")
public class AppController {

    @Autowired
    private DiffService diffService;

    public AppController(){
        diffService = new DiffService();
    }

    /**
     * Creates Left Item under defined ID and encoded JSON Body.
     * @param id Defines the identifier of the JSON element.
     * @param value Encoded Base64 JSON String.
     * @return Status 201 - if element was successfully created.
     *         Status 406 - if not a JSON object.
     */
    @PostMapping("{id}/left")
    public ResponseEntity<String> createLeftItem(
            @PathVariable("id") Integer id,
            @RequestBody byte[] value
            ){
        try {
            diffService.createLeftItem(id, value);
            return new ResponseEntity<>(HttpStatus.CREATED);
        } catch (JSONException e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.NOT_ACCEPTABLE);
        }
    }

    /**
     * Creates Right Item under defined ID and encoded JSON Body.
     * @param id Defines the identifier of the JSON element.
     * @param value Encoded Base64 JSON String.
     * @return Status 201 - if element was successfully created.
     *         Status 406 - if not a JSON object.
     */
    @PostMapping("{id}/right")
    public ResponseEntity<String> createRightItem(
            @PathVariable("id") Integer id,
            @RequestBody byte[] value) {
        try {
            diffService.createRightItem(id, value);
            return new ResponseEntity<>(HttpStatus.CREATED);
        } catch (JSONException e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.NOT_ACCEPTABLE);
        }
    }

    /**
     * Compares Left and Right items given an ID.
     * @param id Identifier of Left and Right items.
     * @return If elements are equal, it will return the whole element. If they differ, it will return their differences as a JSON.
     */
    @GetMapping("/{id}")
    public ResponseEntity<String> getDifferences(@PathVariable("id") Integer id){
        LeftItem leftItem = diffService.getLeftItem(id).orElse(new LeftItem());
        RightItem rightItem = diffService.getRightItem(id).orElse(new RightItem());
        try {
            if (leftItem.getId() == null || rightItem.getId() == null){
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
            JSONObject responseBody = diffService.compareItems(leftItem, rightItem);
            return ResponseEntity.ok(responseBody.toString());
        } catch (IOException e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
