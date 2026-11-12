package demoescsrsaxon.query.handlers;

import demoescsrsaxon.events.AccountCreditedEvent;
import demoescsrsaxon.query.repository.AccountRepository;
import demoescsrsaxon.query.repository.OperationRepository;
import org.axonframework.eventhandling.EventHandler;
import org.springframework.stereotype.Component;

@Component
public class AccountEventHandler {
    private AccountRepository accountRepository;
    private OperationRepository operationRepository;

    public AccountEventHandler(AccountRepository accountRepository, OperationRepository operationRepository) {
        this.accountRepository = accountRepository;
        this.operationRepository = operationRepository;
    }

    @EventHandler
    public void on(AccountCreditedEvent event){

    }

}
