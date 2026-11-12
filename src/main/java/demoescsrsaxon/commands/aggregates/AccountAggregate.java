
package demoescsrsaxon.commands.aggregates;

import demoescsrsaxon.commands.commands.AddAccountCommand;
import demoescsrsaxon.commands.commands.CreditAccountCommand;
import demoescsrsaxon.enums.AccountStatus;
import demoescsrsaxon.events.AccountActivatedEvent;
import demoescsrsaxon.events.AccountCreatedEvent;

import demoescsrsaxon.events.AccountCreditedEvent;
import lombok.extern.slf4j.Slf4j;

import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.eventsourcing.EventSourcingHandler;
import org.axonframework.modelling.command.AggregateIdentifier;
import org.axonframework.modelling.command.AggregateLifecycle;
import org.axonframework.spring.stereotype.Aggregate;

@Aggregate
@Slf4j
public class AccountAggregate {

    @AggregateIdentifier
    private String accountId;

    private double balance;
    private AccountStatus status;

    public AccountAggregate() {
    }

    // Réception et traitement de la commande
    @CommandHandler
    public AccountAggregate(AddAccountCommand command) {

        log.info("############## AddAccountCommand received ############");

        if (command.getInitialBalance() <= 0) {
            throw new IllegalArgumentException("Balance must be positive");
        }

        AggregateLifecycle.apply(new AccountCreatedEvent(
                command.getId(),
                command.getInitialBalance(),
                AccountStatus.CREATED,
                command.getCurrency()
        ));
        AggregateLifecycle.apply(new AccountActivatedEvent(
                command.getId(),
                AccountStatus.ACTIVATED

        ));
    }

    // Mise à jour de l'état de l'agrégat
    @EventSourcingHandler
    public void on(AccountCreatedEvent event) {

        log.info("############## AccountCreatedEvent occurred ############");

        this.accountId = event.getAccountId();
        this.balance = event.getInitialBalance();
        this.status = event.getStatus();
    }

    @EventSourcingHandler
    public void on(AccountActivatedEvent event) {

        log.info("############## AccountCreatedEvent occurred ############");

        this.accountId = event.getAccountId();
        this.status = event.getStatus();
    }

    @CommandHandler
    public void handle(CreditAccountCommand command){
        log.info("############# CreditAccountCommand Received #############");
        if(!status.equals(AccountStatus.ACTIVATED)) throw new RuntimeException("The account is not activated");
        if (command.getAmount() <= 0) throw new IllegalArgumentException("Amount must be positive");

        AggregateLifecycle.apply(new AccountCreditedEvent(
                command.getId(),
                command.getAmount(),
                command.getCurrency()
        ));
    }

    @EventSourcingHandler
    public void on(AccountCreditedEvent event) {

        log.info("############## AccountCreditedEvent occurred ############");

        this.accountId = event.getAccountId();
        this.balance = this.balance +event.getAmount();

    }


}
