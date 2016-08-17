package com.fitibo.aotearoa.email;

import com.fitibo.aotearoa.model.Order;
import com.fitibo.aotearoa.model.OrderTicket;
import com.fitibo.aotearoa.model.OrderTicketUser;
import com.fitibo.aotearoa.model.Vendor;
import com.fitibo.aotearoa.util.DateUtils;
import com.google.common.collect.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Properties;

/**
 * Created by xiaozou on 8/17/16.
 */
@Service("emailService")
public class EmailService {

	@Autowired
	private JavaMailSender mailSender;

	public boolean sendEmail(Vendor vendor, Order order, List<OrderTicket> tickets) {
		try{
			MimeMessage message = mailSender.createMimeMessage();
			MimeMessageHelper messageHelper = new MimeMessageHelper(message, true, "utf-8");
			messageHelper.setFrom("zi___yue@126.com");
			messageHelper.setSubject("Make Reservations");
			messageHelper.setText(emailContent(vendor, order, tickets), true);
			messageHelper.setTo(vendor.getEmail());
			mailSender.send(message);
			return true;
		} catch (MessagingException e) {
			e.printStackTrace();
			return false;
		}
	}

	public String emailContent(Vendor vendor, Order order, List<OrderTicket> tickets) {

		StringBuffer sb = new StringBuffer();
		String vendorName = vendor.getName();
		sb.append(MAILTEMPLATE_HEADER.replace("#VENDORNAME#", vendorName));

		String tour = order.getSku();
		sb.append("<p>TOUR: ").append(tour).append("</p>");

		HashMap<String, List<OrderTicket>> ticketsMap = new HashMap<>();
		for(OrderTicket ticket : tickets) {
			String key = ticket.getSkuId() + "_" + ticket.getSkuTicketId() + "_" + ticket.getTicketDate() + "_" + ticket.getTicketTime();
			if(ticketsMap.containsKey(key)) {
				ticketsMap.get(key).add(ticket);
			} else {
				ticketsMap.put(key, Lists.newArrayList(ticket));
			}
		}
		for(List<OrderTicket> sameTicketList: ticketsMap.values()) {
			String date = DateUtils.formatDate(sameTicketList.get(0).getTicketDate());
			String time = sameTicketList.get(0).getTicketTime();
			String name = sameTicketList.get(0).getSkuTicket();
			sb.append("<p>DATE: ").append(date).append("</p>");
			sb.append("<p>TIME: ").append(time).append("</p>");
			sb.append("<p>Name: ").append(name).append("</p>");

			int count = 0;
			StringBuffer paxSb = new StringBuffer();
			for(OrderTicket ticket : sameTicketList) {
				for(OrderTicketUser user : ticket.getUsers()) {
					count++;
					paxSb.append(" ").append(user.getName()).append("-").append(user.getWeight()).append("kg;");
				}
			}
			sb.append("<p>TOTAL PAX: ").append(count + " persons: ").append(paxSb).append("</p>");
		}
		sb.append(MAILTEMPLATE_TAIL);
		return sb.toString();
	}

	private final static String MAILTEMPLATE_HEADER = "<html>" +
			"<body>" +
			"<div>" +
			"<p>#VENDORNAME#</p>" +
			"<br/>" +
			"<p>Please kindly help us to make the reservation as blew:</p>" +
			"<br/>";
//			"<p>TOUR: #TOUR#</p>" +
//			"<p>DATE: #DATE#</p>" +
//			"<p>TIME: #TIME#</p>" +
//			"<p>NAME: #NAME#</p>" +
//			"<p>TOTAL PAX: #TOURPAX#</p>" +
	private final static String MAILTEMPLATE_TAIL = "<br/>" +
			"<p>A prompt replay would greatly oblige us.</p>" +
			"<br/>" +
			"<p>Many thanks for your support.</p>" +
			"<br/>" +
			"<p>Best Regards</p>" +
			"<br/>" +
			"<p>Reservation Team</p>" +
			"<p style=\"color: dodgerblue\">the <span style=\"font-weight: bold\">Easy Efficient Excellent</span> travel with <span style=\"font-weight: bold\">EYOUNZ Limited</span></p>" +
			"<p>Tel & Fax: +64 (0)3 357 4405</p>" +
			"<p>Address: 30 Durey Rd, Christchurch Airport Internatinal Terminal Building, Christchurch 8053</p>" +
			"</div>" +
			"</body>" +
			"</html>";
}
