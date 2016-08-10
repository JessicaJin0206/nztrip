package com.fitibo.aotearoa.mapper;

import com.fitibo.aotearoa.model.Agent;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.UUID;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

/**
 * Created by qianhao.zhou on 7/24/16.
 */
public class AgentMapperTest extends BaseTest {

    @Autowired
    private AgentMapper agentMapper;

    @Before
    public void setup() {
        agentMapper.deleteAll();
    }

    @After
    public void tearDown() {
        agentMapper.deleteAll();
    }

    private static void testEquals(Agent agent, Agent other) {
        assertEquals(agent.getName(), other.getName());
        assertEquals(agent.getUserName(), other.getUserName());
        assertEquals(agent.getDescription(), other.getDescription());
        assertEquals(agent.getDiscount(), other.getDiscount());
        assertEquals(agent.getEmail(), other.getEmail());
        assertEquals(agent.getPassword(), other.getPassword());
    }

    @Test
    public void testInsert() {
        Agent agent = new Agent();
        agent.setDescription("hello world");
        agent.setDiscount(90);
        agent.setName("Test Insert");
        agent.setUserName(UUID.randomUUID().toString());
        agent.setUserName("Test User Name");
        agent.setEmail("test@abc.com");
        agent.setPassword("nopassword");
        assertTrue(agentMapper.create(agent) == 1);
        assertTrue(agent.getId() > 0);
        testEquals(agentMapper.findById(agent.getId()), agent);
        try {
            agentMapper.create(agent);
            fail("insert agent with same name shall fall");
        } catch (Exception e) {
        }
        assertTrue(agentMapper.deleteById(agent.getId()) == 1);
    }
}
