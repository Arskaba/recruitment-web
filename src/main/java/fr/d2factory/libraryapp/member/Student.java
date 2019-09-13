package fr.d2factory.libraryapp.member;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public class Student extends Member {
    @Override
    public void payBook(int numberOfDays) {
        float priceToPay;
        if (numberOfDays <= 30) {
            priceToPay = getWallet() - (0.10f * numberOfDays);
        } else {
            priceToPay = getWallet() - (0.15f * (numberOfDays - 30) + (0.10f * 30));
        }
        setWallet(priceToPay);
    }

    @Override
    public boolean isLate(LocalDate date) {
        return ChronoUnit.DAYS.between(date, LocalDate.now()) > 30;
    }
}
