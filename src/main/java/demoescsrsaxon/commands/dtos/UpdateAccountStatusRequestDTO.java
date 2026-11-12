package demoescsrsaxon.commands.dtos;

import demoescsrsaxon.enums.AccountStatus;

public record UpdateAccountStatusRequestDTO(String accountId, AccountStatus status) {
}
