package pl.parser.nbp.validate;

import com.sun.javaws.exceptions.InvalidArgumentException;

import java.util.regex.Pattern;

public class Validate {
    private static final int PARAM_LENGTH = 3;

    public static final void validateArgs(String[] args) throws InvalidArgumentException {
        isParamLengthCorrect(args);
        isCurrencyCorrect(args[0]);
        isDateFromCorrect(args[1]);
        isDateToCorrect(args[2]);
    }

    private static final void isParamLengthCorrect(String[] args) {
        if(args.length != PARAM_LENGTH) {
            throw new IllegalArgumentException("Wrong number of parameters. Sholud be [CUR yyyy-MM-dd yyyy-mm-dd] [Currency, date from, date to]");
        }
    }

    private static final void isCurrencyCorrect(String arg) {

        if(!Pattern.matches("[A-Z]{3}", arg)) {
            throw new IllegalArgumentException("Currency argument should be in ISO code, for example EUR, USD");
        }
    }

    private static final boolean checkDate(String date){
        return Pattern.matches("\\d{4}-\\d{2}-\\d{2}", date);
    }

    private static final void isDateFromCorrect(String arg) {
        if(!checkDate(arg)) {
            throw new IllegalArgumentException("Argument 2 should be date looks like yyyy-mm-dd");
        }
    }

    private static final void isDateToCorrect(String arg){
        if(!checkDate(arg)) {
            throw new IllegalArgumentException("Argument 3 should be date looks like yyyy-mm-dd");
        }
    }
}
