package org.sid.ebankingbackend.web;

import org.sid.ebankingbackend.AccountHistoryDTO;
import org.sid.ebankingbackend.dtos.AccountOperationDTO;
import org.sid.ebankingbackend.dtos.BankAccountDTO;
import org.sid.ebankingbackend.exceptions.BankAccountNotFoundException;
import org.sid.ebankingbackend.services.BankAccountService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class BankAccountRestAPI {
    private final BankAccountService bankAccountService;

    private BankAccountRestAPI(BankAccountService bankAccountService){
        this.bankAccountService=bankAccountService;

    }
    @GetMapping("/accounts/{accountId}")
    public BankAccountDTO getBankAccount(@PathVariable String accountId) throws BankAccountNotFoundException {
        return bankAccountService.getBankAccount(accountId);
     }

        @GetMapping("/accounts")
     public List<BankAccountDTO> listBankAccounts(){
        return bankAccountService.bankAccountList();
     }
     @GetMapping("/accounts/{accountId}/pageOperations")
     public AccountHistoryDTO getAccountHistory(@PathVariable String accountId,
                                                @RequestParam(name="page",defaultValue = "0") int page,
                                                @RequestParam(name="size",defaultValue = "5")int size) throws BankAccountNotFoundException {
        return bankAccountService.getAccountHistory(accountId,page,size);
     }
}