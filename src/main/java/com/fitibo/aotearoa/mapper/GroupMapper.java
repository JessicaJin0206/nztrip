package com.fitibo.aotearoa.mapper;

import com.fitibo.aotearoa.model.Group;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.session.RowBounds;

import java.util.List;

public interface GroupMapper {
    @Delete({
            "delete from `group`",
            "where id = #{id}"
    })
    int deleteByPrimaryKey(Integer id);

    @Insert({
            "insert into `group` (uuid,type, ",
            "agent_id, primary_contact, ",
            "primary_contact_email, primary_contact_phone, ",
            "primary_contact_wechat, ticket_date_start, ticket_date_end, total_cost_price, ",
            "total_price, status, ",
            "create_time, update_time, ",
            "remark)",
            "values (#{uuid},#{type,jdbcType=TINYINT}, ",
            "#{agentId}, #{primaryContact}, ",
            "#{primaryContactEmail}, #{primaryContactPhone}, ",
            "#{primaryContactWechat}, now(), now(), #{totalCostPrice}, ",
            "#{totalPrice}, #{status,jdbcType=TINYINT}, ",
            "#{createTime}, #{updateTime}, ",
            "#{remark})"
    })
    @Options(useGeneratedKeys = true, keyColumn = "id", keyProperty = "id", flushCache = Options.FlushCachePolicy.DEFAULT)
    int insert(Group record);

    int insertSelective(Group record);

    @Select({
            "select * ",
            "from `group`",
            "where id = #{id}"
    })
    @ResultMap("ResultMapWithBLOBs")
    Group findById(Integer id);

    int updateByPrimaryKeySelective(Group record);

    @Update({
            "update `group`",
            "set",
            "primary_contact = #{primaryContact},",
            "primary_contact_email = #{primaryContactEmail},",
            "primary_contact_phone = #{primaryContactPhone},",
            "primary_contact_wechat = #{primaryContactWechat},",
            "total_cost_price = #{totalCostPrice},",
            "total_price = #{totalPrice},",
            "update_time = now(),",
            "remark = #{remark}",
            "where id = #{id}"
    })
    int update(Group record);

    @Update({
            "update `group`",
            "set",
            "status = #{status},",
            "update_time = now()",
            "where id = #{groupId}"
    })
    int updateGroupStatus(@Param("groupId") int groupId, @Param("status") int status);

    @Update({
            "update `group`",
            "set",
            "ticket_date_start = #{ticketDateStart},",
            "ticket_date_end = #{ticketDateEnd}",
            "where id = #{id}"
    })
    int updateTicketDate(Group group);

    @Select({
            "select g.*,agent.name as agent_name",
            "from `group` g join agent on g.agent_id = agent.id",
            "order by create_time desc"
    })
    @ResultMap("ResultMapWithAgentName")
    List<Group> findByPage(RowBounds rowBounds);
}