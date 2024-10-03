package project.gym;

import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import project.gym.enums.PassTypeEnum;
import project.gym.enums.PaymentMethodType;
import project.gym.model.PaymentMethod;
import project.gym.model.Room;
import project.gym.model.PassType;
import project.gym.repo.PassTypeRepo;
import project.gym.repo.PaymentMethodRepo;
import project.gym.repo.RoomRepo;

@Component
public class DatabaseSeeder {
    private final RoomRepo roomRepo;
    private final PassTypeRepo passTypeRepo;
    private final PaymentMethodRepo paymentMethodRepo;

    public DatabaseSeeder(RoomRepo roomRepo, PassTypeRepo passTypeRepo, PaymentMethodRepo paymentMethodRepo) {
        this.roomRepo = roomRepo;
        this.passTypeRepo = passTypeRepo;
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
        if (passTypeRepo.count() != 0) {
            return;
        }

        passTypeRepo.save(new PassType().withType(PassTypeEnum.COLLEGE).withMonthlyPrice(50).withLength(12));
        passTypeRepo.save(new PassType().withType(PassTypeEnum.SENIOR).withMonthlyPrice(45).withLength(12));
        passTypeRepo.save(new PassType().withType(PassTypeEnum.DYNAMIC).withMonthlyPrice(85).withLength(12));
        passTypeRepo.save(new PassType().withType(PassTypeEnum.UNLIMITED).withMonthlyPrice(100).withLength(null));
        passTypeRepo.save(new PassType().withType(PassTypeEnum.YEARLY).withMonthlyPrice(70).withLength(12));
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
