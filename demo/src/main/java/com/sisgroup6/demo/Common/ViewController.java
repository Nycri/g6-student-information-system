package com.sisgroup6.demo.Common;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class ViewController {

    @GetMapping("/")
    public String root() {
        return "auth/index";
    }

    @GetMapping("/auth/index")
    public String authIndex() {
        return "auth/index";
    }

    @GetMapping("/auth/login")
    public String authLogin() {
        return "auth/login";
    }

    @GetMapping("/auth/forgot-password")
    public String authForgotPassword() {
        return "auth/forgot-password";
    }

    @GetMapping("/admin/{page}")
    public String adminPages(@PathVariable String page) {
        if (page.endsWith(".html")) {
            page = page.substring(0, page.length() - 5);
        }
        return "admin/" + page;
    }

    @GetMapping("/faculty/{page}")
    public String facultyPages(@PathVariable String page) {
        if (page.endsWith(".html")) {
            page = page.substring(0, page.length() - 5);
        }
        return "faculty/" + page;
    }

    @GetMapping("/student/{page}")
    public String studentPages(@PathVariable String page) {
        if (page.endsWith(".html")) {
            page = page.substring(0, page.length() - 5);
        }
        return "student/" + page;
    }
}
