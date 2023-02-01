package com.increff.pos.service;

import static org.junit.Assert.assertEquals;

import java.time.ZonedDateTime;
import java.util.List;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.increff.pos.AbstractUnitTest;
import com.increff.pos.TestUtil;
import com.increff.pos.pojo.PosDaySales;
import com.increff.pos.util.DateTimeProvider;

public class DaySalesServiceTest extends AbstractUnitTest {

    @Autowired
    private DaySalesService service;
    
    @Autowired
    private TestUtil testUtil;

    @Test
    public void testAddAndGetNew() {
        PosDaySales pojo = testUtil.getDaySalesPojo(5, 5, 25.0);
        service.add(pojo);

        List<PosDaySales> list = service.getAllPaginated(0, 5);

        assertEquals(1, list.size());
        assertEquals(Integer.valueOf(5), list.get(0).getInvoicedItemsCount());
        assertEquals(Integer.valueOf(5), list.get(0).getInvoicedOrderCount());
        assertEquals(Double.valueOf(25.0), list.get(0).getTotalRevenue());

    }

    @Test
    public void testUpdate() {
        PosDaySales pojo = testUtil.getDaySalesPojo(5, 5, 25.0);
        service.add(pojo);

        PosDaySales pojo1 = testUtil.getDaySalesPojo(5, 1, 25.0);
        service.add(pojo1);
        List<PosDaySales> list = service.getAllPaginated(0, 5);

        assertEquals(1, list.size());
        assertEquals(Integer.valueOf(10), list.get(0).getInvoicedItemsCount());
        assertEquals(Integer.valueOf(6), list.get(0).getInvoicedOrderCount());
        assertEquals(Double.valueOf(50.0), list.get(0).getTotalRevenue());
    }

    @Test
    public void testGetAllPaginated() {
        ZonedDateTime customAppTime = DateTimeProvider.getInstance().timeNow().withHour(12).withMinute(0).withSecond(0)
                .withNano(0);

        for (int i = 1; i <= 5; i++) {
            DateTimeProvider.getInstance().setTime(customAppTime.plusDays(i));

            PosDaySales pojo = testUtil.getDaySalesPojo(i, i, i * 5.0);
            service.add(pojo);
        }

        List<PosDaySales> list = service.getAllPaginated(0, 2);

        assertEquals(2, list.size());
    }

    @Test
    public void testGetByDate() {
        ZonedDateTime customAppTime = DateTimeProvider.getInstance().timeNow().withHour(12).withMinute(0).withSecond(0)
                .withNano(0);

        for (int i = 1; i <= 5; i++) {
            DateTimeProvider.getInstance().setTime(customAppTime.plusDays(i));

            PosDaySales pojo = testUtil.getDaySalesPojo(i, i, i * 5.0);
            service.add(pojo);
        }

        PosDaySales pojo = service.getByDate(customAppTime.plusDays(3));

        assertEquals(Integer.valueOf(3), pojo.getInvoicedItemsCount());
        assertEquals(Integer.valueOf(3), pojo.getInvoicedOrderCount());
        assertEquals(Double.valueOf(15.0), pojo.getTotalRevenue());
    }

    @Test
    public void testGetByDateRange() {
        ZonedDateTime customAppTime = DateTimeProvider.getInstance().timeNow().withHour(12).withMinute(0).withSecond(0)
                .withNano(0);

        for (int i = 1; i <= 5; i++) {
            DateTimeProvider.getInstance().setTime(customAppTime.plusDays(i));

            PosDaySales pojo = testUtil.getDaySalesPojo(i, i, i * 5.0);
            service.add(pojo);
        }

        List<PosDaySales> pojo = service.getByDateRange(customAppTime.plusDays(2), customAppTime.plusDays(4), 0, 5);

        assertEquals(3, pojo.size());
    }

    @Test
    public void testGetTotalEntriesInDateRange() {
        ZonedDateTime customAppTime = DateTimeProvider.getInstance().timeNow().withHour(12).withMinute(0).withSecond(0)
                .withNano(0);

        for (int i = 1; i <= 5; i++) {
            DateTimeProvider.getInstance().setTime(customAppTime.plusDays(i));
            PosDaySales pojo = testUtil.getDaySalesPojo(i, i, i * 5.0);
            service.add(pojo);

        }

        Integer pojo = service.getTotalEntriesinDateRange(customAppTime.plusDays(2).withHour(0),
                customAppTime.plusDays(4).withHour(23).withMinute(59).withSecond(59));

        assertEquals(Integer.valueOf(3), pojo);
    }

    @Test
    public void testGetTotalEntries() {
        ZonedDateTime customAppTime = DateTimeProvider.getInstance().timeNow().withHour(12).withMinute(0).withSecond(0)
                .withNano(0);

        for (int i = 1; i <= 5; i++) {
            DateTimeProvider.getInstance().setTime(customAppTime.plusDays(i));
            PosDaySales pojo = testUtil.getDaySalesPojo(i, i, i * 5.0);
            service.add(pojo);

        }

        Integer pojo = service.getTotalEntries();

        assertEquals(Integer.valueOf(5), pojo);
    }
}
