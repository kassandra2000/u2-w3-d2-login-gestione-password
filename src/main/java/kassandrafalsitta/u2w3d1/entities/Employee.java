package kassandrafalsitta.u2w3d1.entities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import kassandrafalsitta.u2w3d1.enums.Role;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@ToString
@Entity
@Table(name = "employees")
@JsonIgnoreProperties({"password", "role", "authorities", "enabled", "accountNonLocked", "accountNonExpired", "credentialsNonExpired"})
public class Employee implements UserDetails {
        @Id
        @GeneratedValue
        @Setter(AccessLevel.NONE)
        private UUID id;
        private String username;
        private String name;
        private String surname;
        private String email;
        private String avatar;
        private String password;
        @Enumerated(EnumType.STRING)
        private Role role;
        //costruttore

        public Employee( String username, String name, String surname, String email, String avatar,String password) {
                this.username = username;
                this.name = name;
                this.surname = surname;
                this.email = email;
                this.avatar = avatar;
                this.password = password;
                this.role = Role.EMPLOYEE;
        }

        @Override
        public Collection<? extends GrantedAuthority> getAuthorities() {
                return List.of(new SimpleGrantedAuthority(this.role.name()));
        }

}
