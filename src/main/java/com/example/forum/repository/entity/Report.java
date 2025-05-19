package com.example.forum.repository.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Entity
@Table(name = "report")
//@Getter
//@Setter
@Data
public class Report {
    @Id
    @Column
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column
    private String content;

    @Column
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdDate;

    //@Column
    //@Temporal(TemporalType.TIMESTAMP)
    //private Date updatedDate;

}

