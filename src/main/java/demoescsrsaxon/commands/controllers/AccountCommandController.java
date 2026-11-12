
package demoescsrsaxon.commands.controllers;

import demoescsrsaxon.commands.commands.AddAccountCommand;
import demoescsrsaxon.commands.commands.CreditAccountCommand;
import demoescsrsaxon.commands.commands.DebitAccountCommand;
import demoescsrsaxon.commands.commands.UpdateAccountStatusCommand;
import demoescsrsaxon.commands.dtos.AddNewAccountRequestDTO;
import demoescsrsaxon.commands.dtos.CreditAccountRequestDTO;

import demoescsrsaxon.commands.dtos.DebitAccountRequestDTO;
import demoescsrsaxon.commands.dtos.UpdateAccountStatusRequestDTO;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.axonframework.eventsourcing.eventstore.EventStore;

import org.springframework.web.bind.annotation.*;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Stream;

@RestController
@RequestMapping("/commands/accounts")
public class AccountCommandController {

    private final CommandGateway commandGateway;
    private final EventStore eventStore;

    // Injection des deux dépendances
    public AccountCommandController(
            CommandGateway commandGateway,
            EventStore eventStore) {

        this.commandGateway = commandGateway;
        this.eventStore = eventStore;
    }

    @PostMapping("/add")
    public CompletableFuture<String> addNewAccount(
            @RequestBody AddNewAccountRequestDTO request) {

        return commandGateway.send(new AddAccountCommand(
                UUID.randomUUID().toString(),
                request.initialBalance(),
                request.currency()
        ));
    }

    @ExceptionHandler(Exception.class)
    public String exceptionHandler(Exception exception) {
        return exception.getMessage();
    }

    @GetMapping("/events/{accountId}")
    public Stream<?> getAccountEvents(
            @PathVariable String accountId) {

        return eventStore.readEvents(accountId).asStream();
    }

    @PostMapping("/credit")
    public CompletableFuture<String> creditAccount(
            @RequestBody CreditAccountRequestDTO request) {

        return commandGateway.send(new CreditAccountCommand(
                request.accountId(),
                request.amount(),
                request.currency()
        ));
    }

    @PostMapping("/debit")
    public CompletableFuture<String> debitAccount(@RequestBody DebitAccountRequestDTO request){
        CompletableFuture<String> response = commandGateway.send(new DebitAccountCommand(
                request.accountId(),
                request.amount(),
                request.currency()
        ));
        return response;
    }
    @PutMapping("/updateStatus")
    public CompletableFuture<String> updateStatus(@RequestBody UpdateAccountStatusRequestDTO request){
        CompletableFuture<String> response = commandGateway.send(new UpdateAccountStatusCommand(
                request.accountId(),
                request.status()
        ));
        return response;
    }


}
