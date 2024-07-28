package project.gym.seed;

import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import project.gym.enums.PassType;
import project.gym.enums.PaymentMethodType;
import project.gym.model.Pass;
import project.gym.model.PaymentMethod;
import project.gym.model.Room;
import project.gym.repo.PassRepo;
import project.gym.repo.PaymentMethodRepo;
import project.gym.repo.RoomRepo;

@Component
public class DatabaseSeeder {
    private final RoomRepo roomRepo;
    private final PassRepo passRepo;
    private final PaymentMethodRepo paymentMethodRepo;

    public DatabaseSeeder(RoomRepo roomRepo, PassRepo passRepo, PaymentMethodRepo paymentMethodRepo) {
        this.roomRepo = roomRepo;
        this.passRepo = passRepo;
        this.paymentMethodRepo = paymentMethodRepo;
    }

    @EventListener
    public void seed(ContextRefreshedEvent event) {
        seedRoomTable();
        seedPassTable();
        seedPaymentMethodTable();
    }

    private void seedRoomTable() {
        if (roomRepo.count() != 0) {
            return;
        }

        roomRepo.save(new Room().withName("A2").withPersonLimit(10));
        roomRepo.save(new Room().withName("B5").withPersonLimit(20));
        roomRepo.save(new Room().withName("G7").withPersonLimit(30));
    }

    private void seedPassTable() {
        if (passRepo.count() != 0) {
            return;
        }

        passRepo.save(new Pass().withType(PassType.STUDENT).withMonthlyPrice(50).withLength(12));
        passRepo.save(new Pass().withType(PassType.INDEFINITE).withMonthlyPrice(100).withLength(null));
        passRepo.save(new Pass().withType(PassType.YEARLY).withMonthlyPrice(70).withLength(12));
    }

    private void seedPaymentMethodTable() {
        if (paymentMethodRepo.count() != 0) {
            return;
        }

        paymentMethodRepo.save(new PaymentMethod().withType(PaymentMethodType.CARD));
        paymentMethodRepo.save(new PaymentMethod().withType(PaymentMethodType.CASH));
        paymentMethodRepo.save(new PaymentMethod().withType(PaymentMethodType.PAYPAL));
        paymentMethodRepo.save(new PaymentMethod().withType(PaymentMethodType.BLIK));
    }
}
