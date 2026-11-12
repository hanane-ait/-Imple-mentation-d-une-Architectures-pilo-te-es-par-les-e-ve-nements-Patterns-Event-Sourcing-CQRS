package demoescsrsaxon.query.handlers;

import demoescsrsaxon.enums.OperationType;
import demoescsrsaxon.events.*;
import demoescsrsaxon.query.entities.Account;
import demoescsrsaxon.query.entities.AccountOperation;
import demoescsrsaxon.query.repository.AccountRepository;
import demoescsrsaxon.query.repository.OperationRepository;
import lombok.extern.slf4j.Slf4j;
import org.axonframework.eventhandling.EventHandler;
import org.axonframework.eventhandling.EventMessage;
import org.axonframework.queryhandling.QueryUpdateEmitter;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class AccountEventHandler {
    private AccountRepository accountRepository;
    private OperationRepository operationRepository;
    private QueryUpdateEmitter queryUpdateEmitter;

    public AccountEventHandler(AccountRepository accountRepository, OperationRepository operationRepository, QueryUpdateEmitter queryUpdateEmitter) {
        this.accountRepository = accountRepository;
        this.operationRepository = operationRepository;
        this.queryUpdateEmitter = queryUpdateEmitter;
    }

    @EventHandler
    public void on(AccountCreatedEvent event, EventMessage eventMessage){
        log.info("============== Query Side AccountCreatedEvent Recived ==============");
        Account account = Account.builder()
                .id(event.getAccountId())
                .balance(event.getInitialBalance())
                .status(event.getStatus())
                .currency(event.getCurrency())
                .createdAt(eventMessage.getTimestamp())
                .build();
        accountRepository.save(account);
    }

    @EventHandler
    public void on (AccountActivatedEvent event){
        log.info("============== Query Side AccountActivatedEvent Recived ==============");
        Account account = accountRepository.findById(event.getAccountId()).get();
        account.setStatus(event.getStatus());
        accountRepository.save(account);
    }

    @EventHandler
    public void on (AccountStatusUpdatedEvent event){
        log.info("============== Query Side AccountStatusUpdatedEvent Recived ==============");
        Account account = accountRepository.findById(event.getAccountId()).get();
        account.setStatus(event.getStatus());
        accountRepository.save(account);
    }

    @EventHandler
    public void on (AccountCreditedEvent event, EventMessage eventMessage){
        log.info("============== Query Side AccountCreditedEvent Recived ==============");
        Account account = accountRepository.findById(event.getAccountId()).get();
        AccountOperation accountOperation = AccountOperation.builder()
                .amount(event.getAmount())
                .date(eventMessage.getTimestamp())
                .type(OperationType.CREDIT)
                .currency(event.getCurrency())
                .account(account)
                .build();
        operationRepository.save(accountOperation);
        account.setBalance(account.getBalance()+accountOperation.getAmount());
        accountRepository.save(account);
        queryUpdateEmitter.emit (e->true, accountOperation);
    }

    @EventHandler
    public void on (AccountDebitedEvent event, EventMessage eventMessage){
        log.info("============== Query Side AccountDebitedEvent Recived ==============");
        Account account = accountRepository.findById(event.getAccountId()).get();
        AccountOperation accountOperation = AccountOperation.builder()
                .amount(event.getAmount())
                .date(eventMessage.getTimestamp())
                .type(OperationType.DEBIT)
                .currency(event.getCurrency())
                .account(account)
                .build();
        operationRepository.save(accountOperation);
        account.setBalance(account.getBalance()-accountOperation.getAmount());
        accountRepository.save(account);
        queryUpdateEmitter.emit (e->true, accountOperation);
    }

}
