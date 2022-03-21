package com.study.ddd.entity;

import lombok.Builder;
import lombok.Getter;
import org.springframework.util.Assert;

/**
 * 주문서 DTO
 */
@Getter
public class RequestOrder {
    private Integer itemId;
    private Integer orderQuantity;
    private String itemName;
    private Integer itemPrice;

    /**
     * 주문도메인 빌더 생성자
     * @param itemId
     * @param itemQuantity
     */
    @Builder(builderClassName = "requestOrderBuilder", builderMethodName = "requestOrderBuilder")
    public RequestOrder(Integer itemId, Integer itemQuantity) {
        Assert.notNull(itemId, "itemId must be not null");
        Assert.notNull(itemQuantity, "itemQuantity must be not null");
        this.itemId = itemId;
        this.orderQuantity = itemQuantity;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public void setItemPrice(Integer itemPrice) {
        this.itemPrice = itemPrice;
    }
}
