package com.study.ddd.entity;

import lombok.Builder;
import lombok.Getter;
import org.springframework.util.Assert;

/**
 * 상품 DTO
 */
@Getter
public class Item {
    Integer itemId;
    String itemName;
    Integer itemPrice;
    Integer quantity;


    /**
     * csv에서 읽은 데이터를 상품객체로 생성하기 위한 빌더 생성자
     * @param itemId : 상품번호
     * @param itemName : 상품명
     * @param itemPrice : 판매가격
     * @param quantity : 재고수량
     */
    @Builder(builderClassName = "selectItemBuilder", builderMethodName = "selectItemBuilder")
    public Item(Integer itemId, String itemName, Integer itemPrice, Integer quantity) {
        Assert.notNull(itemId, "itemId must not be null");
        Assert.notNull(itemName, "itemName must not be null");
        Assert.notNull(itemPrice, "itemPrice must not be null");
        Assert.notNull(quantity, "quantity must not be null");
        this.itemId = itemId;
        this.itemName = itemName;
        this.itemPrice = itemPrice;
        this.quantity = quantity;
    }

    /**
     * 쇼핑몰 도메인에서 주문 시 재고 차감
     */
    public void changeQuantity(int orderQuantity) {
        this.quantity = orderQuantity;
    }

    @Override
    public String toString() {
        return "상품번호 = " + itemId +
                ", 상품명 = '" + itemName + '\'' +
                ", 판매가격 = " + itemPrice +
                ", 수량 = " + quantity;
    }
}
