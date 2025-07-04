package com.dev.zine.model;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "task_instance")
@Data
@NoArgsConstructor
public class TaskInstance {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name ="task_instance_id" , nullable = false)
    private Long taskInstanceId;

    @ManyToOne
    @JoinColumn(name = "task_id")
    private Task taskId;
    
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name="room_id", referencedColumnName = "id")
    private Rooms roomId;

    @Column(name = "type")
    private String type;

    @Column(name = "name")
    private String name;

    @Column(name = "status")
    private String status;

    @Column(name = "completion_percentage", columnDefinition = "int default 0")
    @Max(100)
    @Min(0)
    private Integer completionPercentage;

    @JsonIgnore
    @OneToMany(mappedBy = "taskInstanceId", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<UserTaskAssigned> userTaskAssigned = new ArrayList<>();

    @JsonIgnore
    @OneToMany(mappedBy = "taskInstance", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<TaskInstanceComments> taskInstanceComments = new ArrayList<>();

    @JsonIgnore
    @OneToMany(mappedBy = "taskInstance", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<InstanceLinks> instanceLinks = new ArrayList<>();

    @JsonIgnore
    @OneToMany(mappedBy = "taskInstance", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<InstanceCheckpoints> instanceCheckpoints = new ArrayList<>();
}
