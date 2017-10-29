package com.fitibo.aotearoa.mapper;

import com.fitibo.aotearoa.model.Email;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

/**
 * Created by qianhao.zhou on 9/1/16.
 */
@Mapper
public interface EmailQueueMapper {

    @Insert("insert into `email_queue` (`order_id`, `from`, `to`, `subject`, `content`, group_id) values(#{orderId}, #{from}, #{to}, #{subject}, #{content}, #{groupId})")
    @Options(useGeneratedKeys = true, keyColumn = "id", keyProperty = "id", flushCache = Options.FlushCachePolicy.DEFAULT)
    int create(Email email);

    @Select("select * from email_queue where succeed = 0 order by retry")
    @Results({
            @Result(property = "id", column = "id"),
            @Result(property = "from", column = "from"),
            @Result(property = "orderId", column = "order_id"),
            @Result(property = "to", column = "to"),
            @Result(property = "subject", column = "subject"),
            @Result(property = "content", column = "content"),
            @Result(property = "retry", column = "retry"),
            @Result(property = "succeed", column = "succeed"),
            @Result(property = "group_id", column = "groupId"),
    })
    List<Email> findAllFailedEmails();

    @Update("update email_queue set succeed = 1 where id = #{id}")
    int updateSucceed(int id);

    @Update("update email_queue set retry = retry+1 where id = #{id}")
    int updateRetry(int id);
}
