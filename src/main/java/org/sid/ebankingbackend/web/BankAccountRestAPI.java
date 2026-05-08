package org.sid.ebankingbackend.web;

import org.sid.ebankingbackend.services.BankAccountService;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class BankAccountRestAPI {
    private final BankAccountService bankAccountService;

    private BankAccountRestAPI(BankAccountService bankAccountService){
        this.bankAccountService=bankAccountService;

    }


}
