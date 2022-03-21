package com.study.ddd.service;


import com.study.ddd.entity.Item;

import java.io.FileNotFoundException;
import java.util.List;

public interface ItemService {
    List<Item> getItemListFromCsv() throws FileNotFoundException;
    Item getItemById(int itemId);
}
