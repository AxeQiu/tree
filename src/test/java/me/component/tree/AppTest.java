package me.component.tree;

import me.component.tree.param.*;

import org.junit.jupiter.api.Test;

import org.springframework.test.web.servlet.MockMvc;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
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
    public void addNodeTest(@Autowired MockMvc mvc) throws Exception {
        //插入根节点
        NodeCreationParam rootNode = new NodeCreationParam();
        rootNode.setParentId(0L);
        rootNode.setName("图书");

        mvc.perform(
            post("/add-root-node")
            .contentType("application/json; charset=utf-8")
            .content(objectMapper.writeValueAsString(rootNode)))
        .andExpect(status().isOk());
    }
}
