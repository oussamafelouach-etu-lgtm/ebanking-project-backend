package org.sid.ebankingbackend;

import lombok.Data;
import org.sid.ebankingbackend.dtos.AccountOperationDTO;

import java.util.List;
@Data
public class AccountHistoryDTO {
    private String accountId;
    private int currentPage;
    private int totalPages;
    private int pageSize;
    private double balance;
    private List<AccountOperationDTO> accountOperationDTOS;
}
