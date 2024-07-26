package com.example.trackit.model.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "users")
@Getter
@Setter
public class User extends BaseEntity{

    @Column(nullable = false)
    private String username;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(name = "registration_date", nullable = false)
    private LocalDate registrationDate;
    @ManyToOne
    private Role role;

    @OneToMany(mappedBy = "user")
    private List<Budget> budgets;

    @OneToMany(mappedBy = "user")
    private List<Expense> expenses;

    @OneToMany(mappedBy = "user")
    private List<Saving> savings;



    public User() {
        this.budgets = new ArrayList<>();
        this.expenses = new ArrayList<>();
        this.savings = new ArrayList<>();
        this.registrationDate = LocalDate.now();
    }

    public User(String username, String email, String password) {
        this.budgets = new ArrayList<>();
        this.expenses = new ArrayList<>();
        this.savings = new ArrayList<>();
        this.registrationDate = LocalDate.now();
        this.username = username;
        this.email = email;
        this.password = password;
    }
}
