package com.binarydiff.binaryDiff.repositories;

import com.binarydiff.binaryDiff.model.LeftItem;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LeftItemRepository extends CrudRepository<LeftItem, Integer> {
}
