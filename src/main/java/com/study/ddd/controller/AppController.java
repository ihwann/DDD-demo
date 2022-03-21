package com.study.ddd.controller;


import com.study.ddd.common.PrintUtil;
import com.study.ddd.constant.CommandKey;
import com.study.ddd.domain.ShoppingMall;
import com.study.ddd.entity.Item;
import com.study.ddd.domain.Pay;
import com.study.ddd.entity.RequestOrder;
import com.study.ddd.service.ItemService;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Controller;

import javax.annotation.PostConstruct;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * 어플리케이션의 메인 컨트롤러
 * 입력 커맨드로 메인페이지, 쇼핑몰 초기화, 주문, 종료 등 기능제어 담당
 */
@Controller
@RequiredArgsConstructor
public class AppController {

    private final ItemService itemService;
    private final ApplicationContext context;

    private BufferedReader br;
    private ShoppingMall shoppingMall;
    private Pay pay;

    /**
     * 빈이 초기화 되면서 BufferReader와 쇼핑몰 객체 생성
     *
     * @throws FileNotFoundException
     */
    @PostConstruct
    public void init() throws FileNotFoundException {
        br = new BufferedReader(new InputStreamReader(System.in));
        shoppingMall = new ShoppingMall();
    }

    /**
     * 메인페이지 호출
     * 주문, 종료 커맨드 검증, 쇼핑몰에 상품리스트 set
     *
     * @throws IOException
     * @throws InterruptedException
     */
    public void commandMainPage() throws IOException, InterruptedException {
        shoppingMall.setItemList(itemService.getItemListFromCsv());
        PrintUtil.printMain();
        String input = br.readLine();

        if (CommandKey.ORDER == CommandKey.of(input)) {
            commandItemList(br);
        } else if (CommandKey.QUIT == CommandKey.of(input)) {
            orderClose(br);
        } else {
            PrintUtil.inValidInputCommand();
            commandMainPage();
        }
    }

    /**
     * 메인에서 첫주문 실행 시 상품목록 출력
     */
    public void commandItemList(BufferedReader br) throws IOException, InterruptedException {
        Map<Integer, Item> itemList = shoppingMall.getItemList();
        for (Integer itemId : itemList.keySet()) {
            PrintUtil.printItem(itemList.get(itemId));
        }
        commandOrder(br);
    }

    /**
     * 사용자의 상품번호, 수량 정보로 주문서 생성
     * 쉼표(,)로 구분하여 여러 상품을 한꺼번에 입력 가능하며
     * 상품번호, 수량에 공백이 입력될 때 까지 반복적으로 수행 가능
     *
     * @param br
     * @throws IOException
     * @throws InterruptedException
     */
    private void commandOrder(BufferedReader br) throws IOException, InterruptedException {
        List<RequestOrder> orderList = new ArrayList<>();

        while (true) {
            PrintUtil.printOrderItemId();
            String[] requestItemId = br.readLine().split(",");
            PrintUtil.printOrderItemQuantity();
            String[] requestItemQuantity = br.readLine().split(",");

            if (isInputDataEmpty(requestItemId, requestItemQuantity)) {
                break;
            }
            if (!isValidOrderInputData(requestItemId, requestItemQuantity)) {
                orderList.clear();
                break;
            }

            for (int i = 0; i < requestItemId.length; i++) {
                String itemId = StringUtils.deleteWhitespace(requestItemId[i]);
                String quantity = StringUtils.deleteWhitespace(requestItemQuantity[i]);
                RequestOrder requestOrder = RequestOrder.requestOrderBuilder()
                        .itemId(Integer.parseInt(itemId))
                        .itemQuantity(Integer.parseInt(quantity))
                        .build();
                orderList.add(requestOrder);
            }
        }

        if (orderList.size() > 0) {
            requestOrder(orderList);
        } else {
            PrintUtil.inValidInputCommand();
            commandMainPage();
        }
    }

    /**
     * q, quit 입력 시 프로그램 종료
     *
     * @param br
     * @throws IOException
     */
    public void orderClose(BufferedReader br) throws IOException {
        PrintUtil.printGoodByeMessage();
        br.close();
        System.exit(SpringApplication.exit(context));
    }

    /**
     * 완성된 주문서를 쇼핑몰에 전달
     *
     * @param orderList : 주문서
     * @throws IOException
     * @throws InterruptedException
     */
    private void requestOrder(List<RequestOrder> orderList) throws IOException, InterruptedException {
        boolean isValidOrder = shoppingMall.validationOrder(orderList);
        if (isValidOrder) {
            Optional<Pay> requestPayInfo = Optional.ofNullable(shoppingMall.order(orderList));
            if (requestPayInfo.isPresent()) {
                Pay payInfo = requestPayInfo.get();
                payInfo.payPG(payInfo);
            } else {
                return;
            }
        }
        commandMainPage();
    }

    /**
     * 주문서 작성 페이지에서 사용자 입력 값 검증
     *
     * @param requestItemId
     * @param requestItemQuantity
     * @return
     */
    private boolean isValidOrderInputData(String[] requestItemId, String[] requestItemQuantity) {
        if (requestItemId.length != requestItemQuantity.length) return false;
        for (int i = 0; i < requestItemId.length; i++) {
            String id = StringUtils.deleteWhitespace(requestItemId[i]);
            String quantity = StringUtils.deleteWhitespace(requestItemQuantity[i]);
            if (!StringUtils.isNumeric(id) || !StringUtils.isNumeric(quantity)) return false;
        }

        return true;
    }

    private boolean isInputDataEmpty(String[] requestItemId, String[] requestItemQuantity) {
        if (requestItemId.length == 1 && requestItemQuantity.length == 1) {
            String id = StringUtils.deleteWhitespace(requestItemId[0]).trim();
            String quantity = StringUtils.deleteWhitespace(requestItemQuantity[0]).trim();

            return id.isEmpty() && quantity.isEmpty();
        } else {
            return false;
        }
    }
}
