package com.yuyan.service;

import org.yuyan.springmvc.beans.Bean;

@Bean
public class SalaryService {
    public Integer calcSalary(Integer experience) {
        return experience * 500;
    }
}
