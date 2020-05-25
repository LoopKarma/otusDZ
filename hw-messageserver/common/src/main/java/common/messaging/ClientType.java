package common.messaging;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ClientType {
    FRONTEND("frontend"),
    DATABASE("database");

    private final String type;
}
