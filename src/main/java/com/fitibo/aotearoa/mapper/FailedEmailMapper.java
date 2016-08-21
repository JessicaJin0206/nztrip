package com.fitibo.aotearoa.mapper;

import com.fitibo.aotearoa.model.FailedEmail;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface FailedEmailMapper {

    @Insert("insert into `failed_email`(`order_id`, `from`, `to`, `subject`, `content`) " +
            "values(#{orderId}, #{from}, #{to}, #{subject}, #{content})")
    @Options(useGeneratedKeys = true, keyColumn = "id", keyProperty = "id", flushCache = Options.FlushCachePolicy.DEFAULT)
    int create(FailedEmail failedEmail);

    @Select("select * from failed_email")
    @Results({
            @Result(column = "id", property = "id"),
            @Result(column = "order_id", property = "orderId"),
            @Result(column = "from", property = "from"),
            @Result(column = "to", property = "to"),
            @Result(column = "subject", property = "subject"),
            @Result(column = "content", property = "content")
    })
    List<FailedEmail> findAll();

    @Delete("delete from failed_email where id = #{id}")
    int delete(int id);
}
