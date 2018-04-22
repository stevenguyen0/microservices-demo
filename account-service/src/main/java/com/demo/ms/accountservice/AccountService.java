package com.demo.ms.accountservice;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.demo.ms.accountservice.entity.Account;
import com.demo.ms.accountservice.repository.AccountRepository;

@RestController
@RequestMapping("/v1/accounts")
public class AccountService {
	
	@Autowired
    AccountRepository accountRepository;
	
	@GetMapping("/{id}")
	public Account getAccountt(@PathVariable Long id) {
		return accountRepository.findById(id).get();
	}
	
	@PostMapping
	public Account createNote(@Valid @RequestBody Account account) {
		account.setStatus("NEW");
	    return accountRepository.save(account);
	}
}
