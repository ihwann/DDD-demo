package com.study.ddd.constant;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * 콘솔에 입력 가능한 커맨드 관리를 위한 enum
 */
public enum CommandKey {
    ORDER("ORDER", Arrays.asList("o", "order")),
    QUIT("QUIT", Arrays.asList("q", "quit")),
    EMPTY("INVALID", Collections.EMPTY_LIST);


    private String inputName;
    private List<String> inputGroup;

    CommandKey(String inputName, List<String> inputGroup) {
        this.inputName = inputName;
        this.inputGroup = inputGroup;
    }

    public static CommandKey of(String code) {
        return Arrays.stream(CommandKey.values())
                .filter(certKey -> certKey.checkAvailCode(code))
                .findAny()
                .orElse(CommandKey.EMPTY);
    }

    public boolean checkAvailCode(String code) {
        return inputGroup.stream().anyMatch(s -> s.equals(code));
    }

}
