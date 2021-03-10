package com.binarydiff.binaryDiff.services;

import com.binarydiff.binaryDiff.model.Item;
import com.binarydiff.binaryDiff.model.LeftItem;
import com.binarydiff.binaryDiff.model.RightItem;
import com.binarydiff.binaryDiff.repositories.LeftItemRepository;
import com.binarydiff.binaryDiff.repositories.RightItemRepository;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.MapDifference;
import com.google.common.collect.Maps;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * Diff finder service.
 *
 * It handles the creation and manipulation of Left and Right Items.
 * @author Juan Cruz Riera
 */
@Service
public class DiffService {

    @Autowired
    LeftItemRepository leftItemRepository;

    @Autowired
    RightItemRepository rightItemRepository;

    public DiffService(){}

    /**
     * Compare two {@link Item} instances ({@link LeftItem} and {@link RightItem}) and return their differences (if any).
     * It is extensible in order to compare any Item if ever required.
     * @param firstItem first selected item to compare.
     * @param secondItem second selected item to compare.
     * @return Original JSON if Items are equal. JSON with differences if Items are not equal.
     * @throws IOException On mapper reading tree operation.
     */
    public JSONObject compareItems(Item firstItem, Item secondItem) throws IOException {
        JSONObject result = new JSONObject();
        ObjectMapper mapper = new ObjectMapper();
        //Compare whether item values are equal
        if (mapper.readTree(firstItem.getValue().toString()).equals(mapper.readTree(secondItem.getValue().toString()))){
            //If equal, return JSON value.
            result = firstItem.getValue();
        }else{
            //If not equal, get differences and build JSON Object

            byte[] leftJson = firstItem.getValue().toString().getBytes();
            byte[] rightJson = secondItem.getValue().toString().getBytes();
            TypeReference<HashMap<String, Object>> type = new TypeReference<HashMap<String, Object>>() {};
            Map<String, Object> leftMap = mapper.readValue(leftJson, type);
            Map<String, Object> rightMap = mapper.readValue(rightJson, type);

            //Retrieve differences with guava Maps library.
            MapDifference<String, Object> difference = Maps.difference(leftMap, rightMap);
            JSONObject finalResult = result;
            //Add differences between equal nodes.
            difference.entriesDiffering().forEach((key, value) -> {
                try {
                    finalResult.put(key, value);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            });
            //Add LeftItem non present nodes on RightItem
            difference.entriesOnlyOnLeft().forEach((key, value) -> {
                try {
                    finalResult.put(key, value);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            });
            //Add RightItem non present nodes on LeftItem
            difference.entriesOnlyOnRight().forEach((key, value) -> {
                try {
                    finalResult.put(key, value);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            });
            return finalResult;
        }
        return result;
    }

    /**
     * Create {@link LeftItem} instance and save it on database.
     * @param id Identifier of the Item.
     * @param value JSON encoded value.
     * @return LeftItem created and saved on database.
     * @throws JSONException thrown if value is not a valid encoded JSON object.
     */
    public LeftItem createLeftItem(Integer id, byte[] value) throws JSONException {
        JSONObject jsonValue = new JSONObject(new String(Base64.getDecoder().decode(value)));
        LeftItem leftItem = new LeftItem(id, jsonValue);
        return leftItemRepository.save(leftItem);
    }

    /**
     * Create {@link RightItem} instance and save it on database.
     * @param id Identifier of the Item.
     * @param value JSON encoded value.
     * @return RightItem created and saved on database.
     * @throws JSONException thrown if value is not a valid encoded JSON object.
     */
    public RightItem createRightItem(Integer id, byte[] value) throws JSONException {
        JSONObject jsonValue = new JSONObject(new String(Base64.getDecoder().decode(value)));
        RightItem rightItem = new RightItem(id, jsonValue);
        return rightItemRepository.save(rightItem);
    }

    /**
     * Retrieve {@link LeftItem} from database given an ID.
     * @param id Identifier of the Item.
     * @return Optional Item object, empty if not found.
     */
    public Optional<LeftItem> getLeftItem(Integer id) {
        return leftItemRepository.findById(id);
    }

    /**
     * Retrieve {@link RightItem} from database given an ID.
     * @param id Identifier of the Item.
     * @return Optional Item object, empty if not found.
     */
    public Optional<RightItem> getRightItem(Integer id) {
        return rightItemRepository.findById(id);
    }
}
