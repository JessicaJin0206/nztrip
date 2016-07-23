package io.qhzhou.nztrip.mapper;

import io.qhzhou.nztrip.model.Agent;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.Assert.assertEquals;

/**
 * Created by qianhao.zhou on 7/24/16.
 */
public class AgentMapperTest extends BaseTest {

    @Autowired
    private AgentMapper agentMapper;

    @Test
    public void test() {
        Agent agent = agentMapper.findById(1);
        assertEquals("qhzhou", agent.getUserName());
        assertEquals("周千昊", agent.getName());
        assertEquals("password", agent.getPassword());
        assertEquals(100, agent.getDiscount());
        assertEquals("z.qianhao@gmail.com", agent.getEmail());
    }
}
