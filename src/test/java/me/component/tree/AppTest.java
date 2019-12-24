package me.component.tree;

import me.component.tree.param.*;

import java.nio.file.*;
import java.util.stream.Stream;

import org.junit.jupiter.api.Test;

import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;

import com.fasterxml.jackson.databind.*;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class AppTest {

    @Autowired
    ObjectMapper objectMapper;

    @Test
    @Sql(statements = "delete from tree")
    public void testAddNode(@Autowired MockMvc mvc) throws Exception {

        //implemention node:
        //测试根节点插入
        //由于响应结果中包含自增的id, 无法用andExpect(content().json())方法
        //判断 暂时只测试到status().isOk()

        NodeCreationParam param = new NodeCreationParam();
        param.setParentId(0L);
        param.setName("图书");

        mvc.perform(
            put("/add-root-node")
            .contentType("application/json; charset=utf-8")
            .content(objectMapper.writeValueAsString(param)))
        .andExpect(status().isOk());
    }

    @Test
    @Sql(scripts = "classpath:test-data.sql")
    public void testGetAndRemoveTree(@Autowired MockMvc mvc) throws Exception {

        //implemention node:
        //插入测试数据, 测试获取树的功能
        //脚本中使用id=1作为根节点

        Stream<String> stream = Files.lines(Paths.get("src/test/resources/test-data.json"));
        StringBuilder sbd = new StringBuilder();
        stream.forEach(line -> sbd.append(line));
        String testDataJson = sbd.toString();

        mvc.perform(get("/get-tree?id=1"))
            .andExpect(status().isOk())
            .andExpect(content().json(testDataJson));

        mvc.perform(delete("/remove-tree?id=1"))
            .andExpect(status().isOk());

        mvc.perform(get("/get-tree?id=1"))
            .andExpect(status().isOk())
            .andExpect(content().json("{}"));
    }

}
