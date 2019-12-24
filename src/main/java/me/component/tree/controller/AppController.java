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
     * 获取根节点
     */
    @GetMapping("/get-root-node")
    public ResponseEntity<NodeView> getRootNode() {
        return new ResponseEntity<NodeView>(treeSrv.getRootNode(), HttpStatus.OK);
    }

    /**
     * 获取一个节点及其子节点
     */
    @GetMapping("/get-one-level-tree")
    public ResponseEntity<TreeView> getOneLevelTree(long id) {
        return new ResponseEntity<TreeView>(treeSrv.getOneLevelTree(id), HttpStatus.OK);
    }

    /**
     * 获取根节点及其下级节点
     */
    @GetMapping("/get-root-one-level-tree")
    public ResponseEntity<? extends Object> getRootOneLevelTree() {
        TreeView treeView = treeSrv.getOneLevelTree(treeSrv.getRootNode().getId());
        return
            treeView == null ?
            new ResponseEntity<String>("{}", HttpStatus.OK) :
            new ResponseEntity<TreeView>(treeView, HttpStatus.OK);
    }

    /**
     * 获取一颗子树
     */
    @GetMapping("/get-tree")
    public ResponseEntity<? extends Object> getTree(long id) {
        TreeView treeView = treeSrv.getTree(id);
        return
            treeView == null ?
            new ResponseEntity<String>("{}", HttpStatus.OK) :
            new ResponseEntity<TreeView>(treeView, HttpStatus.OK);
    }
    
    /**
     * 增加根节点
     */
    @PutMapping("/add-root-node")
    public ResponseEntity<NodeView> addRootNode(@Valid @RequestBody NodeCreationParam param) {
        return new ResponseEntity<NodeView>(treeSrv.addRootNode(param), HttpStatus.OK);
    }

    /**
     * 增加子节点
     */
    @PostMapping("/add-node")
    public ResponseEntity<NodeView> addNode(@Valid @RequestBody NodeCreationParam param) {
        return new ResponseEntity<NodeView>(treeSrv.addNode(param), HttpStatus.OK);
    }

    /**
     * 删除一颗子树
     */
    @DeleteMapping("/remove-tree")
    public ResponseEntity<Void> removeTree(long id) {
        //implemention note: 参数id为根节点id
        //返回被删除的树
        treeSrv.removeTree(id);
        return new ResponseEntity<Void>(HttpStatus.OK);
    }
    
}
