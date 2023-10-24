package com.example.a29;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class MyController {
    @ResponseBody
    public Student student(Student student){
        return student;
    }
}
