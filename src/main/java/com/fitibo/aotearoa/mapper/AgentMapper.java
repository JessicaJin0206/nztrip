package com.fitibo.aotearoa.mapper;

import com.fitibo.aotearoa.model.Agent;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

/**
 * Created by qianhao.zhou on 7/24/16.
 */
public interface AgentMapper {

    @Select("select * from agent")
    @Results({
            @Result(column = "id", property = "id"),
            @Result(column = "name", property = "name"),
            @Result(column = "email", property = "email"),
            @Result(column = "user_name", property = "userName"),
            @Result(column = "password", property = "password"),
            @Result(column = "description", property = "description"),
            @Result(column = "default_contact", property = "defaultContact"),
            @Result(column = "default_contact_email", property = "defaultContactEmail"),
            @Result(column = "default_contact_phone", property = "defaultContactPhone"),
            @Result(column = "vendor_id", property = "vendorId"),
    })
    List<Agent> findAll();

    @Select("select * from agent where id = #{id}")
    @Results({
            @Result(column = "id", property = "id"),
            @Result(column = "name", property = "name"),
            @Result(column = "email", property = "email"),
            @Result(column = "user_name", property = "userName"),
            @Result(column = "password", property = "password"),
            @Result(column = "description", property = "description"),
            @Result(column = "default_contact", property = "defaultContact"),
            @Result(column = "default_contact_email", property = "defaultContactEmail"),
            @Result(column = "default_contact_phone", property = "defaultContactPhone"),
            @Result(column = "vendor_id", property = "vendorId"),
    })
    Agent findById(int id);

    @Select("select * from agent where user_name = #{user}")
    @Results({
            @Result(column = "id", property = "id"),
            @Result(column = "name", property = "name"),
            @Result(column = "email", property = "email"),
            @Result(column = "user_name", property = "userName"),
            @Result(column = "password", property = "password"),
            @Result(column = "description", property = "description"),
            @Result(column = "default_contact", property = "defaultContact"),
            @Result(column = "default_contact_email", property = "defaultContactEmail"),
            @Result(column = "default_contact_phone", property = "defaultContactPhone"),
            @Result(column = "vendor_id", property = "vendorId"),
    })
    Agent findByUserName(String user);

    @Insert("insert into agent(user_name, password, name, description, discount, email) " +
            "values(#{userName}, #{password}, #{name}, #{description}, #{discount}, #{email})")
    @Options(useGeneratedKeys = true, keyColumn = "id", keyProperty = "id", flushCache = Options.FlushCachePolicy.DEFAULT)
    int create(Agent agent);

    @Delete("delete from agent where id = #{id}")
    int deleteById(int id);

    @Delete("delete from agent")
    int deleteAll();

    @Update("<script>" +
            "update agent " +
            "<set> " +
            "<if test = \"userName != null and userName != ''\">user_name = #{userName},</if>" +
            "<if test = \"name != null and name != ''\">name = #{name},</if>" +
            "email = #{email}," +
            "<if test = \"description != null and description != ''\">description = #{description},</if>" +
            "<if test = \"discount != null and discount != 0\">discount = #{discount},</if>" +
            "<if test = \"password != null and password != ''\">password = #{password},</if>" +
            "<if test = \"defaultContact != null and defaultContact != ''\">default_contact = #{defaultContact},</if>" +
            "<if test = \"defaultContactEmail != null and defaultContactEmail != ''\">default_contact_email = #{defaultContactEmail},</if>" +
            "<if test = \"defaultContactPhone != null and defaultContactPhone != ''\">default_contact_phone = #{defaultContactPhone},</if>" +
            "</set>" +
            "where id = #{id}" +
            "</script>")
    int update(Agent agent);

}
