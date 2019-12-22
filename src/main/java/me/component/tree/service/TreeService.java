package me.component.tree.service;

import me.component.tree.vo.*;
import me.component.tree.param.*;
import me.component.tree.repository.*;

import java.util.*;
import java.util.stream.Collectors;

import javax.persistence.*;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TreeService {

    @Autowired
    private EntityManager em;

    @Autowired
    private TreeRepository treeRepo;

    /**
     * 获取根节点
     */
    @Transactional(readOnly = true)
    public NodeView getRootNode() {
        Node node = treeRepo.findOneByLft(1).orElse(null);
        return NodeView.map(node);
    }

    /**
     * 获取一个节点及其子节点
     */
    @Transactional(readOnly = true)
    public TreeView getOneLevelTree(NodeSearchParam param) {
        Node parent = treeRepo.findById(param.getId()).get();
        ArrayList<Node> list =
            treeRepo.findAllByLftGreaterThanEqualAndRgtLessThanEqualAndDepthLessThanEqualOrderByLft(parent.getLft(), parent.getRgt(), parent.getDepth() + 1);
        //return TreeView.map(parent, children);
        return TreeView.map(list);
    }

    /**
     * 获取一颗树
     */
    @Transactional(readOnly = true)
    public TreeView getTree(NodeSearchParam param) {
        Node root = treeRepo.findById(param.getId()).get();
        //返回列表是有序的, 可以用于构造树结构
        ArrayList<Node> list = treeRepo.findAllByLftGreaterThanEqualAndLftLessThanEqualOrderByLft(root.getLft(), root.getRgt());
        return TreeView.map(list);
    }

    /**
     * 添加根节点
     */
    @Transactional
    public NodeView addRootNode(NodeCreationParam param) {
        if (treeRepo.findOneByLft(1).isPresent()) {
            throw new RuntimeException("根节点已存在");
        }
        Node node = new Node();
        node.setName(param.getName());
        node.setLft(1);
        node.setRgt(2);
        node.setDepth(1);
        treeRepo.save(node);
        return NodeView.map(node);
    }

    /**
     * 添加节点
     */
    @Transactional
    public NodeView addNode(NodeCreationParam param) {
        //select ... for update
        Node parentNode = em.find(
                Node.class,
                param.getParentId(),
                LockModeType.PESSIMISTIC_WRITE,
                Collections.singletonMap("javax.persistence.lock.timeout", 0));

        Node node = new Node();
        node.setName(param.getName());
        node.setLft(parentNode.getRgt());
        node.setRgt(parentNode.getRgt()+1);
        node.setDepth(parentNode.getDepth()+1);
        em.createNativeQuery("update tree set rgt=rgt+2 where rgt >= ?1").setParameter(1, parentNode.getRgt()).executeUpdate();
        em.createNativeQuery("update tree set lft=lft+2 where lft > ?1").setParameter(1, parentNode.getRgt()).executeUpdate();
        em.persist(node);
        em.flush();

        return NodeView.map(node);
    }

}
