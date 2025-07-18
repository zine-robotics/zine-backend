package com.dev.zine.api.model.task;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class TaskInstanceCreateBody {
    private String type;
    private String name;
    private String dpUrl;
    private String description;
    private String status;
    private Integer completionPercentage;
}
