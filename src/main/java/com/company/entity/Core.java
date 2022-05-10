package com.company.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Core {
    private String core;
    private Definition[] definition;
    private boolean isDerivative;
    private String tailStructure;
    private String type;
    private boolean wordExists;

}
