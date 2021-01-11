package jpabook.jpashop.service;

import jpabook.jpashop.domain.item.Book;
import jpabook.jpashop.domain.item.Item;
import jpabook.jpashop.repository.ItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ItemService {

    private final ItemRepository itemRepository;

    @Transactional
    public void saveItem(Item item) {
        itemRepository.save(item);
    }

    @Transactional
    public void updateItem(Long itemId, String name, int price, int stockQuantity) { // 변경 감지 기능
        Item findItem = itemRepository.findOne(itemId); // 영속 상태
//        findItem.change(name, price, stockQuantity); // 이런 메소드를 만들어서 사용하는거 권
        findItem.setPrice(price);
        findItem.setName(name);
        findItem.setStockQuantity(stockQuantity);
//        itemRepository.save(findItem); // @Transactional 에 의해서 commit 되고 update 되기 때문에 save 필요없다
    }


//    @Transactional
//    public void updateItem(Long itemId, Book bookParam) { // 변경 감지기능
//        Item findItem = itemRepository.findOne(itemId); // 영속 상태
//        findItem.setPrice(bookParam.getPrice());
//        findItem.setName(bookParam.getName());
//        findItem.setStockQuantity(bookParam.getStockQuantity());
////        itemRepository.save(findItem); // @Transactional 에 의해서 commit 되고 update 되기 때문에 save 필요없다
//    }

//    @Transactional
//    public Item updateItem(Long itemId, Book bookParam) { // merge 동작 방식
//        Item findItem = itemRepository.findOne(itemId); // 영속 상태
//        findItem.setPrice(bookParam.getPrice());
//        findItem.setName(bookParam.getName());
//        findItem.setStockQuantity(bookParam.getStockQuantity());
//        return findItem;
//    }


    public List<Item> findItems() {
        return itemRepository.findAll();
    }

    public Item findOne(Long itemId) {
        return itemRepository.findOne(itemId);
    }
}
