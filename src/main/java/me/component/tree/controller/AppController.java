package me.component.tree.controller;

import me.component.tree.vo.*;
import me.component.tree.param.*;
import me.component.tree.service.*;

import java.util.*;
import javax.validation.Valid;

import org.springframework.http.*;

import org.springframework.web.bind.annotation.*;

import org.springframework.beans.factory.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;

@RestController
public class AppController {

    @Autowired
    private TreeService treeSrv;

    /**
     * 获取所有根节点
     */
    @GetMapping("/get-root-node")
    public ResponseEntity<NodeView> getRootNode() {
        return new ResponseEntity<NodeView>(treeSrv.getRootNode(), HttpStatus.OK);
    }

    /**
     * 获取一个节点及其子节点
     */
    @PostMapping("/get-one-level-tree")
    public ResponseEntity<TreeView> getOneLevelTree(@Valid @RequestBody NodeSearchParam param) {
        return new ResponseEntity<TreeView>(treeSrv.getOneLevelTree(param), HttpStatus.OK);
    }

    /**
     * 获取根节点及其下级节点
     */
    @GetMapping("/get-root-one-level-tree")
    public ResponseEntity<TreeView> getRootOneLevelTree() {
        TreeView treeView = treeSrv.getOneLevelTree(
                NodeSearchParam.copyFromView(treeSrv.getRootNode()));
        return new ResponseEntity<TreeView>(treeView, HttpStatus.OK);
    }

    /**
     * 获取一颗子树
     */
    @PostMapping("/get-tree")
    public ResponseEntity<TreeView> getTree(@Valid @RequestBody NodeSearchParam param) {
        return new ResponseEntity<TreeView>(treeSrv.getTree(param), HttpStatus.OK);
    }
    
    /**
     * 增加根节点
     */
    @PostMapping("/add-root-node")
    public void addRootNode(@Valid @RequestBody NodeCreationParam param) {
        treeSrv.addRootNode(param);
    }

    /**
     * 增加子节点
     */
    @PostMapping("/add-node")
    public ResponseEntity<NodeView> addNode(@Valid @RequestBody NodeCreationParam param) {
        return new ResponseEntity<NodeView>(treeSrv.addNode(param), HttpStatus.OK);
    }

}
