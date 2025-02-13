package registerationlogin.service;

import java.util.Map;

import registerationlogin.service.impl.DashboardServiceImpl.SalesStats;

public interface DashboardService {

    public SalesStats calculateTodaySalesAndPercentageChange(Long restaurantId);

    // public Map<String, Object> getSalesData();

    // public Map<String, Object> getOrdersData();

    // public Map<String, Object> getTablesData();

    // public Map<String, Object> getCustomerSatisfaction();

    // public Map<String, Object> getSalesTrend();

    // public Map<String, Object> getTopSellingItems();

}
