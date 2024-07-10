package com.example.trackit.model.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "roles")
@Getter
@Setter
public class Role extends BaseEntity{
    private String name;
    @OneToMany(mappedBy = "role")
    private List<User> users;

    public Role() {
        this.users = new ArrayList<>();
    }
}
