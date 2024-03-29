package com.example.reservationsystem.security;

import com.example.reservationsystem.database.dao.EmployeeDao;
import com.example.reservationsystem.domain.Employee;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@AllArgsConstructor
public class UserLoginService implements UserDetailsService {
    private EmployeeDao employeeDao;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return Optional.ofNullable(employeeDao.getByEmail(username))
                .map(this::createUserDetails)
                .orElseThrow();
    }

    private UserDetails createUserDetails(Employee employee) {
        UserDetails details = User.builder()
                .username(employee.getEmail())
                .password(employee.getPasswordHash())
                .roles(getUserRole(employee))
                .build();
        EmployeeUser user = EmployeeUser.fromUserDetails(details);
        user.setEmail(details.getUsername());
        user.setFirstName(employee.getFirstName());
        user.setLastName(employee.getLastName());
        return user;
    }

    private String getUserRole(Employee employee) {
        if (employee.getPriority() == 0) {
            return Role.ADMIN.getRoleString();
        }
        return Role.USER.getRoleString();
    }
}
