package com.study.ddd.service.impl;

import com.study.ddd.common.CsvManager;
import com.study.ddd.entity.Item;
import com.study.ddd.service.ItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.FileNotFoundException;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 상품에 필요한 기능을 담당하는 인터페이스와 구현체
 */
@Service
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {

    private final CsvManager csvManager;

    /**
     * csv 에서 읽은 정보로 상품리스트 생성
     * @return
     * @throws FileNotFoundException
     */
    @Override
    public List<Item> getItemListFromCsv() throws FileNotFoundException {
        List<List<String>> lists = csvManager.readCsv();
        return lists.stream()
                .skip(1)
                .map(list -> {
                    return Item.selectItemBuilder()
                            .itemId(Integer.parseInt(list.get(0)))
                            .itemName(list.get(1))
                            .itemPrice(Integer.parseInt(list.get(2)))
                            .quantity(Integer.parseInt(list.get(3)))
                            .build();
                })
                .collect(Collectors.toList());
    }

    @Override
    public Item getItemById(int itemId) {
        return csvManager.readCsvById(String.valueOf(itemId));
    }
}
