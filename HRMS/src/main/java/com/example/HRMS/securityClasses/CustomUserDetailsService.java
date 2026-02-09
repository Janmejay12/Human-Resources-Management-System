package com.example.HRMS.securityClasses;

import com.example.HRMS.entities.Employee;
import com.example.HRMS.repos.EmployeeRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
@Service
public class CustomUserDetailsService implements UserDetailsService {

        private final EmployeeRepository employeeRepository;

        public CustomUserDetailsService(EmployeeRepository userRepository) {
            this.employeeRepository = userRepository;
        }

        @Override
        public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
            Employee user = employeeRepository.findByEmail(email)
            .orElseThrow(() -> new UsernameNotFoundException("User not found with username: " + email));
            return new CustomEmployee(user);
        }

}
