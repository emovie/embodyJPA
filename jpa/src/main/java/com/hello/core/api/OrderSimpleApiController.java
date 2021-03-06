package com.hello.core.api;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hello.core.domain.Address;
import com.hello.core.domain.Order;
import com.hello.core.domain.OrderStatus;
import com.hello.core.repository.OrderRepository;
import com.hello.core.repository.OrderSearch;
import com.hello.core.repository.order.query.OrderQueryDto;
import com.hello.core.repository.order.query.OrderQueryRepository;
import com.hello.core.repository.order.simplequery.OrderSimpleQueryDto;
import com.hello.core.repository.order.simplequery.OrderSimpleQueryRepository;

import lombok.Data;
import lombok.RequiredArgsConstructor;


/**
 * xToOne(ManyToOne,OneToOne)
 * Order
 * Order -> Member
 * Order -> Delivery
 *
 */
@RestController
@RequiredArgsConstructor
public class OrderSimpleApiController {

	private final OrderRepository orderRepository;
	private final OrderSimpleQueryRepository orderSimpleQueryRepository;
	
	@GetMapping("/api/v1/simple-orders")
	public List<Order> ordersV1() {
		List<Order> all = orderRepository.findAllByString(new OrderSearch());
		for(Order order : all) {
			order.getMember().getName();	// Lazy 강제초기화
			order.getDelivery().getAddress();	// Lazy 강제초기화
		}
		return all;
	}
	
	@GetMapping("api/v2/simple-orders")
	public List<SimpleOrderDto> orderV2() {
		List<Order> orders = orderRepository.findAllByString(new OrderSearch());
		List<SimpleOrderDto> result = orders.stream()
					.map(o -> new SimpleOrderDto(o))
					.collect(Collectors.toList());
		return result;
	}
	
//	@GetMapping("/api/v3/simple-orders")
//	public List<SimpleOrderDto> ordersV3() {
//		List<Order> orders = orderRepository.findAllWithMemberDelivery();
//		List<SimpleOrderDto> result = orders.stream()
//				.map(o -> new SimpleOrderDto(o))
//				.collect(Collectors.toList());
//		return result;
//	}
	
	
	@Data
	static class SimpleOrderDto {
		private Long orderId;
		private String name;
		private LocalDateTime orderDate;
		private OrderStatus orderStatus;
		private Address address;
		
		public SimpleOrderDto(Order order) {
			orderId = order.getId();
			name = order.getMember().getName();
			orderDate = order.getOrderDate();
			orderStatus = order.getStatus();
			address = order.getDelivery().getAddress();
		}
	}
	
}
