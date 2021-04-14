package edu.iis.mto.time;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Clock;
import java.time.Instant;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;

@ExtendWith(MockitoExtension.class)
class OrderTest {
    @Mock
    private Clock clock;

    private Instant testSubmit;
    private Order order;

    @BeforeEach
    void setUp() throws Exception {
        testSubmit = Instant.parse("2010-12-25T00:00:00Z");
        order = new Order(clock);

    }

    @Test
    void whenOrderIsExpiredShouldBeThrownExceptionTest(){
        Instant testConfirm = testSubmit.plus(Order.VALID_PERIOD_HOURS + 1, ChronoUnit.HOURS);
        when(clock.getZone()).thenReturn(ZoneId.systemDefault());
        when(clock.instant()).thenReturn(testSubmit).thenReturn(testConfirm);
        try{
            order.submit();
            order.confirm();
            fail("fail");
        } catch (OrderExpiredException e) {

        }

    }

    @Test
    void whenOrderIsNotExpiredExceptionShouldNodBeThrownTest(){

        Instant testConfirm = testSubmit.plus(Order.VALID_PERIOD_HOURS, ChronoUnit.HOURS);
        when(clock.getZone()).thenReturn(ZoneId.systemDefault());
        when(clock.instant()).thenReturn(testSubmit).thenReturn(testConfirm);
        try{
            order.submit();
            order.confirm();
        } catch (OrderExpiredException e) {
            fail("fail");

        }
    }

    @Test
    void whenOrderIsExpiredShouldChangeStateToCancelledTest(){
        Instant testConfirm = testSubmit.plus(Order.VALID_PERIOD_HOURS + 1, ChronoUnit.HOURS);
        when(clock.getZone()).thenReturn(ZoneId.systemDefault());
        when(clock.instant()).thenReturn(testSubmit).thenReturn(testConfirm);
        try{
            order.submit();
            order.confirm();
            fail("fail");
        } catch (OrderExpiredException e) {

        }
        Order.State orderState = order.getOrderState();
        assertEquals(orderState, Order.State.CANCELLED);
    }

    @Test
    void whenOrderIsNotExpiredStateOfOrderShouldBeConfirmed(){
        Instant testConfirm = testSubmit.plus(Order.VALID_PERIOD_HOURS, ChronoUnit.HOURS);
        when(clock.getZone()).thenReturn(ZoneId.systemDefault());
        when(clock.instant()).thenReturn(testSubmit).thenReturn(testConfirm);
        try{
            order.submit();
            order.confirm();
        } catch (OrderExpiredException e) {
            fail("fail");

        }
        Order.State orderState = order.getOrderState();
        assertEquals(orderState, Order.State.CONFIRMED);
    }
}


