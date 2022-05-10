package com.company.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Words {
    private boolean success;

    private boolean matchFound;

    private String[] suggestions;
}
