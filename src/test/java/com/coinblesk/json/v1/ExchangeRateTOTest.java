package com.coinblesk.json.v1;

import org.junit.Test;

import static org.junit.Assert.*;

public class ExchangeRateTOTest
{
    @Test
    public void typeCanBeSet()
    {
        ExchangeRateTO subject = new ExchangeRateTO();
        subject.type(Type.SUCCESS);
        assert(subject.type().equals(Type.SUCCESS));
    }

    @Test
    public void messageCanBeSet()
    {
        ExchangeRateTO subject = new ExchangeRateTO();
        subject.message("This is some default message");
        assert(subject.message().equals("This is some default message"));
    }

    @Test
    public void nameCanBeSet()
    {
        ExchangeRateTO subject = new ExchangeRateTO();
        subject.name("USD/CHF");
        assert(subject.name().equals("USD/CHF"));
    }

    @Test
    public void rateCanBeSet()
    {
        ExchangeRateTO subject = new ExchangeRateTO();
        subject.rate("1.1337");
        assert(subject.rate().equals("1.1337"));
    }

    @Test
    public void nameAndRateCanBeSetWithBuilderPattern()
    {
        ExchangeRateTO subject = new ExchangeRateTO();
        subject.name("EUR/CHF").rate("0.942");
        assert(subject.name().equals("EUR/CHF"));
        assert(subject.rate().equals("0.942"));
    }

    @Test
    public void typeAndMessageCanBeSetWithBuilderPattern()
    {
        ExchangeRateTO subject = new ExchangeRateTO();
        subject.type(Type.SERVER_ERROR).message("some error");
        assert(subject.type().equals(Type.SERVER_ERROR));
        assert(subject.message().equals("some error"));
    }

    @Test
    public void nameReturnsExchangeRateTOObject()
    {
        ExchangeRateTO subject = new ExchangeRateTO();
        assert(subject.name("HKD/USD")
                .getClass()
                .isAssignableFrom(ExchangeRateTO.class));
    }

    @Test
    public void rateReturnsExchangeRateTOObject()
    {
        ExchangeRateTO subject = new ExchangeRateTO();
        assert(subject.rate("1.1337")
                .getClass()
                .isAssignableFrom(ExchangeRateTO.class));
    }

    @Test
    public void typeReturnsExchangeRateTOObject()
    {
        ExchangeRateTO subject = new ExchangeRateTO();
        assert(subject.type(Type.SUCCESS)
                .getClass()
                .isAssignableFrom(ExchangeRateTO.class));
    }

    @Test
    public void messageReturnsExchangeRateTOObject()
    {
        ExchangeRateTO subject = new ExchangeRateTO();
        assert(subject.message("some message")
                .getClass()
                .isAssignableFrom(ExchangeRateTO.class));
    }
}