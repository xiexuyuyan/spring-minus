package com.yuyan.service;

import com.yuyan.beans.Bean;

@Bean
public class SalaryService {
    public Integer calcSalary(Integer experience) {
        return experience * 500;
    }
}
