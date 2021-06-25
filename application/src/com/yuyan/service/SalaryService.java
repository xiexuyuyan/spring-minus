package com.yuyan.service;

import org.yuyan.beans.Bean;

@Bean
public class SalaryService {
    public Integer calcSalary(Integer experience) {
        return experience * 500;
    }
}
