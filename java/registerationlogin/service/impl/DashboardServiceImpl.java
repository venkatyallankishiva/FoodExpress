package registerationlogin.service.impl;

import java.time.LocalDateTime;

import org.springframework.stereotype.Service;

import registerationlogin.repository.OrderRepository;
import registerationlogin.service.DashboardService;

@Service
public class DashboardServiceImpl implements DashboardService {

    private OrderRepository orderRepository;

    public DashboardServiceImpl(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    @Override
    public SalesStats calculateTodaySalesAndPercentageChange(Long restaurantId) {
        // Today's range
        LocalDateTime todayStart = LocalDateTime.now().withHour(0).withMinute(0).withSecond(0);
        LocalDateTime todayEnd = todayStart.plusDays(1);

        // Yesterday's range
        LocalDateTime yesterdayStart = todayStart.minusDays(1);
        LocalDateTime yesterdayEnd = todayStart;

        // Fetch sales for the specific restaurant
        Double todaySales = orderRepository.findTotalSalesByDateRangeAndRestaurant(
                todayStart, todayEnd, restaurantId);
        Double yesterdaySales = orderRepository.findTotalSalesByDateRangeAndRestaurant(
                yesterdayStart, yesterdayEnd, restaurantId);

        // Calculate percentage change
        double percentageChange = calculatePercentageChange(todaySales, yesterdaySales);

        return new SalesStats(todaySales, percentageChange);
    }

    // Same percentage calculation logic as before
    private double calculatePercentageChange(double todaySales, double yesterdaySales) {
        if (yesterdaySales == 0) {
            return todaySales == 0 ? 0 : Double.POSITIVE_INFINITY;
        }
        return ((todaySales - yesterdaySales) / yesterdaySales) * 100;
    }

    public static record SalesStats(double todayTotalSales, double percentageChange) {
    }

    // @Override
    // public Map<String, Object> getSalesData() {
    // Map<String, Object> data = new HashMap<>();
    // data.put("totalSales", orderRepository.getTodaySales());
    // data.put("percentageChange", orderRepository.getSalesPercentageChange());
    // return data;
    // }

    // @Override
    // public Map<String, Object> getOrdersData() {
    // Map<String, Object> data = new HashMap<>();
    // data.put("activeOrders", orderRepository.countActiveOrders());
    // data.put("preparing", orderRepository.countPreparingOrders());
    // data.put("ready", orderRepository.countReadyOrders());
    // return data;
    // }

    // @Override
    // public Map<String, Object> getTablesData() {
    // Map<String, Object> data = new HashMap<>();
    // data.put("occupied", orderRepository.countOccupiedTables());
    // data.put("capacity", 20); // Assuming a fixed total number of tables
    // return data;
    // }

    // @Override
    // public Map<String, Object> getCustomerSatisfaction() {
    // Map<String, Object> data = new HashMap<>();
    // data.put("rating", orderRepository.getAverageRating());
    // data.put("reviews", orderRepository.countReviews());
    // return data;
    // }

    // @Override
    // public Map<String, Object> getSalesTrend() {
    // Map<String, Object> data = new HashMap<>();
    // data.put("labels", List.of("Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun"));
    // data.put("sales", orderRepository.getSalesTrendForWeek());
    // return data;
    // }

    // @Override
    // public Map<String, Object> getTopSellingItems() {
    // return orderRepository.getTopSellingItems();
    // }

}
