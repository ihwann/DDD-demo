package com.study.ddd.common;

import com.study.ddd.entity.Item;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.NoSuchElementException;

/**
 * resource 경로의 csv 파일을 읽는 기능을 담당
 */
@Component
public class CsvManager {

    private final String FILE_PATH = "src/main/resources/items.csv";

    public List<List<String>> readCsv() {
        List<List<String>> csvList = new ArrayList<List<String>>();
        BufferedReader br = null;

        try {
            br = Files.newBufferedReader(Paths.get(FILE_PATH));
            //Charset.forName("UTF-8");
            String line = "";

            while ((line = br.readLine()) != null) {
                //CSV 1행을 저장하는 리스트
                List<String> tmpList = new ArrayList<String>();
                String array[] = line.split(",(?=([^\"]*\"[^\"]*\")*[^\"]*$)", -1);
                //배열에서 리스트 반환
                tmpList = Arrays.asList(array);
                csvList.add(tmpList);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (br != null) {
                    br.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return csvList;
    }

    public Item readCsvById(String id) {
        BufferedReader br = null;
        List<Item> itemByList = new ArrayList<>();
        try {
            br = Files.newBufferedReader(Paths.get(FILE_PATH));
            //Charset.forName("UTF-8");
            String line = "";

            while ((line = br.readLine()) != null) {
                //CSV 1행을 저장하는 리스트
                List<String> tmpList = new ArrayList<String>();
                String array[] = line.split(",(?=([^\"]*\"[^\"]*\")*[^\"]*$)", -1);
                //배열에서 리스트 반환
                if (id.equals(array[0])) {
                    Item item = Item.selectItemBuilder()
                            .itemId(Integer.parseInt(array[0]))
                            .itemName(array[1])
                            .itemPrice(Integer.parseInt(array[2]))
                            .quantity(Integer.parseInt(array[3]))
                            .build();
                    itemByList.add(item);
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (br != null) {
                    br.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        if (itemByList.size() > 0) {
            return itemByList.get(0);
        } else {
            throw new NoSuchElementException("존재하지 않는 상품 입니다.");
        }
    }
}
