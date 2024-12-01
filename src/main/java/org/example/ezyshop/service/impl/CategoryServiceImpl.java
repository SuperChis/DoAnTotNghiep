package org.example.ezyshop.service.impl;

import org.example.ezyshop.repository.CategoryRepository;
import org.example.ezyshop.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CategoryServiceImpl implements CategoryService {
    @Autowired
    private CategoryRepository repository;


}
