package com.study.ddd.domain;

import com.study.ddd.common.PrintUtil;
import com.study.ddd.entity.Item;
import com.study.ddd.entity.RequestOrder;
import lombok.Getter;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 쇼핑몰 도메인
 * 상품정보를 가지고 있으며, 주문서 검증, 재고파악, 이상 없다면 결제 도메인으로 주문서 전달
 */
@Getter
public class ShoppingMall {
    // 쇼핑몰에 있는 상품 리스트, 멀티스레드 환경의 데이터 동기화를 위해 ConcurrentHashMap으로 관리
    private Map<Integer, Item> itemList = new ConcurrentHashMap<>();

    // 상품정보 set
    public void setItemList(List<Item> itemList) {
        this.itemList = itemList.stream()
                .collect(Collectors.toMap(Item::getItemId, Function.identity()));
    }

    // 주문서 검증
    public boolean validationOrder(List<RequestOrder> orderList) {
        for (RequestOrder order : orderList) {
            // 상품리스트에 존재하는 상품번호 인지
            if (!itemList.containsKey(order.getItemId())) {
                PrintUtil.printNotFoundItemException();
                return false;
            }
            // 주문수량이 유효한지
            Item item = itemList.get(order.getItemId());
            if (isOutOfStockItem(order, item)) {
                PrintUtil.printSoldOutException();
                return false;
            }
        }
        return true;
    }

    // 주문수량 검증
    private boolean isOutOfStockItem(RequestOrder order, Item item) {
        int orderQuantity = order.getOrderQuantity();
        int itemQuantity = item.getQuantity();

        itemQuantity -= orderQuantity;
        return itemQuantity < 0;
    }

    // 주문서를 통해 상품리스트의 재고를 차감하고 결제 도메인으로 결제정보 전달
    public Pay order(List<RequestOrder> orderList) throws InterruptedException {
        synchronized (itemList) {
            for (RequestOrder order : orderList) {
                Item item = itemList.get(order.getItemId());
                int itemQuantity = item.getQuantity();
                order.setItemName(item.getItemName());
                order.setItemPrice(item.getItemPrice());

                int orderQuantity = order.getOrderQuantity();

                if (isOutOfStockItem(order, item)) {
                    PrintUtil.printSoldOutException(order);
                    return null;
                }
                item.changeQuantity(itemQuantity - orderQuantity);
            }
        }
        return Pay.payBuilder()
                .orderList(orderList)
                .build();
    }
}
