package com.binarydiff.binaryDiff.repositories;

import com.binarydiff.binaryDiff.model.RightItem;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RightItemRepository extends CrudRepository<RightItem, Integer> {

}
