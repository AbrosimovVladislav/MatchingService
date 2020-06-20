package ru.vakoom.matchingservice.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class TestController {

    @GetMapping("/")
    public String index() {
        return "redirect:swagger-ui.html";
    }

}
