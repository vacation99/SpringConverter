package com.example.daniil.SpringBootTestApp.Controllers;

import com.example.daniil.SpringBootTestApp.Models.Currency;
import com.example.daniil.SpringBootTestApp.Models.History;
import com.example.daniil.SpringBootTestApp.Repo.CurrencyRepo;
import com.example.daniil.SpringBootTestApp.Repo.HistoryRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Controller
public class MainController {
    @Autowired
    CurrencyRepo currencyRepo;
    @Autowired
    HistoryRepo historyRepo;

    private Date date = new Date();

    @GetMapping("/hello")
    public String hello() {
        return "hello";
    }

    @GetMapping("/main")
    public String main(Model model) {
        Iterable<Currency> currencies = currencyRepo.findAll();
        model.addAttribute("model", currencies);
        return "main";
    }

    @PostMapping("/main")
    public String convert(@RequestParam("currencyName1") String currencyName1,
                          @RequestParam("currencyName2") String currencyName2,
                          @RequestParam("amountCurrency") String amountCurrency) {
        Currency currency1 = currencyRepo.findByCharCode(parseName(currencyName1));
        Currency currency2 = currencyRepo.findByCharCode(parseName(currencyName2));

        String one = currency1.getValue();
        String two = currency2.getValue();

        int nominal1 = Integer.parseInt(currency1.getNominal());
        int nominal2 = Integer.parseInt(currency2.getNominal());

        double result = Double.parseDouble(parseDot(one)) / Double.parseDouble(parseDot(two));
        result = result * Integer.parseInt(amountCurrency) / nominal1 * nominal2;

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd.MM.yyyy");
        String currentDate = simpleDateFormat.format(date);

        History history = new History(currencyName1, currencyName2, amountCurrency, currentDate, result);
        historyRepo.save(history);

        return "redirect:/history";
    }

    @GetMapping("/history")
    public String history(Model model) {
        Iterable<History> histories = historyRepo.findAll();
        Iterable<Currency> filter = currencyRepo.findAll();

        List<History> list = new ArrayList<>();
        List<History> list2 = new ArrayList<>();

        for (History one : histories) {
            list.add(one);
        }
        for (int i = 1; i < list.size(); i++) {
            if (!list.get(i - 1).getDate().equals(list.get(i).getDate())) {
                list2.add(list.get(i - 1));
            }
            if (i == list.size() - 1) {
                list2.add(list.get(i));
            }
        }
        Iterable<History> filterDate = list2;

        model.addAttribute("model", histories);
        model.addAttribute("filterDate", filterDate);
        model.addAttribute("filter", filter);

        return "history";
    }

    @PostMapping("/historyFilter")
    public String historyFilter(@RequestParam String charCode1,
                                @RequestParam String charCode2,
                                @RequestParam String currencyDate,
                                Model model) {
        Currency currency1 = currencyRepo.findByCharCode(charCode1);
        Currency currency2 = currencyRepo.findByCharCode(charCode2);

        List<History> filter1 = historyRepo.findByCharCode1(charCode1 + " " + "(" + currency1.getName() + ")");

        for (int i = 0; i < filter1.size(); i++) {
            if (!filter1.get(i).getCharCode2().equals(charCode2 + " " + "(" + currency2.getName() + ")"))
                filter1.remove(i);
        }

        if (currencyDate.equals("Пусто")) {
            Iterable<History> filter = filter1;
            model.addAttribute("model", filter);
        }
        else {
            for (int i = 0; i < filter1.size(); i++) {
                if (!filter1.get(i).getDate().equals(currencyDate))
                    filter1.remove(i);
            }
            Iterable<History> filter = filter1;
            model.addAttribute("model", filter);
        }

        return "historyFilter";
    }

    @GetMapping("/clearHistory")
    public String clearHistory() {
        historyRepo.deleteAll();
        return "redirect:/history";
    }

    private String parseDot(String number) {
        char[] numberWithDot = number.toCharArray();
        String line = "";
        for (int i = 0; i < numberWithDot.length; i++) {
            if (numberWithDot[i] == ',') {
                line += ".";
            } else {
                line += numberWithDot[i];
            }
        }
        return line;
    }

    private String parseName(String name) {
        String line = "";
        char[] la = name.toCharArray();
        for (int i = 0; i < la.length; i++) {
            if (la[i] == ' ') {
                break;
            }
            else
                line += la[i];
        }
        return line;
    }
}