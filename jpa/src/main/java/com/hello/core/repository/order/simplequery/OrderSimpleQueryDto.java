package com.hello.core.repository.order.simplequery;

import java.time.LocalDateTime;

import com.hello.core.domain.Address;
import com.hello.core.domain.Order;
import com.hello.core.domain.OrderStatus;

import lombok.Data;

@Data
public class OrderSimpleQueryDto {
	private Long orderId;
	private String name;
	private LocalDateTime orderDate;
	private OrderStatus orderStatus;
	private Address address;
	
	public OrderSimpleQueryDto(Long orderId, String name, LocalDateTime orderDate, OrderStatus orderStatus, Address address) {
		this.orderId = orderId;
		this.name = name;
		this.orderDate = orderDate;
		this.orderStatus = orderStatus;
		this.address = address;
	}
}
