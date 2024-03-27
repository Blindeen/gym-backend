package project.gym.model;

import jakarta.persistence.*;
import lombok.*;
import project.gym.enums.Role;

import java.util.Date;
import java.util.Set;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@With
public class Member {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(nullable = false)
    private String firstName;

    @Column(nullable = false)
    private String lastName;

    @Column(unique = true, nullable = false)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    @Temporal(TemporalType.DATE)
    private Date birthdate;

    @Enumerated(EnumType.STRING)
    @Column(columnDefinition = "varchar(255) default 'CUSTOMER'")
    private Role role = Role.CUSTOMER;

    @Column(columnDefinition = "boolean default false")
    private boolean archived = false;

    @OneToOne(cascade = CascadeType.ALL)
    private Contact contact;

    @ManyToMany
    private Set<Activity> activities;
}
