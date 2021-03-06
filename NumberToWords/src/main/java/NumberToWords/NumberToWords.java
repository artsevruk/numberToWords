package NumberToWords;

import org.apache.log4j.Logger;

import java.lang.*;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * Класс позволяет переводить числовую запись в строковую
 */
public class NumberToWords extends Number {
    private static final Logger logger = Logger.getLogger(NumberToWords.class);

    //Справочник для чисел прописью
    private static final String DIGIT1[] = {"", "один", "два", "три", "четыре", "пять", "шесть", "семь", "восемь", "девять"};
    private static final String DIGIT11[] = {"", "одиннадцать", "двенадцать", "тринадцать", "четырнадцать", "пятнадцать", "шестнадцать", "семнадцать", "восемнадцать", "девятнадцать"};
    private static final String DIGIT20[] = {"", "десять", "двадцать", "тридцать", "сорок", "пятьдесят", "шестьдесят", "семьдесят", "восемьдесят", "девяносто"};
    private static final String DIGIT100[] = {"", "сто", "двести", "триста", "четыреста", "пятьсот", "шестьсот", "семьсот", "восемьсот", "девятьсот"};
    private static final ArrayList<String> FORMONE = new ArrayList<String>(Arrays.asList("", "тысяча", "миллион", "миллиард", "триллион", "квадриллион", "квинтиллион", "секстиллион", "септиллион", "октиллион", "нониллион", "дециллион"));
    private static final ArrayList<String> FORMTWO = new ArrayList<String>(Arrays.asList("", "тысячи", "миллиона", "миллиарда", "триллиона", "квадриллиона", "квинтиллиона", "секстиллиона", "септиллиона", "октиллиона", "нониллиона", "дециллиона"));
    private static final ArrayList<String> FORMFIVE = new ArrayList<String>(Arrays.asList("", "тысяч", "миллионов", "миллиардов", "триллионов", "квадриллионов", "квинтиллионов", "секстиллионов", "септиллионов", "октиллионов", "нониллионов", "дециллионов"));
    private static final ArrayList<String> FORMDOUBLE = new ArrayList<String>(Arrays.asList("", "десятых", "сотых", "тысячных", "десятитысячных", "стотысячных", "миллионных", "десятимиллионных", "стомиллионных", "миллиардных", "десятимиллиардных", "стомиллиардных"));

    private StringBuilder numberToSb = new StringBuilder();
    private String number;


    public NumberToWords() {

    }

    /**
     * Метод получение целой части числа
     */
    public String getIntNumber() {
        return numberAbs(intNumber(number));
    }

    /**
     * Метод получение дробной части числа
     */
    public String getDoubleNumber() {
        return doubleNumber(number);
    }

    /**
     * Метод добавляет в numberToSb отрицательное число или положительоне
     *
     * @param srtNumber
     */
    private void addFront(String srtNumber) {
        String minus = " минус ";
        if (!front(intNumber(srtNumber))) {
            numberToSb.append(minus);
        }
    }

    /**
     * Метод добавляет в StringBuilder numberToSb слова соответствующие переданному числу strNumber
     *
     * @param strNumber
     */
    private void addDigit(String strNumber) {
        if (strNumber.length() == 1) {
            numberToSb.append(digit(strNumber.substring(0, 1), DIGIT1)).append(" ");
        } else if (strNumber.length() == 2) {
            if (10 < Integer.parseInt(strNumber) && Integer.parseInt(strNumber) < 20) {
                numberToSb.append(digit(strNumber.substring(1), DIGIT11)).append(" ");
            } else {
                numberToSb.append(digit(strNumber.substring(0, 1), DIGIT20)).append(" ");
                addDigit(strNumber.substring(1));
            }
        } else if (strNumber.length() == 3) {
            numberToSb.append(digit(strNumber.substring(0, 1), DIGIT100)).append(" ");
            addDigit(strNumber.substring(1));
        }
    }

    /**
     * Метод проверяет число на последнюю цифру и неравенство к numberNotEquals
     * @param strIntegerOrDoubleNumber
     * @param lastNumeral
     * @param numberNotEquals
     * @param intStr11
     * @return
     */
    private boolean ifLastNumeralIsNotEleven(String strIntegerOrDoubleNumber, String lastNumeral, int numberNotEquals, int intStr11) {
        if (strIntegerOrDoubleNumber.substring(strIntegerOrDoubleNumber.length() - 1).equals(lastNumeral) && numberNotEquals != intStr11)
            return true;
        else return false;
    }

    /**
     * Метод добавляет в StringBuilder numberToSb степени и склонения
     *
     * @param strNumber
     * @throws IndexOutOfBoundsException
     */
    private void addFormatNumber(String strNumber) throws IndexOutOfBoundsException {
        String one = " одна ";
        String two = " две ";
        ArrayList<String> segments = splitNumberToSegments(strNumber);
        for (int i = 0; i < segments.size(); i++) {
            String strSegment = segments.get(i);
            int intStr = Integer.parseInt(strSegment);
            int intStr11 = intStr;
            if (strSegment.length() > 2)
                intStr11 = Integer.parseInt(strSegment.substring(1));
            addDigit(strSegment);

            if (segments.size() >= 1 && intStr > 0) {
                if (ifLastNumeralIsNotEleven(strSegment,"1", 11, intStr11))
                {
                    if (i >= segments.size() - 2) {
                        numberToSb.replace(numberToSb.length() - 5, numberToSb.length(), one);
                    }
                    numberToSb.append(FORMONE.get(segments.size() - 1 - i)).append(" ");

                } else if (ifLastNumeralIsNotEleven(strSegment,"2", 12, intStr11) ||
                        ifLastNumeralIsNotEleven(strSegment,"3", 13, intStr11) ||
                        ifLastNumeralIsNotEleven(strSegment,"4", 14, intStr11)) {
                    if ((i >= segments.size() - 2) && (strSegment.substring(strSegment.length() - 1).equals("2"))) {
                        numberToSb.replace(numberToSb.length() - 4, numberToSb.length(), two);
                    }
                    numberToSb.append(FORMTWO.get(segments.size() - 1 - i)).append(" ");
                } else {
                    numberToSb.append(FORMFIVE.get(segments.size() - 1 - i)).append(" ");
                }
            }
        }
    }

    /**
     * Метод добавляет в StringBuilder numberToSb постфикс для целой части числа и дробной части числа
     *
     * @param strIntegerNumber
     * @param strDoubleNumber
     * @throws IndexOutOfBoundsException
     */
    private void addFormatForIntegerAndDouble(String strIntegerNumber, String strDoubleNumber) throws IndexOutOfBoundsException {
        int intStr11 = 1;
        int indexStrIntegerNumber = 0;
        int indexStrDoubleNumber = 1;
        String zero = "ноль ";
        String wholeForOne = "целая ";
        String wholeForFive = "целых ";
        String strForDoubleOne = "ая ";
        String[] strArrayNumber = new String[2];
        strArrayNumber[0] = strIntegerNumber;
        strArrayNumber[1] = strDoubleNumber;

        for (int i = 0; i <= strArrayNumber.length - 1; i++) {

            if (numberIsZero(strArrayNumber[i])) {
                if (strArrayNumber[i].length() > 1) {
                    intStr11 = Integer.parseInt(strArrayNumber[i].substring(strArrayNumber[i].length() - 2));

                    if (i == indexStrDoubleNumber) addFormatNumber(strArrayNumber[i]);
                }

                if (i == indexStrIntegerNumber) addFormatNumber(strArrayNumber[i]);


                if (i == indexStrDoubleNumber) numberToSb.append(FORMDOUBLE.get(strArrayNumber[i].length()));

                if (ifLastNumeralIsNotEleven(strArrayNumber[i],"1", 11, intStr11)) {
                    if (i == indexStrDoubleNumber)
                        numberToSb.replace(numberToSb.length() - 2, numberToSb.length(), strForDoubleOne);
                }
            } else if (i == indexStrIntegerNumber) numberToSb.append(zero);

            if (i == indexStrIntegerNumber) {
                if (ifLastNumeralIsNotEleven(strArrayNumber[i],"1", 11, intStr11)) {

                    numberToSb.append(wholeForOne);
                } else numberToSb.append(wholeForFive);
            }
        }
    }

    /**
     * Метод переводит числовую запись в строковую
     *
     * @return переведенное число в пропись
     */
    public String convertNumberToWords() {
        addFront(number);
        try {
            addFormatForIntegerAndDouble(getIntNumber(), getDoubleNumber());
        } catch (NumberFormatException e) {
            logger.error("Invalid number format. " + e);
            e.printStackTrace();
        } catch (IndexOutOfBoundsException e) {
            logger.error("Big number. It is necessary to expand the catalog. " + e);
            e.printStackTrace();
        }

        String numberToString = numberToSb.toString();
        clear();

        while (numberToString.contains("  ")) {
            numberToString = numberToString.replace("  ", " ");
        }
        if (numberToString.substring(0, 1).contains(" ")) numberToString = numberToString.substring(1);
        if (numberToString.substring(numberToString.length() - 1).contains(" "))
            numberToString = numberToString.substring(0, numberToString.length() - 1);

        return numberToString;
    }

    /**
     * Метод переводит и печатает числовую запись в строковую с аргументом
     *
     * @return переведенное число в пропись
     */
    public String convertNumberToWords(String numberStr) {
        this.number = numberStr;
        return convertNumberToWords();
    }

    /**
     * Метод очищает переменную StringBuilder numberToSb в которой находится конвертированая строковая запись
     */
    private void clear() {
        int startIndexNumberToSb = 0;
        numberToSb.delete(startIndexNumberToSb, numberToSb.length());
    }
}