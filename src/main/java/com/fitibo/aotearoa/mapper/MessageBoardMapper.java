package com.fitibo.aotearoa.mapper;

import com.fitibo.aotearoa.model.MessageBoard;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.session.RowBounds;

import java.util.List;

/**
 * Created by qianhao.zhou on 8/10/16.
 */
public interface MessageBoardMapper {

    @Insert("insert into message_board (admin_id, content, create_time) values (#{adminId},#{content},#{createTime})")
    @Options(useGeneratedKeys = true, keyColumn = "id", keyProperty = "id", flushCache = Options.FlushCachePolicy.DEFAULT)
    int create(MessageBoard messageBoard);

    @Select("select * from message_board order by create_time desc")
    @Results({
            @Result(column = "id", property = "id"),
            @Result(column = "admin_id", property = "adminId"),
            @Result(column = "content", property = "content"),
            @Result(column = "create_time", property = "createTime")
    })
    List<MessageBoard> findByPage(RowBounds rowBounds);
}
