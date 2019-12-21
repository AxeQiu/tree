package me.component.tree.repository;

import javax.persistence.*;

@lombok.Getter
@lombok.Setter
@Entity
@Table(name = "tree")
public class Node {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column
    private String name;

    @Column
    private int lft;

    @Column
    private int rgt;

    @Column
    private int depth;
}
