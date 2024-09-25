package project.gym.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonUnwrapped;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import project.gym.enums.Role;

import java.time.LocalDate;
import java.util.Collection;
import java.util.List;
import java.util.Set;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@With
public class Member implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonIgnore
    private long id;

    @Column(nullable = false)
    private String firstName;

    @Column(nullable = false)
    private String lastName;

    @Column(unique = true, nullable = false)
    private String email;

    @Column(nullable = false)
    @JsonIgnore
    private String password;

    @Column(nullable = false)
    private LocalDate birthdate;

    @Enumerated(EnumType.STRING)
    @JsonIgnore
    private Role role = Role.CUSTOMER;

    @JsonIgnore
    private boolean archived = false;

    @OneToOne(cascade = CascadeType.ALL)
    @JsonUnwrapped
    private Contact contact;

    @OneToOne
    private Image profilePicture;

    @ManyToMany(mappedBy = "members")
    @JsonIgnore
    private Set<Activity> activities;

    @ManyToOne
    @JsonIgnore
    private Pass pass;

    @ManyToOne
    @JsonIgnore
    private PaymentMethod paymentMethod;

    @OneToMany(mappedBy = "trainer")
    @JsonIgnore
    private Set<Activity> trainerActivities;

    @OneToOne(cascade = CascadeType.ALL)
    @JsonIgnore
    private AccountConfirmation accountConfirmation;

    @OneToOne(cascade = CascadeType.ALL)
    @JsonIgnore
    private PasswordReset passwordReset;

    @Override
    @JsonIgnore
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("ROLE_" + role.name()));
    }

    @Override
    @JsonIgnore
    public String getUsername() {
        return email;
    }

    @Override
    @JsonIgnore
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    @JsonIgnore
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    @JsonIgnore
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    @JsonIgnore
    public boolean isEnabled() {
        return true;
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof Member member) {
            return id == member.getId();
        }

        return false;
    }
}
