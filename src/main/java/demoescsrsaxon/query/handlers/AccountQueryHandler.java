package demoescsrsaxon.query.handlers;

import demoescsrsaxon.query.dtos.AccountStatementResponseDTO;
import demoescsrsaxon.query.entities.Account;
import demoescsrsaxon.query.entities.AccountOperation;
import demoescsrsaxon.query.queries.GetAccountStatementQuery;
import demoescsrsaxon.query.queries.GetAllAccountsQuery;
import demoescsrsaxon.query.queries.WatchEventQuery;
import demoescsrsaxon.query.repository.AccountRepository;
import demoescsrsaxon.query.repository.OperationRepository;
import org.axonframework.queryhandling.QueryHandler;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class AccountQueryHandler {
    private final AccountRepository accountRepository;
    private final OperationRepository operationRepository;

    public AccountQueryHandler(AccountRepository accountRepository, OperationRepository operationRepository) {
        this.accountRepository = accountRepository;
        this.operationRepository = operationRepository;
    }

    @QueryHandler
    public List<Account> on(GetAllAccountsQuery query) {
        return accountRepository.findAll();
    }


    @QueryHandler
    public AccountStatementResponseDTO on(GetAccountStatementQuery query){
        Account account = accountRepository.findById(query.getAccountId()).get();
        List<AccountOperation> operations = operationRepository.findByAccountId(query.getAccountId());
        return new AccountStatementResponseDTO(account, operations);
    }

    @QueryHandler
    public AccountOperation on (WatchEventQuery query){
        return AccountOperation.builder().build();
    }
}