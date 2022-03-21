package com.study.ddd;


import com.study.ddd.common.PrintUtil;
import com.study.ddd.domain.Pay;
import com.study.ddd.domain.ShoppingMall;
import com.study.ddd.entity.Item;
import com.study.ddd.entity.RequestOrder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

public class MultiTreadTest {

    private Item item;
    private ShoppingMall shoppingMall;

    /**
     * 멀티스레드 주문 테스트를 위한 기본정보 초기화
     */
    @BeforeEach
    public void setup() {
        Item 상품 = Item.selectItemBuilder()
                .itemId(1)
                .itemName("블랙 맨투맨")
                .itemPrice(1000)
                .quantity(10)
                .build();
        List<Item> itemList = new ArrayList<>();
        itemList.add(상품);

        shoppingMall = new ShoppingMall();
        shoppingMall.setItemList(itemList);
    }

    /**
     * CompletableFuture 를 사용하여 재고가 10개인 상품을 비동기로 5개, 6개 주문하고
     * 늦게도착한 주문서는 수량부족으로 SoldOutException 발생
     *
     * @throws ExecutionException
     * @throws InterruptedException
     */
    @Test
    public void multiTreadTest() throws ExecutionException, InterruptedException {

        CompletableFuture<Void> orderThread1 = CompletableFuture.runAsync(() -> {
            try {
                RequestOrder order = RequestOrder.requestOrderBuilder()
                        .itemId(1)
                        .itemQuantity(5)
                        .build();

                List<RequestOrder> orderList = new ArrayList<>();
                orderList.add(order);

                Optional<Pay> successPay1 = Optional.ofNullable(shoppingMall.order(orderList));
                if (successPay1.isPresent()) {
                    Pay pay = successPay1.get();
                    pay.payPG(pay);
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });

        CompletableFuture<Void> orderThread2 = CompletableFuture.runAsync(() -> {
            try {
                RequestOrder order = RequestOrder.requestOrderBuilder()
                        .itemId(1)
                        .itemQuantity(6)
                        .build();

                List<RequestOrder> orderList = new ArrayList<>();
                orderList.add(order);

                Optional<Pay> successPay2 = Optional.ofNullable(shoppingMall.order(orderList));
                if (successPay2.isPresent()) {
                    Pay pay = successPay2.get();
                    pay.payPG(pay);
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });

        //CompletableFuture.allOf(orderThread1, orderThread2).;
        CompletableFuture.anyOf(orderThread1, orderThread2).isCompletedExceptionally();
    }
}
