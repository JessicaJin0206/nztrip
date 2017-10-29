package com.fitibo.aotearoa.mapper;

import com.fitibo.aotearoa.model.GroupMember;
import org.apache.ibatis.annotations.*;

import java.util.List;

/**
 * Created by qianhao.zhou on 8/3/16.
 */
public interface GroupMemberMapper {

    @Insert({
            "<script>",
            "insert into group_member (group_id, name, age, weight, people_type)",
            "values ",
            "<foreach  collection='list' item='item' separator=','>",
            "(#{item.groupId}, #{item.name}, #{item.age}, #{item.weight}, #{item.peopleType})",
            "</foreach>",
            "</script>"
    })
    int batchCreate(List<GroupMember> groupMembers);

    @Update({
            "update group_member set name = #{item.name}, age = #{item.age}, weight = #{item.weight}, people_type=#{item.peopleType} where id = #{item.id}",
    })
    int update(@Param("item") GroupMember groupMember);


    @Select("select * from group_member where group_id = #{groupId}")
    @Results({
            @Result(column = "id", property = "id"),
            @Result(column = "name", property = "name"),
            @Result(column = "group_id", property = "groupId"),
            @Result(column = "people_type", property = "peopleType"),
            @Result(column = "age", property = "age"),
            @Result(column = "weight", property = "weight")
    })
    List<GroupMember> findByGroupId(int groupId);

    @Delete("delete from group_member where id = #{item.id} ")
    int delete(@Param("item") GroupMember groupMember);

    @Delete("delete from group_member where group_id = #{groupId} ")
    int deleteByGroupId(int groupId);
}
