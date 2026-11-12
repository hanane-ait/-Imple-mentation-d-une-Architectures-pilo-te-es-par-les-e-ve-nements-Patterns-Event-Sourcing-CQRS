package demoescsrsaxon.events;

import demoescsrsaxon.enums.AccountStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class AccountActivatedEvent {
    private String accountId;
    private AccountStatus status;

}
