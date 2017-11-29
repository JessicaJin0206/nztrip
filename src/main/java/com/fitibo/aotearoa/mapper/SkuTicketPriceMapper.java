package com.fitibo.aotearoa.mapper;

import com.fitibo.aotearoa.model.SkuTicketPrice;
import com.fitibo.aotearoa.model.SkuTicketPriceForExport;

import org.apache.ibatis.annotations.*;
import org.apache.ibatis.session.RowBounds;

import java.util.Date;
import java.util.List;

/**
 * Created by qianhao.zhou on 7/24/16.
 */
public interface SkuTicketPriceMapper {

    @Insert({
            "<script>",
            "<if test = 'skuTicketPrices != null and skuTicketPrices.size() > 0'>",
            "insert into sku_ticket_price (sku_id, sku_ticket_id, date, time, sale_price, cost_price, description, total_count, current_count)",
            "values ",
            "<foreach  collection='skuTicketPrices' item='item' separator=','>",
            "(#{item.skuId}, #{item.skuTicketId}, #{item.date}, #{item.time}, #{item.salePrice}, #{item.costPrice}, #{item.description}, #{item.totalCount}, #{item.currentCount})",
            "</foreach>",
            "</if>",
            "</script>"
    })
    int batchCreate(@Param("skuTicketPrices") List<SkuTicketPrice> skuTicketPrices);

    @Select("select * from sku_ticket_price where sku_id = #{skuId} and date >= #{start} and date < #{end}")
    @Results({
            @Result(column = "id", property = "id"),
            @Result(column = "sku_id", property = "skuId"),
            @Result(column = "sku_ticket_id", property = "skuTicketId"),
            @Result(column = "date", property = "date"),
            @Result(column = "time", property = "time"),
            @Result(column = "cost_price", property = "costPrice"),
            @Result(column = "sale_price", property = "salePrice"),
            @Result(column = "description", property = "description"),
            @Result(column = "total_count", property = "totalCount"),
            @Result(column = "current_count", property = "currentCount"),
    })
    List<SkuTicketPrice> findBySkuIdAndStartTime(@Param("skuId") int skuId,
                                                 @Param("start") Date start, @Param("end") Date end);

    @Select("select * from sku_ticket_price where id = #{id}")
    @Results({
            @Result(column = "id", property = "id"),
            @Result(column = "sku_id", property = "skuId"),
            @Result(column = "sku_ticket_id", property = "skuTicketId"),
            @Result(column = "date", property = "date"),
            @Result(column = "time", property = "time"),
            @Result(column = "cost_price", property = "costPrice"),
            @Result(column = "sale_price", property = "salePrice"),
            @Result(column = "description", property = "description"),
            @Result(column = "total_count", property = "totalCount"),
            @Result(column = "current_count", property = "currentCount"),
    })
    SkuTicketPrice findById(int id);

    @Select({"<script>",
            "select * from sku_ticket_price where id in ",
            "<foreach collection='ids' item='id' index='index' open='(' separator=',' close=')'>#{id}",
            "</foreach>",
            "</script>"})
    @Results({
            @Result(column = "id", property = "id"),
            @Result(column = "sku_id", property = "skuId"),
            @Result(column = "sku_ticket_id", property = "skuTicketId"),
            @Result(column = "date", property = "date"),
            @Result(column = "time", property = "time"),
            @Result(column = "cost_price", property = "costPrice"),
            @Result(column = "sale_price", property = "salePrice"),
            @Result(column = "description", property = "description"),
            @Result(column = "total_count", property = "totalCount"),
            @Result(column = "current_count", property = "currentCount"),
    })
    List<SkuTicketPrice> findByIds(@Param("ids") List<Integer> ids);

    @Select("select * from sku_ticket_price where sku_id = #{skuId} and date > date_add(now(), interval -1 day)")
    @Results({
            @Result(column = "id", property = "id"),
            @Result(column = "sku_id", property = "skuId"),
            @Result(column = "sku_ticket_id", property = "skuTicketId"),
            @Result(column = "date", property = "date"),
            @Result(column = "time", property = "time"),
            @Result(column = "cost_price", property = "costPrice"),
            @Result(column = "sale_price", property = "salePrice"),
            @Result(column = "description", property = "description"),
            @Result(column = "total_count", property = "totalCount"),
            @Result(column = "current_count", property = "currentCount"),
    })
    List<SkuTicketPrice> findBySkuId(@Param("skuId") int skuId);

    @Select("select * from sku_ticket_price where sku_id = #{skuId} and date >= #{from} and date < #{to} and valid = 1 ")
    @Results({
            @Result(column = "id", property = "id"),
            @Result(column = "sku_id", property = "skuId"),
            @Result(column = "sku_ticket_id", property = "skuTicketId"),
            @Result(column = "date", property = "date"),
            @Result(column = "time", property = "time"),
            @Result(column = "cost_price", property = "costPrice"),
            @Result(column = "sale_price", property = "salePrice"),
            @Result(column = "description", property = "description"),
            @Result(column = "total_count", property = "totalCount"),
            @Result(column = "current_count", property = "currentCount"),
    })
    List<SkuTicketPrice> findBySkuIdAndDuration(@Param("skuId") int skuId, @Param("from") Date from, @Param("to") Date to);

    @Select("select * from sku_ticket_price where sku_id = #{skuId} and sku_ticket_id = #{skuTicketId} and date =#{date} and valid = 1 ")
    @Results({
            @Result(column = "id", property = "id"),
            @Result(column = "sku_id", property = "skuId"),
            @Result(column = "sku_ticket_id", property = "skuTicketId"),
            @Result(column = "date", property = "date"),
            @Result(column = "time", property = "time"),
            @Result(column = "cost_price", property = "costPrice"),
            @Result(column = "sale_price", property = "salePrice"),
            @Result(column = "description", property = "description"),
            @Result(column = "total_count", property = "totalCount"),
            @Result(column = "current_count", property = "currentCount"),
    })
    List<SkuTicketPrice> findBySkuTicketIdAndDate(@Param("skuId") int skuId,
                                                  @Param("skuTicketId") int skuTicketId,
                                                  @Param("date") Date date,
                                                  RowBounds rowBounds);

    @Select("select distinct(time) from sku_ticket_price where sku_id = #{skuId} and date = #{date} and valid = 1 ")
    List<String> findDistinctTicketBySkuIdAndDate(@Param("skuId") int skuId, @Param("date") Date date);

    @Select("select * from sku_ticket_price where sku_id = #{skuId} and sku_ticket_id = #{skuTicketId} and date =#{date} and valid = 1 ")
    @Results({
            @Result(column = "id", property = "id"),
            @Result(column = "sku_id", property = "skuId"),
            @Result(column = "sku_ticket_id", property = "skuTicketId"),
            @Result(column = "date", property = "date"),
            @Result(column = "time", property = "time"),
            @Result(column = "cost_price", property = "costPrice"),
            @Result(column = "sale_price", property = "salePrice"),
            @Result(column = "description", property = "description"),
            @Result(column = "total_count", property = "totalCount"),
            @Result(column = "current_count", property = "currentCount"),
    })
    List<SkuTicketPrice> findAvailableBySkuTicketIdAndDate(@Param("skuId") int skuId, @Param("skuTicketId") int skuTicketId,
                                                           @Param("date") Date date, RowBounds rowBounds);

    @Select("select * from sku_ticket_price where sku_id = #{skuId} and sku_ticket_id = #{skuTicketId} and date > date_add(now(), interval -1 day) and valid = 1 order by date desc")
    @Results({
            @Result(column = "id", property = "id"),
            @Result(column = "sku_id", property = "skuId"),
            @Result(column = "sku_ticket_id", property = "skuTicketId"),
            @Result(column = "date", property = "date"),
            @Result(column = "time", property = "time"),
            @Result(column = "cost_price", property = "costPrice"),
            @Result(column = "sale_price", property = "salePrice"),
            @Result(column = "description", property = "description"),
            @Result(column = "total_count", property = "totalCount"),
            @Result(column = "current_count", property = "currentCount"),
    })
    List<SkuTicketPrice> findBySkuTicketId(@Param("skuId") int skuId,
                                           @Param("skuTicketId") int skuTicketId,
                                           RowBounds rowBounds);

    @Select("select * from sku_ticket_price where sku_id = #{skuId} and sku_ticket_id = #{skuTicketId} and date > date_add(now(), interval -1 day) and valid = 1 order by date desc")
    @Results({
            @Result(column = "id", property = "id"),
            @Result(column = "sku_id", property = "skuId"),
            @Result(column = "sku_ticket_id", property = "skuTicketId"),
            @Result(column = "date", property = "date"),
            @Result(column = "time", property = "time"),
            @Result(column = "cost_price", property = "costPrice"),
            @Result(column = "sale_price", property = "salePrice"),
            @Result(column = "description", property = "description"),
            @Result(column = "total_count", property = "totalCount"),
            @Result(column = "current_count", property = "currentCount"),
    })
    List<SkuTicketPrice> findAvailableBySkuTicketId(@Param("skuId") int skuId,
                                                    @Param("skuTicketId") int skuTicketId, RowBounds rowBounds);

    @Select("select * from sku_ticket_price where sku_id = #{skuId} and sku_ticket_id = #{skuTicketId} and valid = 1 order by date desc")
    @Results({
            @Result(column = "id", property = "id"),
            @Result(column = "sku_id", property = "skuId"),
            @Result(column = "sku_ticket_id", property = "skuTicketId"),
            @Result(column = "date", property = "date"),
            @Result(column = "time", property = "time"),
            @Result(column = "cost_price", property = "costPrice"),
            @Result(column = "sale_price", property = "salePrice"),
            @Result(column = "description", property = "description"),
            @Result(column = "total_count", property = "totalCount"),
            @Result(column = "current_count", property = "currentCount"),
    })
    List<SkuTicketPrice> findAllBySkuTicketId(@Param("skuId") int skuId,
                                              @Param("skuTicketId") int skuTicketId, RowBounds rowBounds);


    @Delete({
            "<script>",
            "update sku_ticket_price set valid = 0 where sku_id = #{skuId} and sku_ticket_id = #{skuTicketId} and date = #{date} ",
            "<if test ='times != null and times.size() > 0'>",
            "and time in ",
            "<foreach  collection='times' open='(' close=')' item='item' separator=','>#{item}",
            "</foreach>",
            "</if>",
            "</script>"
    })
    int batchDelete(@Param("skuId") int skuId,
                    @Param("skuTicketId") int skuTicketId,
                    @Param("date") Date date,
                    @Param("times") List<String> times);

    @Delete({
            "<script>",
            "update sku_ticket_price set valid = 0 where sku_id = #{skuId} ",
            "and sku_ticket_id in ",
            "<foreach  collection='skuTicketIds' open='(' close=')' item='item' separator=','>#{item}</foreach> ",
            "and `date` &gt;= #{startDate} and `date` &lt;= #{endDate}",
            "</script>"
    })
    int batchDeleteByDate(@Param("skuId") int skuId,
                          @Param("skuTicketIds") List<Integer> skuTicketIds,
                          @Param("startDate") Date startDate,
                          @Param("endDate") Date endDate);

    @Select("select distinct `time` from sku_ticket_price where sku_id = #{skuId} and date >= #{startDate} and date <= #{endDate}")
    List<String> getSessionsBySkuIdAndDate(@Param("skuId") int skuId,
                                           @Param("startDate") Date startDate,
                                           @Param("endDate") Date endDate);

    @Select("select distinct(`time`) FROM sku_ticket_price p JOIN sku_ticket t ON p.sku_ticket_id = t.id WHERE p.sku_id = #{skuId} AND t.sku_id = #{skuId} AND status = 10 AND p.date >= NOW()")
    List<String> getSessionsBySkuId(@Param("skuId") int skuId);

    //这个数据库语句用来导出价格（按照时间天数步长为1天的递增确定时间的范围）
    @Select("SELECT * FROM sku_ticket NATURAL JOIN ( SELECT number, sale_price, cost_price, Min(date) AS start_date, Max(date) AS end_date, time, sku_ticket_id AS id FROM ( SELECT ticket_price.*, IF ( ticket_price.sku_ticket_id =@a AND ticket_price.sale_price = @b AND ticket_price.cost_price = @c AND ticket_price.time =@d AND datediff(ticket_price.date ,@e) = 1,@num :=@num ,@num :=@num + 1 ) AS number, @a := ticket_price.sku_ticket_id, @b := ticket_price.sale_price, @c := ticket_price.cost_price, @d := ticket_price.time, @e := ticket_price.date FROM ( SELECT * FROM sku_ticket_price WHERE sku_id = #{skuId} AND datediff(date,NOW()) >= 0 and valid = 1 ORDER BY sku_ticket_id, time, date ) ticket_price, ( SELECT @a := NULL, @b := NULL, @c := NULL, @d := NULL, @e := NULL, @num := 0 ) temp ) result GROUP BY number, sku_ticket_id, sale_price, cost_price, time ) AS price WHERE status = 10 ORDER BY start_date")
    @Results({
            @Result(column = "id", property = "id"),
            @Result(column = "sku_id", property = "skuId"),
            @Result(column = "name", property = "name"),
            @Result(column = "start_date", property = "startDate"),
            @Result(column = "end_date", property = "endDate"),
            @Result(column = "time", property = "time"),
            @Result(column = "cost_price", property = "costPrice"),
            @Result(column = "sale_price", property = "salePrice"),
    })
    List<SkuTicketPriceForExport> findSkuTicketPriceForExportBySkuId(@Param("skuId") int skuId);
}
