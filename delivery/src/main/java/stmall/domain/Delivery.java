package stmall.domain;

import java.util.Date;
import java.util.List;
import javax.persistence.*;
import lombok.Data;
import stmall.DeliveryApplication;
import stmall.domain.DeliveryCanceled;
import stmall.domain.DeliveryStarted;

@Entity
@Table(name = "Delivery_table")
@Data
public class Delivery {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String userId;

    private Long orderId;

    private String productName;

    private Integer qty;

    private Long productId;

    private String status;

    private String courier;

    @PostPersist
    public void onPostPersist() {
        // DeliveryStarted deliveryStarted = new DeliveryStarted(this);
        // deliveryStarted.publishAfterCommit();
    }

    @PostUpdate
    public void onPostUpdate() {
        // DeliveryCanceled deliveryCanceled = new DeliveryCanceled(this);
        // deliveryCanceled.publishAfterCommit();
    }

    public static DeliveryRepository repository() {
        DeliveryRepository deliveryRepository = DeliveryApplication.applicationContext.getBean(
            DeliveryRepository.class
        );
        return deliveryRepository;
    }

    public void completeDelivery(
        CompleteDeliveryCommand completeDeliveryCommand
    ) {
        this.setCourier(completeDeliveryCommand.getCourier());
        this.setStatus("DeliveryCompleted");
        DeliveryCompleted deliveryCompleted = new DeliveryCompleted(this);
        deliveryCompleted.publishAfterCommit();
    }

    public void returnDelivery(ReturnDeliveryCommand returnDeliveryCommand) {
        this.setCourier(returnDeliveryCommand.getCourier());
        this.setStatus("DeliveryReturned");
        DeliveryReturned deliveryReturned = new DeliveryReturned(this);
        deliveryReturned.publishAfterCommit();
    }

    public static void startDelivery(OrderPlaced orderPlaced) {
        //* Example 1:  new item 
        Delivery delivery = new Delivery();
        delivery.setOrderId(orderPlaced.getId());
        delivery.setProductId(orderPlaced.getProductId());
        delivery.setProductName(orderPlaced.getProductName());
        delivery.setQty(orderPlaced.getQty());
        delivery.setStatus("DeliveryStarted");
        repository().save(delivery);

        DeliveryStarted deliveryStarted = new DeliveryStarted(delivery);
        deliveryStarted.publishAfterCommit();
        

        /** Example 2:  finding and process
        
        repository().findById(orderPlaced.get???()).ifPresent(delivery->{
            
            delivery // do something
            repository().save(delivery);

            DeliveryStarted deliveryStarted = new DeliveryStarted(delivery);
            deliveryStarted.publishAfterCommit();

         });
        */

    }

    public static void cancelDelivery(OrderCanceled orderCanceled) {
        /** Example 1:  new item 
        Delivery delivery = new Delivery();
        repository().save(delivery);

        DeliveryCanceled deliveryCanceled = new DeliveryCanceled(delivery);
        deliveryCanceled.publishAfterCommit();
        */

        //** Example 2:  finding and process
        
        repository().findById(orderCanceled.getId()).ifPresent(delivery->{
            delivery.setStatus("DeliveryCanceled");
            repository().save(delivery);

            DeliveryCanceled deliveryCanceled = new DeliveryCanceled(delivery);
            deliveryCanceled.publishAfterCommit();

         });
        

    }
}
