package org.sid.ebankingbackend;

import ch.qos.logback.core.net.SyslogOutputStream;
import org.sid.ebankingbackend.entities.*;
import org.sid.ebankingbackend.enums.AccountStatus;
import org.sid.ebankingbackend.enums.OperationType;
import org.sid.ebankingbackend.exceptions.BalanceNotsufficientException;
import org.sid.ebankingbackend.exceptions.BankAccountNotFoundException;
import org.sid.ebankingbackend.exceptions.CustomerNotFoundException;
import org.sid.ebankingbackend.repositories.AccountOperationRepository;
import org.sid.ebankingbackend.repositories.BankAccountRepository;
import org.sid.ebankingbackend.repositories.CustomerRepository;
import org.sid.ebankingbackend.services.BankAccountService;
import org.sid.ebankingbackend.services.Bankervice;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.stream.Stream;

@SpringBootApplication
public class EbankingBackendApplication {

    public static void main(String[] args) {
        SpringApplication.run(EbankingBackendApplication.class, args);
    }
    @Bean
    CommandLineRunner commandLineRunner (BankAccountService bankAccountService){
        return args -> {
            Stream.of("Hassan","Imane","Mohamed").forEach(name->{
                Customer customer=new Customer();
                customer.setName(name);
                customer.setEmail(name+"@gmail.com");
                bankAccountService.saveCustomer(customer);
            });
            bankAccountService.listCustomers().forEach(customer->{
                try {
                    bankAccountService.saveCurrentBankAccount(Math.random()*90000,9000,customer.getId());
                    bankAccountService.saveSavingBankAccount(Math.random()*90000,5.5,customer.getId());
                    List<BankAccount> bankAccounts=bankAccountService.bankAccountList();
                    for (BankAccount bankAccount:bankAccounts){
                        for (int i=0;i<10;i++){
                            bankAccountService.credit(bankAccount.getId(), 1000+Math.random()*12000, "Credit");
                            bankAccountService.debit(bankAccount.getId(), 1000+Math.random()*9000, "Debit");

                        }
                    }
                } catch (CustomerNotFoundException e) {
                    e.printStackTrace();
                } catch (BankAccountNotFoundException e) {
                    throw new RuntimeException(e);
                } catch (BalanceNotsufficientException e) {
                    throw new RuntimeException(e);
                }
            });
        };

    };
    //@Bean
    public CommandLineRunner start(CustomerRepository customerRepository, BankAccountRepository bankAccountRepository, AccountOperationRepository accountOperationRepository) {
        return args -> {
            Stream.of("Hassan","Yassine","Aisha").forEach(name->{
                Customer customer=new Customer();
                customer.setName(name);
                customer.setEmail(name+"@gmail.com");
                customerRepository.save(customer);
            });
            customerRepository.findAll().forEach(cust->{
                CurrentAccount currentAccount = new CurrentAccount();
                currentAccount.setId(UUID.randomUUID().toString());
                currentAccount.setBalance(Math.random()*90000);
                currentAccount.setCreatedAt(new Date());
                currentAccount.setStatus(AccountStatus.CREATED);
                currentAccount.setCustomer(cust);
                currentAccount.setOverDraft(9000);
                bankAccountRepository.save(currentAccount);

                SavingAccount savingAccount = new SavingAccount();
                savingAccount.setId(UUID.randomUUID().toString());
                savingAccount.setBalance(Math.random()*90000);
                savingAccount.setCreatedAt(new Date());
                savingAccount.setStatus(AccountStatus.CREATED);
                savingAccount.setCustomer(cust);
                savingAccount.setInterestRate(5.5);
                bankAccountRepository.save(savingAccount);


            });
            bankAccountRepository.findAll().forEach(
                    acc->{
                        for(int i=0;i<10;i++){
                            AccountOperation accountOperation=new AccountOperation();
                            accountOperation.setOperationDate(new Date());
                            accountOperation.setAmount(Math.random()*12000);
                            accountOperation.setType(Math.random()>0.5? OperationType.DEBIT:OperationType.CREDIT);
                            accountOperation.setBankAccount(acc);
                            accountOperationRepository.save(accountOperation);

                        }
                        BankAccount bankAccount=
                                bankAccountRepository.findById("0bc4c156-1bb8-44dd-a8e2-415317d479fa").orElse(null);
                        if(bankAccount!=null){
                        System.out.println("***********************");
                        System.out.println(bankAccount.getId());
                        System.out.println(bankAccount.getBalance());
                        System.out.println(bankAccount.getStatus());
                        System.out.println(bankAccount.getCreatedAt());
                        System.out.println(bankAccount.getCustomer().getName());
                        System.out.println(bankAccount.getClass().getSimpleName());
                        if (bankAccount instanceof CurrentAccount){
                            System.out.println("Over Draf=>"+((CurrentAccount) bankAccount).getOverDraft());
                        }else if (bankAccount instanceof SavingAccount){
                            System.out.println("Rate Interest=>"+((SavingAccount) bankAccount).getInterestRate());
                        }
                        bankAccount.getAccountOperations().forEach(
                                op->{
                                    System.out.println(op.getType()+"\t"+op.getAmount()+"\t"+op.getOperationDate());
                                }
                        );
                        }
                    }
            );
        };
    };


}
