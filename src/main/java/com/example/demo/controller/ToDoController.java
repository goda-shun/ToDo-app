package com.example.demo.controller;

import org.springframework.stereotype.Controller;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;


@Controller
@RequiredArgsConstructor

public class ToDoController {

  @GetMapping("/")
  public String home() {
    return "User/index";
  }

  @GetMapping("/new")
  public String form() {
    return "User/ToDo-form";
  }

}
