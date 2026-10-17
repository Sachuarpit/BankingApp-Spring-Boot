package net.javaguides.Banking.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import net.javaguides.Banking.entity.Account;

public interface AccountRepository extends JpaRepository <Account, Long> {

}
