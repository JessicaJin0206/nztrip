package com.fitibo.aotearoa.mapper;

import com.fitibo.aotearoa.model.Attachment;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * Created by zhouqianhao on 11/03/2017.
 */
public interface AttachmentMapper {

    @Select("select * from attachment where email_id = #{emailId}")
    @Results({
            @Result(column = "id", property = "id"),
            @Result(column = "data", property = "data"),
            @Result(column = "name", property = "name"),
            @Result(column = "email_id", property = "emailId")
    }
    )
    List<Attachment> findByEmailId(@Param("emailId") int emailId);


    @Insert({
            "<script>",
            "<if test = 'attachments != null and attachments.size() > 0'>",
            "insert into attachment (data, name, email_id)",
            "values ",
            "<foreach  collection='attachments' item='item' separator=','>",
            "(#{item.data}, #{item.name}, #{item.emailId})",
            "</foreach>",
            "</if>",
            "</script>"
    })
    int create(@Param("attachments") List<Attachment> attachments);
}
