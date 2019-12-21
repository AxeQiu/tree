package me.component.tree.repository;

import java.util.*;

import org.springframework.data.jpa.repository.JpaRepository;

public interface TreeRepository extends JpaRepository<Node, Long> {
    Optional<Node> findOneByLft(int lft);
    List<Node> findAllByLftGreaterThanAndRgtLessThanOrderByLft(int lft, int rgt);
    List<Node> findAllByDepthAndLftGreaterThanAndRgtLessThan(int depth, int lft, int rgt);
}
