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
		return orderRepository.findAllByString(new OrderSearch()).stream()
							.map(SimpleOrderDto::new)
							.collect(Collectors.toList());
	}
	
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