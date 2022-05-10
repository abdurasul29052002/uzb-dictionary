package com.company.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Meanings {
    private Examples[] examples;
    private String reference;
    private String tags;
    private String text;
}
