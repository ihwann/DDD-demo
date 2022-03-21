package com.study.ddd.common;

import com.study.ddd.entity.Item;
import com.study.ddd.domain.Pay;
import com.study.ddd.entity.RequestOrder;

import java.text.DecimalFormat;
import java.util.List;

/**
 * 콘솔 출력만 담당하는 클래스
 */
public class PrintUtil {

    private static final DecimalFormat MONEY_FORMATTER = new DecimalFormat("###,###");

    public static void printMain() {
        System.out.println("============================================");
        System.out.print(" 입력(o[order] : 주문, q[quit] 종료) : ");
    }

    public static void printOrderItemId() {
        System.out.print(" 상품번호(,쉼표 구분): ");
    }

    public static void printOrderItemQuantity() {
        System.out.print(" 수량(,쉼표 구분): ");
    }

    public static void printGoodByeMessage() {
        System.out.println("고객님의 주문 감사합니다.");
    }

    public static void inValidInputCommand() {
        System.out.println("입력 정보를 확인 해주세요");
    }

    public static void printNotFoundItemException() {
        System.out.println("NotFoundItemException 발생. 존재하지 않는 상품 입니다.");
    }

    public static void printSoldOutException() {
        System.out.println("SoldOutException 발생. 주문한 상품량이 재고량보다 큽니다.");
    }

    public static void printSoldOutException(RequestOrder order) {
        System.out.println("SoldOutException 발생. "
                + "주문하신 " + order.getItemName() + " "
                + order.getOrderQuantity() + "개"
                + " 상품량이 재고량보다 큽니다.");
    }

    public static void printItemList(List<Item> itemList) {
        for (Item item : itemList) {
            System.out.println(item.toString());
        }
        System.out.println();
    }

    public static void printItem(Item item) {
        System.out.println(item.toString());
    }

    public static void printReceipt(Pay pay) {
        System.out.println("주문 내역: ");
        System.out.println("--------------------------------------------------------------------");
        for (RequestOrder payItem : pay.getItemList()) {
            System.out.println(payItem.getItemName() + " - " + payItem.getOrderQuantity() + "개");
        }
        System.out.println("--------------------------------------------------------------------");
        System.out.println("주문금액: " + MONEY_FORMATTER.format(pay.getTotalItemAmt()));
        if (pay.getDeliveryAmt() > 0) System.out.println("배송비: " + MONEY_FORMATTER.format(pay.getDeliveryAmt()));
        System.out.println("--------------------------------------------------------------------");
        System.out.println("지불금액: " + MONEY_FORMATTER.format(pay.getTotalPayAmt()));
    }

    public static void printPaymentSuccess() {
        System.out.println("결제 되었습니다. 감사합니다.");
    }


}
