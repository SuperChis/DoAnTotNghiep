package org.example.ezyshop.controller;

import org.example.ezyshop.service.StoreService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/store")
public class StoreController {
    @Autowired
    StoreService service;
}
