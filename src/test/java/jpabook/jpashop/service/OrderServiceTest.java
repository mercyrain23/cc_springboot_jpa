package jpabook.jpashop.service;

import jpabook.jpashop.domain.Address;
import jpabook.jpashop.domain.Member;
import jpabook.jpashop.domain.Order;
import jpabook.jpashop.domain.OrderStatus;
import jpabook.jpashop.domain.item.Book;
import jpabook.jpashop.domain.item.Item;
import jpabook.jpashop.exception.NotEnoughStockException;
import jpabook.jpashop.repository.OrderRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class OrderServiceTest {

    @Autowired EntityManager em;
    @Autowired OrderService orderService;
    @Autowired OrderRepository orderRepository;

    @Test
    public void 상품주문() throws Exception {
        Member member = createMember("회원1");

        Book book = createBook("시골 JPA", 10000, 10);

        int orderCount = 2;
        Long orderId = orderService.order(member.getId(), book.getId(), orderCount);

        Order getOrder = orderRepository.findOne(orderId);

        assertEquals(OrderStatus.ORDER, getOrder.getStatus()); // 상품 주문시 상태는 ORDER
        assertEquals(1, getOrder.getOrderItems().size()); // 주문은 상품 종류 수가 정확해야 한다
        assertEquals(10000 * orderCount, getOrder.getTotalPrice()); // 주문 가격 * 수량
        assertEquals(8, book.getStockQuantity()); // 주문 수량만큼 재고가 줄어야
    }

////    @Test(expected = NotEnoughStockException.class)
//    @Test
//    public void 상품주문_재고수량초과 throws Exception {
//        Member member = createMember("회원1");
//        Item item = createBook("시골 JPA", 10000, 10);
//
//        int orderCount = 11;
//
//        orderService.order(member.getId(), item.getId(), orderCount);
//
//        fail("재고 수량 부족 예외 발생");
//    }

    @Test
    public void 주문취소() throws Exception {
        Member member = createMember("회원1");
        Book item = createBook("시골 JPA", 10000, 10);

        int orderCount = 2;
        Long orderId = orderService.order(member.getId(), item.getId(), orderCount);

        orderService.cancelOrder(orderId);

        Order getOrder = orderRepository.findOne(orderId);

        assertEquals(OrderStatus.CANCEL, getOrder.getStatus());
        assertEquals(10, item.getStockQuantity());
    }

    @Test
    public void 상품주문_재고수량초과() throws Exception {

    }

    private Book createBook(String name, int price, int stockQuantity) {
        Book book = new Book();
        book.setName(name);
        book.setPrice(price);
        book.setStockQuantity(stockQuantity);
        em.persist(book);
        return book;
    }

    private Member createMember(String name) {
        Member member = new Member();
        member.setName(name);
        member.setAddress(new Address("서울", "경기", "123-123"));
        em.persist(member);
        return member;
    }
}