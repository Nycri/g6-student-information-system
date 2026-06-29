package com.sisgroup6.demo.Admin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class AdminDataInitializer implements CommandLineRunner {

    @Autowired
    private AdminRepository adminRepository;

    @Override
    public void run(String... args) throws Exception {
        if (adminRepository.count() == 0) {
            Admin defaultAdmin = new Admin(
                "admin",
                "admin123",
                "System Administrator",
                "admin@university.edu"
            );
            adminRepository.save(defaultAdmin);
            System.out.println("Seeded default administrator account: username 'admin', password 'admin123'");
        }
    }
}
