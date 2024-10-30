package project.gym.model;

import java.time.LocalDate;
import java.util.Collection;
import java.util.List;
import java.util.Set;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonUnwrapped;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.With;
import project.gym.enums.Role;

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

    @Column(name = "avatar_url")
    private String avatarURL;

    @JsonIgnore
    private boolean archived = false;

    @OneToOne(cascade = CascadeType.ALL)
    @JsonUnwrapped
    private Contact contact;

    @ManyToMany(mappedBy = "members")
    @JsonIgnore
    private Set<Activity> activities;

    @OneToOne(cascade = CascadeType.ALL)
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
