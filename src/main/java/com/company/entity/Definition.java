package com.company.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Definition {
    private Meanings[] meanings;
    private Phrases[] phrases;
    private String tags;
}
