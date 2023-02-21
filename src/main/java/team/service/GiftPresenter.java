package team.service;

public class GiftPresenter {
    private GiftChooseHelper giftChooseHelper;
    private PaymentSystem paymentSystem;
    private DeliverySystem deliverySystem;

    public GiftPresenter(GiftChooseHelper giftChooseHelper, PaymentSystem paymentSystem, DeliverySystem deliverySystem) {
        this.giftChooseHelper = giftChooseHelper;
        this.paymentSystem = paymentSystem;
        this.deliverySystem = deliverySystem;
    }

    /*public void present(Person person) {
        Gift gift = giftChooseHelper.choose(person);
        System.out.println(String.format("Gift has been chosen: %s", gift.getName()));
        paymentSystem.pay(gift);
        deliverySystem.deliver(gift, person);
    }
     */

}
