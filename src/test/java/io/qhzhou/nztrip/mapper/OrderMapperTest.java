package io.qhzhou.nztrip.mapper;

import io.qhzhou.nztrip.constants.OrderStatus;
import io.qhzhou.nztrip.model.Order;
import io.qhzhou.nztrip.util.GuidGenerator;
import org.apache.ibatis.session.RowBounds;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.Assert.assertEquals;

/**
 * Created by qianhao.zhou on 7/25/16.
 */
public class OrderMapperTest extends BaseTest {

    @Autowired
    private OrderMapper orderMapper;
    private static final int AGENT_ID = 256;

    @Before
    public void setup() {
        Order order1 = new Order();
        order1.setSkuId(100);
        order1.setAgentId(AGENT_ID);
        order1.setPrice(1024);
        order1.setRemark("{}");
        order1.setStatus(OrderStatus.PENDING);
        order1.setPrimaryContact("will");
        order1.setPrimaryContactEmail("will@abc.com");
        order1.setPrimaryContactWechat("123124523");
        order1.setPrimaryContactPhone("1342323456");
        order1.setUuid(GuidGenerator.generate(14));
        assertEquals(orderMapper.create(order1), 1);

        Order order2 = new Order();
        order2.setSkuId(101);
        order2.setAgentId(AGENT_ID);
        order2.setPrice(1000);
        order2.setRemark("{}");
        order2.setStatus(OrderStatus.CONFIRMED);
        order2.setPrimaryContact("phil");
        order2.setPrimaryContactEmail("phil@abc.com");
        order2.setPrimaryContactWechat("1234124523");
        order2.setPrimaryContactPhone("1342123456");
        order2.setUuid(GuidGenerator.generate(14));
        assertEquals(orderMapper.create(order2), 1);

    }

    private static void testEquals(Order order, Order other) {
        assertEquals(order.getSkuId(), other.getSkuId());
        assertEquals(order.getUuid(), other.getUuid());
        assertEquals(order.getAgentId(), other.getAgentId());
        assertEquals(order.getCreateTime(), other.getCreateTime());
        assertEquals(order.getUpdateTime(), other.getUpdateTime());
        assertEquals(order.getPrice(), other.getPrice());
        assertEquals(order.getGatheringInfo(), other.getGatheringInfo());
        assertEquals(order.getPrimaryContact(), other.getPrimaryContact());
        assertEquals(order.getPrimaryContactEmail(), other.getPrimaryContactEmail());
        assertEquals(order.getPrimaryContactPhone(), other.getPrimaryContactPhone());
        assertEquals(order.getPrimaryContactWechat(), other.getPrimaryContactWechat());
        assertEquals(order.getSecondaryContact(), other.getPrimaryContact());
        assertEquals(order.getSecondaryContactEmail(), other.getPrimaryContactEmail());
        assertEquals(order.getSecondaryContactPhone(), other.getPrimaryContactPhone());
        assertEquals(order.getSecondaryContactWechat(), other.getPrimaryContactWechat());
    }

    @After
    public void tearDown() {

    }

    @Test
    public void test() {
        assertEquals(orderMapper.findByAgentId(-1, new RowBounds(0, 1)).size(), 0);
        assertEquals(orderMapper.findByAgentId(-1, new RowBounds(0, 100)).size(), 0);
        assertEquals(orderMapper.findByAgentId(AGENT_ID, new RowBounds(0, 1)).size(), 1);
        assertEquals(orderMapper.findByAgentId(AGENT_ID, new RowBounds(0, 2)).size(), 2);
        assertEquals(orderMapper.findByAgentId(AGENT_ID, new RowBounds(0, 100)).size(), 2);
        assertEquals(orderMapper.findByAgentIdAndStatus(AGENT_ID, OrderStatus.CONFIRMED, new RowBounds(0, 100)).size(), 1);
        assertEquals(orderMapper.findByAgentIdAndStatus(AGENT_ID, OrderStatus.PENDING, new RowBounds(0, 100)).size(), 1);
        assertEquals(orderMapper.findByAgentIdAndStatus(AGENT_ID, OrderStatus.CLOSED, new RowBounds(0, 100)).size(), 0);
    }
}
