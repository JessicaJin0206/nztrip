package com.fitibo.aotearoa.service;

import com.fitibo.aotearoa.dto.Token;
import com.fitibo.aotearoa.mapper.SkuRecordMapper;
import com.fitibo.aotearoa.mapper.SkuTicketMapper;
import com.fitibo.aotearoa.model.Sku;
import com.fitibo.aotearoa.model.SkuRecord;
import com.fitibo.aotearoa.model.SkuTicket;
import com.fitibo.aotearoa.vo.AddPriceRequest;
import com.fitibo.aotearoa.vo.AddSkuInventoryRequest;
import com.fitibo.aotearoa.vo.DeleteSkuInventoryRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.Date;

/**
 * 订单日志对应service
 * Created by 11022 on 2017/8/16.
 */
@Service("skuRecordService")
public class SkuRecordService {
    private final static Logger logger = LoggerFactory.getLogger(SkuRecordService.class);

    @Autowired
    private SkuRecordMapper skuRecordMapper;

    @Autowired
    private SkuTicketMapper skuTicketMapper;

    public void createSku(Token token, Sku sku) {
        SkuRecord skuRecord = new SkuRecord();
        skuRecord.setOperateTime(new Date());
        skuRecord.setOperateType("创建");
        skuRecord.setSkuId(sku.getId());
        skuRecord.setOperatorId(token.getId());
        skuRecord.setOperatorType(token.getRole().toString());
        skuRecord.setContentChangeFrom("");
        skuRecord.setContentChangeTo("");
        skuRecordMapper.insert(skuRecord);
    }

    public void updateSku(Token token, Sku oldSku, Sku newSku) {
        Field[] fields = Sku.class.getDeclaredFields();
        Date date = new Date();
        for (Field field : fields) {
            try {
                if (Collection.class.isAssignableFrom(field.getType())) {
                    continue;
                }
                PropertyDescriptor pd = new PropertyDescriptor(field.getName(), Sku.class);
                Method getMethod = pd.getReadMethod();
                Object o1 = getMethod.invoke(oldSku);
                Object o2 = getMethod.invoke(newSku);
                String s1 = o1 == null ? "" : o1.toString();
                String s2 = o2 == null ? "" : o2.toString();
                if (!s1.equals(s2)) {
                    SkuRecord skuRecord = new SkuRecord();
                    skuRecord.setOperateTime(date);
                    skuRecord.setOperateType(field.getName());
                    skuRecord.setSkuId(oldSku.getId());
                    skuRecord.setOperatorId(token.getId());
                    skuRecord.setOperatorType(token.getRole().toString());
                    skuRecord.setContentChangeFrom(s1);
                    skuRecord.setContentChangeTo(s2);
                    skuRecordMapper.insert(skuRecord);
                }
            } catch (Exception e) {
                logger.error("error in reflect Sku.class in updateSku");
                throw new RuntimeException(e);
            }
        }
    }

    public void deleteTicket(SkuTicket skuTicket, Token token) {
        SkuRecord skuRecord = new SkuRecord();
        skuRecord.setOperateTime(new Date());
        skuRecord.setOperateType("Ticket");
        skuRecord.setSkuId(skuTicket.getSkuId());
        skuRecord.setOperatorId(token.getId());
        skuRecord.setContentChangeFrom(skuTicket.toString());
        skuRecord.setContentChangeTo("");
        skuRecord.setOperatorType(token.getRole().toString());
        skuRecordMapper.insert(skuRecord);
    }

    public void updateTicket(SkuTicket newSkuTicket, Token token) {
        SkuTicket skuTicket = skuTicketMapper.findById(newSkuTicket.getId());
        Date date = new Date();
        Field[] fields = SkuTicket.class.getDeclaredFields();
        for (Field field : fields) {
            try {
                if (Collection.class.isAssignableFrom(field.getType())) {
                    continue;
                }
                PropertyDescriptor pd = new PropertyDescriptor(field.getName(), SkuTicket.class);
                Method getMethod = pd.getReadMethod();
                Object o1 = getMethod.invoke(skuTicket);
                Object o2 = getMethod.invoke(newSkuTicket);
                String s1 = o1 == null ? "" : o1.toString();
                String s2 = o2 == null ? "" : o2.toString();
                if (!s1.equals(s2)) {
                    SkuRecord skuRecord = new SkuRecord();
                    skuRecord.setOperateTime(date);
                    skuRecord.setOperateType("ticket: " + skuTicket.getName() + " " + field.getName());
                    skuRecord.setSkuId(skuTicket.getSkuId());
                    skuRecord.setOperatorId(token.getId());
                    skuRecord.setOperatorType(token.getRole().toString());
                    skuRecord.setContentChangeFrom(s1);
                    skuRecord.setContentChangeTo(s2);
                    skuRecordMapper.insert(skuRecord);
                }
            } catch (Exception e) {
                logger.error("error in reflect SkuTicket.class in updateTicket");
                throw new RuntimeException(e);
            }
        }
    }

    public void addTicket(SkuTicket skuTicket, Token token) {
        SkuRecord skuRecord = new SkuRecord();
        skuRecord.setOperateTime(new Date());
        skuRecord.setOperateType("add Ticket");
        skuRecord.setSkuId(skuTicket.getSkuId());
        skuRecord.setOperatorId(token.getId());
        skuRecord.setOperatorType(token.getRole().toString());
        skuRecord.setContentChangeFrom("");
        skuRecord.setContentChangeTo(skuTicket.toString());
        skuRecordMapper.insert(skuRecord);
    }

    public void addTicketPrice(int skuId, AddPriceRequest request, Token token) {
        SkuRecord skuRecord = new SkuRecord();
        skuRecord.setOperateTime(new Date());
        skuRecord.setOperateType("add Ticket Price");
        skuRecord.setSkuId(skuId);
        skuRecord.setOperatorId(token.getId());
        skuRecord.setOperatorType(token.getRole().toString());
        skuRecord.setContentChangeFrom("");
        skuRecord.setContentChangeTo(request.toString());
        skuRecordMapper.insert(skuRecord);
    }

    public void deleteTicketPrice(int skuId, AddPriceRequest request, Token token) {
        SkuRecord skuRecord = new SkuRecord();
        skuRecord.setOperateTime(new Date());
        skuRecord.setOperateType("delete Ticket Price");
        skuRecord.setSkuId(skuId);
        skuRecord.setOperatorId(token.getId());
        skuRecord.setOperatorType(token.getRole().toString());
        skuRecord.setContentChangeFrom(request.toString());
        skuRecord.setContentChangeTo("");
        skuRecordMapper.insert(skuRecord);
    }


    public void deleteInventory(DeleteSkuInventoryRequest request, Token token) {
        SkuRecord skuRecord = new SkuRecord();
        skuRecord.setOperateTime(new Date());
        skuRecord.setOperateType("delete Inventory");
        skuRecord.setSkuId(request.getSkuId());
        skuRecord.setOperatorId(token.getId());
        skuRecord.setOperatorType(token.getRole().toString());
        skuRecord.setContentChangeFrom(request.toString());
        skuRecord.setContentChangeTo("");
        skuRecordMapper.insert(skuRecord);
    }

    public void addInventory(AddSkuInventoryRequest request, Token token) {
        SkuRecord skuRecord = new SkuRecord();
        skuRecord.setOperateTime(new Date());
        skuRecord.setOperateType("Add Inventory");
        skuRecord.setSkuId(request.getSkuId());
        skuRecord.setOperatorId(token.getId());
        skuRecord.setOperatorType(token.getRole().toString());
        skuRecord.setContentChangeFrom("");
        skuRecord.setContentChangeTo(request.toString());
        skuRecordMapper.insert(skuRecord);
    }

}
