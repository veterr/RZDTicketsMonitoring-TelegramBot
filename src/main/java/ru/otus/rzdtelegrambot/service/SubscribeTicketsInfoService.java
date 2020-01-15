package ru.otus.rzdtelegrambot.service;

import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import ru.otus.rzdtelegrambot.model.Car;
import ru.otus.rzdtelegrambot.model.UserTicketsSubscription;
import ru.otus.rzdtelegrambot.repository.UserTicketsSubscriptionMongoRepository;
import ru.otus.rzdtelegrambot.utils.CarPatterns;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Обрабатывает запрос пользователя "Подписаться" и "Отписаться" на поезда
 *
 * @author Sergei Viacheslaev
 */
@Service
public class SubscribeTicketsInfoService {


    private UserTicketsSubscriptionMongoRepository subscriptionsRepository;

    public SubscribeTicketsInfoService(UserTicketsSubscriptionMongoRepository repository) {
        this.subscriptionsRepository = repository;

    }

    public void saveUserSubscription(CallbackQuery usersQuery) {
        long chatId = usersQuery.getMessage().getChatId();
        String[] queryData = usersQuery.getData().split("\\|");
        String callbackMessage = usersQuery.getMessage().getText();

        String trainNumber = queryData[1];
        String dateDepart = queryData[2];


        String trainName = callbackMessage.substring(callbackMessage.indexOf("'") + 1, callbackMessage.lastIndexOf("'"));
        String stationDepart = callbackMessage.substring(callbackMessage.lastIndexOf("Отправление:") + 13,
                callbackMessage.indexOf(",")).trim();
        String stationArrival = callbackMessage.substring(callbackMessage.lastIndexOf("Прибытие:") + 10,
                callbackMessage.lastIndexOf(",")).trim();


        List<Car> availableCars = parseCarsFromMessage(callbackMessage);

        UserTicketsSubscription usersSubscription =
                new UserTicketsSubscription(chatId, trainNumber, trainName, stationDepart, stationArrival, dateDepart, availableCars);

        subscriptionsRepository.save(usersSubscription);


    }

    public void deleteUserSubscription(CallbackQuery usersQuery) {
        String[] queryData = usersQuery.getData().split("\\|");
        String subscriptionID = queryData[2];

        subscriptionsRepository.deleteById(subscriptionID);

    }


    public List<UserTicketsSubscription> getUsersSubscriptions(long chatId) {
        return subscriptionsRepository.findByChatId(chatId);
    }

    private List<Car> parseCarsFromMessage(String message) {
        List<Car> availableCars = new ArrayList<>();

        if (message.contains("Плац")) {
            String plackartTariff = message.substring(lastIndexOf(CarPatterns.PLACKART_START, message), lastIndexOf(CarPatterns.PLACKART_END, message));
            availableCars.add(new Car("Плац", 0, Integer.parseInt(plackartTariff)));
        }

        if (message.contains("Купе")) {
            String kuperTariff = message.substring(lastIndexOf(CarPatterns.KUPE_START, message), lastIndexOf(CarPatterns.KUPE_END, message));
            availableCars.add(new Car("Купе", 0, Integer.parseInt(kuperTariff)));
        }

        if (message.contains("Люкс")) {
            String luxTariff = message.substring(lastIndexOf(CarPatterns.LUX_START, message), lastIndexOf(CarPatterns.LUX_END, message));
            availableCars.add(new Car("Люкс", 0, Integer.parseInt(luxTariff)));
        }

        if (message.contains("Мягкий")) {
            String miagkiyTariff = message.substring(lastIndexOf(CarPatterns.MIAGKIY_START, message), lastIndexOf(CarPatterns.MIAGKIY_END, message));
            availableCars.add(new Car("Мягкий", 0, Integer.parseInt(miagkiyTariff)));
        }

        if (message.contains("Сид")) {
            String sidyachiTariff = message.substring(lastIndexOf(CarPatterns.SIDYACHI_START, message), lastIndexOf(CarPatterns.SIDYACHI_END, message));
            availableCars.add(new Car("Сид", 0, Integer.parseInt(sidyachiTariff)));
        }


        return availableCars;

    }

    private int lastIndexOf(Pattern pattern, String input) {
        Matcher matcher = pattern.matcher(input);
        return matcher.find() ? matcher.end() : -1;
    }


}
