package com.study.ddd.domain;

import com.study.ddd.common.PrintUtil;
import com.study.ddd.entity.RequestOrder;
import lombok.Builder;
import lombok.Getter;
import org.springframework.util.Assert;

import java.util.List;

/**
 * 결제 도메인
 * 배송비, 최종 주문결젝므액 세팅
 */
@Getter
public class Pay {
    List<RequestOrder> itemList;
    Integer deliveryAmt = 0;
    Integer totalItemAmt;
    Integer totalPayAmt;


    /**
     * 주문서로부터 결제 도메인 생성을 위한 빌더 생성자
     *
     * @param orderList
     */
    @Builder(builderClassName = "payBuilder", builderMethodName = "payBuilder")
    public Pay(List<RequestOrder> orderList) {
        Assert.notEmpty(orderList, "결제할 상품이 없습니다.");
        this.itemList = orderList;
        setDeliveryAmt(this.itemList);
        setTotalPayAmt(this.totalItemAmt);
    }

    /**
     * 배송비 세팅
     *
     * @param itemList
     */
    private void setDeliveryAmt(List<RequestOrder> itemList) {
        this.totalItemAmt = itemList.stream()
                .mapToInt(order -> order.getItemPrice() * order.getOrderQuantity())
                .sum();

        if (totalItemAmt < 50000) this.deliveryAmt = 2500;
        //setTotalPayAmt(totalItemAmt);
    }

    /**
     * 최종 결제금액 세팅
     *
     * @param totalItemAmt
     */
    private void setTotalPayAmt(Integer totalItemAmt) {
        this.totalPayAmt = this.deliveryAmt + totalItemAmt;
    }

    /**
     * PG사로 결제요청
     * @param pay
     * @return
     */
    public boolean payPG(Pay pay) {
        try {
            PrintUtil.printReceipt(this);
            PrintUtil.printPaymentSuccess();
            return true;
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return false;
        }
    }
}
