package com.hello.core.repository.order.query;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.persistence.EntityManager;

import org.springframework.stereotype.Repository;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class OrderQueryRepository {

	private final EntityManager em;
	
	public List<OrderQueryDto> findOrderQueryDtos() {
		List<OrderQueryDto> result = findOrders();
		result.forEach(o -> {
			List<OrderItemQueryDto> orderItems = findOrderItems(o.getOrderId());
			o.setOrderItems(orderItems);
		});
		return result;
	}
	
	public List<OrderQueryDto> findAllByDto_optimization() {
		List<OrderQueryDto> result = findOrders();
		
		List<Long> orderIds = result.stream()
		.map(o-> o.getOrderId())
		.collect(Collectors.toList());
		
		List<OrderItemQueryDto> orderItems = em.createQuery( 
			"select new jpabook.jpashop.repository.order.query.OrderItemQueryDto(oi.order.id, i.name, oi.orderPrice, oi.count)" +
					" from OrderItem oi" +
					" join oi.item i" +
					" where oi.order.id in :orderIds"
					, OrderItemQueryDto.class)
					.setParameter("orderIds", orderIds)
					.getResultList();
		Map<Long, List<OrderItemQueryDto>> orderItemMap = orderItems.stream()
		.collect(Collectors.groupingBy(orderItemQueryDto -> orderItemQueryDto.getOrderId()));
		
		result.forEach(o -> o.setOrderItems(orderItemMap.get(o.getOrderId())));
	
		return result;
	}
	
	public List<OrderFlagDto> findAllByDto_flat() {
		return em.createQuery("select new" +
				" jpabook.jpashop.repository.order.query.OrderFlagDto(o.id, m.name, o.orderDate, o.status, d.address, i.name, oi.orderPrice, oi.count)" +
				" from Order o" +
				" join o.member m" +
				" join o.delivery d" +
				" join o.orderItems oi" +
				" join oi.item i", OrderFlagDto.class)
				.getResultList();
	}
	
	private List<OrderItemQueryDto> findOrderItems(Long orderId) {
		return em.createQuery(
					"select new jpabook.jpashop.repository.order.query.OrderItemQueryDto(oi.order.id, i.name, oi.orderPrice, oi.count)" +
						" from OrderItem oi" +
						" join oi.item i" +
						" where oi.order.id = :orderId", OrderItemQueryDto.class
				)
				.setParameter("orderId", orderId)
				.getResultList();
	}
	
	private List<OrderQueryDto> findOrders() {
		return em.createQuery(
			"select new jpabook.jpashop.repository.order.query.OrderQueryDto(o.id, m.name, o.orderDate, o.status, d.address)" +
					" from Order o" +
					" join o.memeber m" +
					" join o.delivery d", OrderQueryDto.class
		).getResultList();
	}

}
