package me.component.tree.repository;

import java.util.*;

import org.springframework.data.jpa.repository.JpaRepository;

public interface TreeRepository extends JpaRepository<Node, Long> {

    Optional<Node> findOneByLft(int lft);
    
    ArrayList<Node>
        findAllByLftGreaterThanEqualAndLftLessThanEqualOrderByLft(int low, int high);

    ArrayList<Node>
        findAllByLftGreaterThanEqualAndRgtLessThanEqualAndDepthLessThanEqualOrderByLft(int low, int high, int depthHigh);

}
