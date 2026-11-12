package demoescsrsaxon.query.dtos;

import demoescsrsaxon.query.entities.Account;
import demoescsrsaxon.query.entities.AccountOperation;

import java.util.List;

public record AccountStatementResponseDTO(
        Account account , List<AccountOperation> operations
        )
{
}
