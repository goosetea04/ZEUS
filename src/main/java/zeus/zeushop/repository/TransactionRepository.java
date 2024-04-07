package zeus.zeushop.repository;

import zeus.zeushop.model.Transaction;
import zeus.zeushop.model.TransactionStatus;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Repository
public class TransactionRepository {
    private final List<Transaction> transactions = new ArrayList<>();

    public Transaction create(Transaction transaction) {
        transactions.add(transaction);
        return transaction;
    }

    public List<Transaction> findAll() {
        return new ArrayList<>(transactions);
    }

    public List<Transaction> findByStatus(TransactionStatus status) {
        return transactions.stream()
                .filter(transaction -> transaction.getStatus() == status)
                .collect(Collectors.toList());
    }
}
