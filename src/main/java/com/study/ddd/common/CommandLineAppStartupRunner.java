package com.study.ddd.common;

import com.study.ddd.controller.AppController;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

/**
 * main 메소드에서 실행되는 클래스, ApplicationRunner  run 메소드 구현으로
 * 주입된 AppController의 메인페이지 호출
 */
@Component
@RequiredArgsConstructor
public class CommandLineAppStartupRunner implements ApplicationRunner {
    private final AppController appController;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        appController.commandMainPage();
    }
}
